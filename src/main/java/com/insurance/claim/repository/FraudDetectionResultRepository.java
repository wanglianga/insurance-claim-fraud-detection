package com.insurance.claim.repository;

import com.insurance.claim.entity.FraudDetectionResult;
import com.insurance.claim.enums.FraudType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FraudDetectionResultRepository extends JpaRepository<FraudDetectionResult, Long> {

    List<FraudDetectionResult> findByClaimId(Long claimId);

    List<FraudDetectionResult> findByClaimIdOrderByRiskScoreDesc(Long claimId);

    List<FraudDetectionResult> findByFraudType(FraudType fraudType);

    @Query("SELECT f FROM FraudDetectionResult f WHERE " +
           "f.claim.id = :claimId AND f.fraudType = :fraudType")
    List<FraudDetectionResult> findByClaimIdAndFraudType(
            @Param("claimId") Long claimId, @Param("fraudType") FraudType fraudType);

    @Query("SELECT f FROM FraudDetectionResult f WHERE f.verified <> true ORDER BY f.riskScore DESC")
    List<FraudDetectionResult> findUnverifiedResults();

    @Query("SELECT f FROM FraudDetectionResult f WHERE f.riskScore >= :threshold ORDER BY f.riskScore DESC")
    List<FraudDetectionResult> findHighRiskResults(@Param("threshold") Integer threshold);

    @Query("SELECT SUM(f.riskScore) FROM FraudDetectionResult f WHERE f.claim.id = :claimId")
    Integer sumRiskScoreByClaimId(@Param("claimId") Long claimId);

    void deleteByClaimId(Long claimId);
}
