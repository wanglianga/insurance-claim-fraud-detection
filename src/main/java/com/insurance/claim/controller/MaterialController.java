package com.insurance.claim.controller;

import com.insurance.claim.dto.ApiResponse;
import com.insurance.claim.dto.HospitalInvoiceSubmitRequest;
import com.insurance.claim.dto.LiabilitySubmitRequest;
import com.insurance.claim.dto.PhotoUploadRequest;
import com.insurance.claim.dto.RepairQuoteSubmitRequest;
import com.insurance.claim.entity.AccidentPhoto;
import com.insurance.claim.entity.ExpenseItem;
import com.insurance.claim.entity.HospitalInvoice;
import com.insurance.claim.entity.LiabilityDetermination;
import com.insurance.claim.entity.RepairQuote;
import com.insurance.claim.service.MaterialVerificationService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/materials")
public class MaterialController {

    private final MaterialVerificationService materialVerificationService;

    public MaterialController(MaterialVerificationService materialVerificationService) {
        this.materialVerificationService = materialVerificationService;
    }

    @GetMapping("/claim/{claimNumber}/photos")
    public ApiResponse<List<AccidentPhoto>> getPhotosByClaim(@PathVariable String claimNumber) {
        List<AccidentPhoto> photos = materialVerificationService.getPhotosByClaim(claimNumber);
        return ApiResponse.success(photos);
    }

    @GetMapping("/claim/{claimNumber}/invoices")
    public ApiResponse<List<HospitalInvoice>> getInvoicesByClaim(@PathVariable String claimNumber) {
        List<HospitalInvoice> invoices = materialVerificationService.getInvoicesByClaim(claimNumber);
        return ApiResponse.success(invoices);
    }

    @GetMapping("/claim/{claimNumber}/quotes")
    public ApiResponse<List<RepairQuote>> getQuotesByClaim(@PathVariable String claimNumber) {
        List<RepairQuote> quotes = materialVerificationService.getQuotesByClaim(claimNumber);
        return ApiResponse.success(quotes);
    }

    @GetMapping("/claim/{claimNumber}/liabilities")
    public ApiResponse<List<LiabilityDetermination>> getLiabilitiesByClaim(
            @PathVariable String claimNumber) {
        List<LiabilityDetermination> liabilities = materialVerificationService.getLiabilitiesByClaim(claimNumber);
        return ApiResponse.success(liabilities);
    }

    @GetMapping("/expenses")
    public ApiResponse<List<ExpenseItem>> getExpenseItems(
            @RequestParam String sourceType,
            @RequestParam Long sourceId) {
        List<ExpenseItem> items = materialVerificationService.getExpenseItems(sourceType, sourceId);
        return ApiResponse.success(items);
    }

    @PostMapping("/photos")
    public ApiResponse<AccidentPhoto> uploadPhoto(@Valid @RequestBody PhotoUploadRequest request) {
        AccidentPhoto photo = materialVerificationService.uploadPhoto(request);
        return ApiResponse.success("照片上传成功", photo);
    }

    @PostMapping("/invoices")
    public ApiResponse<HospitalInvoice> submitInvoice(@Valid @RequestBody HospitalInvoiceSubmitRequest request) {
        HospitalInvoice invoice = materialVerificationService.submitHospitalInvoice(request);
        return ApiResponse.success("医院票据提交成功", invoice);
    }

    @PostMapping("/quotes")
    public ApiResponse<RepairQuote> submitQuote(@Valid @RequestBody RepairQuoteSubmitRequest request) {
        RepairQuote quote = materialVerificationService.submitRepairQuote(request);
        return ApiResponse.success("维修报价提交成功", quote);
    }

    @PostMapping("/liabilities")
    public ApiResponse<LiabilityDetermination> submitLiability(
            @Valid @RequestBody LiabilitySubmitRequest request) {
        LiabilityDetermination liability = materialVerificationService.submitLiabilityDetermination(request);
        return ApiResponse.success("责任认定提交成功", liability);
    }

    @PutMapping("/photos/{photoId}/verify")
    public ApiResponse<AccidentPhoto> verifyPhoto(
            @PathVariable Long photoId,
            @RequestParam boolean verified,
            @RequestParam(required = false) String remark) {
        AccidentPhoto photo = materialVerificationService.verifyPhoto(photoId, verified, remark);
        return ApiResponse.success("照片验证完成", photo);
    }

    @PutMapping("/invoices/{invoiceId}/verify")
    public ApiResponse<HospitalInvoice> verifyInvoice(
            @PathVariable Long invoiceId,
            @RequestParam boolean verified,
            @RequestParam(required = false) String remark) {
        HospitalInvoice invoice = materialVerificationService.verifyInvoice(invoiceId, verified, remark);
        return ApiResponse.success("票据验证完成", invoice);
    }

    @PutMapping("/quotes/{quoteId}/verify")
    public ApiResponse<RepairQuote> verifyQuote(
            @PathVariable Long quoteId,
            @RequestParam boolean verified,
            @RequestParam(required = false) String remark) {
        RepairQuote quote = materialVerificationService.verifyQuote(quoteId, verified, remark);
        return ApiResponse.success("报价单验证完成", quote);
    }

    @PutMapping("/liabilities/{liabilityId}/verify")
    public ApiResponse<LiabilityDetermination> verifyLiability(
            @PathVariable Long liabilityId,
            @RequestParam boolean verified,
            @RequestParam(required = false) String remark) {
        LiabilityDetermination liability = materialVerificationService.verifyLiability(
                liabilityId, verified, remark);
        return ApiResponse.success("认定书验证完成", liability);
    }
}
