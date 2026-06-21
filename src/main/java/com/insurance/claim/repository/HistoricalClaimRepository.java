package com.insurance.claim.repository;

import com.insurance.claim.entity.HistoricalClaim;
import com.insurance.claim.enums.AccidentType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface HistoricalClaimRepository extends JpaRepository<HistoricalClaim, Long> {

    Optional<HistoricalClaim> findByClaimNumber(String claimNumber);

    List<HistoricalClaim> findByInsuredIdCard(String insuredIdCard);

    List<HistoricalClaim> findByPolicyHolderIdCard(String policyHolderIdCard);

    List<HistoricalClaim> findByPolicyNumber(String policyNumber);

    @Query("SELECT h FROM HistoricalClaim h WHERE " +
           "(h.policyHolderIdCard = :idCard OR h.insuredIdCard = :idCard) " +
           "AND h.accidentTime BETWEEN :startTime AND :endTime")
    List<HistoricalClaim> findByIdCardAndDateRange(
            @Param("idCard") String idCard,
            @Param("startTime") LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime);

    @Query("SELECT h FROM HistoricalClaim h WHERE " +
           "h.accidentType = :accidentType AND " +
           "ABS(h.accidentLatitude - :latitude) < 0.01 AND " +
           "ABS(h.accidentLongitude - :longitude) < 0.01 AND " +
           "h.accidentTime BETWEEN :startTime AND :endTime")
    List<HistoricalClaim> findSimilarAccidents(
            @Param("accidentType") AccidentType accidentType,
            @Param("latitude") Double latitude,
            @Param("longitude") Double longitude,
            @Param("startTime") LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime);

    @Query("SELECT h FROM HistoricalClaim h WHERE h.fraudConfirmed = true " +
           "AND (h.policyHolderIdCard = :idCard OR h.insuredIdCard = :idCard)")
    List<HistoricalClaim> findFraudClaimsByIdCard(@Param("idCard") String idCard);

    @Query("SELECT COUNT(h) FROM HistoricalClaim h WHERE " +
           "(h.policyHolderIdCard = :idCard OR h.insuredIdCard = :idCard) " +
           "AND h.fraudConfirmed = true")
    long countFraudClaimsByIdCard(@Param("idCard") String idCard);

    @Query("SELECT COUNT(h) FROM HistoricalClaim h WHERE " +
           "(h.policyHolderIdCard = :idCard OR h.insuredIdCard = :idCard)")
    long countTotalClaimsByIdCard(@Param("idCard") String idCard);

    @Query("SELECT AVG(h.actualCompensation) FROM HistoricalClaim h WHERE " +
           "h.accidentType = :accidentType AND h.claimStatus = 'COMPENSATED'")
    java.math.BigDecimal findAverageCompensationByAccidentType(@Param("accidentType") AccidentType accidentType);

    boolean existsByClaimNumber(String claimNumber);
}
