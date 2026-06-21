package com.insurance.claim.dto;

import com.insurance.claim.enums.MaterialType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PhotoUploadRequest {

    @NotBlank(message = "报案号不能为空")
    private String claimNumber;

    @NotNull(message = "材料类型不能为空")
    private MaterialType materialType;

    private String fileName;

    private String filePath;

    private Long fileSize;

    private String contentType;

    private String photoHash;

    private String uploaderId;

    private String uploaderName;

    private String photoDescription;

    private String photoTag;

    private Double gpsLatitude;

    private Double gpsLongitude;

    private LocalDateTime captureTime;

    private String cameraModel;

    private Boolean exifVerified;
}
