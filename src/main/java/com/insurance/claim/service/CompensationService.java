
package com.insurance.claim.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.insurance.claim.dto.CompensationReviewRequest;
import com.insurance.claim.dto.FraudDetectionResultDTO;
import com.insurance.claim.dto.ManualReviewRequest;
import com.insurance.claim.entity.*;
import com.insurance.claim.enums.ClaimStatus;
import com.insurance.claim.enums.ReviewResult;
import com.insurance.claim.exception.BusinessException;
import com.insurance.claim.repository.*;
import com.insurance.claim.util.IdGenerator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CompensationService {

    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(CompensationService.class);
    private final CompensationConclusionRepository conclusionRepository;
    private final ClaimReportRepository claimReportRepository;
    private final FraudDetectionResultRepository fraudDetectionResultRepository;
    private final AccidentPhotoRepository accidentPhotoRepository;
    private final HospitalInvoiceRepository hospitalInvoiceRepository;
    private final RepairQuoteRepository repairQuoteRepository;
    private final LiabilityDeterminationRepository liabilityDeterminationRepository;
    private final SurveyRecordRepository surveyRecordRepository;
    private final HistoricalClaimRepository historicalClaimRepository;
    private final FraudDetectionService fraudDetectionService;
    private final MaterialVerificationService materialVerificationService;

    public CompensationService(CompensationConclusionRepository conclusionRepository, ClaimReportRepository claimReportRepository, FraudDetectionResultRepository fraudDetectionResultRepository, AccidentPhotoRepository accidentPhotoRepository, HospitalInvoiceRepository hospitalInvoiceRepository, RepairQuoteRepository repairQuoteRepository, LiabilityDeterminationRepository liabilityDeterminationRepository, SurveyRecordRepository surveyRecordRepository, HistoricalClaimRepository historicalClaimRepository, FraudDetectionService fraudDetectionService, MaterialVerificationService materialVerificationService) {
        this.conclusionRepository = conclusionRepository;
        this.claimReportRepository = claimReportRepository;
        this.fraudDetectionResultRepository = fraudDetectionResultRepository;
        this.accidentPhotoRepository = accidentPhotoRepository;
        this.hospitalInvoiceRepository = hospitalInvoiceRepository;
        this.repairQuoteRepository = repairQuoteRepository;
        this.liabilityDeterminationRepository = liabilityDeterminationRepository;
        this.surveyRecordRepository = surveyRecordRepository;
        this.historicalClaimRepository = historicalClaimRepository;
        this.fraudDetectionService = fraudDetectionService;
        this.materialVerificationService = materialVerificationService;
    }
    public CompensationConclusion getConclusionByClaim(String claimNumber) {
        ClaimReport claim = claimReportRepository.findByClaimNumber(claimNumber)
                .orElseThrow(() -> new BusinessException("CLAIM_NOT_FOUND",
                        "报案不存在: " + claimNumber));
        return conclusionRepository.findFirstByClaimIdOrderByCreatedAtDesc(claim.getId())
                .orElse(null);
    }
    public List<CompensationConclusion> getConclusionsByReviewResult(ReviewResult result) {
        return conclusionRepository.findByReviewResult(result);
    }
    public List<CompensationConclusion> getFraudConfirmedConclusions() {
        return conclusionRepository.findByFraudConfirmedTrue();
    }
    public List<ClaimReport> getClaimsPendingReview() {
        List<ClaimStatus> pendingStatuses = List.of(
                ClaimStatus.REVIEWING,
                ClaimStatus.MANUAL_REVIEW_REQUIRED,
                ClaimStatus.FRAUD_SUSPECTED
        );
        return claimReportRepository.findAll().stream()
                .filter(c -> pendingStatuses.contains(c.getStatus()))
                .collect(Collectors.toList());
    }
    public List<ClaimReport> getClaimsPendingManualReview() {
        return claimReportRepository.findByStatus(ClaimStatus.MANUAL_REVIEW_REQUIRED);
    }

    @Transactional
    public CompensationConclusion performCompensationReview(CompensationReviewRequest request) {
        ClaimReport claim = claimReportRepository.findByClaimNumber(request.getClaimNumber())
                .orElseThrow(() -> new BusinessException("CLAIM_NOT_FOUND",
                        "报案不存在: " + request.getClaimNumber()));

        if (claim.getStatus() != ClaimStatus.REVIEWING
                && claim.getStatus() != ClaimStatus.MANUAL_REVIEW_REQUIRED
                && claim.getStatus() != ClaimStatus.FRAUD_SUSPECTED) {
            throw new BusinessException("INVALID_CLAIM_STATUS",
                    "当前案件状态不允许审核: " + claim.getStatus());
        }

        materialVerificationService.checkMaterialsComplete(claim.getId());

        FraudDetectionResultDTO fraudResult = fraudDetectionService.performFullFraudDetection(claim.getId());

        if (fraudResult.getManualReviewRequired() && !Boolean.TRUE.equals(request.getManualReviewPerformed())) {
            throw new BusinessException("MANUAL_REVIEW_REQUIRED",
                    "该案件存在欺诈风险，需要人工复核后才能进行赔付审核");
        }

        CompensationConclusion conclusion = buildConclusion(claim, request, fraudResult);

        conclusion = conclusionRepository.save(conclusion);

        updateClaimStatusAfterReview(claim, conclusion);

        if (conclusion.getFraudConfirmed() == Boolean.TRUE) {
            saveHistoricalClaim(claim, conclusion);
        }

        log.info("赔付审核完成，报案号: {}, 审核结果: {}, 最终赔付金额: {}",
                claim.getClaimNumber(), conclusion.getReviewResult(),
                conclusion.getFinalApprovedAmount());

        return conclusion;
    }

    private CompensationConclusion buildConclusion(ClaimReport claim, CompensationReviewRequest request,
                                                   FraudDetectionResultDTO fraudResult) {
        CompensationConclusion conclusion = new CompensationConclusion();
        conclusion.setConclusionNumber(IdGenerator.generateConclusionNumber());
        conclusion.setClaim(claim);
        conclusion.setReviewResult(request.getReviewResult());
        conclusion.setFinalApprovedAmount(request.getFinalApprovedAmount());
        conclusion.setRejectedAmount(request.getRejectedAmount());
        conclusion.setRejectionReason(request.getRejectionReason());
        conclusion.setPartialRejectionReason(request.getPartialRejectionReason());
        conclusion.setFraudConfirmed(request.getFraudConfirmed());
        conclusion.setFraudType(request.getFraudType());
        conclusion.setFraudDescription(request.getFraudDescription());
        conclusion.setFraudEvidence(request.getFraudEvidence());
        conclusion.setFraudScore(fraudResult.getTotalRiskScore());
        conclusion.setFraudRiskLevel(fraudResult.getRiskLevel());
        conclusion.setMissingMaterials(request.getMissingMaterials());
        conclusion.setPolicyVerificationRemark(request.getPolicyVerificationRemark());
        conclusion.setLiabilityVerificationRemark(request.getLiabilityVerificationRemark());
        conclusion.setSurveyVerificationRemark(request.getSurveyVerificationRemark());
        conclusion.setManualReviewPerformed(request.getManualReviewPerformed());
        conclusion.setManualReviewerId(request.getReviewerId());
        conclusion.setManualReviewerName(request.getReviewerName());
        conclusion.setManualReviewTime(request.getReviewTime() != null ? request.getReviewTime() : LocalDateTime.now());
        conclusion.setManualReviewOpinion(request.getReviewOpinion());
        conclusion.setSeniorReviewerId(request.getSeniorReviewerId());
        conclusion.setSeniorReviewerName(request.getSeniorReviewerName());
        conclusion.setSeniorReviewOpinion(request.getSeniorReviewOpinion());
        conclusion.setConclusionRemark(request.getConclusionRemark());
        conclusion.setMaterialsVerifiedComplete(claim.getMaterialsComplete());
        conclusion.setPolicyVerificationPassed(verifyPolicy(claim));
        conclusion.setLiabilityVerificationPassed(verifyLiability(claim));
        conclusion.setSurveyVerificationPassed(verifySurvey(claim));

        extractFraudDetails(conclusion, fraudResult);

        calculateReimbursableAmount(conclusion, claim);

        return conclusion;
    }

    private void extractFraudDetails(CompensationConclusion conclusion, FraudDetectionResultDTO fraudResult) {
        List<String> tamperedPhotos = new ArrayList<>();
        List<String> reusedInvoices = new ArrayList<>();
        List<String> relatedPersons = new ArrayList<>();
        List<String> multiplePolicies = new ArrayList<>();

        for (FraudDetectionResultDTO.FraudItem item : fraudResult.getFraudItems()) {
            switch (item.getFraudType()) {
                case "DUPLICATE_CLAIM":
                    conclusion.setDuplicateClaimFound(true);
                    conclusion.setDuplicateClaimNumber(item.getEvidence());
                    break;
                case "TIME_CONFLICT":
                    conclusion.setTimeConflictFound(true);
                    conclusion.setTimeConflictDescription(item.getRiskDescription());
                    break;
                case "IMAGE_TAMPERING":
                    conclusion.setImageTamperFound(true);
                    tamperedPhotos.add(item.getEvidence());
                    break;
                case "ABNORMAL_AMOUNT":
                    conclusion.setAbnormalAmountFound(true);
                    conclusion.setAbnormalAmountDescription(item.getRiskDescription());
                    break;
                case "RELATED_PERSON_FRAUD":
                    conclusion.setRelatedPersonFound(true);
                    relatedPersons.add(item.getEvidence());
                    break;
                case "CROSS_REGION_SUSPICION":
                    conclusion.setCrossRegionSuspicion(true);
                    conclusion.setCrossRegionDescription(item.getRiskDescription());
                    break;
                case "INVOICE_REUSE":
                    conclusion.setInvoiceReuseFound(true);
                    reusedInvoices.add(item.getEvidence());
                    break;
                case "MULTI_POLICY_FRAUD":
                    conclusion.setMultiplePolicyFraud(true);
                    multiplePolicies.add(item.getEvidence());
                    break;
            }
        }

        if (!tamperedPhotos.isEmpty()) {
            conclusion.setTamperedPhotos(String.join("; ", tamperedPhotos));
        }
        if (!reusedInvoices.isEmpty()) {
            conclusion.setReusedInvoiceNumbers(String.join("; ", reusedInvoices));
        }
        if (!relatedPersons.isEmpty()) {
            conclusion.setRelatedPersons(String.join("; ", relatedPersons));
        }
        if (!multiplePolicies.isEmpty()) {
            conclusion.setMultiplePolicyNumbers(String.join("; ", multiplePolicies));
        }
    }

    private boolean verifyPolicy(ClaimReport claim) {
        if (claim.getPolicy() == null) {
            return false;
        }
        return claim.getPolicy().getStatus().name().equals("ACTIVE")
                && !claim.getAccidentTime().toLocalDate().isBefore(claim.getPolicy().getEffectiveDate())
                && !claim.getAccidentTime().toLocalDate().isAfter(claim.getPolicy().getExpiryDate());
    }

    private boolean verifyLiability(ClaimReport claim) {
        List<LiabilityDetermination> liabilities = liabilityDeterminationRepository.findByClaimId(claim.getId());
        if (liabilities.isEmpty()) {
            return claim.getPoliceReported() != Boolean.TRUE;
        }
        return liabilities.stream().anyMatch(LiabilityDetermination::getVerified);
    }

    private boolean verifySurvey(ClaimReport claim) {
        List<SurveyRecord> surveys = surveyRecordRepository.findByClaimId(claim.getId());
        if (surveys.isEmpty()) {
            return false;
        }
        return surveys.stream().anyMatch(SurveyRecord::getVerified);
    }

    private void calculateReimbursableAmount(CompensationConclusion conclusion, ClaimReport claim) {
        if (conclusion.getFinalApprovedAmount() != null) {
            return;
        }

        BigDecimal totalInvoiced = BigDecimal.ZERO;
        BigDecimal totalReimbursable = BigDecimal.ZERO;

        List<HospitalInvoice> invoices = hospitalInvoiceRepository.findByClaimId(claim.getId());
        for (HospitalInvoice invoice : invoices) {
            if (Boolean.TRUE.equals(invoice.getDuplicateFound())
                    || Boolean.TRUE.equals(invoice.getAmountAbnormal())) {
                continue;
            }
            totalInvoiced = totalInvoiced.add(invoice.getTotalAmount() != null ? invoice.getTotalAmount() : BigDecimal.ZERO);
            totalReimbursable = totalReimbursable.add(
                    invoice.getReimbursableAmount() != null ? invoice.getReimbursableAmount() : BigDecimal.ZERO);
        }

        List<RepairQuote> quotes = repairQuoteRepository.findByClaimId(claim.getId());
        for (RepairQuote quote : quotes) {
            if (Boolean.TRUE.equals(quote.getDuplicateFound())
                    || Boolean.TRUE.equals(quote.getAmountAbnormal())) {
                continue;
            }
            totalInvoiced = totalInvoiced.add(quote.getTotalQuoteAmount() != null ? quote.getTotalQuoteAmount() : BigDecimal.ZERO);
        }

        BigDecimal policyCoverage = claim.getPolicy().getCoverageAmount();
        if (policyCoverage == null) {
            policyCoverage = totalReimbursable;
        }

        BigDecimal approvedAmount = totalReimbursable.min(policyCoverage);

        if (conclusion.getReviewResult() == ReviewResult.APPROVED) {
            conclusion.setFinalApprovedAmount(approvedAmount);
            conclusion.setRejectedAmount(totalInvoiced.subtract(approvedAmount));
        } else if (conclusion.getReviewResult() == ReviewResult.REJECTED) {
            conclusion.setFinalApprovedAmount(BigDecimal.ZERO);
            conclusion.setRejectedAmount(totalInvoiced);
        }
    }

    private void updateClaimStatusAfterReview(ClaimReport claim, CompensationConclusion conclusion) {
        switch (conclusion.getReviewResult()) {
            case APPROVED:
                claim.setStatus(ClaimStatus.APPROVED);
                break;
            case REJECTED:
                claim.setStatus(ClaimStatus.REJECTED);
                break;
            case ADDITIONAL_MATERIALS_REQUIRED:
                claim.setStatus(ClaimStatus.MATERIALS_PENDING);
                break;
            case FRAUD_CONFIRMED:
                claim.setStatus(ClaimStatus.FRAUD_SUSPECTED);
                break;
            case TRANSFERRED_TO_SPECIALIST:
                claim.setStatus(ClaimStatus.MANUAL_REVIEW_REQUIRED);
                break;
        }

        claim.setReviewerId(conclusion.getManualReviewerId());
        claim.setReviewerName(conclusion.getManualReviewerName());
        claimReportRepository.save(claim);
    }

    private void saveHistoricalClaim(ClaimReport claim, CompensationConclusion conclusion) {
        HistoricalClaim historicalClaim = new HistoricalClaim();
        historicalClaim.setClaimNumber(claim.getClaimNumber());
        historicalClaim.setPolicyNumber(claim.getPolicy().getPolicyNumber());
        historicalClaim.setPolicyHolderName(claim.getPolicy().getPolicyHolderName());
        historicalClaim.setPolicyHolderIdCard(claim.getPolicy().getPolicyHolderIdCard());
        historicalClaim.setInsuredName(claim.getPolicy().getInsuredName());
        historicalClaim.setInsuredIdCard(claim.getPolicy().getInsuredIdCard());
        historicalClaim.setAccidentType(claim.getAccidentType());
        historicalClaim.setAccidentTime(claim.getAccidentTime());
        historicalClaim.setAccidentLocation(claim.getAccidentLocation());
        historicalClaim.setAccidentRegion(claim.getAccidentRegion());
        historicalClaim.setClaimAmount(claim.getClaimedAmount());
        historicalClaim.setApprovedAmount(conclusion.getFinalApprovedAmount());
        historicalClaim.setRejectedAmount(conclusion.getRejectedAmount());
        historicalClaim.setActualCompensation(conclusion.getFinalApprovedAmount());
        historicalClaim.setClaimStatus(claim.getStatus().name());
        historicalClaim.setFraudSuspected(true);
        historicalClaim.setFraudConfirmed(true);
        historicalClaim.setFraudType(conclusion.getFraudType());
        historicalClaim.setFraudDescription(conclusion.getFraudDescription());
        historicalClaim.setInsuranceCompany("Default Insurance");
        historicalClaim.setDataSource("SYSTEM");
        historicalClaimRepository.save(historicalClaim);
    }

    @Transactional
    public CompensationConclusion performManualReview(ManualReviewRequest request) {
        ClaimReport claim = claimReportRepository.findByClaimNumber(request.getClaimNumber())
                .orElseThrow(() -> new BusinessException("CLAIM_NOT_FOUND",
                        "报案不存在: " + request.getClaimNumber()));

        if (claim.getStatus() != ClaimStatus.MANUAL_REVIEW_REQUIRED
                && claim.getStatus() != ClaimStatus.FRAUD_SUSPECTED) {
            throw new BusinessException("INVALID_STATUS_FOR_REVIEW",
                    "当前案件状态不允许人工复核: " + claim.getStatus());
        }

        CompensationConclusion conclusion = conclusionRepository.findFirstByClaimIdOrderByCreatedAtDesc(claim.getId())
                .orElse(new CompensationConclusion());

        conclusion.setConclusionNumber(IdGenerator.generateConclusionNumber());
        conclusion.setClaim(claim);
        conclusion.setReviewResult(request.getReviewResult());
        conclusion.setFinalApprovedAmount(request.getFinalApprovedAmount());
        conclusion.setRejectedAmount(request.getRejectedAmount());
        conclusion.setRejectionReason(request.getRejectionReason());
        conclusion.setFraudConfirmed(request.getFraudConfirmed());
        conclusion.setFraudType(request.getFraudType() != null ? request.getFraudType().name() : null);
        conclusion.setFraudDescription(request.getFraudDescription());
        conclusion.setFraudEvidence(request.getFraudEvidence());
        conclusion.setManualReviewPerformed(true);
        conclusion.setManualReviewerId(request.getReviewerId());
        conclusion.setManualReviewerName(request.getReviewerName());
        conclusion.setManualReviewTime(request.getReviewTime() != null ? request.getReviewTime() : LocalDateTime.now());
        conclusion.setManualReviewOpinion(request.getReviewOpinion());
        conclusion.setSeniorReviewerId(request.getSeniorReviewerId());
        conclusion.setSeniorReviewerName(request.getSeniorReviewerName());
        conclusion.setSeniorReviewOpinion(request.getSeniorReviewOpinion());
        conclusion.setSeniorReviewTime(LocalDateTime.now());
        conclusion.setFinalReviewerId(request.getReviewerId());
        conclusion.setFinalReviewerName(request.getReviewerName());
        conclusion.setFinalReviewTime(LocalDateTime.now());
        conclusion.setFinalReviewRemark(request.getRemark());
        conclusion.setConclusionRemark(request.getRemark());
        conclusion.setAppealAllowed(true);
        conclusion.setAppealDeadline(LocalDateTime.now().plusDays(15));

        conclusion = conclusionRepository.save(conclusion);

        updateClaimStatusAfterReview(claim, conclusion);

        if (conclusion.getFraudConfirmed() == Boolean.TRUE) {
            saveHistoricalClaim(claim, conclusion);
        }

        log.info("人工复核完成，报案号: {}, 复核结果: {}", claim.getClaimNumber(), conclusion.getReviewResult());

        return conclusion;
    }

    @Transactional
    public CompensationConclusion processCompensationPayment(String claimNumber,
                                                            String paymentTransactionId,
                                                            String recipientAccount,
                                                            String recipientName) {
        ClaimReport claim = claimReportRepository.findByClaimNumber(claimNumber)
                .orElseThrow(() -> new BusinessException("CLAIM_NOT_FOUND",
                        "报案不存在: " + claimNumber));

        if (claim.getStatus() != ClaimStatus.APPROVED) {
            throw new BusinessException("INVALID_STATUS_FOR_PAYMENT",
                    "当前案件状态不允许支付: " + claim.getStatus());
        }

        CompensationConclusion conclusion = conclusionRepository.findFirstByClaimIdOrderByCreatedAtDesc(claim.getId())
                .orElseThrow(() -> new BusinessException("CONCLUSION_NOT_FOUND",
                        "未找到该案件的赔付结论"));

        conclusion.setCompensationPaymentTime(LocalDateTime.now());
        conclusion.setPaymentTransactionId(paymentTransactionId);
        conclusion.setRecipientAccount(recipientAccount);
        conclusion.setRecipientName(recipientName);

        conclusion = conclusionRepository.save(conclusion);

        claim.setStatus(ClaimStatus.COMPENSATED);
        claimReportRepository.save(claim);

        log.info("赔付款项处理完成，报案号: {}, 支付金额: {}, 交易ID: {}",
                claimNumber, conclusion.getFinalApprovedAmount(), paymentTransactionId);

        return conclusion;
    }
}