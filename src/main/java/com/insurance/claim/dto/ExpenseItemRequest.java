package com.insurance.claim.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public class ExpenseItemRequest {

    private String itemCode;
    private String itemName;
    private String itemCategory;
    private String itemType;
    private String itemSpecification;
    private String unit;
    private Integer quantity;
    private BigDecimal unitPrice;
    private BigDecimal totalAmount;
    private BigDecimal reimbursableAmount;
    private BigDecimal selfPayAmount;
    private Integer reimbursementRate;
    private String doctorAdvice;
    private java.time.LocalDate treatmentDate;

    public String getItemCode() {
        return itemCode;
    }
    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }
    public String getItemName() {
        return itemName;
    }
    public void setItemName(String itemName) {
        this.itemName = itemName;
    }
    public String getItemCategory() {
        return itemCategory;
    }
    public void setItemCategory(String itemCategory) {
        this.itemCategory = itemCategory;
    }
    public String getItemType() {
        return itemType;
    }
    public void setItemType(String itemType) {
        this.itemType = itemType;
    }
    public String getItemSpecification() {
        return itemSpecification;
    }
    public void setItemSpecification(String itemSpecification) {
        this.itemSpecification = itemSpecification;
    }
    public String getUnit() {
        return unit;
    }
    public void setUnit(String unit) {
        this.unit = unit;
    }
    public Integer getQuantity() {
        return quantity;
    }
    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
    public BigDecimal getUnitPrice() {
        return unitPrice;
    }
    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }
    public BigDecimal getTotalAmount() {
        return totalAmount;
    }
    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }
    public BigDecimal getReimbursableAmount() {
        return reimbursableAmount;
    }
    public void setReimbursableAmount(BigDecimal reimbursableAmount) {
        this.reimbursableAmount = reimbursableAmount;
    }
    public BigDecimal getSelfPayAmount() {
        return selfPayAmount;
    }
    public void setSelfPayAmount(BigDecimal selfPayAmount) {
        this.selfPayAmount = selfPayAmount;
    }
    public Integer getReimbursementRate() {
        return reimbursementRate;
    }
    public void setReimbursementRate(Integer reimbursementRate) {
        this.reimbursementRate = reimbursementRate;
    }
    public String getDoctorAdvice() {
        return doctorAdvice;
    }
    public void setDoctorAdvice(String doctorAdvice) {
        this.doctorAdvice = doctorAdvice;
    }
    public java.time.LocalDate getTreatmentDate() {
        return treatmentDate;
    }
    public void setTreatmentDate(java.time.LocalDate treatmentDate) {
        this.treatmentDate = treatmentDate;
    }
}