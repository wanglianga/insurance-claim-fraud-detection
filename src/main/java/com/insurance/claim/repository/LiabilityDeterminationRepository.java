package com.insurance.claim.repository;

import com.insurance.claim.entity.LiabilityDetermination;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LiabilityDeterminationRepository extends JpaRepository<LiabilityDetermination, Long> {

    List<LiabilityDetermination> findByClaimId(Long claimId);

    Optional<LiabilityDetermination> findByDeterminationNumber(String determinationNumber);

    Optional<LiabilityDetermination> findByDeterminationHash(String determinationHash);

    @Query("SELECT l FROM LiabilityDetermination l WHERE l.determinationHash = :hash AND l.id <> :excludeId")
    List<LiabilityDetermination> findByHashExcludingId(@Param("hash") String hash, @Param("excludeId") Long excludeId);

    @Query("SELECT l FROM LiabilityDetermination l WHERE " +
           "(l.partyAIdCard = :idCard OR l.partyBIdCard = :idCard) " +
           "AND l.determinationDate BETWEEN :startDate AND :endDate AND l.id <> :excludeId")
    List<LiabilityDetermination> findByPartyAndDateRange(
            @Param("idCard") String idCard,
            @Param("startDate") java.time.LocalDateTime startDate,
            @Param("endDate") java.time.LocalDateTime endDate,
            @Param("excludeId") Long excludeId);

    @Query("SELECT COUNT(l) > 0 FROM LiabilityDetermination l WHERE l.determinationHash = :hash")
    boolean existsByDeterminationHash(@Param("hash") String hash);

    @Query("SELECT l FROM LiabilityDetermination l WHERE l.conflictWithSurvey = true")
    List<LiabilityDetermination> findConflictWithSurvey();
}
