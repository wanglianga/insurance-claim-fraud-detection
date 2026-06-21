package com.insurance.claim.entity;

import com.insurance.claim.enums.LiabilityType;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "survey_records")
@EqualsAndHashCode(callSuper = true)
public class SurveyRecord extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "claim_id", nullable = false)
    private ClaimReport claim;

    @Column(name = "survey_number", unique = true, length = 50)
    private String surveyNumber;

    @Column(name = "surveyor_id", length = 50)
    private String surveyorId;

    @Column(name = "surveyor_name", length = 100)
    private String surveyorName;

    @Column(name = "surveyor_phone", length = 20)
    private String surveyorPhone;

    @Column(name = "survey_time", nullable = false)
    private LocalDateTime surveyTime;

    @Column(name = "arrival_time")
    private LocalDateTime arrivalTime;

    @Column(name = "departure_time")
    private LocalDateTime departureTime;

    @Column(name = "survey_location", length = 300)
    private String surveyLocation;

    @Column(name = "site_latitude")
    private Double siteLatitude;

    @Column(name = "site_longitude")
    private Double siteLongitude;

    @Column(name = "distance_from_accident_meters")
    private Double distanceFromAccidentMeters;

    @Column(name = "weather_condition", length = 50)
    private String weatherCondition;

    @Column(name = "road_condition", length = 50)
    private String roadCondition;

    @Column(name = "lighting_condition", length = 50)
    private String lightingCondition;

    @Column(name = "damage_description", columnDefinition = "TEXT")
    private String damageDescription;

    @Column(name = "damage_extent", length = 100)
    private String damageExtent;

    @Column(name = "estimated_loss_amount", precision = 15, scale = 2)
    private BigDecimal estimatedLossAmount;

    @Column(name = "injury_description", columnDefinition = "TEXT")
    private String injuryDescription;

    @Column(name = "injury_severity", length = 50)
    private String injurySeverity;

    @Column(name = "interview_record", columnDefinition = "TEXT")
    private String interviewRecord;

    @Column(name = "interviewee_name", length = 100)
    private String intervieweeName;

    @Column(name = "interviewee_relation", length = 50)
    private String intervieweeRelation;

    @Column(name = "statement_consistent")
    private Boolean statementConsistent;

    @Column(name = "inconsistency_points", columnDefinition = "TEXT")
    private String inconsistencyPoints;

    @Enumerated(EnumType.STRING)
    @Column(name = "liability_judgment", length = 30)
    private LiabilityType liabilityJudgment;

    @Column(name = "liability_judgment_basis", columnDefinition = "TEXT")
    private String liabilityJudgmentBasis;

    @Column(name = "site_sketch_path", length = 500)
    private String siteSketchPath;

    @Column(name = "accident_reconstruction", columnDefinition = "TEXT")
    private String accidentReconstruction;

    @Column(name = "abnormal_situation", columnDefinition = "TEXT")
    private String abnormalSituation;

    @Column(name = "fraud_suspicion")
    private Boolean fraudSuspicion;

    @Column(name = "fraud_suspicion_reason", columnDefinition = "TEXT")
    private String fraudSuspicionReason;

    @Column(name = "survey_conclusion", columnDefinition = "TEXT")
    private String surveyConclusion;

    @Column(name = "next_step_suggestion", columnDefinition = "TEXT")
    private String nextStepSuggestion;

    @Column(name = "verified")
    private Boolean verified;
}
