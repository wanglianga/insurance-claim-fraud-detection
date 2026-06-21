package com.insurance.claim.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "fraud.detection")
public class FraudDetectionConfig {

    private int duplicateClaimWindowHours = 72;

    private int timeConflictWindowMinutes = 60;

    private double abnormalAmountThresholdMultiplier = 3.0;

    private boolean imageTamperCheckEnabled = true;

    private boolean crossRegionCheckEnabled = true;

    private int relatedPersonDepth = 3;

    private int autoApprovalThreshold = 85;

    private int manualReviewThreshold = 50;

    public int getDuplicateClaimWindowHours() {
        return duplicateClaimWindowHours;
    }

    public void setDuplicateClaimWindowHours(int duplicateClaimWindowHours) {
        this.duplicateClaimWindowHours = duplicateClaimWindowHours;
    }

    public int getTimeConflictWindowMinutes() {
        return timeConflictWindowMinutes;
    }

    public void setTimeConflictWindowMinutes(int timeConflictWindowMinutes) {
        this.timeConflictWindowMinutes = timeConflictWindowMinutes;
    }

    public double getAbnormalAmountThresholdMultiplier() {
        return abnormalAmountThresholdMultiplier;
    }

    public void setAbnormalAmountThresholdMultiplier(double abnormalAmountThresholdMultiplier) {
        this.abnormalAmountThresholdMultiplier = abnormalAmountThresholdMultiplier;
    }

    public boolean isImageTamperCheckEnabled() {
        return imageTamperCheckEnabled;
    }

    public void setImageTamperCheckEnabled(boolean imageTamperCheckEnabled) {
        this.imageTamperCheckEnabled = imageTamperCheckEnabled;
    }

    public boolean isCrossRegionCheckEnabled() {
        return crossRegionCheckEnabled;
    }

    public void setCrossRegionCheckEnabled(boolean crossRegionCheckEnabled) {
        this.crossRegionCheckEnabled = crossRegionCheckEnabled;
    }

    public int getRelatedPersonDepth() {
        return relatedPersonDepth;
    }

    public void setRelatedPersonDepth(int relatedPersonDepth) {
        this.relatedPersonDepth = relatedPersonDepth;
    }

    public int getAutoApprovalThreshold() {
        return autoApprovalThreshold;
    }

    public void setAutoApprovalThreshold(int autoApprovalThreshold) {
        this.autoApprovalThreshold = autoApprovalThreshold;
    }

    public int getManualReviewThreshold() {
        return manualReviewThreshold;
    }

    public void setManualReviewThreshold(int manualReviewThreshold) {
        this.manualReviewThreshold = manualReviewThreshold;
    }
}
