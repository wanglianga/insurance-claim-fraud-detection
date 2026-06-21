package com.insurance.claim.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

public class ThirdPartyCallback extends BaseEntity {

    @Column(name = "callback_id", unique = true, length = 50)
    private String callbackId;
    @Column(name = "related_claim_id", length = 50)
    private String relatedClaimId;
    @Column(name = "related_claim_number", length = 50)
    private String relatedClaimNumber;
    @Column(name = "source_system", length = 100)
    private String sourceSystem;
    @Column(name = "callback_type", length = 50)
    private String callbackType;
    @Column(name = "callback_event", length = 100)
    private String callbackEvent;
    @Column(name = "callback_time")
    private LocalDateTime callbackTime;
    @Column(name = "verification_target", length = 100)
    private String verificationTarget;
    @Column(name = "verification_result", length = 50)
    private String verificationResult;
    @Column(name = "verification_detail", columnDefinition = "TEXT")
    private String verificationDetail;
    @Column(name = "original_request_data", columnDefinition = "TEXT")
    private String originalRequestData;
    @Column(name = "callback_response_data", columnDefinition = "TEXT")
    private String callbackResponseData;
    @Column(name = "signature_verified")
    private Boolean signatureVerified;
    @Column(name = "processed")
    private Boolean processed;
    @Column(name = "processing_time")
    private LocalDateTime processingTime;
    @Column(name = "processing_result", length = 50)
    private String processingResult;
    @Column(name = "processing_remark", columnDefinition = "TEXT")
    private String processingRemark;
    @Column(name = "processor_id", length = 50)
    private String processorId;
    @Column(name = "processor_name", length = 100)
    private String processorName;
    @Column(name = "error_message", columnDefinition = "TEXT")
    private String errorMessage;
    @Column(name = "retry_count")
    private Integer retryCount;

