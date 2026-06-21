package com.insurance.claim.entity;

import com.insurance.claim.enums.AccidentType;
import com.insurance.claim.enums.ClaimStatus;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "claim_reports")
public class ClaimReport extends BaseEntity {

    @Column(name = "claim_number", unique = true, nullable = false, length = 50)
    private String claimNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "policy_id", nullable = false)
    private InsurancePolicy policy;

    @Column(name = "reporter_name", length = 100)
    private String reporterName;

    @Column(name = "reporter_phone", length = 20)
    private String reporterPhone;

    @Column(name = "reporter_relation", length = 50)
    private String reporterRelation;

    @Enumerated(EnumType.STRING)
    @Column(name = "accident_type", nullable = false, length = 30)
    private AccidentType accidentType;

    @Column(name = "accident_time", nullable = false)
    private LocalDateTime accidentTime;

    @Column(name = "report_time", nullable = false)
    private LocalDateTime reportTime;

    @Column(name = "accident_location", length = 300)
    private String accidentLocation;

    @Column(name = "accident_latitude")
    private Double accidentLatitude;

    @Column(name = "accident_longitude")
    private Double accidentLongitude;

    @Column(name = "accident_region", length = 50)
    private String accidentRegion;

    @Column(name = "accident_description", columnDefinition = "TEXT")
    private String accidentDescription;

    @Column(name = "injury_description", columnDefinition = "TEXT")
    private String injuryDescription;

    @Column(name = "damage_description", columnDefinition = "TEXT")
    private String damageDescription;

    @Column(name = "claimed_amount", precision = 15, scale = 2)
    private BigDecimal claimedAmount;

    @Column(name = "third_party_involved")
    private Boolean thirdPartyInvolved;

    @Column(name = "third_party_name", length = 100)
    private String thirdPartyName;

    @Column(name = "third_party_phone", length = 20)
    private String thirdPartyPhone;

    @Column(name = "third_party_insurance", length = 200)
    private String thirdPartyInsurance;

    @Column(name = "police_reported")
    private Boolean policeReported;

    @Column(name = "police_station", length = 100)
    private String policeStation;

    @Column(name = "police_report_number", length = 50)
    private String policeReportNumber;

    @Column(name = "witness_name", length = 100)
    private String witnessName;

    @Column(name = "witness_phone", length = 20)
    private String witnessPhone;

    @Column(name = "survey_assigned")
    private Boolean surveyAssigned;

    @Column(name = "surveyor_id", length = 50)
    private String surveyorId;

    @Column(name = "surveyor_name", length = 100)
    private String surveyorName;

    @Column(name = "materials_complete")
    private Boolean materialsComplete;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 30)
    private ClaimStatus status;

    @Column(name = "fraud_score")
    private Integer fraudScore;

    @Column(name = "fraud_suspected")
    private Boolean fraudSuspected;

    @Column(name = "manual_review_required")
    private Boolean manualReviewRequired;

    @Column(name = "reviewer_id", length = 50)
    private String reviewerId;

    @Column(name = "reviewer_name", length = 100)
    private String reviewerName;

    @Column(name = "related_accident_id", length = 50)
    private String relatedAccidentId;

    @Column(name = "multiple_policies_involved")
    private Boolean multiplePoliciesInvolved;

    public String getClaimNumber() {
        return claimNumber;
    }

    public void setClaimNumber(String claimNumber) {
        this.claimNumber = claimNumber;
    }

    public InsurancePolicy getPolicy() {
        return policy;
    }

    public void setPolicy(InsurancePolicy policy) {
        this.policy = policy;
    }

    public String getReporterName() {
        return reporterName;
    }

    public void setReporterName(String reporterName) {
        this.reporterName = reporterName;
    }

    public String getReporterPhone() {
        return reporterPhone;
    }

    public void setReporterPhone(String reporterPhone) {
        this.reporterPhone = reporterPhone;
    }

    public String getReporterRelation() {
        return reporterRelation;
    }

    public void setReporterRelation(String reporterRelation) {
        this.reporterRelation = reporterRelation;
    }

    public AccidentType getAccidentType() {
        return accidentType;
    }

    public void setAccidentType(AccidentType accidentType) {
        this.accidentType = accidentType;
    }

    public LocalDateTime getAccidentTime() {
        return accidentTime;
    }

    public void setAccidentTime(LocalDateTime accidentTime) {
        this.accidentTime = accidentTime;
    }

    public LocalDateTime getReportTime() {
        return reportTime;
    }

    public void setReportTime(LocalDateTime reportTime) {
        this.reportTime = reportTime;
    }

    public String getAccidentLocation() {
        return accidentLocation;
    }

    public void setAccidentLocation(String accidentLocation) {
        this.accidentLocation = accidentLocation;
    }

    public Double getAccidentLatitude() {
        return accidentLatitude;
    }

    public void setAccidentLatitude(Double accidentLatitude) {
        this.accidentLatitude = accidentLatitude;
    }

    public Double getAccidentLongitude() {
        return accidentLongitude;
    }

    public void setAccidentLongitude(Double accidentLongitude) {
        this.accidentLongitude = accidentLongitude;
    }

    public String getAccidentRegion() {
        return accidentRegion;
    }

    public void setAccidentRegion(String accidentRegion) {
        this.accidentRegion = accidentRegion;
    }

    public String getAccidentDescription() {
        return accidentDescription;
    }

    public void setAccidentDescription(String accidentDescription) {
        this.accidentDescription = accidentDescription;
    }

    public String getInjuryDescription() {
        return injuryDescription;
    }

    public void setInjuryDescription(String injuryDescription) {
        this.injuryDescription = injuryDescription;
    }

    public String getDamageDescription() {
        return damageDescription;
    }

    public void setDamageDescription(String damageDescription) {
        this.damageDescription = damageDescription;
    }

    public BigDecimal getClaimedAmount() {
        return claimedAmount;
    }

    public void setClaimedAmount(BigDecimal claimedAmount) {
        this.claimedAmount = claimedAmount;
    }

    public Boolean getThirdPartyInvolved() {
        return thirdPartyInvolved;
    }

    public void setThirdPartyInvolved(Boolean thirdPartyInvolved) {
        this.thirdPartyInvolved = thirdPartyInvolved;
    }

    public String getThirdPartyName() {
        return thirdPartyName;
    }

    public void setThirdPartyName(String thirdPartyName) {
        this.thirdPartyName = thirdPartyName;
    }

    public String getThirdPartyPhone() {
        return thirdPartyPhone;
    }

    public void setThirdPartyPhone(String thirdPartyPhone) {
        this.thirdPartyPhone = thirdPartyPhone;
    }

    public String getThirdPartyInsurance() {
        return thirdPartyInsurance;
    }

    public void setThirdPartyInsurance(String thirdPartyInsurance) {
        this.thirdPartyInsurance = thirdPartyInsurance;
    }

    public Boolean getPoliceReported() {
        return policeReported;
    }

    public void setPoliceReported(Boolean policeReported) {
        this.policeReported = policeReported;
    }

    public String getPoliceStation() {
        return policeStation;
    }

    public void setPoliceStation(String policeStation) {
        this.policeStation = policeStation;
    }

    public String getPoliceReportNumber() {
        return policeReportNumber;
    }

    public void setPoliceReportNumber(String policeReportNumber) {
        this.policeReportNumber = policeReportNumber;
    }

    public String getWitnessName() {
        return witnessName;
    }

    public void setWitnessName(String witnessName) {
        this.witnessName = witnessName;
    }

    public String getWitnessPhone() {
        return witnessPhone;
    }

    public void setWitnessPhone(String witnessPhone) {
        this.witnessPhone = witnessPhone;
    }

    public Boolean getSurveyAssigned() {
        return surveyAssigned;
    }

    public void setSurveyAssigned(Boolean surveyAssigned) {
        this.surveyAssigned = surveyAssigned;
    }

    public String getSurveyorId() {
        return surveyorId;
    }

    public void setSurveyorId(String surveyorId) {
        this.surveyorId = surveyorId;
    }

    public String getSurveyorName() {
        return surveyorName;
    }

    public void setSurveyorName(String surveyorName) {
        this.surveyorName = surveyorName;
    }

    public Boolean getMaterialsComplete() {
        return materialsComplete;
    }

    public void setMaterialsComplete(Boolean materialsComplete) {
        this.materialsComplete = materialsComplete;
    }

    public ClaimStatus getStatus() {
        return status;
    }

    public void setStatus(ClaimStatus status) {
        this.status = status;
    }

    public Integer getFraudScore() {
        return fraudScore;
    }

    public void setFraudScore(Integer fraudScore) {
        this.fraudScore = fraudScore;
    }

    public Boolean getFraudSuspected() {
        return fraudSuspected;
    }

    public void setFraudSuspected(Boolean fraudSuspected) {
        this.fraudSuspected = fraudSuspected;
    }

    public Boolean getManualReviewRequired() {
        return manualReviewRequired;
    }

    public void setManualReviewRequired(Boolean manualReviewRequired) {
        this.manualReviewRequired = manualReviewRequired;
    }

    public String getReviewerId() {
        return reviewerId;
    }

    public void setReviewerId(String reviewerId) {
        this.reviewerId = reviewerId;
    }

    public String getReviewerName() {
        return reviewerName;
    }

    public void setReviewerName(String reviewerName) {
        this.reviewerName = reviewerName;
    }

    public String getRelatedAccidentId() {
        return relatedAccidentId;
    }

    public void setRelatedAccidentId(String relatedAccidentId) {
        this.relatedAccidentId = relatedAccidentId;
    }

    public Boolean getMultiplePoliciesInvolved() {
        return multiplePoliciesInvolved;
    }

    public void setMultiplePoliciesInvolved(Boolean multiplePoliciesInvolved) {
        this.multiplePoliciesInvolved = multiplePoliciesInvolved;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        ClaimReport that = (ClaimReport) o;

        if (claimNumber != null ? !claimNumber.equals(that.claimNumber) : that.claimNumber != null) return false;
        if (policy != null ? !policy.getId().equals(that.policy != null ? that.policy.getId() : null) : that.policy != null) return false;
        if (reporterName != null ? !reporterName.equals(that.reporterName) : that.reporterName != null) return false;
        if (reporterPhone != null ? !reporterPhone.equals(that.reporterPhone) : that.reporterPhone != null) return false;
        if (reporterRelation != null ? !reporterRelation.equals(that.reporterRelation) : that.reporterRelation != null) return false;
        if (accidentType != that.accidentType) return false;
        if (accidentTime != null ? !accidentTime.equals(that.accidentTime) : that.accidentTime != null) return false;
        if (reportTime != null ? !reportTime.equals(that.reportTime) : that.reportTime != null) return false;
        if (accidentLocation != null ? !accidentLocation.equals(that.accidentLocation) : that.accidentLocation != null) return false;
        if (accidentLatitude != null ? !accidentLatitude.equals(that.accidentLatitude) : that.accidentLatitude != null) return false;
        if (accidentLongitude != null ? !accidentLongitude.equals(that.accidentLongitude) : that.accidentLongitude != null) return false;
        if (accidentRegion != null ? !accidentRegion.equals(that.accidentRegion) : that.accidentRegion != null) return false;
        if (accidentDescription != null ? !accidentDescription.equals(that.accidentDescription) : that.accidentDescription != null) return false;
        if (injuryDescription != null ? !injuryDescription.equals(that.injuryDescription) : that.injuryDescription != null) return false;
        if (damageDescription != null ? !damageDescription.equals(that.damageDescription) : that.damageDescription != null) return false;
        if (claimedAmount != null ? !claimedAmount.equals(that.claimedAmount) : that.claimedAmount != null) return false;
        if (thirdPartyInvolved != null ? !thirdPartyInvolved.equals(that.thirdPartyInvolved) : that.thirdPartyInvolved != null) return false;
        if (thirdPartyName != null ? !thirdPartyName.equals(that.thirdPartyName) : that.thirdPartyName != null) return false;
        if (thirdPartyPhone != null ? !thirdPartyPhone.equals(that.thirdPartyPhone) : that.thirdPartyPhone != null) return false;
        if (thirdPartyInsurance != null ? !thirdPartyInsurance.equals(that.thirdPartyInsurance) : that.thirdPartyInsurance != null) return false;
        if (policeReported != null ? !policeReported.equals(that.policeReported) : that.policeReported != null) return false;
        if (policeStation != null ? !policeStation.equals(that.policeStation) : that.policeStation != null) return false;
        if (policeReportNumber != null ? !policeReportNumber.equals(that.policeReportNumber) : that.policeReportNumber != null) return false;
        if (witnessName != null ? !witnessName.equals(that.witnessName) : that.witnessName != null) return false;
        if (witnessPhone != null ? !witnessPhone.equals(that.witnessPhone) : that.witnessPhone != null) return false;
        if (surveyAssigned != null ? !surveyAssigned.equals(that.surveyAssigned) : that.surveyAssigned != null) return false;
        if (surveyorId != null ? !surveyorId.equals(that.surveyorId) : that.surveyorId != null) return false;
        if (surveyorName != null ? !surveyorName.equals(that.surveyorName) : that.surveyorName != null) return false;
        if (materialsComplete != null ? !materialsComplete.equals(that.materialsComplete) : that.materialsComplete != null) return false;
        if (status != that.status) return false;
        if (fraudScore != null ? !fraudScore.equals(that.fraudScore) : that.fraudScore != null) return false;
        if (fraudSuspected != null ? !fraudSuspected.equals(that.fraudSuspected) : that.fraudSuspected != null) return false;
        if (manualReviewRequired != null ? !manualReviewRequired.equals(that.manualReviewRequired) : that.manualReviewRequired != null) return false;
        if (reviewerId != null ? !reviewerId.equals(that.reviewerId) : that.reviewerId != null) return false;
        if (reviewerName != null ? !reviewerName.equals(that.reviewerName) : that.reviewerName != null) return false;
        if (relatedAccidentId != null ? !relatedAccidentId.equals(that.relatedAccidentId) : that.relatedAccidentId != null) return false;
        return multiplePoliciesInvolved != null ? multiplePoliciesInvolved.equals(that.multiplePoliciesInvolved) : that.multiplePoliciesInvolved == null;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (claimNumber != null ? claimNumber.hashCode() : 0);
        result = 31 * result + (policy != null ? policy.getId().hashCode() : 0);
        result = 31 * result + (reporterName != null ? reporterName.hashCode() : 0);
        result = 31 * result + (reporterPhone != null ? reporterPhone.hashCode() : 0);
        result = 31 * result + (reporterRelation != null ? reporterRelation.hashCode() : 0);
        result = 31 * result + (accidentType != null ? accidentType.hashCode() : 0);
        result = 31 * result + (accidentTime != null ? accidentTime.hashCode() : 0);
        result = 31 * result + (reportTime != null ? reportTime.hashCode() : 0);
        result = 31 * result + (accidentLocation != null ? accidentLocation.hashCode() : 0);
        result = 31 * result + (accidentLatitude != null ? accidentLatitude.hashCode() : 0);
        result = 31 * result + (accidentLongitude != null ? accidentLongitude.hashCode() : 0);
        result = 31 * result + (accidentRegion != null ? accidentRegion.hashCode() : 0);
        result = 31 * result + (accidentDescription != null ? accidentDescription.hashCode() : 0);
        result = 31 * result + (injuryDescription != null ? injuryDescription.hashCode() : 0);
        result = 31 * result + (damageDescription != null ? damageDescription.hashCode() : 0);
        result = 31 * result + (claimedAmount != null ? claimedAmount.hashCode() : 0);
        result = 31 * result + (thirdPartyInvolved != null ? thirdPartyInvolved.hashCode() : 0);
        result = 31 * result + (thirdPartyName != null ? thirdPartyName.hashCode() : 0);
        result = 31 * result + (thirdPartyPhone != null ? thirdPartyPhone.hashCode() : 0);
        result = 31 * result + (thirdPartyInsurance != null ? thirdPartyInsurance.hashCode() : 0);
        result = 31 * result + (policeReported != null ? policeReported.hashCode() : 0);
        result = 31 * result + (policeStation != null ? policeStation.hashCode() : 0);
        result = 31 * result + (policeReportNumber != null ? policeReportNumber.hashCode() : 0);
        result = 31 * result + (witnessName != null ? witnessName.hashCode() : 0);
        result = 31 * result + (witnessPhone != null ? witnessPhone.hashCode() : 0);
        result = 31 * result + (surveyAssigned != null ? surveyAssigned.hashCode() : 0);
        result = 31 * result + (surveyorId != null ? surveyorId.hashCode() : 0);
        result = 31 * result + (surveyorName != null ? surveyorName.hashCode() : 0);
        result = 31 * result + (materialsComplete != null ? materialsComplete.hashCode() : 0);
        result = 31 * result + (status != null ? status.hashCode() : 0);
        result = 31 * result + (fraudScore != null ? fraudScore.hashCode() : 0);
        result = 31 * result + (fraudSuspected != null ? fraudSuspected.hashCode() : 0);
        result = 31 * result + (manualReviewRequired != null ? manualReviewRequired.hashCode() : 0);
        result = 31 * result + (reviewerId != null ? reviewerId.hashCode() : 0);
        result = 31 * result + (reviewerName != null ? reviewerName.hashCode() : 0);
        result = 31 * result + (relatedAccidentId != null ? relatedAccidentId.hashCode() : 0);
        result = 31 * result + (multiplePoliciesInvolved != null ? multiplePoliciesInvolved.hashCode() : 0);
        return result;
    }
}
