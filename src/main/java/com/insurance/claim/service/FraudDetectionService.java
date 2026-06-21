package com.insurance.claim.service;

import com.insurance.claim.config.FraudDetectionConfig;
import com.insurance.claim.dto.FraudDetectionResultDTO;
import com.insurance.claim.entity.*;
import com.insurance.claim.enums.FraudType;
import com.insurance.claim.repository.*;
import com.insurance.claim.util.DistanceUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class FraudDetectionService {

    private final FraudDetectionConfig config;
    private final ClaimReportRepository claimReportRepository;
    private final AccidentPhotoRepository accidentPhotoRepository;
    private final HospitalInvoiceRepository hospitalInvoiceRepository;
    private final RepairQuoteRepository repairQuoteRepository;
    private final HistoricalClaimRepository historicalClaimRepository;
    private final BlacklistClueRepository blacklistClueRepository;
    private final RelatedPersonRepository relatedPersonRepository;
    private final SurveyRecordRepository surveyRecordRepository;
    private final FraudDetectionResultRepository fraudDetectionResultRepository;
    private final InsurancePolicyRepository insurancePolicyRepository;
    private final RedisTemplate<String, Object> redisTemplate;

    private static final String FRAUD_CACHE_PREFIX = "fraud:claim:";
    private static final String INVOICE_HASH_PREFIX = "fraud:invoice:";
    private static final String PHOTO_HASH_PREFIX = "fraud:photo:";

    @Transactional
    public FraudDetectionResultDTO performFullFraudDetection(Long claimId) {
        ClaimReport claim = claimReportRepository.findById(claimId)
                .orElseThrow(() -> new IllegalArgumentException("报案记录不存在: " + claimId));

        String cacheKey = FRAUD_CACHE_PREFIX + claimId;
        FraudDetectionResultDTO cachedResult = (FraudDetectionResultDTO) redisTemplate.opsForValue().get(cacheKey);
        if (cachedResult != null) {
            return cachedResult;
        }

        fraudDetectionResultRepository.deleteByClaimId(claimId);

        List<FraudDetectionResultDTO.FraudItem> fraudItems = new ArrayList<>();

        fraudItems.addAll(detectDuplicateClaims(claim));
        fraudItems.addAll(detectTimeConflicts(claim));
        fraudItems.addAll(detectImageTampering(claim));
        fraudItems.addAll(detectAbnormalAmount(claim));
        fraudItems.addAll(detectRelatedPersonFraud(claim));
        fraudItems.addAll(detectCrossRegionSuspicion(claim));
        fraudItems.addAll(detectInvoiceReuse(claim));
        fraudItems.addAll(detectMultiplePolicyFraud(claim));
        fraudItems.addAll(checkBlacklist(claim));
        fraudItems.addAll(detectHistoricalFraudPatterns(claim));
        fraudItems.addAll(checkSurveyConsistency(claim));

        int totalRiskScore = fraudItems.stream()
                .mapToInt(FraudDetectionResultDTO.FraudItem::getRiskScore)
                .sum();

        FraudDetectionResultDTO result = new FraudDetectionResultDTO();
        result.setClaimNumber(claim.getClaimNumber());
        result.setTotalRiskScore(totalRiskScore);
        result.setRiskLevel(determineRiskLevel(totalRiskScore));
        result.setFraudItems(fraudItems);
        result.setManualReviewRequired(totalRiskScore >= config.getManualReviewThreshold());
        result.setAutoApproveAllowed(totalRiskScore < config.getAutoApprovalThreshold());

        for (FraudDetectionResultDTO.FraudItem item : fraudItems) {
            FraudDetectionResult detectionResult = new FraudDetectionResult();
            detectionResult.setClaim(claim);
            detectionResult.setDetectionRule(item.getDetectionRule());
            detectionResult.setFraudType(FraudType.valueOf(item.getFraudType()));
            detectionResult.setRiskScore(item.getRiskScore());
            detectionResult.setRiskLevel(item.getRiskLevel());
            detectionResult.setRiskDescription(item.getRiskDescription());
            detectionResult.setEvidence(item.getEvidence());
            detectionResult.setDetectionTime(LocalDateTime.now());
            detectionResult.setDetectedBy("SYSTEM");
            detectionResult.setVerified(false);
            fraudDetectionResultRepository.save(detectionResult);
        }

        claim.setFraudScore(totalRiskScore);
        claim.setFraudSuspected(totalRiskScore >= config.getManualReviewThreshold());
        claim.setManualReviewRequired(totalRiskScore >= config.getManualReviewThreshold());
        claimReportRepository.save(claim);

        redisTemplate.opsForValue().set(cacheKey, result, 1, TimeUnit.HOURS);

        return result;
    }

    private List<FraudDetectionResultDTO.FraudItem> detectDuplicateClaims(ClaimReport claim) {
        List<FraudDetectionResultDTO.FraudItem> items = new ArrayList<>();

        LocalDateTime startTime = claim.getAccidentTime().minusHours(config.getDuplicateClaimWindowHours());
        LocalDateTime endTime = claim.getAccidentTime().plusHours(config.getDuplicateClaimWindowHours());

        if (claim.getAccidentLatitude() != null && claim.getAccidentLongitude() != null) {
            List<ClaimReport> nearbyAccidents = claimReportRepository.findNearbyAccidents(
                    claim.getAccidentLatitude(),
                    claim.getAccidentLongitude(),
                    startTime,
                    endTime,
                    claim.getId()
            );

            for (ClaimReport nearby : nearbyAccidents) {
                double distance = DistanceUtil.calculateDistanceMeters(
                        claim.getAccidentLatitude(), claim.getAccidentLongitude(),
                        nearby.getAccidentLatitude(), nearby.getAccidentLongitude()
                );

                if (distance < 500) {
                    FraudDetectionResultDTO.FraudItem item = new FraudDetectionResultDTO.FraudItem();
                    item.setDetectionRule("重复报案检测");
                    item.setFraudType(FraudType.DUPLICATE_CLAIM.name());
                    item.setRiskScore(30);
                    item.setRiskLevel("HIGH");
                    item.setRiskDescription(String.format(
                            "检测到在相近时间（%d小时内）和相近地点（%.0f米内）存在其他报案: %s",
                            config.getDuplicateClaimWindowHours(), distance, nearby.getClaimNumber()
                    ));
                    item.setEvidence(String.format(
                            "本报案: %s, 事故时间: %s; 邻近报案: %s, 事故时间: %s, 距离: %.0f米",
                            claim.getClaimNumber(), claim.getAccidentTime(),
                            nearby.getClaimNumber(), nearby.getAccidentTime(), distance
                    ));
                    items.add(item);
                }
            }
        }

        if (claim.getPolicy() != null && claim.getPolicy().getInsuredIdCard() != null) {
            List<ClaimReport> samePersonClaims = claimReportRepository.findByInsuredIdCardAndAccidentTimeBetween(
                    claim.getPolicy().getInsuredIdCard(),
                    startTime,
                    endTime,
                    claim.getId()
            );

            for (ClaimReport samePerson : samePersonClaims) {
                if (samePerson.getAccidentType() == claim.getAccidentType()) {
                    FraudDetectionResultDTO.FraudItem item = new FraudDetectionResultDTO.FraudItem();
                    item.setDetectionRule("同一人重复报案检测");
                    item.setFraudType(FraudType.DUPLICATE_CLAIM.name());
                    item.setRiskScore(40);
                    item.setRiskLevel("HIGH");
                    item.setRiskDescription(String.format(
                            "同一被保险人在%d小时内对相同事故类型重复报案: %s",
                            config.getDuplicateClaimWindowHours(), samePerson.getClaimNumber()
                    ));
                    item.setEvidence(String.format(
                            "被保险人: %s, 本次报案: %s, 历史报案: %s, 事故类型均为: %s",
                            claim.getPolicy().getInsuredName(),
                            claim.getClaimNumber(),
                            samePerson.getClaimNumber(),
                            claim.getAccidentType()
                    ));
                    items.add(item);
                }
            }
        }

        if (claim.getPoliceReportNumber() != null && !claim.getPoliceReportNumber().isEmpty()) {
            claimReportRepository.findByPoliceReportNumber(claim.getPoliceReportNumber())
                    .ifPresent(existing -> {
                        if (!existing.getId().equals(claim.getId())) {
                            FraudDetectionResultDTO.FraudItem item = new FraudDetectionResultDTO.FraudItem();
                            item.setDetectionRule("相同交警报案号检测");
                            item.setFraudType(FraudType.DUPLICATE_CLAIM.name());
                            item.setRiskScore(50);
                            item.setRiskLevel("HIGH");
                            item.setRiskDescription(String.format(
                                    "检测到相同交警报案号已用于其他报案: %s", existing.getClaimNumber()
                            ));
                            item.setEvidence(String.format(
                                    "交警报案号: %s, 已用于报案: %s, 本次报案: %s",
                                    claim.getPoliceReportNumber(), existing.getClaimNumber(), claim.getClaimNumber()
                            ));
                            items.add(item);
                        }
                    });
        }

        if (claim.getRelatedAccidentId() != null && !claim.getRelatedAccidentId().isEmpty()) {
            claim.setMultiplePoliciesInvolved(true);
        }

        return items;
    }

    private List<FraudDetectionResultDTO.FraudItem> detectTimeConflicts(ClaimReport claim) {
        List<FraudDetectionResultDTO.FraudItem> items = new ArrayList<>();

        if (claim.getPolicy() == null || claim.getPolicy().getInsuredIdCard() == null) {
            return items;
        }

        LocalDateTime startTime = claim.getAccidentTime().minusMinutes(config.getTimeConflictWindowMinutes());
        LocalDateTime endTime = claim.getAccidentTime().plusMinutes(config.getTimeConflictWindowMinutes());

        List<HistoricalClaim> historicalClaims = historicalClaimRepository.findByIdCardAndDateRange(
                claim.getPolicy().getInsuredIdCard(), startTime, endTime
        );

        for (HistoricalClaim hc : historicalClaims) {
            Duration duration = Duration.between(hc.getAccidentTime(), claim.getAccidentTime());
            long minutesApart = Math.abs(duration.toMinutes());

            if (minutesApart < config.getTimeConflictWindowMinutes()) {
                FraudDetectionResultDTO.FraudItem item = new FraudDetectionResultDTO.FraudItem();
                item.setDetectionRule("时间冲突检测");
                item.setFraudType(FraudType.TIME_CONFLICT.name());
                item.setRiskScore(45);
                item.setRiskLevel("HIGH");
                item.setRiskDescription(String.format(
                        "被保险人在%d分钟内发生两起事故，存在时间冲突: 历史报案 %s",
                        minutesApart, hc.getClaimNumber()
                ));
                item.setEvidence(String.format(
                        "被保险人: %s, 本次事故时间: %s, 历史事故时间: %s, 间隔: %d分钟, 历史报案: %s",
                        claim.getPolicy().getInsuredName(),
                        claim.getAccidentTime(),
                        hc.getAccidentTime(),
                        minutesApart,
                        hc.getClaimNumber()
                ));
                items.add(item);
            }
        }

        if (claim.getReportTime() != null && claim.getAccidentTime() != null) {
            Duration delay = Duration.between(claim.getAccidentTime(), claim.getReportTime());
            long hoursDelay = delay.toHours();

            if (hoursDelay > 48) {
                FraudDetectionResultDTO.FraudItem item = new FraudDetectionResultDTO.FraudItem();
                item.setDetectionRule("延迟报案检测");
                item.setFraudType(FraudType.TIME_CONFLICT.name());
                item.setRiskScore(25);
                item.setRiskLevel("MEDIUM");
                item.setRiskDescription(String.format(
                        "报案时间距事故时间超过48小时（实际延迟%d小时），存在异常", hoursDelay
                ));
                item.setEvidence(String.format(
                        "事故时间: %s, 报案时间: %s, 延迟: %d小时",
                        claim.getAccidentTime(), claim.getReportTime(), hoursDelay
                ));
                items.add(item);
            }
        }

        return items;
    }

    private List<FraudDetectionResultDTO.FraudItem> detectImageTampering(ClaimReport claim) {
        List<FraudDetectionResultDTO.FraudItem> items = new ArrayList<>();

        if (!config.isImageTamperCheckEnabled()) {
            return items;
        }

        List<AccidentPhoto> photos = accidentPhotoRepository.findByClaimId(claim.getId());

        for (AccidentPhoto photo : photos) {
            if (photo.getPhotoHash() != null) {
                String cacheKey = PHOTO_HASH_PREFIX + photo.getPhotoHash();
                Boolean hashExists = redisTemplate.hasKey(cacheKey);

                if (Boolean.TRUE.equals(hashExists)) {
                    String existingClaim = (String) redisTemplate.opsForValue().get(cacheKey);
                    if (!String.valueOf(claim.getId()).equals(existingClaim)) {
                        FraudDetectionResultDTO.FraudItem item = new FraudDetectionResultDTO.FraudItem();
                        item.setDetectionRule("照片重复使用检测");
                        item.setFraudType(FraudType.IMAGE_TAMPERING.name());
                        item.setRiskScore(35);
                        item.setRiskLevel("HIGH");
                        item.setRiskDescription(String.format(
                                "照片哈希值已在其他报案中使用: %s", existingClaim
                        ));
                        item.setEvidence(String.format(
                                "照片文件名: %s, 哈希值: %s, 已用于报案: %s",
                                photo.getFileName(), photo.getPhotoHash(), existingClaim
                        ));
                        items.add(item);

                        photo.setTamperDetected(true);
                        photo.setTamperConfidence(90);
                        accidentPhotoRepository.save(photo);
                    }
                } else {
                    redisTemplate.opsForValue().set(cacheKey, String.valueOf(claim.getId()), 30, TimeUnit.DAYS);
                }

                List<AccidentPhoto> existingPhotos = accidentPhotoRepository.findByPhotoHashExcludingId(
                        photo.getPhotoHash(), photo.getId()
                );

                if (!existingPhotos.isEmpty()) {
                    for (AccidentPhoto existing : existingPhotos) {
                        FraudDetectionResultDTO.FraudItem item = new FraudDetectionResultDTO.FraudItem();
                        item.setDetectionRule("照片重复使用检测（数据库）");
                        item.setFraudType(FraudType.IMAGE_TAMPERING.name());
                        item.setRiskScore(35);
                        item.setRiskLevel("HIGH");
                        item.setRiskDescription(String.format(
                                "相同照片已用于其他报案: %s", existing.getClaim().getClaimNumber()
                        ));
                        item.setEvidence(String.format(
                                "照片哈希值: %s, 本次报案: %s, 已用报案: %s",
                                photo.getPhotoHash(), claim.getClaimNumber(), existing.getClaim().getClaimNumber()
                        ));
                        items.add(item);
                    }

                    photo.setTamperDetected(true);
                    photo.setTamperConfidence(95);
                    accidentPhotoRepository.save(photo);
                }
            }

            if (Boolean.TRUE.equals(photo.getTamperDetected())) {
                FraudDetectionResultDTO.FraudItem item = new FraudDetectionResultDTO.FraudItem();
                item.setDetectionRule("影像篡改检测");
                item.setFraudType(FraudType.IMAGE_TAMPERING.name());
                item.setRiskScore(photo.getTamperConfidence() != null ? photo.getTamperConfidence() : 40);
                item.setRiskLevel("HIGH");
                item.setRiskDescription(String.format(
                        "检测到照片可能被篡改: %s", photo.getFileName()
                ));
                item.setEvidence(String.format(
                        "照片: %s, 篡改置信度: %d%%",
                        photo.getFileName(), photo.getTamperConfidence()
                ));
                items.add(item);
            }

            if (photo.getGpsLatitude() != null && photo.getGpsLongitude() != null
                    && claim.getAccidentLatitude() != null && claim.getAccidentLongitude() != null) {

                double distance = DistanceUtil.calculateDistanceMeters(
                        photo.getGpsLatitude(), photo.getGpsLongitude(),
                        claim.getAccidentLatitude(), claim.getAccidentLongitude()
                );

                if (distance > 5000) {
                    FraudDetectionResultDTO.FraudItem item = new FraudDetectionResultDTO.FraudItem();
                    item.setDetectionRule("照片GPS位置异常检测");
                    item.setFraudType(FraudType.IMAGE_TAMPERING.name());
                    item.setRiskScore(30);
                    item.setRiskLevel("MEDIUM");
                    item.setRiskDescription(String.format(
                            "照片拍摄位置与事故地点相距%.0f米，存在异常", distance
                    ));
                    item.setEvidence(String.format(
                            "照片: %s, 拍摄位置: (%.6f, %.6f), 事故位置: (%.6f, %.6f), 距离: %.0f米",
                            photo.getFileName(),
                            photo.getGpsLatitude(), photo.getGpsLongitude(),
                            claim.getAccidentLatitude(), claim.getAccidentLongitude(),
                            distance
                    ));
                    items.add(item);
                }
            }

            if (photo.getCaptureTime() != null && claim.getAccidentTime() != null) {
                Duration diff = Duration.between(claim.getAccidentTime(), photo.getCaptureTime());
                long hoursDiff = Math.abs(diff.toHours());

                if (hoursDiff > 72) {
                    FraudDetectionResultDTO.FraudItem item = new FraudDetectionResultDTO.FraudItem();
                    item.setDetectionRule("照片拍摄时间异常检测");
                    item.setFraudType(FraudType.IMAGE_TAMPERING.name());
                    item.setRiskScore(25);
                    item.setRiskLevel("MEDIUM");
                    item.setRiskDescription(String.format(
                            "照片拍摄时间与事故时间相差%d小时，存在异常", hoursDiff
                    ));
                    item.setEvidence(String.format(
                            "照片: %s, 拍摄时间: %s, 事故时间: %s, 相差: %d小时",
                            photo.getFileName(), photo.getCaptureTime(), claim.getAccidentTime(), hoursDiff
                    ));
                    items.add(item);
                }
            }
        }

        return items;
    }

    private List<FraudDetectionResultDTO.FraudItem> detectAbnormalAmount(ClaimReport claim) {
        List<FraudDetectionResultDTO.FraudItem> items = new ArrayList<>();

        if (claim.getClaimedAmount() == null) {
            return items;
        }

        BigDecimal avgCompensation = historicalClaimRepository.findAverageCompensationByAccidentType(
                claim.getAccidentType()
        );

        if (avgCompensation != null && avgCompensation.compareTo(BigDecimal.ZERO) > 0) {
            BigDecimal multiplier = claim.getClaimedAmount()
                    .divide(avgCompensation, 2, RoundingMode.HALF_UP);

            if (multiplier.compareTo(BigDecimal.valueOf(config.getAbnormalAmountThresholdMultiplier())) > 0) {
                FraudDetectionResultDTO.FraudItem item = new FraudDetectionResultDTO.FraudItem();
                item.setDetectionRule("理赔金额异常检测");
                item.setFraudType(FraudType.ABNORMAL_AMOUNT.name());
                item.setRiskScore(40);
                item.setRiskLevel("HIGH");
                item.setRiskDescription(String.format(
                        "索赔金额%.2f元是同类型事故平均赔付%.2f元的%.2f倍，超过阈值%.1f倍",
                        claim.getClaimedAmount(), avgCompensation, multiplier,
                        config.getAbnormalAmountThresholdMultiplier()
                ));
                item.setEvidence(String.format(
                        "事故类型: %s, 索赔金额: %.2f元, 历史平均赔付: %.2f元, 倍数: %.2f, 阈值: %.1f倍",
                        claim.getAccidentType(), claim.getClaimedAmount(),
                        avgCompensation, multiplier, config.getAbnormalAmountThresholdMultiplier()
                ));
                items.add(item);
            }
        }

        List<HospitalInvoice> invoices = hospitalInvoiceRepository.findByClaimId(claim.getId());
        for (HospitalInvoice invoice : invoices) {
            if (Boolean.TRUE.equals(invoice.getAmountAbnormal())) {
                FraudDetectionResultDTO.FraudItem item = new FraudDetectionResultDTO.FraudItem();
                item.setDetectionRule("医院票据金额异常检测");
                item.setFraudType(FraudType.ABNORMAL_AMOUNT.name());
                item.setRiskScore(35);
                item.setRiskLevel("HIGH");
                item.setRiskDescription(String.format(
                        "医院票据金额异常: %s, 金额: %.2f元",
                        invoice.getInvoiceNumber(), invoice.getTotalAmount()
                ));
                item.setEvidence(String.format(
                        "票据号: %s, 医院: %s, 总金额: %.2f元, 异常标记: %s",
                        invoice.getInvoiceNumber(), invoice.getHospitalName(),
                        invoice.getTotalAmount(), invoice.getAmountAbnormal()
                ));
                items.add(item);
            }
        }

        List<RepairQuote> quotes = repairQuoteRepository.findByClaimId(claim.getId());
        for (RepairQuote quote : quotes) {
            if (Boolean.TRUE.equals(quote.getAmountAbnormal())) {
                FraudDetectionResultDTO.FraudItem item = new FraudDetectionResultDTO.FraudItem();
                item.setDetectionRule("维修报价金额异常检测");
                item.setFraudType(FraudType.ABNORMAL_AMOUNT.name());
                item.setRiskScore(35);
                item.setRiskLevel("HIGH");
                item.setRiskDescription(String.format(
                        "维修报价金额异常: %s, 偏离市场均价%d%%",
                        quote.getQuoteNumber(), quote.getAmountDeviationPercent()
                ));
                item.setEvidence(String.format(
                        "报价单号: %s, 修理厂: %s, 报价金额: %.2f元, 市场均价: %.2f元, 偏离度: %d%%",
                        quote.getQuoteNumber(), quote.getRepairShopName(),
                        quote.getTotalQuoteAmount(), quote.getMarketAverageAmount(),
                        quote.getAmountDeviationPercent()
                ));
                items.add(item);
            }

            if (Boolean.TRUE.equals(quote.getUnusualPartsDetected())) {
                FraudDetectionResultDTO.FraudItem item = new FraudDetectionResultDTO.FraudItem();
                item.setDetectionRule("异常配件检测");
                item.setFraudType(FraudType.ABNORMAL_AMOUNT.name());
                item.setRiskScore(30);
                item.setRiskLevel("MEDIUM");
                item.setRiskDescription(String.format(
                        "维修报价中检测到异常配件: %s", quote.getUnusualPartsDescription()
                ));
                item.setEvidence(String.format(
                        "报价单号: %s, 异常配件说明: %s",
                        quote.getQuoteNumber(), quote.getUnusualPartsDescription()
                ));
                items.add(item);
            }
        }

        return items;
    }

    private List<FraudDetectionResultDTO.FraudItem> detectRelatedPersonFraud(ClaimReport claim) {
        List<FraudDetectionResultDTO.FraudItem> items = new ArrayList<>();

        if (claim.getPolicy() == null) {
            return items;
        }

        String insuredIdCard = claim.getPolicy().getInsuredIdCard();
        if (insuredIdCard == null) {
            return items;
        }

        List<RelatedPerson> relations = relatedPersonRepository.findRelationsUpToDepth(
                insuredIdCard, config.getRelatedPersonDepth()
        );

        for (RelatedPerson relation : relations) {
            String relatedIdCard = relation.getPersonIdCard().equals(insuredIdCard)
                    ? relation.getRelatedPersonIdCard()
                    : relation.getPersonIdCard();

            List<HistoricalClaim> fraudClaims = historicalClaimRepository.findFraudClaimsByIdCard(relatedIdCard);
            if (!fraudClaims.isEmpty()) {
                FraudDetectionResultDTO.FraudItem item = new FraudDetectionResultDTO.FraudItem();
                item.setDetectionRule("关联人员历史欺诈检测");
                item.setFraudType(FraudType.RELATED_PERSON_FRAUD.name());
                item.setRiskScore(45);
                item.setRiskLevel("HIGH");
                item.setRiskDescription(String.format(
                        "检测到%s度关联人员存在历史欺诈记录: %s",
                        relation.getRelationDegree(), relation.getRelationType()
                ));
                item.setEvidence(String.format(
                        "被保险人: %s, 关联人员: %s, 关系: %s, 关联度: %d度, " +
                                "关联人员历史欺诈报案数: %d, 欺诈报案: %s",
                        claim.getPolicy().getInsuredName(),
                        relation.getPersonIdCard().equals(insuredIdCard)
                                ? relation.getRelatedPersonName() : relation.getPersonName(),
                        relation.getRelationType(),
                        relation.getRelationDegree(),
                        fraudClaims.size(),
                        fraudClaims.get(0).getClaimNumber()
                ));
                items.add(item);
            }

            long relatedClaimCount = historicalClaimRepository.countTotalClaimsByIdCard(relatedIdCard);
            long relatedFraudCount = historicalClaimRepository.countFraudClaimsByIdCard(relatedIdCard);

            if (relatedClaimCount >= 5 && relatedFraudCount > 0) {
                double fraudRate = (double) relatedFraudCount / relatedClaimCount;
                if (fraudRate > 0.2) {
                    FraudDetectionResultDTO.FraudItem item = new FraudDetectionResultDTO.FraudItem();
                    item.setDetectionRule("关联人员高欺诈率检测");
                    item.setFraudType(FraudType.RELATED_PERSON_FRAUD.name());
                    item.setRiskScore(35);
                    item.setRiskLevel("HIGH");
                    item.setRiskDescription(String.format(
                            "%s度关联人员历史报案欺诈率达%.1f%%，超过阈值",
                            relation.getRelationDegree(), fraudRate * 100
                    ));
                    item.setEvidence(String.format(
                            "关联人员历史报案数: %d, 其中欺诈报案数: %d, 欺诈率: %.1f%%",
                            relatedClaimCount, relatedFraudCount, fraudRate * 100
                    ));
                    items.add(item);
                }
            }
        }

        if (claim.getThirdPartyName() != null && claim.getThirdPartyInvolved() == Boolean.TRUE) {
            boolean isRelated = relatedPersonRepository.areRelatedWithinDepth(
                    insuredIdCard, claim.getThirdPartyName(), config.getRelatedPersonDepth()
            );

            if (isRelated) {
                FraudDetectionResultDTO.FraudItem item = new FraudDetectionResultDTO.FraudItem();
                item.setDetectionRule("事故双方关联检测");
                item.setFraudType(FraudType.RELATED_PERSON_FRAUD.name());
                item.setRiskScore(50);
                item.setRiskLevel("HIGH");
                item.setRiskDescription("事故双方存在关联关系，涉嫌保险欺诈");
                item.setEvidence(String.format(
                        "被保险人: %s, 第三方: %s, 双方存在%d度以内关联关系",
                        claim.getPolicy().getInsuredName(), claim.getThirdPartyName(),
                        config.getRelatedPersonDepth()
                ));
                items.add(item);
            }
        }

        return items;
    }

    private List<FraudDetectionResultDTO.FraudItem> detectCrossRegionSuspicion(ClaimReport claim) {
        List<FraudDetectionResultDTO.FraudItem> items = new ArrayList<>();

        if (!config.isCrossRegionCheckEnabled()) {
            return items;
        }

        if (claim.getPolicy() == null || claim.getAccidentRegion() == null) {
            return items;
        }

        String policyRegion = claim.getPolicy().getIssuingRegion();
        String accidentRegion = claim.getAccidentRegion();

        if (policyRegion != null && !policyRegion.equals(accidentRegion)) {
            FraudDetectionResultDTO.FraudItem item = new FraudDetectionResultDTO.FraudItem();
            item.setDetectionRule("跨地区报案检测");
            item.setFraudType(FraudType.CROSS_REGION_SUSPICION.name());
            item.setRiskScore(20);
            item.setRiskLevel("LOW");
            item.setRiskDescription(String.format(
                    "事故发生地%s与保单签发地%s不一致", accidentRegion, policyRegion
            ));
            item.setEvidence(String.format(
                    "保单签发地: %s, 事故发生地: %s, 被保险人: %s",
                    policyRegion, accidentRegion, claim.getPolicy().getInsuredName()
            ));
            items.add(item);
        }

        if (claim.getPoliceStation() != null && claim.getAccidentRegion() != null) {
            List<HistoricalClaim> similarAccidents = historicalClaimRepository.findSimilarAccidents(
                    claim.getAccidentType(),
                    claim.getAccidentLatitude(),
                    claim.getAccidentLongitude(),
                    claim.getAccidentTime().minusDays(90),
                    claim.getAccidentTime()
            );

            if (!similarAccidents.isEmpty()) {
                for (HistoricalClaim similar : similarAccidents) {
                    if (similar.getFraudConfirmed() == Boolean.TRUE) {
                        FraudDetectionResultDTO.FraudItem item = new FraudDetectionResultDTO.FraudItem();
                        item.setDetectionRule("历史欺诈高发区域检测");
                        item.setFraudType(FraudType.CROSS_REGION_SUSPICION.name());
                        item.setRiskScore(35);
                        item.setRiskLevel("HIGH");
                        item.setRiskDescription(String.format(
                                "事故发生区域90天内存在已确认的欺诈报案: %s", similar.getClaimNumber()
                        ));
                        item.setEvidence(String.format(
                                "本区域90天内相似事故: %s, 其中已确认欺诈: %s, 欺诈类型: %s",
                                similar.getClaimNumber(), similar.getFraudConfirmed(), similar.getFraudType()
                        ));
                        items.add(item);
                    }
                }
            }
        }

        return items;
    }

    private List<FraudDetectionResultDTO.FraudItem> detectInvoiceReuse(ClaimReport claim) {
        List<FraudDetectionResultDTO.FraudItem> items = new ArrayList<>();

        List<HospitalInvoice> invoices = hospitalInvoiceRepository.findByClaimId(claim.getId());

        for (HospitalInvoice invoice : invoices) {
            if (invoice.getInvoiceHash() != null) {
                String cacheKey = INVOICE_HASH_PREFIX + invoice.getInvoiceHash();
                Boolean hashExists = redisTemplate.hasKey(cacheKey);

                if (Boolean.TRUE.equals(hashExists)) {
                    String existingClaim = (String) redisTemplate.opsForValue().get(cacheKey);
                    if (!String.valueOf(claim.getId()).equals(existingClaim)) {
                        FraudDetectionResultDTO.FraudItem item = new FraudDetectionResultDTO.FraudItem();
                        item.setDetectionRule("票据重复使用检测（缓存）");
                        item.setFraudType(FraudType.INVOICE_REUSE.name());
                        item.setRiskScore(45);
                        item.setRiskLevel("HIGH");
                        item.setRiskDescription(String.format(
                                "票据哈希值已在其他报案中使用: %s", existingClaim
                        ));
                        item.setEvidence(String.format(
                                "票据号: %s, 医院: %s, 哈希值: %s, 已用于报案: %s",
                                invoice.getInvoiceNumber(), invoice.getHospitalName(),
                                invoice.getInvoiceHash(), existingClaim
                        ));
                        items.add(item);

                        invoice.setDuplicateFound(true);
                        invoice.setDuplicateClaimId(existingClaim);
                        hospitalInvoiceRepository.save(invoice);
                    }
                } else {
                    redisTemplate.opsForValue().set(cacheKey, String.valueOf(claim.getId()), 365, TimeUnit.DAYS);
                }

                if (invoice.getInvoiceNumber() != null) {
                    List<HospitalInvoice> existing = hospitalInvoiceRepository.findByInvoiceNumberExcludingId(
                            invoice.getInvoiceNumber(), invoice.getId()
                    );

                    if (!existing.isEmpty()) {
                        for (HospitalInvoice dup : existing) {
                            FraudDetectionResultDTO.FraudItem item = new FraudDetectionResultDTO.FraudItem();
                            item.setDetectionRule("票据号重复使用检测");
                            item.setFraudType(FraudType.INVOICE_REUSE.name());
                            item.setRiskScore(50);
                            item.setRiskLevel("HIGH");
                            item.setRiskDescription(String.format(
                                    "相同票据号已用于其他报案: %s", dup.getClaim().getClaimNumber()
                            ));
                            item.setEvidence(String.format(
                                    "票据号: %s, 医院: %s, 本次报案: %s, 已用报案: %s",
                                    invoice.getInvoiceNumber(), invoice.getHospitalName(),
                                    claim.getClaimNumber(), dup.getClaim().getClaimNumber()
                            ));
                            items.add(item);
                        }

                        invoice.setDuplicateFound(true);
                        hospitalInvoiceRepository.save(invoice);
                    }
                }
            }

            if (invoice.getPatientIdCard() != null && invoice.getAdmissionDate() != null
                    && invoice.getDischargeDate() != null) {
                List<HospitalInvoice> samePeriod = hospitalInvoiceRepository.findByPatientAndDateRange(
                        invoice.getPatientIdCard(),
                        invoice.getAdmissionDate(),
                        invoice.getDischargeDate(),
                        invoice.getId()
                );

                if (!samePeriod.isEmpty()) {
                    for (HospitalInvoice dup : samePeriod) {
                        FraudDetectionResultDTO.FraudItem item = new FraudDetectionResultDTO.FraudItem();
                        item.setDetectionRule("同一患者同期票据重复检测");
                        item.setFraudType(FraudType.INVOICE_REUSE.name());
                        item.setRiskScore(40);
                        item.setRiskLevel("HIGH");
                        item.setRiskDescription(String.format(
                                "同一患者在相同住院时段存在其他票据: %s", dup.getInvoiceNumber()
                        ));
                        item.setEvidence(String.format(
                                "患者: %s, 住院时段: %s 至 %s, 本次票据: %s, 重复票据: %s",
                                invoice.getPatientName(), invoice.getAdmissionDate(), invoice.getDischargeDate(),
                                invoice.getInvoiceNumber(), dup.getInvoiceNumber()
                        ));
                        items.add(item);
                    }
                }
            }
        }

        return items;
    }

    private List<FraudDetectionResultDTO.FraudItem> detectMultiplePolicyFraud(ClaimReport claim) {
        List<FraudDetectionResultDTO.FraudItem> items = new ArrayList<>();

        if (claim.getPolicy() == null || claim.getPolicy().getInsuredIdCard() == null) {
            return items;
        }

        if (claim.getRelatedAccidentId() != null && !claim.getRelatedAccidentId().isEmpty()) {
            List<ClaimReport> relatedClaims = claimReportRepository.findRelatedAccidents(claim.getRelatedAccidentId());

            if (relatedClaims.size() > 1) {
                long differentPolicies = relatedClaims.stream()
                        .map(c -> c.getPolicy().getId())
                        .distinct()
                        .count();

                if (differentPolicies > 1) {
                    FraudDetectionResultDTO.FraudItem item = new FraudDetectionResultDTO.FraudItem();
                    item.setDetectionRule("同一事故多保单欺诈检测");
                    item.setFraudType(FraudType.MULTI_POLICY_FRAUD.name());
                    item.setRiskScore(45);
                    item.setRiskLevel("HIGH");
                    item.setRiskDescription(String.format(
                            "同一事故关联%d个不同保单，涉嫌多保多赔", differentPolicies
                    ));
                    item.setEvidence(String.format(
                            "关联事故ID: %s, 关联报案数: %d, 涉及不同保单数: %d, " +
                                    "涉及保单人: %s",
                            claim.getRelatedAccidentId(), relatedClaims.size(), differentPolicies,
                            relatedClaims.stream()
                                    .map(c -> c.getPolicy().getPolicyHolderName())
                                    .distinct()
                                    .toList()
                    ));
                    items.add(item);

                    claim.setMultiplePoliciesInvolved(true);
                }
            }
        }

        List<InsurancePolicy> activePolicies = insurancePolicyRepository.findActivePoliciesByIdCardAndDate(
                claim.getPolicy().getInsuredIdCard(),
                claim.getAccidentTime().toLocalDate()
        );

        if (activePolicies.size() > 1) {
            boolean sameTypeMultiple = activePolicies.stream()
                    .map(InsurancePolicy::getPolicyType)
                    .distinct()
                    .count() < activePolicies.size();

            if (sameTypeMultiple) {
                FraudDetectionResultDTO.FraudItem item = new FraudDetectionResultDTO.FraudItem();
                item.setDetectionRule("同一被保险人多份同类保单检测");
                item.setFraudType(FraudType.MULTI_POLICY_FRAUD.name());
                item.setRiskScore(30);
                item.setRiskLevel("MEDIUM");
                item.setRiskDescription(String.format(
                        "被保险人在事故发生时持有%d份有效保单，其中存在重复类型", activePolicies.size()
                ));
                item.setEvidence(String.format(
                        "被保险人: %s, 有效保单数: %d, 保单号: %s",
                        claim.getPolicy().getInsuredName(), activePolicies.size(),
                        activePolicies.stream().map(InsurancePolicy::getPolicyNumber).toList()
                ));
                items.add(item);
            }
        }

        return items;
    }

    private List<FraudDetectionResultDTO.FraudItem> checkBlacklist(ClaimReport claim) {
        List<FraudDetectionResultDTO.FraudItem> items = new ArrayList<>();
        LocalDateTime now = LocalDateTime.now();

        if (claim.getPolicy() != null) {
            if (claim.getPolicy().getPolicyHolderIdCard() != null) {
                boolean blacklisted = blacklistClueRepository.isPersonBlacklisted(
                        claim.getPolicy().getPolicyHolderIdCard(), now
                );
                if (blacklisted) {
                    FraudDetectionResultDTO.FraudItem item = new FraudDetectionResultDTO.FraudItem();
                    item.setDetectionRule("黑名单检测-投保人");
                    item.setFraudType(FraudType.OTHER.name());
                    item.setRiskScore(60);
                    item.setRiskLevel("CRITICAL");
                    item.setRiskDescription("投保人在黑名单中，需重点核查");
                    item.setEvidence(String.format(
                            "投保人: %s, 身份证号: %s, 在黑名单中",
                            claim.getPolicy().getPolicyHolderName(),
                            claim.getPolicy().getPolicyHolderIdCard()
                    ));
                    items.add(item);
                }
            }

            if (claim.getPolicy().getInsuredIdCard() != null) {
                boolean blacklisted = blacklistClueRepository.isPersonBlacklisted(
                        claim.getPolicy().getInsuredIdCard(), now
                );
                if (blacklisted) {
                    FraudDetectionResultDTO.FraudItem item = new FraudDetectionResultDTO.FraudItem();
                    item.setDetectionRule("黑名单检测-被保险人");
                    item.setFraudType(FraudType.OTHER.name());
                    item.setRiskScore(60);
                    item.setRiskLevel("CRITICAL");
                    item.setRiskDescription("被保险人在黑名单中，需重点核查");
                    item.setEvidence(String.format(
                            "被保险人: %s, 身份证号: %s, 在黑名单中",
                            claim.getPolicy().getInsuredName(),
                            claim.getPolicy().getInsuredIdCard()
                    ));
                    items.add(item);
                }
            }
        }

        if (claim.getThirdPartyName() != null && claim.getThirdPartyPhone() != null) {
            List<BlacklistClue> blacklist = blacklistClueRepository.findActiveBlacklistByPersonIdCard(
                    claim.getThirdPartyPhone(), now
            );
            if (!blacklist.isEmpty()) {
                FraudDetectionResultDTO.FraudItem item = new FraudDetectionResultDTO.FraudItem();
                item.setDetectionRule("黑名单检测-第三方");
                item.setFraudType(FraudType.OTHER.name());
                item.setRiskScore(55);
                item.setRiskLevel("CRITICAL");
                item.setRiskDescription("事故第三方在黑名单中");
                item.setEvidence(String.format(
                        "第三方: %s, 在黑名单中, 线索编号: %s",
                        claim.getThirdPartyName(), blacklist.get(0).getClueNumber()
                ));
                items.add(item);
            }
        }

        return items;
    }

    private List<FraudDetectionResultDTO.FraudItem> detectHistoricalFraudPatterns(ClaimReport claim) {
        List<FraudDetectionResultDTO.FraudItem> items = new ArrayList<>();

        if (claim.getPolicy() == null || claim.getPolicy().getInsuredIdCard() == null) {
            return items;
        }

        String insuredIdCard = claim.getPolicy().getInsuredIdCard();
        long totalClaims = historicalClaimRepository.countTotalClaimsByIdCard(insuredIdCard);
        long fraudClaims = historicalClaimRepository.countFraudClaimsByIdCard(insuredIdCard);

        if (totalClaims >= 3) {
            double fraudRate = (double) fraudClaims / totalClaims;

            if (fraudRate > 0.3) {
                FraudDetectionResultDTO.FraudItem item = new FraudDetectionResultDTO.FraudItem();
                item.setDetectionRule("历史高欺诈率检测");
                item.setFraudType(FraudType.OTHER.name());
                item.setRiskScore(40);
                item.setRiskLevel("HIGH");
                item.setRiskDescription(String.format(
                        "被保险人历史报案欺诈率达%.1f%%，属于高风险客户", fraudRate * 100
                ));
                item.setEvidence(String.format(
                        "被保险人: %s, 历史报案数: %d, 其中欺诈数: %d, 欺诈率: %.1f%%",
                        claim.getPolicy().getInsuredName(), totalClaims, fraudClaims, fraudRate * 100
                ));
                items.add(item);
            }

            if (totalClaims >= 10) {
                FraudDetectionResultDTO.FraudItem item = new FraudDetectionResultDTO.FraudItem();
                item.setDetectionRule("高频报案检测");
                item.setFraudType(FraudType.OTHER.name());
                item.setRiskScore(25);
                item.setRiskLevel("MEDIUM");
                item.setRiskDescription(String.format(
                        "被保险人历史报案达%d次，属于高频报案客户", totalClaims
                ));
                item.setEvidence(String.format(
                        "被保险人: %s, 历史报案数: %d",
                        claim.getPolicy().getInsuredName(), totalClaims
                ));
                items.add(item);
            }
        }

        return items;
    }

    private List<FraudDetectionResultDTO.FraudItem> checkSurveyConsistency(ClaimReport claim) {
        List<FraudDetectionResultDTO.FraudItem> items = new ArrayList<>();

        List<SurveyRecord> surveys = surveyRecordRepository.findByClaimId(claim.getId());

        for (SurveyRecord survey : surveys) {
            if (survey.getSiteLatitude() != null && survey.getSiteLongitude() != null
                    && claim.getAccidentLatitude() != null && claim.getAccidentLongitude() != null) {

                double distance = DistanceUtil.calculateDistanceMeters(
                        survey.getSiteLatitude(), survey.getSiteLongitude(),
                        claim.getAccidentLatitude(), claim.getAccidentLongitude()
                );

                survey.setDistanceFromAccidentMeters(distance);
                surveyRecordRepository.save(survey);

                if (distance > 10000) {
                    FraudDetectionResultDTO.FraudItem item = new FraudDetectionResultDTO.FraudItem();
                    item.setDetectionRule("查勘地点与报案地点不一致检测");
                    item.setFraudType(FraudType.OTHER.name());
                    item.setRiskScore(30);
                    item.setRiskLevel("MEDIUM");
                    item.setRiskDescription(String.format(
                            "查勘员现场查勘地点与报案地点相距%.0f米，存在异常", distance
                    ));
                    item.setEvidence(String.format(
                            "查勘员: %s, 查勘位置: (%.6f, %.6f), 报案位置: (%.6f, %.6f), 距离: %.0f米",
                            survey.getSurveyorName(),
                            survey.getSiteLatitude(), survey.getSiteLongitude(),
                            claim.getAccidentLatitude(), claim.getAccidentLongitude(),
                            distance
                    ));
                    items.add(item);
                }
            }

            if (Boolean.TRUE.equals(survey.getFraudSuspicion())) {
                FraudDetectionResultDTO.FraudItem item = new FraudDetectionResultDTO.FraudItem();
                item.setDetectionRule("查勘员欺诈疑点报告");
                item.setFraudType(FraudType.OTHER.name());
                item.setRiskScore(35);
                item.setRiskLevel("HIGH");
                item.setRiskDescription(String.format(
                        "查勘员报告存在欺诈疑点: %s", survey.getFraudSuspicionReason()
                ));
                item.setEvidence(String.format(
                        "查勘员: %s, 疑点说明: %s",
                        survey.getSurveyorName(), survey.getFraudSuspicionReason()
                ));
                items.add(item);
            }

            if (Boolean.FALSE.equals(survey.getStatementConsistent())) {
                FraudDetectionResultDTO.FraudItem item = new FraudDetectionResultDTO.FraudItem();
                item.setDetectionRule("当事人陈述不一致检测");
                item.setFraudType(FraudType.OTHER.name());
                item.setRiskScore(30);
                item.setRiskLevel("MEDIUM");
                item.setRiskDescription(String.format(
                        "当事人陈述存在不一致: %s", survey.getInconsistencyPoints()
                ));
                item.setEvidence(String.format(
                        "被询问人: %s, 不一致点: %s",
                        survey.getIntervieweeName(), survey.getInconsistencyPoints()
                ));
                items.add(item);
            }
        }

        return items;
    }

    private String determineRiskLevel(int totalScore) {
        if (totalScore >= 80) {
            return "CRITICAL";
        } else if (totalScore >= 50) {
            return "HIGH";
        } else if (totalScore >= 30) {
            return "MEDIUM";
        } else if (totalScore >= 10) {
            return "LOW";
        } else {
            return "NONE";
        }
    }

    public void evictFraudCache(Long claimId) {
        String cacheKey = FRAUD_CACHE_PREFIX + claimId;
        redisTemplate.delete(cacheKey);
    }
}
