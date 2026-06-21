package com.insurance.claim.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "material_supplements")
@EqualsAndHashCode(callSuper = true)
public class MaterialSupplement extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "claim_id", nullable = false)
    private ClaimReport claim;

    @Column(name = "supplement_number", unique = true, length = 50)
    private String supplementNumber;

    @Column(name = "requested_materials", columnDefinition = "TEXT")
    private String requestedMaterials;

    @Column(name = "request_reason", columnDefinition = "TEXT")
    private String requestReason;

    @Column(name = "requester_id", length = 50)
    private String requesterId;

    @Column(name = "requester_name", length = 100)
    private String requesterName;

    @Column(name = "request_time")
    private LocalDateTime requestTime;

    @Column(name = "deadline")
    private LocalDateTime deadline;

    @Column(name = "notification_sent")
    private Boolean notificationSent;

    @Column(name = "notification_channel", length = 50)
    private String notificationChannel;

    @Column(name = "notification_time")
    private LocalDateTime notificationTime;

    @Column(name = "supplied_materials", columnDefinition = "TEXT")
    private String suppliedMaterials;

    @Column(name = "supplier_id", length = 50)
    private String supplierId;

    @Column(name = "supplier_name", length = 100)
    private String supplierName;

    @Column(name = "supply_time")
    private LocalDateTime supplyTime;

    @Column(name = "all_supplied")
    private Boolean allSupplied;

    @Column(name = "missing_after_supplement", columnDefinition = "TEXT")
    private String missingAfterSupplement;

    @Column(name = "reviewer_id", length = 50)
    private String reviewerId;

    @Column(name = "reviewer_name", length = 100)
    private String reviewerName;

    @Column(name = "review_time")
    private LocalDateTime reviewTime;

    @Column(name = "review_result", length = 30)
    private String reviewResult;

    @Column(name = "review_remark", columnDefinition = "TEXT")
    private String reviewRemark;

    @Column(name = "supplement_count")
    private Integer supplementCount;
}
