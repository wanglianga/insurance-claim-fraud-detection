package com.insurance.claim.service;

import com.insurance.claim.dto.SurveySubmitRequest;
import com.insurance.claim.entity.ClaimReport;
import com.insurance.claim.entity.SurveyRecord;
import com.insurance.claim.enums.ClaimStatus;
import com.insurance.claim.exception.BusinessException;
import com.insurance.claim.repository.ClaimReportRepository;
import com.insurance.claim.repository.SurveyRecordRepository;
import com.insurance.claim.util.IdGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class SurveyService {

    private final SurveyRecordRepository surveyRecordRepository;
    private final ClaimReportRepository claimReportRepository;
    private final FraudDetectionService fraudDetectionService;

    @Transactional
    public SurveyRecord submitSurvey(SurveySubmitRequest request) {
        ClaimReport claim = claimReportRepository.findByClaimNumber(request.getClaimNumber())
                .orElseThrow(() -> new BusinessException("CLAIM_NOT_FOUND",
                        "报案不存在: " + request.getClaimNumber()));

        SurveyRecord survey = new SurveyRecord();
        survey.setSurveyNumber(IdGenerator.generateSurveyNumber());
        survey.setClaim(claim);
        survey.setSurveyorId(request.getSurveyorId());
        survey.setSurveyorName(request.getSurveyorName());
        survey.setSurveyorPhone(request.getSurveyorPhone());
        survey.setSurveyTime(request.getSurveyTime());
        survey.setArrivalTime(request.getArrivalTime());
        survey.setDepartureTime(request.getDepartureTime());
        survey.setSurveyLocation(request.getSurveyLocation());
        survey.setSiteLatitude(request.getSiteLatitude());
        survey.setSiteLongitude(request.getSiteLongitude());
        survey.setWeatherCondition(request.getWeatherCondition());
        survey.setRoadCondition(request.getRoadCondition());
        survey.setLightingCondition(request.getLightingCondition());
        survey.setDamageDescription(request.getDamageDescription());
        survey.setDamageExtent(request.getDamageExtent());
        survey.setEstimatedLossAmount(request.getEstimatedLossAmount());
        survey.setInjuryDescription(request.getInjuryDescription());
        survey.setInjurySeverity(request.getInjurySeverity());
        survey.setInterviewRecord(request.getInterviewRecord());
        survey.setIntervieweeName(request.getIntervieweeName());
        survey.setIntervieweeRelation(request.getIntervieweeRelation());
        survey.setStatementConsistent(request.getStatementConsistent());
        survey.setInconsistencyPoints(request.getInconsistencyPoints());
        survey.setLiabilityJudgment(request.getLiabilityJudgment());
        survey.setLiabilityJudgmentBasis(request.getLiabilityJudgmentBasis());
        survey.setSiteSketchPath(request.getSiteSketchPath());
        survey.setAccidentReconstruction(request.getAccidentReconstruction());
        survey.setAbnormalSituation(request.getAbnormalSituation());
        survey.setFraudSuspicion(request.getFraudSuspicion());
        survey.setFraudSuspicionReason(request.getFraudSuspicionReason());
        survey.setSurveyConclusion(request.getSurveyConclusion());
        survey.setNextStepSuggestion(request.getNextStepSuggestion());
        survey.setVerified(false);

        survey = surveyRecordRepository.save(survey);

        claim.setStatus(ClaimStatus.REVIEWING);
        claimReportRepository.save(claim);

        fraudDetectionService.evictFraudCache(claim.getId());
        fraudDetectionService.performFullFraudDetection(claim.getId());

        log.info("查勘记录提交成功，查勘单号: {}, 报案号: {}",
                survey.getSurveyNumber(), claim.getClaimNumber());

        return survey;
    }

    @Transactional(readOnly = true)
    public SurveyRecord getSurveyByNumber(String surveyNumber) {
        return surveyRecordRepository.findBySurveyNumber(surveyNumber)
                .orElseThrow(() -> new BusinessException("SURVEY_NOT_FOUND",
                        "查勘记录不存在: " + surveyNumber));
    }

    @Transactional(readOnly = true)
    public List<SurveyRecord> getSurveysByClaimNumber(String claimNumber) {
        ClaimReport claim = claimReportRepository.findByClaimNumber(claimNumber)
                .orElseThrow(() -> new BusinessException("CLAIM_NOT_FOUND",
                        "报案不存在: " + claimNumber));
        return surveyRecordRepository.findLatestByClaimId(claim.getId());
    }

    @Transactional(readOnly = true)
    public SurveyRecord getLatestSurveyByClaimNumber(String claimNumber) {
        ClaimReport claim = claimReportRepository.findByClaimNumber(claimNumber)
                .orElseThrow(() -> new BusinessException("CLAIM_NOT_FOUND",
                        "报案不存在: " + claimNumber));
        return surveyRecordRepository.findFirstByClaimIdOrderByCreatedAtDesc(claim.getId())
                .orElse(null);
    }

    @Transactional(readOnly = true)
    public List<SurveyRecord> getSurveysBySurveyor(String surveyorId) {
        return surveyRecordRepository.findBySurveyorId(surveyorId);
    }

    @Transactional(readOnly = true)
    public List<SurveyRecord> getFraudSuspectedSurveys() {
        return surveyRecordRepository.findFraudSuspectedSurveys();
    }

    @Transactional
    public SurveyRecord verifySurvey(String surveyNumber, String verifierRemark) {
        SurveyRecord survey = getSurveyByNumber(surveyNumber);
        survey.setVerified(true);
        surveyRecordRepository.save(survey);

        ClaimReport claim = survey.getClaim();
        if (claim.getManualReviewRequired() != Boolean.TRUE) {
            claim.setStatus(ClaimStatus.REVIEWING);
            claimReportRepository.save(claim);
        }

        return survey;
    }
}
