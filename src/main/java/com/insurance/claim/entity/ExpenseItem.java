package com.insurance.claim.entity;

import com.insurance.claim.enums.MaterialType;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "expense_items")
public class ExpenseItem extends BaseEntity {

    @Column(name = "source_type", length = 30)
    private String sourceType;

    @Column(name = "source_id")
    private Long sourceId;

    @Enumerated(EnumType.STRING)
    @Column(name = "material_type", length = 30)
    private MaterialType materialType;

    @Column(name = "item_code", length = 50)
    private String itemCode;

    @Column(name = "item_name", length = 200)
    private String itemName;

    @Column(name = "item_category", length = 100)
    private String itemCategory;

    @Column(name = "item_type", length = 50)
    private String itemType;

    @Column(name = "item_specification", length = 300)
    private String itemSpecification;

    @Column(name = "unit", length = 20)
    private String unit;

    @Column(name = "quantity")
    private Integer quantity;

    @Column(name = "unit_price", precision = 15, scale = 2)
    private BigDecimal unitPrice;

    @Column(name = "total_amount", precision = 15, scale = 2)
    private BigDecimal totalAmount;

    @Column(name = "reimbursable_amount", precision = 15, scale = 2)
    private BigDecimal reimbursableAmount;

    @Column(name = "self_pay_amount", precision = 15, scale = 2)
    private BigDecimal selfPayAmount;

    @Column(name = "reimbursement_rate")
    private Integer reimbursementRate;

    @Column(name = "treatment_date")
    private LocalDate treatmentDate;

    @Column(name = "doctor_advice", columnDefinition = "TEXT")
    private String doctorAdvice;

    @Column(name = "medical_necessity_verified")
    private Boolean medicalNecessityVerified;

    @Column(name = "price_reasonable")
    private Boolean priceReasonable;

    @Column(name = "market_average_price", precision = 15, scale = 2)
    private BigDecimal marketAveragePrice;

    @Column(name = "price_deviation_percent")
    private Integer priceDeviationPercent;

    @Column(name = "abnormal_detected")
    private Boolean abnormalDetected;

    @Column(name = "abnormal_type", length = 50)
    private String abnormalType;

    @Column(name = "abnormal_description", columnDefinition = "TEXT")
    private String abnormalDescription;

    @Column(name = "verified")
    private Boolean verified;

    @Column(name = "verifier_remark", columnDefinition = "TEXT")
    private String verifierRemark;

    public String getSourceType() {
        return sourceType;
    }

    public void setSourceType(String sourceType) {
        this.sourceType = sourceType;
    }

    public Long getSourceId() {
        return sourceId;
    }

    public void setSourceId(Long sourceId) {
        this.sourceId = sourceId;
    }

    public MaterialType getMaterialType() {
        return materialType;
    }

    public void setMaterialType(MaterialType materialType) {
        this.materialType = materialType;
    }

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

    public LocalDate getTreatmentDate() {
        return treatmentDate;
    }

    public void setTreatmentDate(LocalDate treatmentDate) {
        this.treatmentDate = treatmentDate;
    }

    public String getDoctorAdvice() {
        return doctorAdvice;
    }

    public void setDoctorAdvice(String doctorAdvice) {
        this.doctorAdvice = doctorAdvice;
    }

    public Boolean getMedicalNecessityVerified() {
        return medicalNecessityVerified;
    }

    public void setMedicalNecessityVerified(Boolean medicalNecessityVerified) {
        this.medicalNecessityVerified = medicalNecessityVerified;
    }

    public Boolean getPriceReasonable() {
        return priceReasonable;
    }

    public void setPriceReasonable(Boolean priceReasonable) {
        this.priceReasonable = priceReasonable;
    }

    public BigDecimal getMarketAveragePrice() {
        return marketAveragePrice;
    }

    public void setMarketAveragePrice(BigDecimal marketAveragePrice) {
        this.marketAveragePrice = marketAveragePrice;
    }

    public Integer getPriceDeviationPercent() {
        return priceDeviationPercent;
    }

    public void setPriceDeviationPercent(Integer priceDeviationPercent) {
        this.priceDeviationPercent = priceDeviationPercent;
    }

    public Boolean getAbnormalDetected() {
        return abnormalDetected;
    }

    public void setAbnormalDetected(Boolean abnormalDetected) {
        this.abnormalDetected = abnormalDetected;
    }

    public String getAbnormalType() {
        return abnormalType;
    }

    public void setAbnormalType(String abnormalType) {
        this.abnormalType = abnormalType;
    }

    public String getAbnormalDescription() {
        return abnormalDescription;
    }

    public void setAbnormalDescription(String abnormalDescription) {
        this.abnormalDescription = abnormalDescription;
    }

    public Boolean getVerified() {
        return verified;
    }

    public void setVerified(Boolean verified) {
        this.verified = verified;
    }

    public String getVerifierRemark() {
        return verifierRemark;
    }

