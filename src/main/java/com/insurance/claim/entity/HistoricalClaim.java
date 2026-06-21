package com.insurance.claim.entity;

import com.insurance.claim.enums.AccidentType;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "historical_claims")
@EqualsAndHashCode(callSuper = true)
public class HistoricalClaim extends BaseEntity {

    @Column(name = "claim_number", unique = true, nullable = false, length = 50)
    private String claimNumber;

    @Column(name = "policy_number", nullable = false, length = 50)
    private String policyNumber;

    @Column(name = "policy_holder_name", length = 100)
    private String policyHolderName;

    @Column(name = "policy_holder_id_card", length = 30)
    private String policyHolderIdCard;

    @Column(name = "insured_name", length = 100)
    private String insuredName;

    @Column(name = "insured_id_card", length = 30)
    private String insuredIdCard;

    @Enumerated(EnumType.STRING)
    @Column(name = "accident_type", length = 30)
    private AccidentType accidentType;

    @Column(name = "accident_time")
    private LocalDateTime accidentTime;

    @Column(name = "accident_location", length = 300)
    private String accidentLocation;

    @Column(name = "accident_region", length = 50)
    private String accidentRegion;

    @Column(name = "accident_latitude")
    private Double accidentLatitude;

    @Column(name = "accident_longitude")
    private Double accidentLongitude;

    @Column(name = "claim_amount", precision = 15, scale = 2)
    private BigDecimal claimAmount;

    @Column(name = "approved_amount", precision = 15, scale = 2)
    private BigDecimal approvedAmount;

    @Column(name = "rejected_amount", precision = 15, scale = 2)
    private BigDecimal rejectedAmount;

    @Column(name = "actual_compensation", precision = 15, scale = 2)
    private BigDecimal actualCompensation;

    @Column(name = "compensation_time")
    private LocalDateTime compensationTime;

    @Column(name = "claim_status", length = 30)
    private String claimStatus;

    @Column(name = "fraud_suspected")
    private Boolean fraudSuspected;

    @Column(name = "fraud_confirmed")
    private Boolean fraudConfirmed;

    @Column(name = "fraud_type", length = 50)
    private String fraudType;

    @Column(name = "fraud_description", columnDefinition = "TEXT")
    private String fraudDescription;

    @Column(name = "involved_persons", columnDefinition = "TEXT")
    private String involvedPersons;

    @Column(name = "involved_vehicles", columnDefinition = "TEXT")
    private String involvedVehicles;

    @Column(name = "related_claim_numbers", columnDefinition = "TEXT")
    private String relatedClaimNumbers;

    @Column(name = "settlement_agreement", columnDefinition = "TEXT")
    private String settlementAgreement;

    @Column(name = "remark", columnDefinition = "TEXT")
    private String remark;

    @Column(name = "insurance_company", length = 100)
    private String insuranceCompany;

    @Column(name = "data_source", length = 50)
    private String dataSource;
}
