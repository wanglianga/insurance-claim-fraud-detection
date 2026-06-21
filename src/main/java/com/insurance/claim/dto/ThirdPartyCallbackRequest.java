package com.insurance.claim.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Map;

@Data
public class ThirdPartyCallbackRequest {

    @NotBlank(message = "回调ID不能为空")
    private String callbackId;

    private String relatedClaimNumber;

    @NotBlank(message = "来源系统不能为空")
    private String sourceSystem;

    private String callbackType;

    private String callbackEvent;

    private LocalDateTime callbackTime;

    private String verificationTarget;

    private String verificationResult;

    private String verificationDetail;

    private Map<String, Object> callbackData;

    private String signature;
}
