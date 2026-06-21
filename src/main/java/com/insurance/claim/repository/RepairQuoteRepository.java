package com.insurance.claim.repository;

import com.insurance.claim.entity.RepairQuote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RepairQuoteRepository extends JpaRepository<RepairQuote, Long> {

    List<RepairQuote> findByClaimId(Long claimId);

    Optional<RepairQuote> findByQuoteNumber(String quoteNumber);

    Optional<RepairQuote> findByQuoteHash(String quoteHash);

    @Query("SELECT r FROM RepairQuote r WHERE r.quoteHash = :hash AND r.id <> :excludeId")
    List<RepairQuote> findByQuoteHashExcludingId(@Param("hash") String hash, @Param("excludeId") Long excludeId);

    @Query("SELECT r FROM RepairQuote r WHERE r.vinNumber = :vinNumber " +
           "AND r.quoteDate BETWEEN :startDate AND :endDate AND r.id <> :excludeId")
    List<RepairQuote> findByVinAndDateRange(
            @Param("vinNumber") String vinNumber,
            @Param("startDate") java.time.LocalDateTime startDate,
            @Param("endDate") java.time.LocalDateTime endDate,
            @Param("excludeId") Long excludeId);

    @Query("SELECT r FROM RepairQuote r WHERE r.vehicleLicensePlate = :plate " +
           "AND r.quoteDate BETWEEN :startDate AND :endDate AND r.id <> :excludeId")
    List<RepairQuote> findByPlateAndDateRange(
            @Param("plate") String plate,
            @Param("startDate") java.time.LocalDateTime startDate,
            @Param("endDate") java.time.LocalDateTime endDate,
            @Param("excludeId") Long excludeId);

    @Query("SELECT r FROM RepairQuote r WHERE r.amountAbnormal = true OR r.unusualPartsDetected = true")
    List<RepairQuote> findSuspiciousQuotes();

    @Query("SELECT COUNT(r) > 0 FROM RepairQuote r WHERE r.quoteHash = :hash")
    boolean existsByQuoteHash(@Param("hash") String hash);
}
