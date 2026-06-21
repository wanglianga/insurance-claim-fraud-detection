package com.insurance.claim.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class MaterialSupplementRequest {

    @NotBlank(message = "报案号不能为空")
    private String claimNumber;

    private List<String> requestedMaterials;

    private String requestReason;

    private String requesterId;

    private String requesterName;

    private LocalDateTime requestTime;

    private LocalDateTime deadline;

    private String notificationChannel;
}
