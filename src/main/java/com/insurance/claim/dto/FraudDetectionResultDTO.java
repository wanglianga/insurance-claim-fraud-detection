package com.insurance.claim.dto;

import java.util.List;

public class FraudDetectionResultDTO {

    private String claimNumber;
    private Integer totalRiskScore;
    private String riskLevel;
    private List<FraudItem> fraudItems;
    private Boolean manualReviewRequired;
    private Boolean autoApproveAllowed;

    public String getClaimNumber() {
        return claimNumber;
    }
    public void setClaimNumber(String claimNumber) {
        this.claimNumber = claimNumber;
    }
    public Integer getTotalRiskScore() {
        return totalRiskScore;
    }
    public void setTotalRiskScore(Integer totalRiskScore) {
        this.totalRiskScore = totalRiskScore;
    }
    public String getRiskLevel() {
        return riskLevel;
    }
    public void setRiskLevel(String riskLevel) {
        this.riskLevel = riskLevel;
    }
    public List<FraudItem> getFraudItems() {
        return fraudItems;
    }
    public void setFraudItems(List<FraudItem> fraudItems) {
        this.fraudItems = fraudItems;
    }
    public Boolean isManualReviewRequired() {
        return manualReviewRequired;
    }
    public Boolean getManualReviewRequired() {
        return manualReviewRequired;
    }
    public void setManualReviewRequired(Boolean manualReviewRequired) {
        this.manualReviewRequired = manualReviewRequired;
    }
    public Boolean isAutoApproveAllowed() {
        return autoApproveAllowed;
    }
    public Boolean getAutoApproveAllowed() {
        return autoApproveAllowed;
    }
    public void setAutoApproveAllowed(Boolean autoApproveAllowed) {
        this.autoApproveAllowed = autoApproveAllowed;
    }

    public static class FraudItem {
        private String detectionRule;
        private String fraudType;
        private Integer riskScore;
        private String riskLevel;
        private String riskDescription;
        private String evidence;

        public String getDetectionRule() {
            return detectionRule;
        }
        public void setDetectionRule(String detectionRule) {
            this.detectionRule = detectionRule;
        }
        public String getFraudType() {
            return fraudType;
        }
        public void setFraudType(String fraudType) {
            this.fraudType = fraudType;
        }
        public Integer getRiskScore() {
            return riskScore;
        }
        public void setRiskScore(Integer riskScore) {
            this.riskScore = riskScore;
        }
        public String getRiskLevel() {
            return riskLevel;
        }
        public void setRiskLevel(String riskLevel) {
            this.riskLevel = riskLevel;
        }
        public String getRiskDescription() {
            return riskDescription;
        }
        public void setRiskDescription(String riskDescription) {
            this.riskDescription = riskDescription;
        }
        public String getEvidence() {
            return evidence;
        }
        public void setEvidence(String evidence) {
            this.evidence = evidence;
        }
    }
}
