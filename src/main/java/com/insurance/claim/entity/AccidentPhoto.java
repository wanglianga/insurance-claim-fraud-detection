package com.insurance.claim.entity;

import com.insurance.claim.enums.MaterialType;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "accident_photos")
public class AccidentPhoto extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "claim_id", nullable = false)
    private ClaimReport claim;

    @Enumerated(EnumType.STRING)
    @Column(name = "material_type", length = 30)
    private MaterialType materialType;

    @Column(name = "file_name", length = 255)
    private String fileName;

    @Column(name = "file_path", length = 500)
    private String filePath;

    @Column(name = "file_size")
    private Long fileSize;

    @Column(name = "content_type", length = 100)
    private String contentType;

    @Column(name = "photo_hash", length = 64)
    private String photoHash;

    @Column(name = "uploader_id", length = 50)
    private String uploaderId;

    @Column(name = "uploader_name", length = 100)
    private String uploaderName;

    @Column(name = "photo_description", length = 500)
    private String photoDescription;

    @Column(name = "photo_tag", length = 100)
    private String photoTag;

    @Column(name = "gps_latitude")
    private Double gpsLatitude;

    @Column(name = "gps_longitude")
    private Double gpsLongitude;

    @Column(name = "capture_time")
    private LocalDateTime captureTime;

    @Column(name = "camera_model", length = 100)
    private String cameraModel;

    @Column(name = "exif_verified")
    private Boolean exifVerified;

    @Column(name = "tamper_detected")
    private Boolean tamperDetected;

    @Column(name = "tamper_confidence")
    private Integer tamperConfidence;

    @Column(name = "verified")
    private Boolean verified;

    @Column(name = "verifier_remark", columnDefinition = "TEXT")
    private String verifierRemark;

    public ClaimReport getClaim() {
        return claim;
    }

    public void setClaim(ClaimReport claim) {
        this.claim = claim;
    }

    public MaterialType getMaterialType() {
        return materialType;
    }

    public void setMaterialType(MaterialType materialType) {
        this.materialType = materialType;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public Long getFileSize() {
        return fileSize;
    }

    public void setFileSize(Long fileSize) {
        this.fileSize = fileSize;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getPhotoHash() {
        return photoHash;
    }

    public void setPhotoHash(String photoHash) {
        this.photoHash = photoHash;
    }

    public String getUploaderId() {
        return uploaderId;
    }

    public void setUploaderId(String uploaderId) {
        this.uploaderId = uploaderId;
    }

    public String getUploaderName() {
        return uploaderName;
    }

    public void setUploaderName(String uploaderName) {
        this.uploaderName = uploaderName;
    }

    public String getPhotoDescription() {
        return photoDescription;
    }

    public void setPhotoDescription(String photoDescription) {
        this.photoDescription = photoDescription;
    }

    public String getPhotoTag() {
        return photoTag;
    }

    public void setPhotoTag(String photoTag) {
        this.photoTag = photoTag;
    }

    public Double getGpsLatitude() {
        return gpsLatitude;
    }

    public void setGpsLatitude(Double gpsLatitude) {
        this.gpsLatitude = gpsLatitude;
    }

    public Double getGpsLongitude() {
        return gpsLongitude;
    }

    public void setGpsLongitude(Double gpsLongitude) {
        this.gpsLongitude = gpsLongitude;
    }

    public LocalDateTime getCaptureTime() {
        return captureTime;
    }

    public void setCaptureTime(LocalDateTime captureTime) {
        this.captureTime = captureTime;
    }

    public String getCameraModel() {
        return cameraModel;
    }

    public void setCameraModel(String cameraModel) {
        this.cameraModel = cameraModel;
    }

    public Boolean getExifVerified() {
        return exifVerified;
    }

    public void setExifVerified(Boolean exifVerified) {
        this.exifVerified = exifVerified;
    }

    public Boolean getTamperDetected() {
        return tamperDetected;
    }

    public void setTamperDetected(Boolean tamperDetected) {
        this.tamperDetected = tamperDetected;
    }

    public Integer getTamperConfidence() {
        return tamperConfidence;
    }

    public void setTamperConfidence(Integer tamperConfidence) {
        this.tamperConfidence = tamperConfidence;
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

        AccidentPhoto that = (AccidentPhoto) o;

        if (claim != null ? !claim.getId().equals(that.claim != null ? that.claim.getId() : null) : that.claim != null) return false;
        if (materialType != that.materialType) return false;
        if (fileName != null ? !fileName.equals(that.fileName) : that.fileName != null) return false;
        if (filePath != null ? !filePath.equals(that.filePath) : that.filePath != null) return false;
        if (fileSize != null ? !fileSize.equals(that.fileSize) : that.fileSize != null) return false;
        if (contentType != null ? !contentType.equals(that.contentType) : that.contentType != null) return false;
        if (photoHash != null ? !photoHash.equals(that.photoHash) : that.photoHash != null) return false;
        if (uploaderId != null ? !uploaderId.equals(that.uploaderId) : that.uploaderId != null) return false;
        if (uploaderName != null ? !uploaderName.equals(that.uploaderName) : that.uploaderName != null) return false;
        if (photoDescription != null ? !photoDescription.equals(that.photoDescription) : that.photoDescription != null) return false;
        if (photoTag != null ? !photoTag.equals(that.photoTag) : that.photoTag != null) return false;
        if (gpsLatitude != null ? !gpsLatitude.equals(that.gpsLatitude) : that.gpsLatitude != null) return false;
        if (gpsLongitude != null ? !gpsLongitude.equals(that.gpsLongitude) : that.gpsLongitude != null) return false;
        if (captureTime != null ? !captureTime.equals(that.captureTime) : that.captureTime != null) return false;
        if (cameraModel != null ? !cameraModel.equals(that.cameraModel) : that.cameraModel != null) return false;
        if (exifVerified != null ? !exifVerified.equals(that.exifVerified) : that.exifVerified != null) return false;
        if (tamperDetected != null ? !tamperDetected.equals(that.tamperDetected) : that.tamperDetected != null) return false;
        if (tamperConfidence != null ? !tamperConfidence.equals(that.tamperConfidence) : that.tamperConfidence != null) return false;
        if (verified != null ? !verified.equals(that.verified) : that.verified != null) return false;
        return verifierRemark != null ? verifierRemark.equals(that.verifierRemark) : that.verifierRemark == null;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (claim != null ? claim.getId().hashCode() : 0);
        result = 31 * result + (materialType != null ? materialType.hashCode() : 0);
        result = 31 * result + (fileName != null ? fileName.hashCode() : 0);
        result = 31 * result + (filePath != null ? filePath.hashCode() : 0);
        result = 31 * result + (fileSize != null ? fileSize.hashCode() : 0);
        result = 31 * result + (contentType != null ? contentType.hashCode() : 0);
        result = 31 * result + (photoHash != null ? photoHash.hashCode() : 0);
        result = 31 * result + (uploaderId != null ? uploaderId.hashCode() : 0);
        result = 31 * result + (uploaderName != null ? uploaderName.hashCode() : 0);
        result = 31 * result + (photoDescription != null ? photoDescription.hashCode() : 0);
        result = 31 * result + (photoTag != null ? photoTag.hashCode() : 0);
        result = 31 * result + (gpsLatitude != null ? gpsLatitude.hashCode() : 0);
        result = 31 * result + (gpsLongitude != null ? gpsLongitude.hashCode() : 0);
        result = 31 * result + (captureTime != null ? captureTime.hashCode() : 0);
        result = 31 * result + (cameraModel != null ? cameraModel.hashCode() : 0);
        result = 31 * result + (exifVerified != null ? exifVerified.hashCode() : 0);
        result = 31 * result + (tamperDetected != null ? tamperDetected.hashCode() : 0);
        result = 31 * result + (tamperConfidence != null ? tamperConfidence.hashCode() : 0);
        result = 31 * result + (verified != null ? verified.hashCode() : 0);
        result = 31 * result + (verifierRemark != null ? verifierRemark.hashCode() : 0);
        return result;
    }
}
