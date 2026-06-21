package com.insurance.claim.entity;

import com.insurance.claim.enums.FraudType;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "blacklist_clues")
public class BlacklistClue extends BaseEntity {

    @Column(name = "clue_number", unique = true, length = 50)
    private String clueNumber;

    @Enumerated(EnumType.STRING)
    @Column(name = "fraud_type", length = 30)
    private FraudType fraudType;

    @Column(name = "person_name", length = 100)
    private String personName;

    @Column(name = "person_id_card", length = 30)
    private String personIdCard;

    @Column(name = "person_phone", length = 20)
    private String personPhone;

    @Column(name = "person_address", length = 300)
    private String personAddress;

    @Column(name = "person_region", length = 50)
    private String personRegion;

    @Column(name = "organization_name", length = 200)
    private String organizationName;

    @Column(name = "organization_license", length = 50)
    private String organizationLicense;

    @Column(name = "organization_region", length = 50)
    private String organizationRegion;

    @Column(name = "vehicle_license_plate", length = 20)
    private String vehicleLicensePlate;

    @Column(name = "vin_number", length = 50)
    private String vinNumber;

    @Column(name = "related_claim_number", length = 50)
    private String relatedClaimNumber;

    @Column(name = "related_policy_number", length = 50)
    private String relatedPolicyNumber;

    @Column(name = "clue_source", length = 100)
    private String clueSource;

    @Column(name = "clue_description", columnDefinition = "TEXT")
    private String clueDescription;

    @Column(name = "evidence", columnDefinition = "TEXT")
    private String evidence;

    @Column(name = "evidence_files", columnDefinition = "TEXT")
    private String evidenceFiles;

    @Column(name = "involved_other_persons", columnDefinition = "TEXT")
    private String involvedOtherPersons;

    @Column(name = "fraud_amount")
    private BigDecimal fraudAmount;

    @Column(name = "risk_level", length = 20)
    private String riskLevel;

    @Column(name = "verified")
    private Boolean verified;

    @Column(name = "verification_result", length = 50)
    private String verificationResult;

    @Column(name = "verifier_id", length = 50)
    private String verifierId;

    @Column(name = "verifier_name", length = 100)
    private String verifierName;

    @Column(name = "verification_time")
    private LocalDateTime verificationTime;

    @Column(name = "verification_remark", columnDefinition = "TEXT")
    private String verificationRemark;

    @Column(name = "blacklisted")
    private Boolean blacklisted;

    @Column(name = "blacklist_time")
    private LocalDateTime blacklistTime;

    @Column(name = "blacklist_expiry_time")
    private LocalDateTime blacklistExpiryTime;

    @Column(name = "report_to_authorities")
    private Boolean reportToAuthorities;

    @Column(name = "authority_report_time")
    private LocalDateTime authorityReportTime;

    @Column(name = "authority_report_number", length = 50)
    private String authorityReportNumber;

    @Column(name = "status", length = 20)
    private String status;

    @Column(name = "remark", columnDefinition = "TEXT")
    private String remark;

    public String getClueNumber() {
        return clueNumber;
    }

    public void setClueNumber(String clueNumber) {
        this.clueNumber = clueNumber;
    }

    public FraudType getFraudType() {
        return fraudType;
    }

    public void setFraudType(FraudType fraudType) {
        this.fraudType = fraudType;
    }

    public String getPersonName() {
        return personName;
    }

    public void setPersonName(String personName) {
        this.personName = personName;
    }

    public String getPersonIdCard() {
        return personIdCard;
    }

    public void setPersonIdCard(String personIdCard) {
        this.personIdCard = personIdCard;
    }

    public String getPersonPhone() {
        return personPhone;
    }

    public void setPersonPhone(String personPhone) {
        this.personPhone = personPhone;
    }

    public String getPersonAddress() {
        return personAddress;
    }

    public void setPersonAddress(String personAddress) {
        this.personAddress = personAddress;
    }

