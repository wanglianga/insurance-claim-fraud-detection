package com.insurance.claim.repository;

import com.insurance.claim.entity.CompensationConclusion;
import com.insurance.claim.enums.ReviewResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CompensationConclusionRepository extends JpaRepository<CompensationConclusion, Long> {

    Optional<CompensationConclusion> findByConclusionNumber(String conclusionNumber);

    Optional<CompensationConclusion> findByClaimId(Long claimId);

    List<CompensationConclusion> findByReviewResult(ReviewResult reviewResult);

    List<CompensationConclusion> findByFraudConfirmedTrue();

    @Query("SELECT c FROM CompensationConclusion c WHERE " +
           "c.manualReviewPerformed = true AND c.manualReviewerId = :reviewerId")
    List<CompensationConclusion> findManualReviewedByReviewerId(@Param("reviewerId") String reviewerId);

    @Query("SELECT c FROM CompensationConclusion c WHERE c.claim.id = :claimId ORDER BY c.createdAt DESC")
    List<CompensationConclusion> findLatestByClaimId(@Param("claimId") Long claimId);

    Optional<CompensationConclusion> findFirstByClaimIdOrderByCreatedAtDesc(Long claimId);

    @Query("SELECT COUNT(c) FROM CompensationConclusion c WHERE " +
           "c.reviewResult = com.insurance.claim.enums.ReviewResult.APPROVED " +
           "AND c.finalReviewTime BETWEEN :startTime AND :endTime")
    long countApprovedInPeriod(
            @Param("startTime") java.time.LocalDateTime startTime,
            @Param("endTime") java.time.LocalDateTime endTime);

    @Query("SELECT COUNT(c) FROM CompensationConclusion c WHERE " +
           "c.fraudConfirmed = true AND c.finalReviewTime BETWEEN :startTime AND :endTime")
    long countFraudConfirmedInPeriod(
            @Param("startTime") java.time.LocalDateTime startTime,
            @Param("endTime") java.time.LocalDateTime endTime);

    boolean existsByConclusionNumber(String conclusionNumber);
}
