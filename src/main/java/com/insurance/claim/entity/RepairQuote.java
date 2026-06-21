package com.insurance.claim.entity;

import com.insurance.claim.enums.MaterialType;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "repair_quotes")
@EqualsAndHashCode(callSuper = true)
public class RepairQuote extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "claim_id", nullable = false)
    private ClaimReport claim;

    @Enumerated(EnumType.STRING)
    @Column(name = "material_type", length = 30)
    private MaterialType materialType;

    @Column(name = "quote_number", length = 50)
    private String quoteNumber;

    @Column(name = "quote_hash", length = 64)
    private String quoteHash;

    @Column(name = "repair_shop_name", length = 200)
    private String repairShopName;

    @Column(name = "repair_shop_qualification", length = 100)
    private String repairShopQualification;

    @Column(name = "repair_shop_region", length = 50)
    private String repairShopRegion;

    @Column(name = "vehicle_info", length = 200)
    private String vehicleInfo;

    @Column(name = "vehicle_license_plate", length = 20)
    private String vehicleLicensePlate;

    @Column(name = "vin_number", length = 50)
    private String vinNumber;

    @Column(name = "mileage")
    private Integer mileage;

    @Column(name = "repair_type", length = 50)
    private String repairType;

    @Column(name = "damage_description", columnDefinition = "TEXT")
    private String damageDescription;

    @Column(name = "total_labor_cost", precision = 15, scale = 2)
    private BigDecimal totalLaborCost;

    @Column(name = "total_parts_cost", precision = 15, scale = 2)
    private BigDecimal totalPartsCost;

    @Column(name = "total_quote_amount", precision = 15, scale = 2)
    private BigDecimal totalQuoteAmount;

    @Column(name = "market_average_amount", precision = 15, scale = 2)
    private BigDecimal marketAverageAmount;

    @Column(name = "amount_deviation_percent")
    private Integer amountDeviationPercent;

    @Column(name = "quote_date")
    private LocalDateTime quoteDate;

    @Column(name = "estimated_completion_days")
    private Integer estimatedCompletionDays;

    @Column(name = "quote_validity_days")
    private Integer quoteValidityDays;

    @Column(name = "quoter_name", length = 100)
    private String quoterName;

    @Column(name = "quoter_phone", length = 20)
    private String quoterPhone;

    @Column(name = "quote_file_path", length = 500)
    private String quoteFilePath;

    @Column(name = "quote_file_name", length = 255)
    private String quoteFileName;

    @Column(name = "parts_detail_file_path", length = 500)
    private String partsDetailFilePath;

    @Column(name = "stamp_verified")
    private Boolean stampVerified;

    @Column(name = "duplicate_found")
    private Boolean duplicateFound;

    @Column(name = "amount_abnormal")
    private Boolean amountAbnormal;

    @Column(name = "unusual_parts_detected")
    private Boolean unusualPartsDetected;

    @Column(name = "unusual_parts_description", columnDefinition = "TEXT")
    private String unusualPartsDescription;

    @Column(name = "upload_time")
    private LocalDateTime uploadTime;

    @Column(name = "uploader_id", length = 50)
    private String uploaderId;

    @Column(name = "verified")
    private Boolean verified;

    @Column(name = "verifier_remark", columnDefinition = "TEXT")
    private String verifierRemark;
}
