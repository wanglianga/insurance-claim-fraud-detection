package com.insurance.claim.dto;

import com.insurance.claim.enums.MaterialType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class RepairQuoteSubmitRequest {

    @NotBlank(message = "报案号不能为空")
    private String claimNumber;

    @NotNull(message = "材料类型不能为空")
    private MaterialType materialType;

    private String quoteNumber;

    @NotBlank(message = "修理厂名称不能为空")
    private String repairShopName;

    private String repairShopQualification;

    private String repairShopRegion;

    private String vehicleInfo;

    private String vehicleLicensePlate;

    private String vinNumber;

    private Integer mileage;

    private String repairType;

    private String damageDescription;

    private BigDecimal totalLaborCost;

    private BigDecimal totalPartsCost;

    @NotNull(message = "报价总金额不能为空")
    private BigDecimal totalQuoteAmount;

    private LocalDateTime quoteDate;

    private Integer estimatedCompletionDays;

    private Integer quoteValidityDays;

    private String quoterName;

    private String quoterPhone;

    private String quoteFilePath;

    private String quoteFileName;

    private String partsDetailFilePath;

    private List<ExpenseItemRequest> partsItems;

    private LocalDateTime uploadTime;

    private String uploaderId;

    private Boolean stampVerified;
}