    public void setVerifierRemark(String verifierRemark) {
        this.verifierRemark = verifierRemark;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        ExpenseItem that = (ExpenseItem) o;

        if (sourceType != null ? !sourceType.equals(that.sourceType) : that.sourceType != null) return false;
        if (sourceId != null ? !sourceId.equals(that.sourceId) : that.sourceId != null) return false;
        if (materialType != that.materialType) return false;
        if (itemCode != null ? !itemCode.equals(that.itemCode) : that.itemCode != null) return false;
        if (itemName != null ? !itemName.equals(that.itemName) : that.itemName != null) return false;
        if (itemCategory != null ? !itemCategory.equals(that.itemCategory) : that.itemCategory != null) return false;
        if (itemType != null ? !itemType.equals(that.itemType) : that.itemType != null) return false;
        if (itemSpecification != null ? !itemSpecification.equals(that.itemSpecification) : that.itemSpecification != null) return false;
        if (unit != null ? !unit.equals(that.unit) : that.unit != null) return false;
        if (quantity != null ? !quantity.equals(that.quantity) : that.quantity != null) return false;
        if (unitPrice != null ? !unitPrice.equals(that.unitPrice) : that.unitPrice != null) return false;
        if (totalAmount != null ? !totalAmount.equals(that.totalAmount) : that.totalAmount != null) return false;
        if (reimbursableAmount != null ? !reimbursableAmount.equals(that.reimbursableAmount) : that.reimbursableAmount != null) return false;
        if (selfPayAmount != null ? !selfPayAmount.equals(that.selfPayAmount) : that.selfPayAmount != null) return false;
        if (reimbursementRate != null ? !reimbursementRate.equals(that.reimbursementRate) : that.reimbursementRate != null) return false;
        if (treatmentDate != null ? !treatmentDate.equals(that.treatmentDate) : that.treatmentDate != null) return false;
        if (doctorAdvice != null ? !doctorAdvice.equals(that.doctorAdvice) : that.doctorAdvice != null) return false;
        if (medicalNecessityVerified != null ? !medicalNecessityVerified.equals(that.medicalNecessityVerified) : that.medicalNecessityVerified != null) return false;
        if (priceReasonable != null ? !priceReasonable.equals(that.priceReasonable) : that.priceReasonable != null) return false;
        if (marketAveragePrice != null ? !marketAveragePrice.equals(that.marketAveragePrice) : that.marketAveragePrice != null) return false;
        if (priceDeviationPercent != null ? !priceDeviationPercent.equals(that.priceDeviationPercent) : that.priceDeviationPercent != null) return false;
        if (abnormalDetected != null ? !abnormalDetected.equals(that.abnormalDetected) : that.abnormalDetected != null) return false;
        if (abnormalType != null ? !abnormalType.equals(that.abnormalType) : that.abnormalType != null) return false;
        if (abnormalDescription != null ? !abnormalDescription.equals(that.abnormalDescription) : that.abnormalDescription != null) return false;
        if (verified != null ? !verified.equals(that.verified) : that.verified != null) return false;
        return verifierRemark != null ? verifierRemark.equals(that.verifierRemark) : that.verifierRemark == null;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (sourceType != null ? sourceType.hashCode() : 0);
        result = 31 * result + (sourceId != null ? sourceId.hashCode() : 0);
        result = 31 * result + (materialType != null ? materialType.hashCode() : 0);
        result = 31 * result + (itemCode != null ? itemCode.hashCode() : 0);
        result = 31 * result + (itemName != null ? itemName.hashCode() : 0);
        result = 31 * result + (itemCategory != null ? itemCategory.hashCode() : 0);
        result = 31 * result + (itemType != null ? itemType.hashCode() : 0);
        result = 31 * result + (itemSpecification != null ? itemSpecification.hashCode() : 0);
        result = 31 * result + (unit != null ? unit.hashCode() : 0);
        result = 31 * result + (quantity != null ? quantity.hashCode() : 0);
        result = 31 * result + (unitPrice != null ? unitPrice.hashCode() : 0);
        result = 31 * result + (totalAmount != null ? totalAmount.hashCode() : 0);
        result = 31 * result + (reimbursableAmount != null ? reimbursableAmount.hashCode() : 0);
        result = 31 * result + (selfPayAmount != null ? selfPayAmount.hashCode() : 0);
        result = 31 * result + (reimbursementRate != null ? reimbursementRate.hashCode() : 0);
        result = 31 * result + (treatmentDate != null ? treatmentDate.hashCode() : 0);
        result = 31 * result + (doctorAdvice != null ? doctorAdvice.hashCode() : 0);
        result = 31 * result + (medicalNecessityVerified != null ? medicalNecessityVerified.hashCode() : 0);
        result = 31 * result + (priceReasonable != null ? priceReasonable.hashCode() : 0);
        result = 31 * result + (marketAveragePrice != null ? marketAveragePrice.hashCode() : 0);
        result = 31 * result + (priceDeviationPercent != null ? priceDeviationPercent.hashCode() : 0);
        result = 31 * result + (abnormalDetected != null ? abnormalDetected.hashCode() : 0);
        result = 31 * result + (abnormalType != null ? abnormalType.hashCode() : 0);
        result = 31 * result + (abnormalDescription != null ? abnormalDescription.hashCode() : 0);
        result = 31 * result + (verified != null ? verified.hashCode() : 0);
        result = 31 * result + (verifierRemark != null ? verifierRemark.hashCode() : 0);
        return result;
    }
}
