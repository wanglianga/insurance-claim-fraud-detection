package com.insurance.claim.entity;

import com.insurance.claim.enums.PolicyStatus;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "insurance_policies")
@EqualsAndHashCode(callSuper = true)
public class InsurancePolicy extends BaseEntity {

    @Column(name = "policy_number", unique = true, nullable = false, length = 50)
    private String policyNumber;

    @Column(name = "policy_holder_name", nullable = false, length = 100)
    private String policyHolderName;

    @Column(name = "policy_holder_id_card", nullable = false, length = 30)
    private String policyHolderIdCard;

    @Column(name = "policy_holder_phone", length = 20)
    private String policyHolderPhone;

    @Column(name = "insured_name", nullable = false, length = 100)
    private String insuredName;

    @Column(name = "insured_id_card", nullable = false, length = 30)
    private String insuredIdCard;

    @Column(name = "policy_type", length = 50)
    private String policyType;

    @Column(name = "coverage_amount", precision = 15, scale = 2)
    private BigDecimal coverageAmount;

    @Column(name = "premium", precision = 15, scale = 2)
    private BigDecimal premium;

    @Column(name = "effective_date", nullable = false)
    private LocalDate effectiveDate;

    @Column(name = "expiry_date", nullable = false)
    private LocalDate expiryDate;

    @Column(name = "insured_item", length = 200)
    private String insuredItem;

    @Column(name = "insured_item_identifier", length = 100)
    private String insuredItemIdentifier;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private PolicyStatus status;

    @Column(name = "issuing_branch", length = 100)
    private String issuingBranch;

    @Column(name = "issuing_region", length = 50)
    private String issuingRegion;

    @Column(name = "agent_name", length = 100)
    private String agentName;

    @Column(name = "risk_level", length = 20)
    private String riskLevel;

    @Column(name = "special_terms", columnDefinition = "TEXT")
    private String specialTerms;
}
