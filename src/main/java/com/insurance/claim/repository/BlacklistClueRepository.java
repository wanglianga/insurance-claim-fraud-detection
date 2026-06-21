package com.insurance.claim.repository;

import com.insurance.claim.entity.BlacklistClue;
import com.insurance.claim.enums.FraudType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface BlacklistClueRepository extends JpaRepository<BlacklistClue, Long> {

    Optional<BlacklistClue> findByClueNumber(String clueNumber);

    @Query("SELECT b FROM BlacklistClue b WHERE " +
           "b.personIdCard = :idCard AND b.blacklisted = true " +
           "AND (b.blacklistExpiryTime IS NULL OR b.blacklistExpiryTime > :now)")
    List<BlacklistClue> findActiveBlacklistByPersonIdCard(
            @Param("idCard") String idCard, @Param("now") LocalDateTime now);

    @Query("SELECT b FROM BlacklistClue b WHERE " +
           "b.organizationLicense = :license AND b.blacklisted = true " +
           "AND (b.blacklistExpiryTime IS NULL OR b.blacklistExpiryTime > :now)")
    List<BlacklistClue> findActiveBlacklistByOrganizationLicense(
            @Param("license") String license, @Param("now") LocalDateTime now);

    @Query("SELECT b FROM BlacklistClue b WHERE " +
           "b.vehicleLicensePlate = :plate AND b.blacklisted = true " +
           "AND (b.blacklistExpiryTime IS NULL OR b.blacklistExpiryTime > :now)")
    List<BlacklistClue> findActiveBlacklistByPlate(
            @Param("plate") String plate, @Param("now") LocalDateTime now);

    @Query("SELECT b FROM BlacklistClue b WHERE " +
           "b.vinNumber = :vin AND b.blacklisted = true " +
           "AND (b.blacklistExpiryTime IS NULL OR b.blacklistExpiryTime > :now)")
    List<BlacklistClue> findActiveBlacklistByVin(
            @Param("vin") String vin, @Param("now") LocalDateTime now);

    @Query("SELECT COUNT(b) > 0 FROM BlacklistClue b WHERE " +
           "b.personIdCard = :idCard AND b.blacklisted = true " +
           "AND (b.blacklistExpiryTime IS NULL OR b.blacklistExpiryTime > :now)")
    boolean isPersonBlacklisted(@Param("idCard") String idCard, @Param("now") LocalDateTime now);

    @Query("SELECT COUNT(b) > 0 FROM BlacklistClue b WHERE " +
           "b.organizationLicense = :license AND b.blacklisted = true " +
           "AND (b.blacklistExpiryTime IS NULL OR b.blacklistExpiryTime > :now)")
    boolean isOrganizationBlacklisted(@Param("license") String license, @Param("now") LocalDateTime now);

    List<BlacklistClue> findByFraudTypeAndBlacklistedTrue(FraudType fraudType);

    List<BlacklistClue> findByRelatedClaimNumber(String relatedClaimNumber);

    @Query("SELECT b FROM BlacklistClue b WHERE b.blacklisted = true ORDER BY b.createdAt DESC")
    List<BlacklistClue> findAllActiveBlacklist();
}
