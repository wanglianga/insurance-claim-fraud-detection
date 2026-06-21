package com.insurance.claim.dto;

import com.insurance.claim.enums.MaterialType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class HospitalInvoiceSubmitRequest {

    @NotBlank(message = "报案号不能为空")
    private String claimNumber;

    @NotNull(message = "材料类型不能为空")
    private MaterialType materialType;

    private String invoiceNumber;

    @NotBlank(message = "医院名称不能为空")
    private String hospitalName;

    private String hospitalLevel;

    private String hospitalRegion;

    @NotBlank(message = "患者姓名不能为空")
    private String patientName;

    @NotBlank(message = "患者身份证号不能为空")
    private String patientIdCard;

    private LocalDate admissionDate;

    private LocalDate dischargeDate;

    private Integer treatmentDays;

    private String department;

    private String doctorName;

    private String diagnosis;

    private String diagnosisCode;

    @NotNull(message = "总金额不能为空")
    private BigDecimal totalAmount;

    private BigDecimal medicalServiceAmount;

    private BigDecimal medicineAmount;

    private BigDecimal examinationAmount;

    private BigDecimal treatmentAmount;

    private BigDecimal surgeryAmount;

    private BigDecimal hospitalizationAmount;

    private BigDecimal otherAmount;

    private BigDecimal selfPayAmount;

    private BigDecimal reimbursableAmount;

    private BigDecimal socialSecurityPaid;

    private BigDecimal commercialInsurancePaid;

    private String invoiceFilePath;

    private String invoiceFileName;

    private String detailListFilePath;

    private List<ExpenseItemRequest> expenseItems;

    private LocalDateTime uploadTime;

    private String uploaderId;

    private Boolean stampVerified;
}
