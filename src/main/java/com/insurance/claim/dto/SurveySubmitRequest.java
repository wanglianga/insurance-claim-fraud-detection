package com.insurance.claim.dto;

import com.insurance.claim.enums.LiabilityType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class SurveySubmitRequest {

    @NotBlank(message = "报案号不能为空")
    private String claimNumber;

    @NotBlank(message = "查勘员ID不能为空")
    private String surveyorId;

    @NotBlank(message = "查勘员姓名不能为空")
    private String surveyorName;

    private String surveyorPhone;

    @NotNull(message = "查勘时间不能为空")
    private LocalDateTime surveyTime;

    private LocalDateTime arrivalTime;

    private LocalDateTime departureTime;

    private String surveyLocation;

    private Double siteLatitude;

    private Double siteLongitude;

    private String weatherCondition;

    private String roadCondition;

    private String lightingCondition;

    private String damageDescription;

    private String damageExtent;

    private BigDecimal estimatedLossAmount;

    private String injuryDescription;

    private String injurySeverity;

    private String interviewRecord;

    private String intervieweeName;

    private String intervieweeRelation;

    private Boolean statementConsistent;

    private String inconsistencyPoints;

    private LiabilityType liabilityJudgment;

    private String liabilityJudgmentBasis;

    private String siteSketchPath;

    private String accidentReconstruction;

    private String abnormalSituation;

    private Boolean fraudSuspicion;

    private String fraudSuspicionReason;

    private String surveyConclusion;

    private String nextStepSuggestion;
}
