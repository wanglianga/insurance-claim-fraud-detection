package com.insurance.claim.service;

import com.insurance.claim.dto.*;
import com.insurance.claim.entity.*;
import com.insurance.claim.enums.ClaimStatus;
import com.insurance.claim.enums.MaterialType;
import com.insurance.claim.exception.BusinessException;
import com.insurance.claim.repository.*;
import com.insurance.claim.util.HashUtil;
import com.insurance.claim.util.IdGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class MaterialVerificationService {

    private final AccidentPhotoRepository accidentPhotoRepository;
    private final HospitalInvoiceRepository hospitalInvoiceRepository;
    private final RepairQuoteRepository repairQuoteRepository;
    private final LiabilityDeterminationRepository liabilityDeterminationRepository;
    private final ExpenseItemRepository expenseItemRepository;
    private final ClaimReportRepository claimReportRepository;
    private final SurveyRecordRepository surveyRecordRepository;
    private final FraudDetectionService fraudDetectionService;

    @Transactional
    public AccidentPhoto uploadPhoto(PhotoUploadRequest request) {
        ClaimReport claim = claimReportRepository.findByClaimNumber(request.getClaimNumber())
                .orElseThrow(() -> new BusinessException("CLAIM_NOT_FOUND",
                        "报案不存在: " + request.getClaimNumber()));

        AccidentPhoto photo = new AccidentPhoto();
        photo.setClaim(claim);
        photo.setMaterialType(request.getMaterialType());
        photo.setFileName(request.getFileName());
        photo.setFilePath(request.getFilePath());
        photo.setFileSize(request.getFileSize());
        photo.setContentType(request.getContentType());
        photo.setPhotoHash(request.getPhotoHash());
        photo.setUploaderId(request.getUploaderId());
        photo.setUploaderName(request.getUploaderName());
        photo.setPhotoDescription(request.getPhotoDescription());
        photo.setPhotoTag(request.getPhotoTag());
        photo.setGpsLatitude(request.getGpsLatitude());
        photo.setGpsLongitude(request.getGpsLongitude());
        photo.setCaptureTime(request.getCaptureTime());
        photo.setCameraModel(request.getCameraModel());
        photo.setExifVerified(request.getExifVerified());
        photo.setTamperDetected(false);
        photo.setTamperConfidence(0);
        photo.setVerified(false);

        if (photo.getPhotoHash() == null && photo.getFilePath() != null) {
            photo.setPhotoHash(HashUtil.sha256(photo.getFilePath() + System.currentTimeMillis()));
        }

        photo = accidentPhotoRepository.save(photo);

        checkMaterialsComplete(claim.getId());
        fraudDetectionService.evictFraudCache(claim.getId());

        log.info("照片上传成功，照片ID: {}, 报案号: {}", photo.getId(), claim.getClaimNumber());

        return photo;
    }

    @Transactional
    public HospitalInvoice submitHospitalInvoice(HospitalInvoiceSubmitRequest request) {
        ClaimReport claim = claimReportRepository.findByClaimNumber(request.getClaimNumber())
                .orElseThrow(() -> new BusinessException("CLAIM_NOT_FOUND",
                        "报案不存在: " + request.getClaimNumber()));

        HospitalInvoice invoice = new HospitalInvoice();
        invoice.setClaim(claim);
        invoice.setMaterialType(request.getMaterialType());
        invoice.setInvoiceNumber(request.getInvoiceNumber());
        invoice.setHospitalName(request.getHospitalName());
        invoice.setHospitalLevel(request.getHospitalLevel());
        invoice.setHospitalRegion(request.getHospitalRegion());
        invoice.setPatientName(request.getPatientName());
        invoice.setPatientIdCard(request.getPatientIdCard());
        invoice.setAdmissionDate(request.getAdmissionDate());
        invoice.setDischargeDate(request.getDischargeDate());
        invoice.setTreatmentDays(request.getTreatmentDays());
        invoice.setDepartment(request.getDepartment());
        invoice.setDoctorName(request.getDoctorName());
        invoice.setDiagnosis(request.getDiagnosis());
        invoice.setDiagnosisCode(request.getDiagnosisCode());
        invoice.setTotalAmount(request.getTotalAmount());
        invoice.setMedicalServiceAmount(request.getMedicalServiceAmount());
        invoice.setMedicineAmount(request.getMedicineAmount());
        invoice.setExaminationAmount(request.getExaminationAmount());
        invoice.setTreatmentAmount(request.getTreatmentAmount());
        invoice.setSurgeryAmount(request.getSurgeryAmount());
        invoice.setHospitalizationAmount(request.getHospitalizationAmount());
        invoice.setOtherAmount(request.getOtherAmount());
        invoice.setSelfPayAmount(request.getSelfPayAmount());
        invoice.setReimbursableAmount(request.getReimbursableAmount());
        invoice.setSocialSecurityPaid(request.getSocialSecurityPaid());
        invoice.setCommercialInsurancePaid(request.getCommercialInsurancePaid());
        invoice.setInvoiceFilePath(request.getInvoiceFilePath());
        invoice.setInvoiceFileName(request.getInvoiceFileName());
        invoice.setDetailListFilePath(request.getDetailListFilePath());
        invoice.setStampVerified(request.getStampVerified());
        invoice.setDuplicateFound(false);
        invoice.setAmountAbnormal(false);
        invoice.setUploadTime(request.getUploadTime() != null ? request.getUploadTime() : java.time.LocalDateTime.now());
        invoice.setUploaderId(request.getUploaderId());
        invoice.setVerified(false);

        String hashContent = (request.getInvoiceNumber() != null ? request.getInvoiceNumber() : "")
                + (request.getTotalAmount() != null ? request.getTotalAmount().toString() : "")
                + (request.getPatientIdCard() != null ? request.getPatientIdCard() : "")
                + (request.getAdmissionDate() != null ? request.getAdmissionDate().toString() : "");
        invoice.setInvoiceHash(HashUtil.sha256(hashContent));

        invoice = hospitalInvoiceRepository.save(invoice);

        if (request.getExpenseItems() != null) {
            saveExpenseItems(invoice.getId(), "HOSPITAL_INVOICE",
                    request.getMaterialType(), request.getExpenseItems());
        }

        checkAmountAbnormal(invoice);
        checkMaterialsComplete(claim.getId());
        fraudDetectionService.evictFraudCache(claim.getId());

        log.info("医院票据提交成功，票据号: {}, 报案号: {}",
                invoice.getInvoiceNumber(), claim.getClaimNumber());

        return invoice;
    }

    @Transactional
    public RepairQuote submitRepairQuote(RepairQuoteSubmitRequest request) {
        ClaimReport claim = claimReportRepository.findByClaimNumber(request.getClaimNumber())
                .orElseThrow(() -> new BusinessException("CLAIM_NOT_FOUND",
                        "报案不存在: " + request.getClaimNumber()));

        RepairQuote quote = new RepairQuote();
        quote.setClaim(claim);
        quote.setMaterialType(request.getMaterialType());
        quote.setQuoteNumber(request.getQuoteNumber());
        quote.setRepairShopName(request.getRepairShopName());
        quote.setRepairShopQualification(request.getRepairShopQualification());
        quote.setRepairShopRegion(request.getRepairShopRegion());
        quote.setVehicleInfo(request.getVehicleInfo());
        quote.setVehicleLicensePlate(request.getVehicleLicensePlate());
        quote.setVinNumber(request.getVinNumber());
        quote.setMileage(request.getMileage());
        quote.setRepairType(request.getRepairType());
        quote.setDamageDescription(request.getDamageDescription());
        quote.setTotalLaborCost(request.getTotalLaborCost());
        quote.setTotalPartsCost(request.getTotalPartsCost());
        quote.setTotalQuoteAmount(request.getTotalQuoteAmount());
        quote.setQuoteDate(request.getQuoteDate());
        quote.setEstimatedCompletionDays(request.getEstimatedCompletionDays());
        quote.setQuoteValidityDays(request.getQuoteValidityDays());
        quote.setQuoterName(request.getQuoterName());
        quote.setQuoterPhone(request.getQuoterPhone());
        quote.setQuoteFilePath(request.getQuoteFilePath());
        quote.setQuoteFileName(request.getQuoteFileName());
        quote.setPartsDetailFilePath(request.getPartsDetailFilePath());
        quote.setStampVerified(request.getStampVerified());
        quote.setDuplicateFound(false);
        quote.setAmountAbnormal(false);
        quote.setUnusualPartsDetected(false);
        quote.setUploadTime(request.getUploadTime() != null ? request.getUploadTime() : java.time.LocalDateTime.now());
        quote.setUploaderId(request.getUploaderId());
        quote.setVerified(false);

        String hashContent = (request.getQuoteNumber() != null ? request.getQuoteNumber() : "")
                + (request.getTotalQuoteAmount() != null ? request.getTotalQuoteAmount().toString() : "")
                + (request.getVinNumber() != null ? request.getVinNumber() : "")
                + (request.getQuoteDate() != null ? request.getQuoteDate().toString() : "");
        quote.setQuoteHash(HashUtil.sha256(hashContent));

        quote = repairQuoteRepository.save(quote);

        if (request.getPartsItems() != null) {
            saveExpenseItems(quote.getId(), "REPAIR_QUOTE",
                    request.getMaterialType(), request.getPartsItems());
        }

        checkQuoteAmountAbnormal(quote);
        checkMaterialsComplete(claim.getId());
        fraudDetectionService.evictFraudCache(claim.getId());

        log.info("维修报价提交成功，报价单号: {}, 报案号: {}",
                quote.getQuoteNumber(), claim.getClaimNumber());

        return quote;
    }

    @Transactional
    public LiabilityDetermination submitLiabilityDetermination(LiabilitySubmitRequest request) {
        ClaimReport claim = claimReportRepository.findByClaimNumber(request.getClaimNumber())
                .orElseThrow(() -> new BusinessException("CLAIM_NOT_FOUND",
                        "报案不存在: " + request.getClaimNumber()));

        LiabilityDetermination determination = new LiabilityDetermination();
        determination.setClaim(claim);
        determination.setMaterialType(request.getMaterialType());
        determination.setDeterminationNumber(request.getDeterminationNumber());
        determination.setDeterminingAuthority(request.getDeterminingAuthority());
        determination.setDeterminingAuthorityLevel(request.getDeterminingAuthorityLevel());
        determination.setDeterminingAuthorityRegion(request.getDeterminingAuthorityRegion());
        determination.setDeterminingOfficerName(request.getDeterminingOfficerName());
        determination.setDeterminingOfficerBadge(request.getDeterminingOfficerBadge());
        determination.setDeterminationDate(request.getDeterminationDate());
        determination.setAccidentSummary(request.getAccidentSummary());
        determination.setEvidenceBasis(request.getEvidenceBasis());
        determination.setLawBasis(request.getLawBasis());
        determination.setLiabilityType(request.getLiabilityType());
        determination.setPartyAName(request.getPartyAName());
        determination.setPartyAIdCard(request.getPartyAIdCard());
        determination.setPartyALiabilityPercent(request.getPartyALiabilityPercent());
        determination.setPartyBName(request.getPartyBName());
        determination.setPartyBIdCard(request.getPartyBIdCard());
        determination.setPartyBLiabilityPercent(request.getPartyBLiabilityPercent());
        determination.setOtherPartiesInfo(request.getOtherPartiesInfo());
        determination.setMediationResult(request.getMediationResult());
        determination.setDisputeResolutionMethod(request.getDisputeResolutionMethod());
        determination.setDocumentFilePath(request.getDocumentFilePath());
        determination.setDocumentFileName(request.getDocumentFileName());
        determination.setStampVerified(request.getStampVerified());
        determination.setSignatureVerified(request.getSignatureVerified());
        determination.setDuplicateFound(false);
        determination.setConflictWithSurvey(false);
        determination.setUploadTime(request.getUploadTime() != null ? request.getUploadTime() : java.time.LocalDateTime.now());
        determination.setUploaderId(request.getUploaderId());
        determination.setVerified(false);

        String hashContent = (request.getDeterminationNumber() != null ? request.getDeterminationNumber() : "")
                + (request.getLiabilityType() != null ? request.getLiabilityType().name() : "")
                + (request.getPartyAIdCard() != null ? request.getPartyAIdCard() : "")
                + (request.getPartyBIdCard() != null ? request.getPartyBIdCard() : "")
                + (request.getDeterminationDate() != null ? request.getDeterminationDate().toString() : "");
        determination.setDeterminationHash(HashUtil.sha256(hashContent));

        determination = liabilityDeterminationRepository.save(determination);

        checkLiabilityConflictWithSurvey(determination);
        checkMaterialsComplete(claim.getId());
        fraudDetectionService.evictFraudCache(claim.getId());

        log.info("责任认定书提交成功，认定书号: {}, 报案号: {}",
                determination.getDeterminationNumber(), claim.getClaimNumber());

        return determination;
    }

    private void saveExpenseItems(Long sourceId, String sourceType,
                                  MaterialType materialType, List<ExpenseItemRequest> items) {
        for (ExpenseItemRequest itemRequest : items) {
            ExpenseItem item = new ExpenseItem();
            item.setSourceType(sourceType);
            item.setSourceId(sourceId);
            item.setMaterialType(materialType);
            item.setItemCode(itemRequest.getItemCode());
            item.setItemName(itemRequest.getItemName());
            item.setItemCategory(itemRequest.getItemCategory());
            item.setItemType(itemRequest.getItemType());
            item.setItemSpecification(itemRequest.getItemSpecification());
            item.setUnit(itemRequest.getUnit());
            item.setQuantity(itemRequest.getQuantity());
            item.setUnitPrice(itemRequest.getUnitPrice());
            item.setTotalAmount(itemRequest.getTotalAmount());
            item.setReimbursableAmount(itemRequest.getReimbursableAmount());
            item.setSelfPayAmount(itemRequest.getSelfPayAmount());
            item.setReimbursementRate(itemRequest.getReimbursementRate());
            item.setTreatmentDate(itemRequest.getTreatmentDate());
            item.setDoctorAdvice(itemRequest.getDoctorAdvice());
            item.setMedicalNecessityVerified(true);
            item.setPriceReasonable(true);
            item.setAbnormalDetected(false);

            if (itemRequest.getItemCode() != null) {
                BigDecimal avgPrice = expenseItemRepository.findAveragePriceByItemCode(itemRequest.getItemCode());
                if (avgPrice != null && avgPrice.compareTo(BigDecimal.ZERO) > 0) {
                    item.setMarketAveragePrice(avgPrice);
                    if (itemRequest.getUnitPrice() != null) {
                        BigDecimal deviation = itemRequest.getUnitPrice()
                                .subtract(avgPrice)
                                .divide(avgPrice, 4, java.math.RoundingMode.HALF_UP)
                                .multiply(new BigDecimal("100"));
                        item.setPriceDeviationPercent(deviation.intValue());

                        if (Math.abs(deviation.intValue()) > 50) {
                            item.setPriceReasonable(false);
                            item.setAbnormalDetected(true);
                            item.setAbnormalType("PRICE_DEVIATION");
                            item.setAbnormalDescription(String.format(
                                    "价格偏离市场均价%d%%", deviation.intValue()
                            ));
                        }
                    }
                }
            }

            expenseItemRepository.save(item);
        }
    }

    private void checkAmountAbnormal(HospitalInvoice invoice) {
        if (invoice.getTotalAmount() == null) {
            return;
        }

        BigDecimal avgAmount = expenseItemRepository.findAveragePriceByItemCode("HOSPITAL_TOTAL_" + invoice.getDiagnosisCode());
        if (avgAmount == null || avgAmount.compareTo(BigDecimal.ZERO) == 0) {
            avgAmount = new BigDecimal("5000");
        }

        BigDecimal deviation = invoice.getTotalAmount()
                .subtract(avgAmount)
                .divide(avgAmount, 4, java.math.RoundingMode.HALF_UP)
                .multiply(new BigDecimal("100"));

        if (Math.abs(deviation.intValue()) > 100) {
            invoice.setAmountAbnormal(true);
            hospitalInvoiceRepository.save(invoice);
        }
    }

    private void checkQuoteAmountAbnormal(RepairQuote quote) {
        if (quote.getTotalQuoteAmount() == null) {
            return;
        }

        BigDecimal estimatedTotal = (quote.getTotalLaborCost() != null ? quote.getTotalLaborCost() : BigDecimal.ZERO)
                .add(quote.getTotalPartsCost() != null ? quote.getTotalPartsCost() : BigDecimal.ZERO);

        BigDecimal difference = quote.getTotalQuoteAmount().subtract(estimatedTotal);
        if (estimatedTotal.compareTo(BigDecimal.ZERO) > 0) {
            BigDecimal deviationPercent = difference
                    .divide(estimatedTotal, 4, java.math.RoundingMode.HALF_UP)
                    .multiply(new BigDecimal("100"));

            quote.setAmountDeviationPercent(deviationPercent.intValue());

            if (Math.abs(deviationPercent.intValue()) > 30) {
                quote.setAmountAbnormal(true);
            }

            quote.setMarketAverageAmount(estimatedTotal);
        }

        repairQuoteRepository.save(quote);
    }

    private void checkLiabilityConflictWithSurvey(LiabilityDetermination determination) {
        List<SurveyRecord> surveys = surveyRecordRepository
                .findLatestByClaimId(determination.getClaim().getId());

        for (SurveyRecord survey : surveys) {
            if (survey.getLiabilityJudgment() != null
                    && determination.getLiabilityType() != null
                    && !survey.getLiabilityJudgment().equals(determination.getLiabilityType())) {
                determination.setConflictWithSurvey(true);
                determination.setConflictDescription(String.format(
                        "责任认定结果与查勘判断不一致。查勘判断: %s, 责任认定: %s",
                        survey.getLiabilityJudgment(), determination.getLiabilityType()
                ));
                liabilityDeterminationRepository.save(determination);
                break;
            }
        }
    }

    @Transactional
    public void checkMaterialsComplete(Long claimId) {
        List<AccidentPhoto> photos = accidentPhotoRepository.findByClaimId(claimId);
        List<HospitalInvoice> invoices = hospitalInvoiceRepository.findByClaimId(claimId);
        List<RepairQuote> quotes = repairQuoteRepository.findByClaimId(claimId);
        List<LiabilityDetermination> liabilities = liabilityDeterminationRepository.findByClaimId(claimId);
        List<SurveyRecord> surveys = surveyRecordRepository.findLatestByClaimId(claimId);

        ClaimReport claim = claimReportRepository.findById(claimId).orElse(null);
        if (claim == null) {
            return;
        }

        boolean materialsComplete = !photos.isEmpty()
                && !surveys.isEmpty()
                && (!invoices.isEmpty() || !quotes.isEmpty());

        claim.setMaterialsComplete(materialsComplete);

        List<String> missing = new ArrayList<>();
        if (photos.isEmpty()) {
            missing.add("事故照片");
        }
        if (surveys.isEmpty()) {
            missing.add("查勘记录");
        }
        if (invoices.isEmpty() && quotes.isEmpty()) {
            missing.add("费用票据（医院票据或维修报价）");
        }
        if (liabilities.isEmpty() && claim.getPoliceReported() == Boolean.TRUE) {
            missing.add("责任认定书");
        }

        if (!missing.isEmpty()) {
            claim.setMaterialsComplete(false);
            if (claim.getStatus() == ClaimStatus.SUBMITTED
                    || claim.getStatus() == ClaimStatus.MATERIALS_PENDING) {
                claim.setStatus(ClaimStatus.MATERIALS_PENDING);
            }
        } else if (claim.getStatus() == ClaimStatus.MATERIALS_PENDING) {
            if (claim.getManualReviewRequired() == Boolean.TRUE) {
                claim.setStatus(ClaimStatus.MANUAL_REVIEW_REQUIRED);
            } else {
                claim.setStatus(ClaimStatus.REVIEWING);
            }
        }

        claimReportRepository.save(claim);
    }

    @Transactional(readOnly = true)
    public List<AccidentPhoto> getPhotosByClaim(String claimNumber) {
        ClaimReport claim = claimReportRepository.findByClaimNumber(claimNumber)
                .orElseThrow(() -> new BusinessException("CLAIM_NOT_FOUND",
                        "报案不存在: " + claimNumber));
        return accidentPhotoRepository.findByClaimId(claim.getId());
    }

    @Transactional(readOnly = true)
    public List<HospitalInvoice> getInvoicesByClaim(String claimNumber) {
        ClaimReport claim = claimReportRepository.findByClaimNumber(claimNumber)
                .orElseThrow(() -> new BusinessException("CLAIM_NOT_FOUND",
                        "报案不存在: " + claimNumber));
        return hospitalInvoiceRepository.findByClaimId(claim.getId());
    }

    @Transactional(readOnly = true)
    public List<RepairQuote> getQuotesByClaim(String claimNumber) {
        ClaimReport claim = claimReportRepository.findByClaimNumber(claimNumber)
                .orElseThrow(() -> new BusinessException("CLAIM_NOT_FOUND",
                        "报案不存在: " + claimNumber));
        return repairQuoteRepository.findByClaimId(claim.getId());
    }

    @Transactional(readOnly = true)
    public List<LiabilityDetermination> getLiabilitiesByClaim(String claimNumber) {
        ClaimReport claim = claimReportRepository.findByClaimNumber(claimNumber)
                .orElseThrow(() -> new BusinessException("CLAIM_NOT_FOUND",
                        "报案不存在: " + claimNumber));
        return liabilityDeterminationRepository.findByClaimId(claim.getId());
    }

    @Transactional(readOnly = true)
    public List<ExpenseItem> getExpenseItems(String sourceType, Long sourceId) {
        return expenseItemRepository.findBySource(sourceType, sourceId);
    }

    @Transactional
    public AccidentPhoto verifyPhoto(Long photoId, boolean verified, String remark) {
        AccidentPhoto photo = accidentPhotoRepository.findById(photoId)
                .orElseThrow(() -> new BusinessException("PHOTO_NOT_FOUND",
                        "照片不存在: " + photoId));
        photo.setVerified(verified);
        photo.setVerifierRemark(remark);
        return accidentPhotoRepository.save(photo);
    }

    @Transactional
    public HospitalInvoice verifyInvoice(Long invoiceId, boolean verified, String remark) {
        HospitalInvoice invoice = hospitalInvoiceRepository.findById(invoiceId)
                .orElseThrow(() -> new BusinessException("INVOICE_NOT_FOUND",
                        "票据不存在: " + invoiceId));
        invoice.setVerified(verified);
        invoice.setVerifierRemark(remark);
        return hospitalInvoiceRepository.save(invoice);
    }

    @Transactional
    public RepairQuote verifyQuote(Long quoteId, boolean verified, String remark) {
        RepairQuote quote = repairQuoteRepository.findById(quoteId)
                .orElseThrow(() -> new BusinessException("QUOTE_NOT_FOUND",
                        "报价单不存在: " + quoteId));
        quote.setVerified(verified);
        quote.setVerifierRemark(remark);
        return repairQuoteRepository.save(quote);
    }

    @Transactional
    public LiabilityDetermination verifyLiability(Long liabilityId, boolean verified, String remark) {
        LiabilityDetermination liability = liabilityDeterminationRepository.findById(liabilityId)
                .orElseThrow(() -> new BusinessException("LIABILITY_NOT_FOUND",
                        "认定书不存在: " + liabilityId));
        liability.setVerified(verified);
        liability.setVerifierRemark(remark);
        return liabilityDeterminationRepository.save(liability);
    }
}
