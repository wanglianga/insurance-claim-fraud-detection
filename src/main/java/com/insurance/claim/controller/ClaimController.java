package com.insurance.claim.controller;

import com.insurance.claim.dto.ApiResponse;
import com.insurance.claim.dto.ClaimSubmitRequest;
import com.insurance.claim.dto.FraudDetectionResultDTO;
import com.insurance.claim.entity.ClaimReport;
import com.insurance.claim.enums.ClaimStatus;
import com.insurance.claim.service.ClaimService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/claims")
public class ClaimController {

    private final ClaimService claimService;

    public ClaimController(ClaimService claimService) {
        this.claimService = claimService;
    }

    @GetMapping("/{claimNumber}")
    public ApiResponse<ClaimReport> getClaim(@PathVariable String claimNumber) {
        ClaimReport claim = claimService.getClaimByNumber(claimNumber);
        return ApiResponse.success(claim);
    }

    @GetMapping("/policy/{policyNumber}")
    public ApiResponse<List<ClaimReport>> getClaimsByPolicy(@PathVariable String policyNumber) {
        List<ClaimReport> claims = claimService.getClaimsByPolicyNumber(policyNumber);
        return ApiResponse.success(claims);
    }

    @GetMapping("/status/{status}")
    public ApiResponse<List<ClaimReport>> getClaimsByStatus(@PathVariable ClaimStatus status) {
        List<ClaimReport> claims = claimService.getClaimsByStatus(status);
        return ApiResponse.success(claims);
    }

    @GetMapping("/surveyor/{surveyorId}")
    public ApiResponse<List<ClaimReport>> getClaimsBySurveyor(@PathVariable String surveyorId) {
        List<ClaimReport> claims = claimService.getClaimsBySurveyor(surveyorId);
        return ApiResponse.success(claims);
    }

    @GetMapping("/reviewer/{reviewerId}")
    public ApiResponse<List<ClaimReport>> getClaimsByReviewer(@PathVariable String reviewerId) {
        List<ClaimReport> claims = claimService.getClaimsByReviewer(reviewerId);
        return ApiResponse.success(claims);
    }

    @GetMapping("/{claimNumber}/fraud-detection")
    public ApiResponse<FraudDetectionResultDTO> getFraudDetectionResult(@PathVariable String claimNumber) {
        FraudDetectionResultDTO result = claimService.getFraudDetectionResult(claimNumber);
        return ApiResponse.success(result);
    }

    @GetMapping("/related/{accidentId}")
    public ApiResponse<List<ClaimReport>> getRelatedClaims(@PathVariable String accidentId) {
        List<ClaimReport> claims = claimService.getRelatedClaims(accidentId);
        return ApiResponse.success(claims);
    }

    @PostMapping("/submit")
    public ApiResponse<ClaimReport> submitClaim(@Valid @RequestBody ClaimSubmitRequest request) {
        ClaimReport claim = claimService.submitClaim(request);
        return ApiResponse.success("报案提交成功", claim);
    }

    @PutMapping("/{claimNumber}/status")
    public ApiResponse<ClaimReport> updateClaimStatus(
            @PathVariable String claimNumber,
            @RequestParam ClaimStatus status) {
        ClaimReport claim = claimService.updateClaimStatus(claimNumber, status);
        return ApiResponse.success("状态更新成功", claim);
    }

    @PutMapping("/{claimNumber}/assign-surveyor")
    public ApiResponse<ClaimReport> assignSurveyor(
            @PathVariable String claimNumber,
            @RequestParam String surveyorId,
            @RequestParam String surveyorName) {
        ClaimReport claim = claimService.assignSurveyor(claimNumber, surveyorId, surveyorName);
        return ApiResponse.success("查勘员分配成功", claim);
    }

    @PostMapping("/{claimNumber}/refresh-fraud")
    public ApiResponse<Void> refreshFraudDetection(@PathVariable String claimNumber) {
        claimService.refreshFraudDetection(claimNumber);
        return ApiResponse.success("欺诈检测已刷新", null);
    }
}
