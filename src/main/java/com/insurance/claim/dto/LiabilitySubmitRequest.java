package com.insurance.claim.dto;

import com.insurance.claim.enums.LiabilityType;
import com.insurance.claim.enums.MaterialType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class LiabilitySubmitRequest {

    @NotBlank(message = "报案号不能为空")
    private String claimNumber;

    @NotNull(message = "材料类型不能为空")
    private MaterialType materialType;

    private String determinationNumber;

    @NotBlank(message = "认定机关不能为空")
    private String determiningAuthority;

    private String determiningAuthorityLevel;

    private String determiningAuthorityRegion;

    private String determiningOfficerName;

    private String determiningOfficerBadge;

    private LocalDateTime determinationDate;

    private String accidentSummary;

    private String evidenceBasis;

    private String lawBasis;

    @NotNull(message = "责任类型不能为空")
    private LiabilityType liabilityType;

    private String partyAName;

    private String partyAIdCard;

    private Integer partyALiabilityPercent;

    private String partyBName;

    private String partyBIdCard;

    private Integer partyBLiabilityPercent;

    private String otherPartiesInfo;

    private String mediationResult;

    private String disputeResolutionMethod;

    private String documentFilePath;

    private String documentFileName;

    private LocalDateTime uploadTime;

    private String uploaderId;

    private Boolean stampVerified;

    private Boolean signatureVerified;
}
