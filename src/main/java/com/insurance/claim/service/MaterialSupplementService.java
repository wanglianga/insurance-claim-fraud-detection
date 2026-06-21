package com.insurance.claim.service;

import com.insurance.claim.dto.MaterialSupplementRequest;
import com.insurance.claim.dto.MaterialSupplyRequest;
import com.insurance.claim.entity.ClaimReport;
import com.insurance.claim.entity.MaterialSupplement;
import com.insurance.claim.enums.ClaimStatus;
import com.insurance.claim.exception.BusinessException;
import com.insurance.claim.repository.ClaimReportRepository;
import com.insurance.claim.repository.MaterialSupplementRepository;
import com.insurance.claim.util.IdGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class MaterialSupplementService {

    private final MaterialSupplementRepository supplementRepository;
    private final ClaimReportRepository claimReportRepository;
    private final FraudDetectionService fraudDetectionService;
    private final MaterialVerificationService materialVerificationService;

    @Transactional
    public MaterialSupplement requestSupplement(MaterialSupplementRequest request) {
        ClaimReport claim = claimReportRepository.findByClaimNumber(request.getClaimNumber())
                .orElseThrow(() -> new BusinessException("CLAIM_NOT_FOUND",
                        "报案不存在: " + request.getClaimNumber()));

        int existingCount = supplementRepository.countByClaimId(claim.getId());

        MaterialSupplement supplement = new MaterialSupplement();
        supplement.setSupplementNumber(IdGenerator.generateSupplementNumber());
        supplement.setClaim(claim);
        supplement.setRequestedMaterials(
                request.getRequestedMaterials() != null
                        ? String.join(";", request.getRequestedMaterials())
                : null);
        supplement.setRequestReason(request.getRequestReason());
        supplement.setRequesterId(request.getRequesterId());
        supplement.setRequesterName(request.getRequesterName());
        supplement.setRequestTime(request.getRequestTime() != null ? request.getRequestTime() : LocalDateTime.now());
        supplement.setDeadline(request.getDeadline() != null ? request.getDeadline() : LocalDateTime.now().plusDays(7));
        supplement.setNotificationChannel(request.getNotificationChannel());
        supplement.setNotificationSent(true);
        supplement.setNotificationTime(LocalDateTime.now());
        supplement.setAllSupplied(false);
        supplement.setSupplementCount(existingCount + 1);

        supplement = supplementRepository.save(supplement);

        claim.setStatus(ClaimStatus.MATERIALS_PENDING);
        claim.setMaterialsComplete(false);
        claimReportRepository.save(claim);

        log.info("材料补传请求已创建，补传单号: {}, 报案号: {}",
                supplement.getSupplementNumber(), claim.getClaimNumber());

        return supplement;
    }

    @Transactional
    public MaterialSupplement supplyMaterials(MaterialSupplyRequest request) {
        MaterialSupplement supplement = supplementRepository.findBySupplementNumber(request.getSupplementNumber())
                .orElseThrow(() -> new BusinessException("SUPPLEMENT_NOT_FOUND",
                        "补传记录不存在: " + request.getSupplementNumber()));

        if (Boolean.TRUE.equals(supplement.getAllSupplied())) {
            throw new BusinessException("ALREADY_SUPPLIED", "该补传请求已完成");
        }

        supplement.setSuppliedMaterials(
                request.getSuppliedMaterials() != null
                        ? String.join(";", request.getSuppliedMaterials())
                : null);
        supplement.setSupplierId(request.getSupplierId());
        supplement.setSupplierName(request.getSupplierName());
        supplement.setSupplyTime(request.getSupplyTime() != null ? request.getSupplyTime() : LocalDateTime.now());

        List<String> requested = supplement.getRequestedMaterials() != null
                ? List.of(supplement.getRequestedMaterials().split(";"))
                : List.of();
        List<String> supplied = supplement.getSuppliedMaterials() != null
                ? List.of(supplement.getSuppliedMaterials().split(";"))
                : List.of();

        boolean allSupplied = requested.isEmpty() || supplied.containsAll(requested);
        supplement.setAllSupplied(allSupplied);

        if (!allSupplied) {
            List<String> missing = requested.stream()
                    .filter(r -> !supplied.contains(r))
                    .toList();
            supplement.setMissingAfterSupplement(String.join(";", missing));
        }

        supplement.setReviewerId(request.getSupplierId());
        supplement.setReviewerName(request.getSupplierName());
        supplement.setReviewTime(LocalDateTime.now());
        supplement.setReviewResult(allSupplied ? "SUPPLIED" : "PARTIAL");

        supplement = supplementRepository.save(supplement);

        ClaimReport claim = supplement.getClaim();
        materialVerificationService.checkMaterialsComplete(claim.getId());

        if (allSupplied) {
            if (claim.getManualReviewRequired() == Boolean.TRUE) {
                claim.setStatus(ClaimStatus.MANUAL_REVIEW_REQUIRED);
            } else {
                claim.setStatus(ClaimStatus.REVIEWING);
            }
            claimReportRepository.save(claim);

            fraudDetectionService.evictFraudCache(claim.getId());
            fraudDetectionService.performFullFraudDetection(claim.getId());
        }

        log.info("材料已补传，补传单号: {}, 是否补全: {}",
                supplement.getSupplementNumber(), allSupplied);

        return supplement;
    }

    @Transactional(readOnly = true)
    public MaterialSupplement getSupplementByNumber(String supplementNumber) {
        return supplementRepository.findBySupplementNumber(supplementNumber)
                .orElseThrow(() -> new BusinessException("SUPPLEMENT_NOT_FOUND",
                        "补传记录不存在: " + supplementNumber));
    }

    @Transactional(readOnly = true)
    public List<MaterialSupplement> getSupplementsByClaim(String claimNumber) {
        ClaimReport claim = claimReportRepository.findByClaimNumber(claimNumber)
                .orElseThrow(() -> new BusinessException("CLAIM_NOT_FOUND",
                        "报案不存在: " + claimNumber));
        return supplementRepository.findLatestByClaimId(claim.getId());
    }

    @Transactional(readOnly = true)
    public List<MaterialSupplement> getOverdueSupplements() {
        return supplementRepository.findOverdueSupplements(LocalDateTime.now());
    }

    @Transactional(readOnly = true)
    public MaterialSupplement getLatestSupplement(String claimNumber) {
        ClaimReport claim = claimReportRepository.findByClaimNumber(claimNumber)
                .orElseThrow(() -> new BusinessException("CLAIM_NOT_FOUND",
                        "报案不存在: " + claimNumber));
        return supplementRepository.findFirstByClaimIdOrderByCreatedAtDesc(claim.getId()).orElse(null);
    }

    @Transactional
    public MaterialSupplement reviewSupplement(String supplementNumber,
                                            String reviewResult,
                                            String reviewRemark,
                                            String reviewerId,
                                            String reviewerName) {
        MaterialSupplement supplement = getSupplementByNumber(supplementNumber);

        supplement.setReviewerId(reviewerId);
        supplement.setReviewerName(reviewerName);
        supplement.setReviewTime(LocalDateTime.now());
        supplement.setReviewResult(reviewResult);
        supplement.setReviewRemark(reviewRemark);

        supplement = supplementRepository.save(supplement);

        if ("ACCEPTED".equals(reviewResult)) {
            ClaimReport claim = supplement.getClaim();
            materialVerificationService.checkMaterialsComplete(claim.getId());

            if (claim.getManualReviewRequired() == Boolean.TRUE) {
                claim.setStatus(ClaimStatus.MANUAL_REVIEW_REQUIRED);
            } else {
                claim.setStatus(ClaimStatus.REVIEWING);
            }
            claimReportRepository.save(claim);

            fraudDetectionService.evictFraudCache(claim.getId());
        }

        return supplement;
    }
}
