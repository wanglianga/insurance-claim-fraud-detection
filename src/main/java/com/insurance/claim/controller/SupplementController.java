package com.insurance.claim.controller;

import com.insurance.claim.dto.ApiResponse;
import com.insurance.claim.dto.MaterialSupplementRequest;
import com.insurance.claim.dto.MaterialSupplyRequest;
import com.insurance.claim.entity.MaterialSupplement;
import com.insurance.claim.service.MaterialSupplementService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/supplements")
public class SupplementController {

    private final MaterialSupplementService supplementService;

    public SupplementController(MaterialSupplementService supplementService) {
        this.supplementService = supplementService;
    }

    @GetMapping("/{supplementNumber}")
    public ApiResponse<MaterialSupplement> getSupplement(@PathVariable String supplementNumber) {
        MaterialSupplement supplement = supplementService.getSupplementByNumber(supplementNumber);
        return ApiResponse.success(supplement);
    }

    @GetMapping("/claim/{claimNumber}")
    public ApiResponse<List<MaterialSupplement>> getSupplementsByClaim(
            @PathVariable String claimNumber) {
        List<MaterialSupplement> supplements = supplementService.getSupplementsByClaim(claimNumber);
        return ApiResponse.success(supplements);
    }

    @GetMapping("/claim/{claimNumber}/latest")
    public ApiResponse<MaterialSupplement> getLatestSupplement(@PathVariable String claimNumber) {
        MaterialSupplement supplement = supplementService.getLatestSupplement(claimNumber);
        return ApiResponse.success(supplement);
    }

    @GetMapping("/overdue")
    public ApiResponse<List<MaterialSupplement>> getOverdueSupplements() {
        List<MaterialSupplement> supplements = supplementService.getOverdueSupplements();
        return ApiResponse.success(supplements);
    }

    @PostMapping("/request")
    public ApiResponse<MaterialSupplement> requestSupplement(
            @Valid @RequestBody MaterialSupplementRequest request) {
        MaterialSupplement supplement = supplementService.requestSupplement(request);
        return ApiResponse.success("材料补传请求已创建", supplement);
    }

    @PostMapping("/supply")
    public ApiResponse<MaterialSupplement> supplyMaterials(
            @Valid @RequestBody MaterialSupplyRequest request) {
        MaterialSupplement supplement = supplementService.supplyMaterials(request);
        return ApiResponse.success("材料已补传", supplement);
    }

    @PutMapping("/{supplementNumber}/review")
    public ApiResponse<MaterialSupplement> reviewSupplement(
            @PathVariable String supplementNumber,
            @RequestParam String reviewResult,
            @RequestParam(required = false) String reviewRemark,
            @RequestParam String reviewerId,
            @RequestParam String reviewerName) {
        MaterialSupplement supplement = supplementService.reviewSupplement(
                supplementNumber, reviewResult, reviewRemark, reviewerId, reviewerName);
        return ApiResponse.success("补传材料审核完成", supplement);
    }
}
