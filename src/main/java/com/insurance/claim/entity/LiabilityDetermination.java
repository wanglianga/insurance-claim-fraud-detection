package com.insurance.claim.entity;

import com.insurance.claim.enums.LiabilityType;
import com.insurance.claim.enums.MaterialType;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "liability_determinations")
@EqualsAndHashCode(callSuper = true)
public class LiabilityDetermination extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "claim_id", nullable = false)
    private ClaimReport claim;

    @Enumerated(EnumType.STRING)
    @Column(name = "material_type", length = 30)
    private MaterialType materialType;

    @Column(name = "determination_number", length = 50)
    private String determinationNumber;

    @Column(name = "determination_hash", length = 64)
    private String determinationHash;

    @Column(name = "determining_authority", length = 200)
    private String determiningAuthority;

    @Column(name = "determining_authority_level", length = 50)
    private String determiningAuthorityLevel;

    @Column(name = "determining_authority_region", length = 50)
    private String determiningAuthorityRegion;

    @Column(name = "determining_officer_name", length = 100)
    private String determiningOfficerName;

    @Column(name = "determining_officer_badge", length = 50)
    private String determiningOfficerBadge;

    @Column(name = "determination_date")
    private LocalDateTime determinationDate;

    @Column(name = "accident_summary", columnDefinition = "TEXT")
    private String accidentSummary;

    @Column(name = "evidence_basis", columnDefinition = "TEXT")
    private String evidenceBasis;

    @Column(name = "law_basis", columnDefinition = "TEXT")
    private String lawBasis;

    @Enumerated(EnumType.STRING)
    @Column(name = "liability_type", length = 30)
    private LiabilityType liabilityType;

    @Column(name = "party_a_name", length = 100)
    private String partyAName;

    @Column(name = "party_a_id_card", length = 30)
    private String partyAIdCard;

    @Column(name = "party_a_liability_percent")
    private Integer partyALiabilityPercent;

    @Column(name = "party_b_name", length = 100)
    private String partyBName;

    @Column(name = "party_b_id_card", length = 30)
    private String partyBIdCard;

    @Column(name = "party_b_liability_percent")
    private Integer partyBLiabilityPercent;

    @Column(name = "other_parties_info", columnDefinition = "TEXT")
    private String otherPartiesInfo;

    @Column(name = "mediation_result", columnDefinition = "TEXT")
    private String mediationResult;

    @Column(name = "dispute_resolution_method", length = 50)
    private String disputeResolutionMethod;

    @Column(name = "document_file_path", length = 500)
    private String documentFilePath;

    @Column(name = "document_file_name", length = 255)
    private String documentFileName;

    @Column(name = "stamp_verified")
    private Boolean stampVerified;

    @Column(name = "signature_verified")
    private Boolean signatureVerified;

    @Column(name = "duplicate_found")
    private Boolean duplicateFound;

    @Column(name = "conflict_with_survey")
    private Boolean conflictWithSurvey;

    @Column(name = "conflict_description", columnDefinition = "TEXT")
    private String conflictDescription;

    @Column(name = "upload_time")
    private LocalDateTime uploadTime;

    @Column(name = "uploader_id", length = 50)
    private String uploaderId;

    @Column(name = "verified")
    private Boolean verified;

    @Column(name = "verifier_remark", columnDefinition = "TEXT")
    private String verifierRemark;
}
