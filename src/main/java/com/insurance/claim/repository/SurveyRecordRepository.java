package com.insurance.claim.repository;

import com.insurance.claim.entity.SurveyRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface SurveyRecordRepository extends JpaRepository<SurveyRecord, Long> {

    Optional<SurveyRecord> findBySurveyNumber(String surveyNumber);

    List<SurveyRecord> findByClaimId(Long claimId);

    List<SurveyRecord> findBySurveyorId(String surveyorId);

    @Query("SELECT s FROM SurveyRecord s WHERE s.claim.id = :claimId ORDER BY s.surveyTime DESC")
    List<SurveyRecord> findLatestByClaimId(@Param("claimId") Long claimId);

    @Query("SELECT s FROM SurveyRecord s WHERE " +
           "ABS(s.siteLatitude - :latitude) < 0.005 AND " +
           "ABS(s.siteLongitude - :longitude) < 0.005 AND " +
           "s.surveyTime BETWEEN :startTime AND :endTime AND s.id <> :excludeId")
    List<SurveyRecord> findNearbySurveys(
            @Param("latitude") Double latitude,
            @Param("longitude") Double longitude,
            @Param("startTime") LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime,
            @Param("excludeId") Long excludeId);

    @Query("SELECT s FROM SurveyRecord s WHERE s.fraudSuspicion = true ORDER BY s.createdAt DESC")
    List<SurveyRecord> findFraudSuspectedSurveys();

    Optional<SurveyRecord> findFirstByClaimIdOrderByCreatedAtDesc(Long claimId);

    boolean existsBySurveyNumber(String surveyNumber);
}
