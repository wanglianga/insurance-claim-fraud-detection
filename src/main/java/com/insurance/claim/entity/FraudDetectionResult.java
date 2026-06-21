package com.insurance.claim.entity;

import com.insurance.claim.enums.FraudType;
import jakarta.persistence.*;

@Entity
@Table(name = "fraud_detection_results")
public class FraudDetectionResult extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "claim_id", nullable = false)
    private ClaimReport claim;

    @Column(name = "detection_rule", length = 100)
    private String detectionRule;

    @Enumerated(EnumType.STRING)
    @Column(name = "fraud_type", length = 30)
    private FraudType fraudType;

    @Column(name = "risk_score")
    private Integer riskScore;

    @Column(name = "risk_level", length = 20)
    private String riskLevel;

    @Column(name = "risk_description", columnDefinition = "TEXT")
    private String riskDescription;

    @Column(name = "evidence", columnDefinition = "TEXT")
    private String evidence;

    @Column(name = "related_data", columnDefinition = "TEXT")
    private String relatedData;

    @Column(name = "detection_time")
    private java.time.LocalDateTime detectionTime;

    @Column(name = "detected_by", length = 50)
    private String detectedBy;

    @Column(name = "verified")
    private Boolean verified;

    @Column(name = "verification_result", length = 30)
    private String verificationResult;

    @Column(name = "verifier_id", length = 50)
    private String verifierId;

    @Column(name = "verifier_name", length = 100)
    private String verifierName;

    @Column(name = "verification_time")
    private java.time.LocalDateTime verificationTime;

    @Column(name = "verification_remark", columnDefinition = "TEXT")
    private String verificationRemark;

    @Column(name = "action_taken", length = 50)
    private String actionTaken;

    @Column(name = "action_remark", columnDefinition = "TEXT")
    private String actionRemark;

    public ClaimReport getClaim() {
        return claim;
    }
    public void setClaim(ClaimReport claim) {
        this.claim = claim;
    }
    public String getDetectionRule() {
        return detectionRule;
    }
    public void setDetectionRule(String detectionRule) {
        this.detectionRule = detectionRule;
    }
    public FraudType getFraudType() {
        return fraudType;
    }
    public void setFraudType(FraudType fraudType) {
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
    public String getRelatedData() {
        return relatedData;
    }
    public void setRelatedData(String relatedData) {
        this.relatedData = relatedData;
    }
    public java.time.LocalDateTime getDetectionTime() {
        return detectionTime;
    }
    public void setDetectionTime(java.time.LocalDateTime detectionTime) {
        this.detectionTime = detectionTime;
    }
    public String getDetectedBy() {
        return detectedBy;
    }
    public void setDetectedBy(String detectedBy) {
        this.detectedBy = detectedBy;
    }
    public Boolean isVerified() {
        return verified;
    }
    public void setVerified(Boolean verified) {
        this.verified = verified;
    }
    public String getVerificationResult() {
        return verificationResult;
    }
    public void setVerificationResult(String verificationResult) {
        this.verificationResult = verificationResult;
    }
    public String getVerifierId() {
        return verifierId;
    }
    public void setVerifierId(String verifierId) {
        this.verifierId = verifierId;
    }
    public String getVerifierName() {
        return verifierName;
    }
    public void setVerifierName(String verifierName) {
        this.verifierName = verifierName;
    }
    public java.time.LocalDateTime getVerificationTime() {
        return verificationTime;
    }
    public void setVerificationTime(java.time.LocalDateTime verificationTime) {
        this.verificationTime = verificationTime;
    }
    public String getVerificationRemark() {
        return verificationRemark;
    }
    public void setVerificationRemark(String verificationRemark) {
        this.verificationRemark = verificationRemark;
    }
    public String getActionTaken() {
        return actionTaken;
    }
    public void setActionTaken(String actionTaken) {
        this.actionTaken = actionTaken;
    }
    public String getActionRemark() {
        return actionRemark;
    }
    public void setActionRemark(String actionRemark) {
        this.actionRemark = actionRemark;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        FraudDetectionResult that = (FraudDetectionResult) o;

        if (claim != null ? !claim.getId().equals(that.claim != null ? that.claim.getId() : null) : that.claim != null) return false;
        if (detectionRule != null ? !detectionRule.equals(that.detectionRule) : that.detectionRule != null) return false;
        if (fraudType != null ? !fraudType.equals(that.fraudType) : that.fraudType != null) return false;
        if (riskScore != null ? !riskScore.equals(that.riskScore) : that.riskScore != null) return false;
        if (riskLevel != null ? !riskLevel.equals(that.riskLevel) : that.riskLevel != null) return false;
        if (riskDescription != null ? !riskDescription.equals(that.riskDescription) : that.riskDescription != null) return false;
        if (evidence != null ? !evidence.equals(that.evidence) : that.evidence != null) return false;
        if (relatedData != null ? !relatedData.equals(that.relatedData) : that.relatedData != null) return false;
        if (detectionTime != null ? !detectionTime.equals(that.detectionTime) : that.detectionTime != null) return false;
        if (detectedBy != null ? !detectedBy.equals(that.detectedBy) : that.detectedBy != null) return false;
        if (verified != null ? !verified.equals(that.verified) : that.verified != null) return false;
        if (verificationResult != null ? !verificationResult.equals(that.verificationResult) : that.verificationResult != null) return false;
        if (verifierId != null ? !verifierId.equals(that.verifierId) : that.verifierId != null) return false;
        if (verifierName != null ? !verifierName.equals(that.verifierName) : that.verifierName != null) return false;
        if (verificationTime != null ? !verificationTime.equals(that.verificationTime) : that.verificationTime != null) return false;
        if (verificationRemark != null ? !verificationRemark.equals(that.verificationRemark) : that.verificationRemark != null) return false;
        if (actionTaken != null ? !actionTaken.equals(that.actionTaken) : that.actionTaken != null) return false;
        return actionRemark != null ? actionRemark.equals(that.actionRemark) : that.actionRemark == null;
    }
    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (claim != null ? claim.getId().hashCode() : 0);
        result = 31 * result + (detectionRule != null ? detectionRule.hashCode() : 0);
        result = 31 * result + (fraudType != null ? fraudType.hashCode() : 0);
        result = 31 * result + (riskScore != null ? riskScore.hashCode() : 0);
        result = 31 * result + (riskLevel != null ? riskLevel.hashCode() : 0);
        result = 31 * result + (riskDescription != null ? riskDescription.hashCode() : 0);
        result = 31 * result + (evidence != null ? evidence.hashCode() : 0);
        result = 31 * result + (relatedData != null ? relatedData.hashCode() : 0);
        result = 31 * result + (detectionTime != null ? detectionTime.hashCode() : 0);
        result = 31 * result + (detectedBy != null ? detectedBy.hashCode() : 0);
        result = 31 * result + (verified != null ? verified.hashCode() : 0);
        result = 31 * result + (verificationResult != null ? verificationResult.hashCode() : 0);
        result = 31 * result + (verifierId != null ? verifierId.hashCode() : 0);
        result = 31 * result + (verifierName != null ? verifierName.hashCode() : 0);
        result = 31 * result + (verificationTime != null ? verificationTime.hashCode() : 0);
        result = 31 * result + (verificationRemark != null ? verificationRemark.hashCode() : 0);
        result = 31 * result + (actionTaken != null ? actionTaken.hashCode() : 0);
        result = 31 * result + (actionRemark != null ? actionRemark.hashCode() : 0);
        return result;
    }
}