    public String getCallbackId() {
        return callbackId;
    }
    public void setCallbackId(String callbackId) {
        this.callbackId = callbackId;
    }
    public String getRelatedClaimId() {
        return relatedClaimId;
    }
    public void setRelatedClaimId(String relatedClaimId) {
        this.relatedClaimId = relatedClaimId;
    }
    public String getRelatedClaimNumber() {
        return relatedClaimNumber;
    }
    public void setRelatedClaimNumber(String relatedClaimNumber) {
        this.relatedClaimNumber = relatedClaimNumber;
    }
    public String getSourceSystem() {
        return sourceSystem;
    }
    public void setSourceSystem(String sourceSystem) {
        this.sourceSystem = sourceSystem;
    }
    public String getCallbackType() {
        return callbackType;
    }
    public void setCallbackType(String callbackType) {
        this.callbackType = callbackType;
    }
    public String getCallbackEvent() {
        return callbackEvent;
    }
    public void setCallbackEvent(String callbackEvent) {
        this.callbackEvent = callbackEvent;
    }
    public LocalDateTime getCallbackTime() {
        return callbackTime;
    }
    public void setCallbackTime(LocalDateTime callbackTime) {
        this.callbackTime = callbackTime;
    }
    public String getVerificationTarget() {
        return verificationTarget;
    }
    public void setVerificationTarget(String verificationTarget) {
        this.verificationTarget = verificationTarget;
    }
    public String getVerificationResult() {
        return verificationResult;
    }
    public void setVerificationResult(String verificationResult) {
        this.verificationResult = verificationResult;
    }
    public String getVerificationDetail() {
        return verificationDetail;
    }
    public void setVerificationDetail(String verificationDetail) {
        this.verificationDetail = verificationDetail;
    }
    public String getOriginalRequestData() {
        return originalRequestData;
    }
    public void setOriginalRequestData(String originalRequestData) {
        this.originalRequestData = originalRequestData;
    }
    public String getCallbackResponseData() {
        return callbackResponseData;
    }
    public void setCallbackResponseData(String callbackResponseData) {
        this.callbackResponseData = callbackResponseData;
    }
    public Boolean isSignatureVerified() {
        return signatureVerified;
    }
    public void setSignatureVerified(Boolean signatureVerified) {
        this.signatureVerified = signatureVerified;
    }
    public Boolean isProcessed() {
        return processed;
    }
    public Boolean getProcessed() {
        return processed;
    }
    public void setProcessed(Boolean processed) {
        this.processed = processed;
    }
    public LocalDateTime getProcessingTime() {
        return processingTime;
    }
    public void setProcessingTime(LocalDateTime processingTime) {
        this.processingTime = processingTime;
    }
    public String getProcessingResult() {
        return processingResult;
    }
    public void setProcessingResult(String processingResult) {
        this.processingResult = processingResult;
    }
    public String getProcessingRemark() {
        return processingRemark;
    }
    public void setProcessingRemark(String processingRemark) {
        this.processingRemark = processingRemark;
    }
    public String getProcessorId() {
        return processorId;
    }
    public void setProcessorId(String processorId) {
        this.processorId = processorId;
    }
    public String getProcessorName() {
        return processorName;
    }
    public void setProcessorName(String processorName) {
        this.processorName = processorName;
    }
    public String getErrorMessage() {
        return errorMessage;
    }
    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
    public Integer getRetryCount() {
        return retryCount;
    }
    public void setRetryCount(Integer retryCount) {
        this.retryCount = retryCount;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        ThirdPartyCallback that = (ThirdPartyCallback) o;

        if (callbackId != null ? !callbackId.equals(that.callbackId) : that.callbackId != null) return false;
        if (relatedClaimId != null ? !relatedClaimId.equals(that.relatedClaimId) : that.relatedClaimId != null) return false;
        if (relatedClaimNumber != null ? !relatedClaimNumber.equals(that.relatedClaimNumber) : that.relatedClaimNumber != null) return false;
        if (sourceSystem != null ? !sourceSystem.equals(that.sourceSystem) : that.sourceSystem != null) return false;
        if (callbackType != null ? !callbackType.equals(that.callbackType) : that.callbackType != null) return false;
        if (callbackEvent != null ? !callbackEvent.equals(that.callbackEvent) : that.callbackEvent != null) return false;
        if (callbackTime != null ? !callbackTime.equals(that.callbackTime) : that.callbackTime != null) return false;
        if (verificationTarget != null ? !verificationTarget.equals(that.verificationTarget) : that.verificationTarget != null) return false;
        if (verificationResult != null ? !verificationResult.equals(that.verificationResult) : that.verificationResult != null) return false;
        if (verificationDetail != null ? !verificationDetail.equals(that.verificationDetail) : that.verificationDetail != null) return false;
        if (originalRequestData != null ? !originalRequestData.equals(that.originalRequestData) : that.originalRequestData != null) return false;
        if (callbackResponseData != null ? !callbackResponseData.equals(that.callbackResponseData) : that.callbackResponseData != null) return false;
        if (signatureVerified != null ? !signatureVerified.equals(that.signatureVerified) : that.signatureVerified != null) return false;
        if (processed != null ? !processed.equals(that.processed) : that.processed != null) return false;
        if (processingTime != null ? !processingTime.equals(that.processingTime) : that.processingTime != null) return false;
        if (processingResult != null ? !processingResult.equals(that.processingResult) : that.processingResult != null) return false;
        if (processingRemark != null ? !processingRemark.equals(that.processingRemark) : that.processingRemark != null) return false;
        if (processorId != null ? !processorId.equals(that.processorId) : that.processorId != null) return false;
        if (processorName != null ? !processorName.equals(that.processorName) : that.processorName != null) return false;
        if (errorMessage != null ? !errorMessage.equals(that.errorMessage) : that.errorMessage != null) return false;
        if (retryCount != null ? !retryCount.equals(that.retryCount) : that.retryCount != null) return false;
        return true;
    }
    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (callbackId != null ? callbackId.hashCode() : 0);
        result = 31 * result + (relatedClaimId != null ? relatedClaimId.hashCode() : 0);
        result = 31 * result + (relatedClaimNumber != null ? relatedClaimNumber.hashCode() : 0);
        result = 31 * result + (sourceSystem != null ? sourceSystem.hashCode() : 0);
        result = 31 * result + (callbackType != null ? callbackType.hashCode() : 0);
        result = 31 * result + (callbackEvent != null ? callbackEvent.hashCode() : 0);
        result = 31 * result + (callbackTime != null ? callbackTime.hashCode() : 0);
        result = 31 * result + (verificationTarget != null ? verificationTarget.hashCode() : 0);
        result = 31 * result + (verificationResult != null ? verificationResult.hashCode() : 0);
        result = 31 * result + (verificationDetail != null ? verificationDetail.hashCode() : 0);
        result = 31 * result + (originalRequestData != null ? originalRequestData.hashCode() : 0);
        result = 31 * result + (callbackResponseData != null ? callbackResponseData.hashCode() : 0);
        result = 31 * result + (signatureVerified != null ? signatureVerified.hashCode() : 0);
        result = 31 * result + (processed != null ? processed.hashCode() : 0);
        result = 31 * result + (processingTime != null ? processingTime.hashCode() : 0);
        result = 31 * result + (processingResult != null ? processingResult.hashCode() : 0);
        result = 31 * result + (processingRemark != null ? processingRemark.hashCode() : 0);
        result = 31 * result + (processorId != null ? processorId.hashCode() : 0);
        result = 31 * result + (processorName != null ? processorName.hashCode() : 0);
        result = 31 * result + (errorMessage != null ? errorMessage.hashCode() : 0);
        result = 31 * result + (retryCount != null ? retryCount.hashCode() : 0);
        return result;
    }

}