    public String getPersonRegion() {
        return personRegion;
    }

    public void setPersonRegion(String personRegion) {
        this.personRegion = personRegion;
    }

    public String getOrganizationName() {
        return organizationName;
    }

    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
    }

    public String getOrganizationLicense() {
        return organizationLicense;
    }

    public void setOrganizationLicense(String organizationLicense) {
        this.organizationLicense = organizationLicense;
    }

    public String getOrganizationRegion() {
        return organizationRegion;
    }

    public void setOrganizationRegion(String organizationRegion) {
        this.organizationRegion = organizationRegion;
    }

    public String getVehicleLicensePlate() {
        return vehicleLicensePlate;
    }

    public void setVehicleLicensePlate(String vehicleLicensePlate) {
        this.vehicleLicensePlate = vehicleLicensePlate;
    }

    public String getVinNumber() {
        return vinNumber;
    }

    public void setVinNumber(String vinNumber) {
        this.vinNumber = vinNumber;
    }

    public String getRelatedClaimNumber() {
        return relatedClaimNumber;
    }

    public void setRelatedClaimNumber(String relatedClaimNumber) {
        this.relatedClaimNumber = relatedClaimNumber;
    }

    public String getRelatedPolicyNumber() {
        return relatedPolicyNumber;
    }

    public void setRelatedPolicyNumber(String relatedPolicyNumber) {
        this.relatedPolicyNumber = relatedPolicyNumber;
    }

    public String getClueSource() {
        return clueSource;
    }

    public void setClueSource(String clueSource) {
        this.clueSource = clueSource;
    }

    public String getClueDescription() {
        return clueDescription;
    }

    public void setClueDescription(String clueDescription) {
        this.clueDescription = clueDescription;
    }

    public String getEvidence() {
        return evidence;
    }

    public void setEvidence(String evidence) {
        this.evidence = evidence;
    }

    public String getEvidenceFiles() {
        return evidenceFiles;
    }

    public void setEvidenceFiles(String evidenceFiles) {
        this.evidenceFiles = evidenceFiles;
    }

    public String getInvolvedOtherPersons() {
        return involvedOtherPersons;
    }

    public void setInvolvedOtherPersons(String involvedOtherPersons) {
        this.involvedOtherPersons = involvedOtherPersons;
    }

    public BigDecimal getFraudAmount() {
        return fraudAmount;
    }

    public void setFraudAmount(BigDecimal fraudAmount) {
        this.fraudAmount = fraudAmount;
    }

    public String getRiskLevel() {
        return riskLevel;
    }

    public void setRiskLevel(String riskLevel) {
        this.riskLevel = riskLevel;
    }

    public Boolean getVerified() {
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

    public LocalDateTime getVerificationTime() {
        return verificationTime;
    }

    public void setVerificationTime(LocalDateTime verificationTime) {
        this.verificationTime = verificationTime;
    }

    public String getVerificationRemark() {
        return verificationRemark;
    }

    public void setVerificationRemark(String verificationRemark) {
        this.verificationRemark = verificationRemark;
    }

    public Boolean getBlacklisted() {
        return blacklisted;
    }

    public void setBlacklisted(Boolean blacklisted) {
        this.blacklisted = blacklisted;
    }

    public LocalDateTime getBlacklistTime() {
        return blacklistTime;
    }

    public void setBlacklistTime(LocalDateTime blacklistTime) {
        this.blacklistTime = blacklistTime;
    }

    public LocalDateTime getBlacklistExpiryTime() {
        return blacklistExpiryTime;
    }

    public void setBlacklistExpiryTime(LocalDateTime blacklistExpiryTime) {
        this.blacklistExpiryTime = blacklistExpiryTime;
    }

    public Boolean getReportToAuthorities() {
        return reportToAuthorities;
    }

    public void setReportToAuthorities(Boolean reportToAuthorities) {
        this.reportToAuthorities = reportToAuthorities;
    }

    public LocalDateTime getAuthorityReportTime() {
        return authorityReportTime;
    }

    public void setAuthorityReportTime(LocalDateTime authorityReportTime) {
        this.authorityReportTime = authorityReportTime;
    }

    public String getAuthorityReportNumber() {
        return authorityReportNumber;
    }

    public void setAuthorityReportNumber(String authorityReportNumber) {
        this.authorityReportNumber = authorityReportNumber;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        BlacklistClue that = (BlacklistClue) o;

        if (clueNumber != null ? !clueNumber.equals(that.clueNumber) : that.clueNumber != null) return false;
        if (fraudType != that.fraudType) return false;
        if (personName != null ? !personName.equals(that.personName) : that.personName != null) return false;
        if (personIdCard != null ? !personIdCard.equals(that.personIdCard) : that.personIdCard != null) return false;
        if (personPhone != null ? !personPhone.equals(that.personPhone) : that.personPhone != null) return false;
        if (personAddress != null ? !personAddress.equals(that.personAddress) : that.personAddress != null) return false;
        if (personRegion != null ? !personRegion.equals(that.personRegion) : that.personRegion != null) return false;
        if (organizationName != null ? !organizationName.equals(that.organizationName) : that.organizationName != null) return false;
        if (organizationLicense != null ? !organizationLicense.equals(that.organizationLicense) : that.organizationLicense != null) return false;
        if (organizationRegion != null ? !organizationRegion.equals(that.organizationRegion) : that.organizationRegion != null) return false;
        if (vehicleLicensePlate != null ? !vehicleLicensePlate.equals(that.vehicleLicensePlate) : that.vehicleLicensePlate != null) return false;
        if (vinNumber != null ? !vinNumber.equals(that.vinNumber) : that.vinNumber != null) return false;
        if (relatedClaimNumber != null ? !relatedClaimNumber.equals(that.relatedClaimNumber) : that.relatedClaimNumber != null) return false;
        if (relatedPolicyNumber != null ? !relatedPolicyNumber.equals(that.relatedPolicyNumber) : that.relatedPolicyNumber != null) return false;
        if (clueSource != null ? !clueSource.equals(that.clueSource) : that.clueSource != null) return false;
        if (clueDescription != null ? !clueDescription.equals(that.clueDescription) : that.clueDescription != null) return false;
        if (evidence != null ? !evidence.equals(that.evidence) : that.evidence != null) return false;
        if (evidenceFiles != null ? !evidenceFiles.equals(that.evidenceFiles) : that.evidenceFiles != null) return false;
        if (involvedOtherPersons != null ? !involvedOtherPersons.equals(that.involvedOtherPersons) : that.involvedOtherPersons != null) return false;
        if (fraudAmount != null ? !fraudAmount.equals(that.fraudAmount) : that.fraudAmount != null) return false;
        if (riskLevel != null ? !riskLevel.equals(that.riskLevel) : that.riskLevel != null) return false;
        if (verified != null ? !verified.equals(that.verified) : that.verified != null) return false;
        if (verificationResult != null ? !verificationResult.equals(that.verificationResult) : that.verificationResult != null) return false;
        if (verifierId != null ? !verifierId.equals(that.verifierId) : that.verifierId != null) return false;
        if (verifierName != null ? !verifierName.equals(that.verifierName) : that.verifierName != null) return false;
        if (verificationTime != null ? !verificationTime.equals(that.verificationTime) : that.verificationTime != null) return false;
        if (verificationRemark != null ? !verificationRemark.equals(that.verificationRemark) : that.verificationRemark != null) return false;
        if (blacklisted != null ? !blacklisted.equals(that.blacklisted) : that.blacklisted != null) return false;
        if (blacklistTime != null ? !blacklistTime.equals(that.blacklistTime) : that.blacklistTime != null) return false;
        if (blacklistExpiryTime != null ? !blacklistExpiryTime.equals(that.blacklistExpiryTime) : that.blacklistExpiryTime != null) return false;
        if (reportToAuthorities != null ? !reportToAuthorities.equals(that.reportToAuthorities) : that.reportToAuthorities != null) return false;
        if (authorityReportTime != null ? !authorityReportTime.equals(that.authorityReportTime) : that.authorityReportTime != null) return false;
        if (authorityReportNumber != null ? !authorityReportNumber.equals(that.authorityReportNumber) : that.authorityReportNumber != null) return false;
        if (status != null ? !status.equals(that.status) : that.status != null) return false;
        return remark != null ? remark.equals(that.remark) : that.remark == null;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (clueNumber != null ? clueNumber.hashCode() : 0);
        result = 31 * result + (fraudType != null ? fraudType.hashCode() : 0);
        result = 31 * result + (personName != null ? personName.hashCode() : 0);
        result = 31 * result + (personIdCard != null ? personIdCard.hashCode() : 0);
        result = 31 * result + (personPhone != null ? personPhone.hashCode() : 0);
        result = 31 * result + (personAddress != null ? personAddress.hashCode() : 0);
        result = 31 * result + (personRegion != null ? personRegion.hashCode() : 0);
        result = 31 * result + (organizationName != null ? organizationName.hashCode() : 0);
        result = 31 * result + (organizationLicense != null ? organizationLicense.hashCode() : 0);
        result = 31 * result + (organizationRegion != null ? organizationRegion.hashCode() : 0);
        result = 31 * result + (vehicleLicensePlate != null ? vehicleLicensePlate.hashCode() : 0);
        result = 31 * result + (vinNumber != null ? vinNumber.hashCode() : 0);
        result = 31 * result + (relatedClaimNumber != null ? relatedClaimNumber.hashCode() : 0);
        result = 31 * result + (relatedPolicyNumber != null ? relatedPolicyNumber.hashCode() : 0);
        result = 31 * result + (clueSource != null ? clueSource.hashCode() : 0);
        result = 31 * result + (clueDescription != null ? clueDescription.hashCode() : 0);
        result = 31 * result + (evidence != null ? evidence.hashCode() : 0);
        result = 31 * result + (evidenceFiles != null ? evidenceFiles.hashCode() : 0);
        result = 31 * result + (involvedOtherPersons != null ? involvedOtherPersons.hashCode() : 0);
        result = 31 * result + (fraudAmount != null ? fraudAmount.hashCode() : 0);
        result = 31 * result + (riskLevel != null ? riskLevel.hashCode() : 0);
        result = 31 * result + (verified != null ? verified.hashCode() : 0);
        result = 31 * result + (verificationResult != null ? verificationResult.hashCode() : 0);
        result = 31 * result + (verifierId != null ? verifierId.hashCode() : 0);
        result = 31 * result + (verifierName != null ? verifierName.hashCode() : 0);
        result = 31 * result + (verificationTime != null ? verificationTime.hashCode() : 0);
        result = 31 * result + (verificationRemark != null ? verificationRemark.hashCode() : 0);
        result = 31 * result + (blacklisted != null ? blacklisted.hashCode() : 0);
        result = 31 * result + (blacklistTime != null ? blacklistTime.hashCode() : 0);
        result = 31 * result + (blacklistExpiryTime != null ? blacklistExpiryTime.hashCode() : 0);
        result = 31 * result + (reportToAuthorities != null ? reportToAuthorities.hashCode() : 0);
        result = 31 * result + (authorityReportTime != null ? authorityReportTime.hashCode() : 0);
        result = 31 * result + (authorityReportNumber != null ? authorityReportNumber.hashCode() : 0);
        result = 31 * result + (status != null ? status.hashCode() : 0);
        result = 31 * result + (remark != null ? remark.hashCode() : 0);
        return result;
    }
}
