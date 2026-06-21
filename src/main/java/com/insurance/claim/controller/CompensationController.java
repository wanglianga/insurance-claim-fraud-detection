package com.insurance.claim.controller;

import com.insurance.claim.dto.ApiResponse;
import com.insurance.claim.dto.CompensationReviewRequest;
import com.insurance.claim.dto.ManualReviewRequest;
import com.insurance.claim.entity.ClaimReport;
import com.insurance.claim.entity.CompensationConclusion;
import com.insurance.claim.enums.ReviewResult;
import com.insurance.claim.service.CompensationService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/compensation")
public class CompensationController {

    private final CompensationService compensationService;

    public CompensationController(CompensationService compensationService) {
        this.compensationService = compensationService;
    }

    @GetMapping("/claim/{claimNumber}")
    public ApiResponse<CompensationConclusion> getConclusionByClaim(
            @PathVariable String claimNumber) {
        CompensationConclusion conclusion = compensationService.getConclusionByClaim(claimNumber);
        return ApiResponse.success(conclusion);
    }

    @GetMapping("/result/{result}")
    public ApiResponse<List<CompensationConclusion>> getConclusionsByResult(
            @PathVariable ReviewResult result) {
        List<CompensationConclusion> conclusions = compensationService.getConclusionsByReviewResult(result);
        return ApiResponse.success(conclusions);
    }

    @GetMapping("/fraud-confirmed")
    public ApiResponse<List<CompensationConclusion>> getFraudConfirmedConclusions() {
        List<CompensationConclusion> conclusions = compensationService.getFraudConfirmedConclusions();
        return ApiResponse.success(conclusions);
    }

    @GetMapping("/pending-review")
    public ApiResponse<List<ClaimReport>> getClaimsPendingReview() {
        List<ClaimReport> claims = compensationService.getClaimsPendingReview();
        return ApiResponse.success(claims);
    }

    @GetMapping("/pending-manual-review")
    public ApiResponse<List<ClaimReport>> getClaimsPendingManualReview() {
        List<ClaimReport> claims = compensationService.getClaimsPendingManualReview();
        return ApiResponse.success(claims);
    }

    @PostMapping("/review")
    public ApiResponse<CompensationConclusion> performReview(
            @Valid @RequestBody CompensationReviewRequest request) {
        CompensationConclusion conclusion = compensationService.performCompensationReview(request);
        return ApiResponse.success("赔付审核完成", conclusion);
    }

    @PostMapping("/manual-review")
    public ApiResponse<CompensationConclusion> performManualReview(
            @Valid @RequestBody ManualReviewRequest request) {
        CompensationConclusion conclusion = compensationService.performManualReview(request);
        return ApiResponse.success("人工复核完成", conclusion);
    }

    @PostMapping("/claim/{claimNumber}/process-payment")
    public ApiResponse<CompensationConclusion> processPayment(
            @PathVariable String claimNumber,
            @RequestParam String paymentTransactionId,
            @RequestParam String recipientAccount,
            @RequestParam String recipientName) {
        CompensationConclusion conclusion = compensationService.processCompensationPayment(
                claimNumber, paymentTransactionId, recipientAccount, recipientName);
        return ApiResponse.success("赔付支付已处理", conclusion);
    }
}
