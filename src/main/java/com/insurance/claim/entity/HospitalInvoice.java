package com.insurance.claim.entity;

import com.insurance.claim.enums.MaterialType;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "hospital_invoices")
@EqualsAndHashCode(callSuper = true)
public class HospitalInvoice extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "claim_id", nullable = false)
    private ClaimReport claim;

    @Enumerated(EnumType.STRING)
    @Column(name = "material_type", length = 30)
    private MaterialType materialType;

    @Column(name = "invoice_number", length = 50)
    private String invoiceNumber;

    @Column(name = "invoice_hash", length = 64)
    private String invoiceHash;

    @Column(name = "hospital_name", length = 200)
    private String hospitalName;

    @Column(name = "hospital_level", length = 20)
    private String hospitalLevel;

    @Column(name = "hospital_region", length = 50)
    private String hospitalRegion;

    @Column(name = "patient_name", length = 100)
    private String patientName;

    @Column(name = "patient_id_card", length = 30)
    private String patientIdCard;

    @Column(name = "admission_date")
    private LocalDate admissionDate;

    @Column(name = "discharge_date")
    private LocalDate dischargeDate;

    @Column(name = "treatment_days")
    private Integer treatmentDays;

    @Column(name = "department", length = 100)
    private String department;

    @Column(name = "doctor_name", length = 100)
    private String doctorName;

    @Column(name = "diagnosis", columnDefinition = "TEXT")
    private String diagnosis;

    @Column(name = "diagnosis_code", length = 50)
    private String diagnosisCode;

    @Column(name = "total_amount", precision = 15, scale = 2)
    private BigDecimal totalAmount;

    @Column(name = "medical_service_amount", precision = 15, scale = 2)
    private BigDecimal medicalServiceAmount;

    @Column(name = "medicine_amount", precision = 15, scale = 2)
    private BigDecimal medicineAmount;

    @Column(name = "examination_amount", precision = 15, scale = 2)
    private BigDecimal examinationAmount;

    @Column(name = "treatment_amount", precision = 15, scale = 2)
    private BigDecimal treatmentAmount;

    @Column(name = "surgery_amount", precision = 15, scale = 2)
    private BigDecimal surgeryAmount;

    @Column(name = "hospitalization_amount", precision = 15, scale = 2)
    private BigDecimal hospitalizationAmount;

    @Column(name = "other_amount", precision = 15, scale = 2)
    private BigDecimal otherAmount;

    @Column(name = "self_pay_amount", precision = 15, scale = 2)
    private BigDecimal selfPayAmount;

    @Column(name = "reimbursable_amount", precision = 15, scale = 2)
    private BigDecimal reimbursableAmount;

    @Column(name = "social_security_paid", precision = 15, scale = 2)
    private BigDecimal socialSecurityPaid;

    @Column(name = "commercial_insurance_paid", precision = 15, scale = 2)
    private BigDecimal commercialInsurancePaid;

    @Column(name = "invoice_file_path", length = 500)
    private String invoiceFilePath;

    @Column(name = "invoice_file_name", length = 255)
    private String invoiceFileName;

    @Column(name = "detail_list_file_path", length = 500)
    private String detailListFilePath;

    @Column(name = "stamp_verified")
    private Boolean stampVerified;

    @Column(name = "duplicate_found")
    private Boolean duplicateFound;

    @Column(name = "duplicate_claim_id", length = 50)
    private String duplicateClaimId;

    @Column(name = "amount_abnormal")
    private Boolean amountAbnormal;

    @Column(name = "upload_time")
    private LocalDateTime uploadTime;

    @Column(name = "uploader_id", length = 50)
    private String uploaderId;

    @Column(name = "verified")
    private Boolean verified;

    @Column(name = "verifier_remark", columnDefinition = "TEXT")
    private String verifierRemark;
}
