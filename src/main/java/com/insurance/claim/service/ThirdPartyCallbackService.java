
package com.insurance.claim.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.insurance.claim.dto.ThirdPartyCallbackRequest;
import com.insurance.claim.entity.ClaimReport;
import com.insurance.claim.entity.ThirdPartyCallback;
import com.insurance.claim.enums.ClaimStatus;
import com.insurance.claim.exception.BusinessException;
import com.insurance.claim.repository.ClaimReportRepository;
import com.insurance.claim.repository.ThirdPartyCallbackRepository;
import com.insurance.claim.util.IdGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ThirdPartyCallbackService {

    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(ThirdPartyCallbackService.class);
    private final ThirdPartyCallbackRepository callbackRepository;
    private final ClaimReportRepository claimReportRepository;
    private final FraudDetectionService fraudDetectionService;
    private final ObjectMapper objectMapper;

    public ThirdPartyCallbackService(ThirdPartyCallbackRepository callbackRepository, ClaimReportRepository claimReportRepository, FraudDetectionService fraudDetectionService, ObjectMapper objectMapper) {
        this.callbackRepository = callbackRepository;
        this.claimReportRepository = claimReportRepository;
        this.fraudDetectionService = fraudDetectionService;
        this.objectMapper = objectMapper;
    }
    public List<ThirdPartyCallback> getCallbacksByClaim(String claimNumber) {
        return callbackRepository.findByRelatedClaimNumber(claimNumber);
    }
    public List<ThirdPartyCallback> getCallbacksBySource(String sourceSystem) {
        return callbackRepository.findBySourceSystem(sourceSystem);
    }
    public ThirdPartyCallback getCallbackById(String callbackId) {
        return callbackRepository.findByCallbackId(callbackId)
                .orElseThrow(() -> new BusinessException("CALLBACK_NOT_FOUND",
                        "回调记录不存在: " + callbackId));
    }

    @Transactional
    public ThirdPartyCallback receiveCallback(ThirdPartyCallbackRequest request) {
        if (callbackRepository.existsByCallbackId(request.getCallbackId())) {
            throw new BusinessException("CALLBACK_ALREADY_EXISTS",
                    "回调ID已存在: " + request.getCallbackId());
        }

        ThirdPartyCallback callback = new ThirdPartyCallback();
        callback.setCallbackId(request.getCallbackId());
        callback.setRelatedClaimNumber(request.getRelatedClaimNumber());
        callback.setSourceSystem(request.getSourceSystem());
        callback.setCallbackType(request.getCallbackType());
        callback.setCallbackEvent(request.getCallbackEvent());
        callback.setCallbackTime(request.getCallbackTime() != null ? request.getCallbackTime() : LocalDateTime.now());
        callback.setVerificationTarget(request.getVerificationTarget());
        callback.setVerificationResult(request.getVerificationResult());
        callback.setVerificationDetail(request.getVerificationDetail());

        try {
            if (request.getCallbackData() != null) {
                callback.setCallbackResponseData(objectMapper.writeValueAsString(request.getCallbackData()));
            }
        } catch (Exception e) {
            log.warn("序列化回调数据失败", e);
        }

        callback.setSignatureVerified(verifySignature(request));
        callback.setProcessed(false);
        callback.setRetryCount(0);

        callback = callbackRepository.save(callback);

        processCallbackAsync(callback.getId());

        log.info("第三方回调已接收，回调ID: {}, 来源: {}",
                callback.getCallbackId(), callback.getSourceSystem());

        return callback;
    }

    @Async
    @Transactional
    public void processCallbackAsync(Long callbackId) {
        try {
            processCallback(callbackId);
        } catch (Exception e) {
            log.error("处理第三方回调失败，回调ID: {}", callbackId, e);
        }
    }

    @Transactional
    public void processCallback(Long callbackId) {
        ThirdPartyCallback callback = callbackRepository.findById(callbackId)
                .orElseThrow(() -> new BusinessException("CALLBACK_NOT_FOUND",
                        "回调记录不存在: " + callbackId));

        if (Boolean.TRUE.equals(callback.getProcessed())) {
            return;
        }

        try {
            String result = callback.getVerificationResult();

            if ("VERIFIED".equals(result) || "SUCCESS".equals(result)) {
                handleSuccessfulVerification(callback);
                callback.setProcessingResult("SUCCESS");
            } else if ("FAILED".equals(result) || "REJECTED".equals(result)) {
                handleFailedVerification(callback);
                callback.setProcessingResult("FAILED");
            } else {
                callback.setProcessingResult("PENDING");
            }

            callback.setProcessed(true);
            callback.setProcessingTime(LocalDateTime.now());
            callback.setProcessingRemark("回调处理完成");

            callbackRepository.save(callback);

            log.info("第三方回调处理完成，回调ID: {}, 处理结果: {}",
                    callback.getCallbackId(), callback.getProcessingResult());

        } catch (Exception e) {
            callback.setRetryCount(callback.getRetryCount() + 1);
            callback.setErrorMessage(e.getMessage());

            if (callback.getRetryCount() >= 3) {
                callback.setProcessed(true);
                callback.setProcessingResult("FAILED");
                callback.setProcessingRemark("重试次数超过限制");
            }

            callbackRepository.save(callback);

            log.error("处理第三方回调失败，回调ID: {}, 重试次数: {}",
                    callback.getCallbackId(), callback.getRetryCount(), e);
        }
    }

    private void handleSuccessfulVerification(ThirdPartyCallback callback) {
        if (callback.getRelatedClaimNumber() == null) {
            return;
        }

        claimReportRepository.findByClaimNumber(callback.getRelatedClaimNumber())
                .ifPresent(claim -> {
                    String callbackType = callback.getCallbackType();
                    switch (callbackType) {
                        case "POLICY_VERIFICATION":
                            handlePolicyVerification(claim, callback);
                            break;
                        case "HOSPITAL_VERIFICATION":
                            handleHospitalVerification(claim, callback);
                            break;
                        case "REPAIR_VERIFICATION":
                            handleRepairVerification(claim, callback);
                            break;
                        case "LIABILITY_VERIFICATION":
                            handleLiabilityVerification(claim, callback);
                            break;
                        case "BLACKLIST_CHECK":
                            handleBlacklistCheck(claim, callback);
                            break;
                    }

                    fraudDetectionService.evictFraudCache(claim.getId());
                    fraudDetectionService.performFullFraudDetection(claim.getId());
                });
    }

    private void handlePolicyVerification(ClaimReport claim, ThirdPartyCallback callback) {
        claim.setReviewerId("SYSTEM");
        claim.setReviewerName("System Callback Handler");
        claimReportRepository.save(claim);
    }

    private void handleHospitalVerification(ClaimReport claim, ThirdPartyCallback callback) {
        log.info("医院验证通过，报案号: {}", claim.getClaimNumber());
    }

    private void handleRepairVerification(ClaimReport claim, ThirdPartyCallback callback) {
        log.info("维修厂验证通过，报案号: {}", claim.getClaimNumber());
    }

    private void handleLiabilityVerification(ClaimReport claim, ThirdPartyCallback callback) {
        log.info("责任认定验证通过，报案号: {}", claim.getClaimNumber());
    }

    private void handleBlacklistCheck(ClaimReport claim, ThirdPartyCallback callback) {
        if ("FAILED".equals(callback.getVerificationResult()) || callback.getVerificationDetail().contains("黑名单")) {
            claim.setStatus(ClaimStatus.FRAUD_SUSPECTED);
            claim.setFraudSuspected(true);
            claim.setManualReviewRequired(true);
            claimReportRepository.save(claim);
        }
    }

    private void handleFailedVerification(ThirdPartyCallback callback) {
        if (callback.getRelatedClaimNumber() == null) {
            return;
        }

        claimReportRepository.findByClaimNumber(callback.getRelatedClaimNumber())
                .ifPresent(claim -> {
                    if (callback.getVerificationDetail() != null
                            && callback.getVerificationDetail().contains("黑名单")) {
                        claim.setStatus(ClaimStatus.FRAUD_SUSPECTED);
                        claim.setFraudSuspected(true);
                        claim.setManualReviewRequired(true);
                        claimReportRepository.save(claim);
                    }
                });
    }

    private boolean verifySignature(ThirdPartyCallbackRequest request) {
        return request.getSignature() != null && !request.getSignature().isEmpty();
    }

    @Scheduled(fixedRate = 300000)
    @Transactional
    public void retryPendingCallbacks() {
        List<ThirdPartyCallback> pending = callbackRepository.findPendingCallbacks();
        log.info("待处理回调数: {}", pending.size());

        for (ThirdPartyCallback callback : pending) {
            if (callback.getRetryCount() < 3) {
                processCallbackAsync(callback.getId());
            }
        }
    }

    @Scheduled(cron = "0 0 1 * * ?")
    @Transactional
    public void cleanupOldCallbacks() {
        LocalDateTime threshold = LocalDateTime.now().minusDays(30);
        List<ThirdPartyCallback> timeout = callbackRepository.findTimeoutCallbacks(threshold);
        log.info("清理超时回调数: {}", timeout.size());
    }
}