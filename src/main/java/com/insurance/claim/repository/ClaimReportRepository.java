package com.insurance.claim.repository;

import com.insurance.claim.entity.ClaimReport;
import com.insurance.claim.enums.ClaimStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ClaimReportRepository extends JpaRepository<ClaimReport, Long> {

    Optional<ClaimReport> findByClaimNumber(String claimNumber);

    List<ClaimReport> findByPolicyId(Long policyId);

    List<ClaimReport> findByStatus(ClaimStatus status);

    @Query("SELECT c FROM ClaimReport c WHERE c.policy.id = :policyId " +
           "AND c.accidentTime BETWEEN :startTime AND :endTime")
    List<ClaimReport> findByPolicyIdAndAccidentTimeBetween(
            @Param("policyId") Long policyId,
            @Param("startTime") LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime);

    @Query("SELECT c FROM ClaimReport c WHERE " +
           "ABS(c.accidentLatitude - :latitude) < 0.01 AND " +
           "ABS(c.accidentLongitude - :longitude) < 0.01 AND " +
           "c.accidentTime BETWEEN :startTime AND :endTime AND c.id <> :excludeId")
    List<ClaimReport> findNearbyAccidents(
            @Param("latitude") Double latitude,
            @Param("longitude") Double longitude,
            @Param("startTime") LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime,
            @Param("excludeId") Long excludeId);

    @Query("SELECT c FROM ClaimReport c WHERE " +
           "c.policy.insuredIdCard = :insuredIdCard AND " +
           "c.accidentTime BETWEEN :startTime AND :endTime AND c.id <> :excludeId")
    List<ClaimReport> findByInsuredIdCardAndAccidentTimeBetween(
            @Param("insuredIdCard") String insuredIdCard,
            @Param("startTime") LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime,
            @Param("excludeId") Long excludeId);

    @Query("SELECT c FROM ClaimReport c WHERE " +
           "c.relatedAccidentId = :accidentId OR c.id = :accidentId")
    List<ClaimReport> findRelatedAccidents(@Param("accidentId") String accidentId);

    List<ClaimReport> findByRelatedAccidentId(String relatedAccidentId);

    @Query("SELECT c FROM ClaimReport c WHERE " +
           "c.accidentTime BETWEEN :startTime AND :endTime " +
           "AND c.status <> 'REJECTED' AND c.status <> 'CLOSED'")
    List<ClaimReport> findRecentActiveClaims(
            @Param("startTime") LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime);

    List<ClaimReport> findBySurveyorId(String surveyorId);

    List<ClaimReport> findByReviewerId(String reviewerId);

    Optional<ClaimReport> findByPoliceReportNumber(String policeReportNumber);

    boolean existsByClaimNumber(String claimNumber);
}
