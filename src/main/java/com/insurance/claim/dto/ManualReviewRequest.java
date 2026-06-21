package com.insurance.claim.dto;

import com.insurance.claim.enums.FraudType;
import com.insurance.claim.enums.ReviewResult;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class ManualReviewRequest {

    @NotBlank(message = "报案号不能为空")
    private String claimNumber;

    @NotNull(message = "人工复核结果不能为空")
    private ReviewResult reviewResult;

    private BigDecimal finalApprovedAmount;

    private BigDecimal rejectedAmount;

    private String rejectionReason;

    private Boolean fraudConfirmed;

    private FraudType fraudType;

    private String fraudDescription;

    private String fraudEvidence;

    @NotBlank(message = "复核人ID不能为空")
    private String reviewerId;

    @NotBlank(message = "复核人姓名不能为空")
    private String reviewerName;

    private LocalDateTime reviewTime;

    private String reviewOpinion;

    private String seniorReviewerId;

    private String seniorReviewerName;

    private String seniorReviewOpinion;

    private String remark;
}
