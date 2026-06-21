package com.insurance.claim.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class MaterialSupplyRequest {

    @NotBlank(message = "补传单号不能为空")
    private String supplementNumber;

    private List<String> suppliedMaterials;

    private String supplierId;

    private String supplierName;

    private LocalDateTime supplyTime;
}
