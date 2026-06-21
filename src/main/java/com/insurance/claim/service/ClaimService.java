package com.insurance.claim.service;

import com.insurance.claim.dto.ClaimSubmitRequest;
import com.insurance.claim.dto.FraudDetectionResultDTO;
import com.insurance.claim.entity.ClaimReport;
import com.insurance.claim.entity.InsurancePolicy;
import com.insurance.claim.enums.ClaimStatus;
import com.insurance.claim.enums.PolicyStatus;
import com.insurance.claim.exception.BusinessException;
import com.insurance.claim.repository.ClaimReportRepository;
import com.insurance.claim.repository.InsurancePolicyRepository;
import com.insurance.claim.util.IdGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ClaimService {
    private static final Logger log = LoggerFactory.getLogger(ClaimService.class);

    private final ClaimReportRepository claimReportRepository;
    private final InsurancePolicyRepository insurancePolicyRepository;
    private final FraudDetectionService fraudDetectionService;

    public ClaimService(ClaimReportRepository claimReportRepository, InsurancePolicyRepository insurancePolicyRepository, FraudDetectionService fraudDetectionService) {
        this.claimReportRepository = claimReportRepository;
        this.insurancePolicyRepository = insurancePolicyRepository;
        this.fraudDetectionService = fraudDetectionService;
    }

    @Transactional
    public ClaimReport submitClaim(ClaimSubmitRequest request) {
        InsurancePolicy policy = insurancePolicyRepository.findByPolicyNumber(request.getPolicyNumber())
                .orElseThrow(() -> new BusinessException("POLICY_NOT_FOUND",
                        "保单不存在: " + request.getPolicyNumber()));

        if (policy.getStatus() != PolicyStatus.ACTIVE) {
            throw new BusinessException("POLICY_NOT_ACTIVE",
                    "保单状态异常，当前状态: " + policy.getStatus());
        }

        LocalDateTime accidentTime = request.getAccidentTime();
        if (accidentTime.toLocalDate().isBefore(policy.getEffectiveDate())
                || accidentTime.toLocalDate().isAfter(policy.getExpiryDate())) {
            throw new BusinessException("ACCIDENT_OUTSIDE_COVERAGE",
                    "事故时间不在保单有效期内");
        }

        ClaimReport claim = new ClaimReport();
        claim.setClaimNumber(IdGenerator.generateClaimNumber());
        claim.setPolicy(policy);
        claim.setReporterName(request.getReporterName());
        claim.setReporterPhone(request.getReporterPhone());
        claim.setReporterRelation(request.getReporterRelation());
        claim.setAccidentType(request.getAccidentType());
        claim.setAccidentTime(request.getAccidentTime());
        claim.setReportTime(request.getReportTime() != null ? request.getReportTime() : LocalDateTime.now());
        claim.setAccidentLocation(request.getAccidentLocation());
        claim.setAccidentLatitude(request.getAccidentLatitude());
        claim.setAccidentLongitude(request.getAccidentLongitude());
        claim.setAccidentRegion(request.getAccidentRegion());
        claim.setAccidentDescription(request.getAccidentDescription());
        claim.setInjuryDescription(request.getInjuryDescription());
        claim.setDamageDescription(request.getDamageDescription());
        claim.setClaimedAmount(request.getClaimedAmount());
        claim.setThirdPartyInvolved(request.getThirdPartyInvolved());
        claim.setThirdPartyName(request.getThirdPartyName());
        claim.setThirdPartyPhone(request.getThirdPartyPhone());
        claim.setThirdPartyInsurance(request.getThirdPartyInsurance());
        claim.setPoliceReported(request.getPoliceReported());
        claim.setPoliceStation(request.getPoliceStation());
        claim.setPoliceReportNumber(request.getPoliceReportNumber());
        claim.setWitnessName(request.getWitnessName());
        claim.setWitnessPhone(request.getWitnessPhone());
        claim.setRelatedAccidentId(request.getRelatedAccidentId());
        claim.setSurveyAssigned(false);
        claim.setMaterialsComplete(false);
        claim.setStatus(ClaimStatus.SUBMITTED);
        claim.setFraudScore(0);
        claim.setFraudSuspected(false);
        claim.setManualReviewRequired(false);
        claim.setMultiplePoliciesInvolved(false);

        claim = claimReportRepository.save(claim);

        log.info("报案提交成功，报案号: {}", claim.getClaimNumber());

        triggerFraudDetectionAsync(claim.getId());

        return claim;
    }

    @Async
    @Transactional
    public void triggerFraudDetectionAsync(Long claimId) {
        try {
            log.info("开始异步欺诈检测，报案ID: {}", claimId);
            fraudDetectionService.performFullFraudDetection(claimId);

            ClaimReport claim = claimReportRepository.findById(claimId).orElse(null);
            if (claim != null) {
                if (claim.getManualReviewRequired() == Boolean.TRUE) {
                    claim.setStatus(ClaimStatus.MANUAL_REVIEW_REQUIRED);
                } else {
                    claim.setStatus(ClaimStatus.MATERIALS_PENDING);
                }
                claimReportRepository.save(claim);
            }
        } catch (Exception e) {
            log.error("异步欺诈检测失败，报案ID: {}", claimId, e);
        }
    }

    @Transactional(readOnly = true)
    public ClaimReport getClaimByNumber(String claimNumber) {
        return claimReportRepository.findByClaimNumber(claimNumber)
                .orElseThrow(() -> new BusinessException("CLAIM_NOT_FOUND",
                        "报案不存在: " + claimNumber));
    }

    @Transactional(readOnly = true)
    public List<ClaimReport> getClaimsByPolicyNumber(String policyNumber) {
        InsurancePolicy policy = insurancePolicyRepository.findByPolicyNumber(policyNumber)
                .orElseThrow(() -> new BusinessException("POLICY_NOT_FOUND",
                        "保单不存在: " + policyNumber));
        return claimReportRepository.findByPolicyId(policy.getId());
    }

    @Transactional(readOnly = true)
    public List<ClaimReport> getClaimsByStatus(ClaimStatus status) {
        return claimReportRepository.findByStatus(status);
    }

    @Transactional(readOnly = true)
    public List<ClaimReport> getClaimsBySurveyor(String surveyorId) {
        return claimReportRepository.findBySurveyorId(surveyorId);
    }

    @Transactional(readOnly = true)
    public List<ClaimReport> getClaimsByReviewer(String reviewerId) {
        return claimReportRepository.findByReviewerId(reviewerId);
    }

    @Transactional
    public ClaimReport updateClaimStatus(String claimNumber, ClaimStatus status) {
        ClaimReport claim = getClaimByNumber(claimNumber);
        claim.setStatus(status);
        return claimReportRepository.save(claim);
    }

    @Transactional
    public ClaimReport assignSurveyor(String claimNumber, String surveyorId, String surveyorName) {
        ClaimReport claim = getClaimByNumber(claimNumber);
        claim.setSurveyAssigned(true);
        claim.setSurveyorId(surveyorId);
        claim.setSurveyorName(surveyorName);
        claim.setStatus(ClaimStatus.SURVEY_PENDING);
        return claimReportRepository.save(claim);
    }

    @Transactional(readOnly = true)
    public FraudDetectionResultDTO getFraudDetectionResult(String claimNumber) {
        ClaimReport claim = getClaimByNumber(claimNumber);
        return fraudDetectionService.performFullFraudDetection(claim.getId());
    }

    @Transactional(readOnly = true)
    public List<ClaimReport> getRelatedClaims(String accidentId) {
        return claimReportRepository.findRelatedAccidents(accidentId);
    }

    @Transactional
    public void refreshFraudDetection(String claimNumber) {
        ClaimReport claim = getClaimByNumber(claimNumber);
        fraudDetectionService.evictFraudCache(claim.getId());
        fraudDetectionService.performFullFraudDetection(claim.getId());
    }
}
