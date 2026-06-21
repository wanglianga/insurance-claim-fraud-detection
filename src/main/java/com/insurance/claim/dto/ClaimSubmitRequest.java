package com.insurance.claim.dto;

import com.insurance.claim.enums.AccidentType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class ClaimSubmitRequest {

    @NotBlank(message = "保单号不能为空")
    private String policyNumber;

    private String reporterName;

    private String reporterPhone;

    private String reporterRelation;

    @NotNull(message = "事故类型不能为空")
    private AccidentType accidentType;

    @NotNull(message = "事故时间不能为空")
    private LocalDateTime accidentTime;

    @NotNull(message = "报案时间不能为空")
    private LocalDateTime reportTime;

    private String accidentLocation;

    private Double accidentLatitude;

    private Double accidentLongitude;

    private String accidentRegion;

    private String accidentDescription;

    private String injuryDescription;

    private String damageDescription;

    private BigDecimal claimedAmount;

    private Boolean thirdPartyInvolved;

    private String thirdPartyName;

    private String thirdPartyPhone;

    private String thirdPartyInsurance;

    private Boolean policeReported;

    private String policeStation;

    private String policeReportNumber;

    private String witnessName;

    private String witnessPhone;

    private String relatedAccidentId;
}
