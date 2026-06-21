package com.insurance.claim.entity;

import com.insurance.claim.enums.ReviewResult;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "compensation_conclusions")
public class CompensationConclusion extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "claim_id", nullable = false)
    private ClaimReport claim;

    @Column(name = "conclusion_number", unique = true, length = 50)
    private String conclusionNumber;

    @Enumerated(EnumType.STRING)
    @Column(name = "review_result", length = 40)
    private ReviewResult reviewResult;

    @Column(name = "final_approved_amount", precision = 15, scale = 2)
    private BigDecimal finalApprovedAmount;

    @Column(name = "rejected_amount", precision = 15, scale = 2)
    private BigDecimal rejectedAmount;

    @Column(name = "rejection_reason", columnDefinition = "TEXT")
    private String rejectionReason;

    @Column(name = "partial_rejection_reason", columnDefinition = "TEXT")
    private String partialRejectionReason;

    @Column(name = "fraud_confirmed")
    private Boolean fraudConfirmed;

    @Column(name = "fraud_type", length = 50)
    private String fraudType;

    @Column(name = "fraud_description", columnDefinition = "TEXT")
    private String fraudDescription;

    @Column(name = "fraud_evidence", columnDefinition = "TEXT")
    private String fraudEvidence;

    @Column(name = "fraud_score")
    private Integer fraudScore;

    @Column(name = "fraud_risk_level", length = 20)
    private String fraudRiskLevel;

    @Column(name = "duplicate_claim_found")
    private Boolean duplicateClaimFound;

    @Column(name = "duplicate_claim_number", length = 50)
    private String duplicateClaimNumber;

    @Column(name = "time_conflict_found")
    private Boolean timeConflictFound;

    @Column(name = "time_conflict_description", columnDefinition = "TEXT")
    private String timeConflictDescription;

    @Column(name = "image_tamper_found")
    private Boolean imageTamperFound;

    @Column(name = "tampered_photos", columnDefinition = "TEXT")
    private String tamperedPhotos;

    @Column(name = "abnormal_amount_found")
    private Boolean abnormalAmountFound;

    @Column(name = "abnormal_amount_description", columnDefinition = "TEXT")
    private String abnormalAmountDescription;

    @Column(name = "related_person_found")
    private Boolean relatedPersonFound;

    @Column(name = "related_persons", columnDefinition = "TEXT")
    private String relatedPersons;

    @Column(name = "cross_region_suspicion")
    private Boolean crossRegionSuspicion;

    @Column(name = "cross_region_description", columnDefinition = "TEXT")
    private String crossRegionDescription;

    @Column(name = "invoice_reuse_found")
    private Boolean invoiceReuseFound;

    @Column(name = "reused_invoice_numbers", columnDefinition = "TEXT")
    private String reusedInvoiceNumbers;

    @Column(name = "multiple_policy_fraud")
    private Boolean multiplePolicyFraud;

    @Column(name = "multiple_policy_numbers", columnDefinition = "TEXT")
    private String multiplePolicyNumbers;

    @Column(name = "materials_verified_complete")
    private Boolean materialsVerifiedComplete;

    @Column(name = "missing_materials", columnDefinition = "TEXT")
    private String missingMaterials;

    @Column(name = "policy_verification_passed")
    private Boolean policyVerificationPassed;

    @Column(name = "policy_verification_remark", columnDefinition = "TEXT")
    private String policyVerificationRemark;

    @Column(name = "liability_verification_passed")
    private Boolean liabilityVerificationPassed;

    @Column(name = "liability_verification_remark", columnDefinition = "TEXT")
    private String liabilityVerificationRemark;

    @Column(name = "survey_verification_passed")
    private Boolean surveyVerificationPassed;

    @Column(name = "survey_verification_remark", columnDefinition = "TEXT")
    private String surveyVerificationRemark;

    @Column(name = "manual_review_performed")
    private Boolean manualReviewPerformed;

    @Column(name = "manual_reviewer_id", length = 50)
    private String manualReviewerId;

    @Column(name = "manual_reviewer_name", length = 100)
    private String manualReviewerName;

    @Column(name = "manual_review_time")
    private LocalDateTime manualReviewTime;

    @Column(name = "manual_review_opinion", columnDefinition = "TEXT")
    private String manualReviewOpinion;

    @Column(name = "senior_reviewer_id", length = 50)
    private String seniorReviewerId;

    @Column(name = "senior_reviewer_name", length = 100)
    private String seniorReviewerName;

    @Column(name = "senior_review_time")
    private LocalDateTime seniorReviewTime;

    @Column(name = "senior_review_opinion", columnDefinition = "TEXT")
    private String seniorReviewOpinion;

    @Column(name = "compensation_payment_time")
    private LocalDateTime compensationPaymentTime;

    @Column(name = "payment_transaction_id", length = 100)
    private String paymentTransactionId;

    @Column(name = "recipient_account", length = 100)
    private String recipientAccount;

    @Column(name = "recipient_name", length = 100)
    private String recipientName;

    @Column(name = "final_reviewer_id", length = 50)
    private String finalReviewerId;

    @Column(name = "final_reviewer_name", length = 100)
    private String finalReviewerName;

    @Column(name = "final_review_time")
    private LocalDateTime finalReviewTime;

    @Column(name = "final_review_remark", columnDefinition = "TEXT")
    private String finalReviewRemark;

    @Column(name = "appeal_allowed")
    private Boolean appealAllowed;

    @Column(name = "appeal_deadline")
    private LocalDateTime appealDeadline;

    @Column(name = "conclusion_remark", columnDefinition = "TEXT")
    private String conclusionRemark;

    public ClaimReport getClaim() {
        return claim;
    }

    public void setClaim(ClaimReport claim) {
        this.claim = claim;
    }

    public String getConclusionNumber() {
        return conclusionNumber;
    }

    public void setConclusionNumber(String conclusionNumber) {
        this.conclusionNumber = conclusionNumber;
    }

    public ReviewResult getReviewResult() {
        return reviewResult;
    }

    public void setReviewResult(ReviewResult reviewResult) {
        this.reviewResult = reviewResult;
    }

    public BigDecimal getFinalApprovedAmount() {
        return finalApprovedAmount;
    }

    public void setFinalApprovedAmount(BigDecimal finalApprovedAmount) {
        this.finalApprovedAmount = finalApprovedAmount;
    }

    public BigDecimal getRejectedAmount() {
        return rejectedAmount;
    }

    public void setRejectedAmount(BigDecimal rejectedAmount) {
        this.rejectedAmount = rejectedAmount;
    }

    public String getRejectionReason() {
        return rejectionReason;
    }

    public void setRejectionReason(String rejectionReason) {
        this.rejectionReason = rejectionReason;
    }

    public String getPartialRejectionReason() {
        return partialRejectionReason;
    }

    public void setPartialRejectionReason(String partialRejectionReason) {
        this.partialRejectionReason = partialRejectionReason;
    }

    public Boolean getFraudConfirmed() {
        return fraudConfirmed;
    }

    public void setFraudConfirmed(Boolean fraudConfirmed) {
        this.fraudConfirmed = fraudConfirmed;
    }

    public String getFraudType() {
        return fraudType;
    }

    public void setFraudType(String fraudType) {
        this.fraudType = fraudType;
    }

    public String getFraudDescription() {
        return fraudDescription;
    }

    public void setFraudDescription(String fraudDescription) {
        this.fraudDescription = fraudDescription;
    }

    public String getFraudEvidence() {
        return fraudEvidence;
    }

    public void setFraudEvidence(String fraudEvidence) {
        this.fraudEvidence = fraudEvidence;
    }

    public Integer getFraudScore() {
        return fraudScore;
    }

    public void setFraudScore(Integer fraudScore) {
        this.fraudScore = fraudScore;
    }

    public String getFraudRiskLevel() {
        return fraudRiskLevel;
    }

    public void setFraudRiskLevel(String fraudRiskLevel) {
        this.fraudRiskLevel = fraudRiskLevel;
    }

    public Boolean getDuplicateClaimFound() {
        return duplicateClaimFound;
    }

    public void setDuplicateClaimFound(Boolean duplicateClaimFound) {
        this.duplicateClaimFound = duplicateClaimFound;
    }

    public String getDuplicateClaimNumber() {
        return duplicateClaimNumber;
    }

    public void setDuplicateClaimNumber(String duplicateClaimNumber) {
        this.duplicateClaimNumber = duplicateClaimNumber;
    }

    public Boolean getTimeConflictFound() {
        return timeConflictFound;
    }

    public void setTimeConflictFound(Boolean timeConflictFound) {
        this.timeConflictFound = timeConflictFound;
    }

    public String getTimeConflictDescription() {
        return timeConflictDescription;
    }

    public void setTimeConflictDescription(String timeConflictDescription) {
        this.timeConflictDescription = timeConflictDescription;
    }

    public Boolean getImageTamperFound() {
        return imageTamperFound;
    }

    public void setImageTamperFound(Boolean imageTamperFound) {
        this.imageTamperFound = imageTamperFound;
    }

    public String getTamperedPhotos() {
        return tamperedPhotos;
    }

    public void setTamperedPhotos(String tamperedPhotos) {
        this.tamperedPhotos = tamperedPhotos;
    }

    public Boolean getAbnormalAmountFound() {
        return abnormalAmountFound;
    }

    public void setAbnormalAmountFound(Boolean abnormalAmountFound) {
        this.abnormalAmountFound = abnormalAmountFound;
    }

    public String getAbnormalAmountDescription() {
        return abnormalAmountDescription;
    }

    public void setAbnormalAmountDescription(String abnormalAmountDescription) {
        this.abnormalAmountDescription = abnormalAmountDescription;
    }

    public Boolean getRelatedPersonFound() {
        return relatedPersonFound;
    }

    public void setRelatedPersonFound(Boolean relatedPersonFound) {
        this.relatedPersonFound = relatedPersonFound;
    }

    public String getRelatedPersons() {
        return relatedPersons;
    }

    public void setRelatedPersons(String relatedPersons) {
        this.relatedPersons = relatedPersons;
    }

    public Boolean getCrossRegionSuspicion() {
        return crossRegionSuspicion;
    }

    public void setCrossRegionSuspicion(Boolean crossRegionSuspicion) {
        this.crossRegionSuspicion = crossRegionSuspicion;
    }

    public String getCrossRegionDescription() {
        return crossRegionDescription;
    }

    public void setCrossRegionDescription(String crossRegionDescription) {
        this.crossRegionDescription = crossRegionDescription;
    }

    public Boolean getInvoiceReuseFound() {
        return invoiceReuseFound;
    }

    public void setInvoiceReuseFound(Boolean invoiceReuseFound) {
        this.invoiceReuseFound = invoiceReuseFound;
    }

    public String getReusedInvoiceNumbers() {
        return reusedInvoiceNumbers;
    }

    public void setReusedInvoiceNumbers(String reusedInvoiceNumbers) {
        this.reusedInvoiceNumbers = reusedInvoiceNumbers;
    }

    public Boolean getMultiplePolicyFraud() {
        return multiplePolicyFraud;
    }

    public void setMultiplePolicyFraud(Boolean multiplePolicyFraud) {
        this.multiplePolicyFraud = multiplePolicyFraud;
    }

    public String getMultiplePolicyNumbers() {
        return multiplePolicyNumbers;
    }

    public void setMultiplePolicyNumbers(String multiplePolicyNumbers) {
        this.multiplePolicyNumbers = multiplePolicyNumbers;
    }

    public Boolean getMaterialsVerifiedComplete() {
        return materialsVerifiedComplete;
    }

    public void setMaterialsVerifiedComplete(Boolean materialsVerifiedComplete) {
        this.materialsVerifiedComplete = materialsVerifiedComplete;
    }

    public String getMissingMaterials() {
        return missingMaterials;
    }

    public void setMissingMaterials(String missingMaterials) {
        this.missingMaterials = missingMaterials;
    }

    public Boolean getPolicyVerificationPassed() {
        return policyVerificationPassed;
    }

    public void setPolicyVerificationPassed(Boolean policyVerificationPassed) {
        this.policyVerificationPassed = policyVerificationPassed;
    }

    public String getPolicyVerificationRemark() {
        return policyVerificationRemark;
    }

    public void setPolicyVerificationRemark(String policyVerificationRemark) {
        this.policyVerificationRemark = policyVerificationRemark;
    }

    public Boolean getLiabilityVerificationPassed() {
        return liabilityVerificationPassed;
    }

    public void setLiabilityVerificationPassed(Boolean liabilityVerificationPassed) {
        this.liabilityVerificationPassed = liabilityVerificationPassed;
    }

    public String getLiabilityVerificationRemark() {
        return liabilityVerificationRemark;
    }

    public void setLiabilityVerificationRemark(String liabilityVerificationRemark) {
        this.liabilityVerificationRemark = liabilityVerificationRemark;
    }

    public Boolean getSurveyVerificationPassed() {
        return surveyVerificationPassed;
    }

    public void setSurveyVerificationPassed(Boolean surveyVerificationPassed) {
        this.surveyVerificationPassed = surveyVerificationPassed;
    }

    public String getSurveyVerificationRemark() {
        return surveyVerificationRemark;
    }

    public void setSurveyVerificationRemark(String surveyVerificationRemark) {
        this.surveyVerificationRemark = surveyVerificationRemark;
    }

    public Boolean getManualReviewPerformed() {
        return manualReviewPerformed;
    }

    public void setManualReviewPerformed(Boolean manualReviewPerformed) {
        this.manualReviewPerformed = manualReviewPerformed;
    }

    public String getManualReviewerId() {
        return manualReviewerId;
    }

    public void setManualReviewerId(String manualReviewerId) {
        this.manualReviewerId = manualReviewerId;
    }

    public String getManualReviewerName() {
        return manualReviewerName;
    }

    public void setManualReviewerName(String manualReviewerName) {
        this.manualReviewerName = manualReviewerName;
    }

    public LocalDateTime getManualReviewTime() {
        return manualReviewTime;
    }

    public void setManualReviewTime(LocalDateTime manualReviewTime) {
        this.manualReviewTime = manualReviewTime;
    }

    public String getManualReviewOpinion() {
        return manualReviewOpinion;
    }

    public void setManualReviewOpinion(String manualReviewOpinion) {
        this.manualReviewOpinion = manualReviewOpinion;
    }

    public String getSeniorReviewerId() {
        return seniorReviewerId;
    }

    public void setSeniorReviewerId(String seniorReviewerId) {
        this.seniorReviewerId = seniorReviewerId;
    }

    public String getSeniorReviewerName() {
        return seniorReviewerName;
    }

    public void setSeniorReviewerName(String seniorReviewerName) {
        this.seniorReviewerName = seniorReviewerName;
    }

    public LocalDateTime getSeniorReviewTime() {
        return seniorReviewTime;
    }

    public void setSeniorReviewTime(LocalDateTime seniorReviewTime) {
        this.seniorReviewTime = seniorReviewTime;
    }

    public String getSeniorReviewOpinion() {
        return seniorReviewOpinion;
    }

    public void setSeniorReviewOpinion(String seniorReviewOpinion) {
        this.seniorReviewOpinion = seniorReviewOpinion;
    }

    public LocalDateTime getCompensationPaymentTime() {
        return compensationPaymentTime;
    }

    public void setCompensationPaymentTime(LocalDateTime compensationPaymentTime) {
        this.compensationPaymentTime = compensationPaymentTime;
    }

    public String getPaymentTransactionId() {
        return paymentTransactionId;
    }

    public void setPaymentTransactionId(String paymentTransactionId) {
        this.paymentTransactionId = paymentTransactionId;
    }

    public String getRecipientAccount() {
        return recipientAccount;
    }

    public void setRecipientAccount(String recipientAccount) {
        this.recipientAccount = recipientAccount;
    }

    public String getRecipientName() {
        return recipientName;
    }

    public void setRecipientName(String recipientName) {
        this.recipientName = recipientName;
    }

    public String getFinalReviewerId() {
        return finalReviewerId;
    }

    public void setFinalReviewerId(String finalReviewerId) {
        this.finalReviewerId = finalReviewerId;
    }

    public String getFinalReviewerName() {
        return finalReviewerName;
    }

    public void setFinalReviewerName(String finalReviewerName) {
        this.finalReviewerName = finalReviewerName;
    }

    public LocalDateTime getFinalReviewTime() {
        return finalReviewTime;
    }

    public void setFinalReviewTime(LocalDateTime finalReviewTime) {
        this.finalReviewTime = finalReviewTime;
    }

    public String getFinalReviewRemark() {
        return finalReviewRemark;
    }

    public void setFinalReviewRemark(String finalReviewRemark) {
        this.finalReviewRemark = finalReviewRemark;
    }

    public Boolean getAppealAllowed() {
        return appealAllowed;
    }

    public void setAppealAllowed(Boolean appealAllowed) {
        this.appealAllowed = appealAllowed;
    }

    public LocalDateTime getAppealDeadline() {
        return appealDeadline;
    }

    public void setAppealDeadline(LocalDateTime appealDeadline) {
        this.appealDeadline = appealDeadline;
    }

    public String getConclusionRemark() {
        return conclusionRemark;
    }

    public void setConclusionRemark(String conclusionRemark) {
        this.conclusionRemark = conclusionRemark;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        CompensationConclusion that = (CompensationConclusion) o;

        if (claim != null ? !claim.getId().equals(that.claim != null ? that.claim.getId() : null) : that.claim != null) return false;
        if (conclusionNumber != null ? !conclusionNumber.equals(that.conclusionNumber) : that.conclusionNumber != null) return false;
        if (reviewResult != that.reviewResult) return false;
        if (finalApprovedAmount != null ? !finalApprovedAmount.equals(that.finalApprovedAmount) : that.finalApprovedAmount != null) return false;
        if (rejectedAmount != null ? !rejectedAmount.equals(that.rejectedAmount) : that.rejectedAmount != null) return false;
        if (rejectionReason != null ? !rejectionReason.equals(that.rejectionReason) : that.rejectionReason != null) return false;
        if (partialRejectionReason != null ? !partialRejectionReason.equals(that.partialRejectionReason) : that.partialRejectionReason != null) return false;
        if (fraudConfirmed != null ? !fraudConfirmed.equals(that.fraudConfirmed) : that.fraudConfirmed != null) return false;
        if (fraudType != null ? !fraudType.equals(that.fraudType) : that.fraudType != null) return false;
        if (fraudDescription != null ? !fraudDescription.equals(that.fraudDescription) : that.fraudDescription != null) return false;
        if (fraudEvidence != null ? !fraudEvidence.equals(that.fraudEvidence) : that.fraudEvidence != null) return false;
        if (fraudScore != null ? !fraudScore.equals(that.fraudScore) : that.fraudScore != null) return false;
        if (fraudRiskLevel != null ? !fraudRiskLevel.equals(that.fraudRiskLevel) : that.fraudRiskLevel != null) return false;
        if (duplicateClaimFound != null ? !duplicateClaimFound.equals(that.duplicateClaimFound) : that.duplicateClaimFound != null) return false;
        if (duplicateClaimNumber != null ? !duplicateClaimNumber.equals(that.duplicateClaimNumber) : that.duplicateClaimNumber != null) return false;
        if (timeConflictFound != null ? !timeConflictFound.equals(that.timeConflictFound) : that.timeConflictFound != null) return false;
        if (timeConflictDescription != null ? !timeConflictDescription.equals(that.timeConflictDescription) : that.timeConflictDescription != null) return false;
        if (imageTamperFound != null ? !imageTamperFound.equals(that.imageTamperFound) : that.imageTamperFound != null) return false;
        if (tamperedPhotos != null ? !tamperedPhotos.equals(that.tamperedPhotos) : that.tamperedPhotos != null) return false;
        if (abnormalAmountFound != null ? !abnormalAmountFound.equals(that.abnormalAmountFound) : that.abnormalAmountFound != null) return false;
        if (abnormalAmountDescription != null ? !abnormalAmountDescription.equals(that.abnormalAmountDescription) : that.abnormalAmountDescription != null) return false;
        if (relatedPersonFound != null ? !relatedPersonFound.equals(that.relatedPersonFound) : that.relatedPersonFound != null) return false;
        if (relatedPersons != null ? !relatedPersons.equals(that.relatedPersons) : that.relatedPersons != null) return false;
        if (crossRegionSuspicion != null ? !crossRegionSuspicion.equals(that.crossRegionSuspicion) : that.crossRegionSuspicion != null) return false;
        if (crossRegionDescription != null ? !crossRegionDescription.equals(that.crossRegionDescription) : that.crossRegionDescription != null) return false;
        if (invoiceReuseFound != null ? !invoiceReuseFound.equals(that.invoiceReuseFound) : that.invoiceReuseFound != null) return false;
        if (reusedInvoiceNumbers != null ? !reusedInvoiceNumbers.equals(that.reusedInvoiceNumbers) : that.reusedInvoiceNumbers != null) return false;
        if (multiplePolicyFraud != null ? !multiplePolicyFraud.equals(that.multiplePolicyFraud) : that.multiplePolicyFraud != null) return false;
        if (multiplePolicyNumbers != null ? !multiplePolicyNumbers.equals(that.multiplePolicyNumbers) : that.multiplePolicyNumbers != null) return false;
        if (materialsVerifiedComplete != null ? !materialsVerifiedComplete.equals(that.materialsVerifiedComplete) : that.materialsVerifiedComplete != null) return false;
        if (missingMaterials != null ? !missingMaterials.equals(that.missingMaterials) : that.missingMaterials != null) return false;
        if (policyVerificationPassed != null ? !policyVerificationPassed.equals(that.policyVerificationPassed) : that.policyVerificationPassed != null) return false;
        if (policyVerificationRemark != null ? !policyVerificationRemark.equals(that.policyVerificationRemark) : that.policyVerificationRemark != null) return false;
        if (liabilityVerificationPassed != null ? !liabilityVerificationPassed.equals(that.liabilityVerificationPassed) : that.liabilityVerificationPassed != null) return false;
        if (liabilityVerificationRemark != null ? !liabilityVerificationRemark.equals(that.liabilityVerificationRemark) : that.liabilityVerificationRemark != null) return false;
        if (surveyVerificationPassed != null ? !surveyVerificationPassed.equals(that.surveyVerificationPassed) : that.surveyVerificationPassed != null) return false;
        if (surveyVerificationRemark != null ? !surveyVerificationRemark.equals(that.surveyVerificationRemark) : that.surveyVerificationRemark != null) return false;
        if (manualReviewPerformed != null ? !manualReviewPerformed.equals(that.manualReviewPerformed) : that.manualReviewPerformed != null) return false;
        if (manualReviewerId != null ? !manualReviewerId.equals(that.manualReviewerId) : that.manualReviewerId != null) return false;
        if (manualReviewerName != null ? !manualReviewerName.equals(that.manualReviewerName) : that.manualReviewerName != null) return false;
        if (manualReviewTime != null ? !manualReviewTime.equals(that.manualReviewTime) : that.manualReviewTime != null) return false;
        if (manualReviewOpinion != null ? !manualReviewOpinion.equals(that.manualReviewOpinion) : that.manualReviewOpinion != null) return false;
        if (seniorReviewerId != null ? !seniorReviewerId.equals(that.seniorReviewerId) : that.seniorReviewerId != null) return false;
        if (seniorReviewerName != null ? !seniorReviewerName.equals(that.seniorReviewerName) : that.seniorReviewerName != null) return false;
        if (seniorReviewTime != null ? !seniorReviewTime.equals(that.seniorReviewTime) : that.seniorReviewTime != null) return false;
        if (seniorReviewOpinion != null ? !seniorReviewOpinion.equals(that.seniorReviewOpinion) : that.seniorReviewOpinion != null) return false;
        if (compensationPaymentTime != null ? !compensationPaymentTime.equals(that.compensationPaymentTime) : that.compensationPaymentTime != null) return false;
        if (paymentTransactionId != null ? !paymentTransactionId.equals(that.paymentTransactionId) : that.paymentTransactionId != null) return false;
        if (recipientAccount != null ? !recipientAccount.equals(that.recipientAccount) : that.recipientAccount != null) return false;
        if (recipientName != null ? !recipientName.equals(that.recipientName) : that.recipientName != null) return false;
        if (finalReviewerId != null ? !finalReviewerId.equals(that.finalReviewerId) : that.finalReviewerId != null) return false;
        if (finalReviewerName != null ? !finalReviewerName.equals(that.finalReviewerName) : that.finalReviewerName != null) return false;
        if (finalReviewTime != null ? !finalReviewTime.equals(that.finalReviewTime) : that.finalReviewTime != null) return false;
        if (finalReviewRemark != null ? !finalReviewRemark.equals(that.finalReviewRemark) : that.finalReviewRemark != null) return false;
        if (appealAllowed != null ? !appealAllowed.equals(that.appealAllowed) : that.appealAllowed != null) return false;
        if (appealDeadline != null ? !appealDeadline.equals(that.appealDeadline) : that.appealDeadline != null) return false;
        return conclusionRemark != null ? conclusionRemark.equals(that.conclusionRemark) : that.conclusionRemark == null;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (claim != null ? claim.getId().hashCode() : 0);
        result = 31 * result + (conclusionNumber != null ? conclusionNumber.hashCode() : 0);
        result = 31 * result + (reviewResult != null ? reviewResult.hashCode() : 0);
        result = 31 * result + (finalApprovedAmount != null ? finalApprovedAmount.hashCode() : 0);
        result = 31 * result + (rejectedAmount != null ? rejectedAmount.hashCode() : 0);
        result = 31 * result + (rejectionReason != null ? rejectionReason.hashCode() : 0);
        result = 31 * result + (partialRejectionReason != null ? partialRejectionReason.hashCode() : 0);
        result = 31 * result + (fraudConfirmed != null ? fraudConfirmed.hashCode() : 0);
        result = 31 * result + (fraudType != null ? fraudType.hashCode() : 0);
        result = 31 * result + (fraudDescription != null ? fraudDescription.hashCode() : 0);
        result = 31 * result + (fraudEvidence != null ? fraudEvidence.hashCode() : 0);
        result = 31 * result + (fraudScore != null ? fraudScore.hashCode() : 0);
        result = 31 * result + (fraudRiskLevel != null ? fraudRiskLevel.hashCode() : 0);
        result = 31 * result + (duplicateClaimFound != null ? duplicateClaimFound.hashCode() : 0);
        result = 31 * result + (duplicateClaimNumber != null ? duplicateClaimNumber.hashCode() : 0);
        result = 31 * result + (timeConflictFound != null ? timeConflictFound.hashCode() : 0);
        result = 31 * result + (timeConflictDescription != null ? timeConflictDescription.hashCode() : 0);
        result = 31 * result + (imageTamperFound != null ? imageTamperFound.hashCode() : 0);
        result = 31 * result + (tamperedPhotos != null ? tamperedPhotos.hashCode() : 0);
        result = 31 * result + (abnormalAmountFound != null ? abnormalAmountFound.hashCode() : 0);
        result = 31 * result + (abnormalAmountDescription != null ? abnormalAmountDescription.hashCode() : 0);
        result = 31 * result + (relatedPersonFound != null ? relatedPersonFound.hashCode() : 0);
        result = 31 * result + (relatedPersons != null ? relatedPersons.hashCode() : 0);
        result = 31 * result + (crossRegionSuspicion != null ? crossRegionSuspicion.hashCode() : 0);
        result = 31 * result + (crossRegionDescription != null ? crossRegionDescription.hashCode() : 0);
        result = 31 * result + (invoiceReuseFound != null ? invoiceReuseFound.hashCode() : 0);
        result = 31 * result + (reusedInvoiceNumbers != null ? reusedInvoiceNumbers.hashCode() : 0);
        result = 31 * result + (multiplePolicyFraud != null ? multiplePolicyFraud.hashCode() : 0);
        result = 31 * result + (multiplePolicyNumbers != null ? multiplePolicyNumbers.hashCode() : 0);
        result = 31 * result + (materialsVerifiedComplete != null ? materialsVerifiedComplete.hashCode() : 0);
        result = 31 * result + (missingMaterials != null ? missingMaterials.hashCode() : 0);
        result = 31 * result + (policyVerificationPassed != null ? policyVerificationPassed.hashCode() : 0);
        result = 31 * result + (policyVerificationRemark != null ? policyVerificationRemark.hashCode() : 0);
        result = 31 * result + (liabilityVerificationPassed != null ? liabilityVerificationPassed.hashCode() : 0);
        result = 31 * result + (liabilityVerificationRemark != null ? liabilityVerificationRemark.hashCode() : 0);
        result = 31 * result + (surveyVerificationPassed != null ? surveyVerificationPassed.hashCode() : 0);
        result = 31 * result + (surveyVerificationRemark != null ? surveyVerificationRemark.hashCode() : 0);
        result = 31 * result + (manualReviewPerformed != null ? manualReviewPerformed.hashCode() : 0);
        result = 31 * result + (manualReviewerId != null ? manualReviewerId.hashCode() : 0);
        result = 31 * result + (manualReviewerName != null ? manualReviewerName.hashCode() : 0);
        result = 31 * result + (manualReviewTime != null ? manualReviewTime.hashCode() : 0);
        result = 31 * result + (manualReviewOpinion != null ? manualReviewOpinion.hashCode() : 0);
        result = 31 * result + (seniorReviewerId != null ? seniorReviewerId.hashCode() : 0);
        result = 31 * result + (seniorReviewerName != null ? seniorReviewerName.hashCode() : 0);
        result = 31 * result + (seniorReviewTime != null ? seniorReviewTime.hashCode() : 0);
        result = 31 * result + (seniorReviewOpinion != null ? seniorReviewOpinion.hashCode() : 0);
        result = 31 * result + (compensationPaymentTime != null ? compensationPaymentTime.hashCode() : 0);
        result = 31 * result + (paymentTransactionId != null ? paymentTransactionId.hashCode() : 0);
        result = 31 * result + (recipientAccount != null ? recipientAccount.hashCode() : 0);
        result = 31 * result + (recipientName != null ? recipientName.hashCode() : 0);
        result = 31 * result + (finalReviewerId != null ? finalReviewerId.hashCode() : 0);
        result = 31 * result + (finalReviewerName != null ? finalReviewerName.hashCode() : 0);
        result = 31 * result + (finalReviewTime != null ? finalReviewTime.hashCode() : 0);
        result = 31 * result + (finalReviewRemark != null ? finalReviewRemark.hashCode() : 0);
        result = 31 * result + (appealAllowed != null ? appealAllowed.hashCode() : 0);
        result = 31 * result + (appealDeadline != null ? appealDeadline.hashCode() : 0);
        result = 31 * result + (conclusionRemark != null ? conclusionRemark.hashCode() : 0);
        return result;
    }
}
