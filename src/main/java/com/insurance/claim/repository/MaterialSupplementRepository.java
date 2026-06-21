package com.insurance.claim.repository;

import com.insurance.claim.entity.MaterialSupplement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface MaterialSupplementRepository extends JpaRepository<MaterialSupplement, Long> {

    Optional<MaterialSupplement> findBySupplementNumber(String supplementNumber);

    List<MaterialSupplement> findByClaimId(Long claimId);

    @Query("SELECT m FROM MaterialSupplement m WHERE m.claim.id = :claimId ORDER BY m.requestTime DESC")
    List<MaterialSupplement> findLatestByClaimId(@Param("claimId") Long claimId);

    @Query("SELECT COUNT(m) FROM MaterialSupplement m WHERE m.claim.id = :claimId")
    int countByClaimId(@Param("claimId") Long claimId);

    @Query("SELECT m FROM MaterialSupplement m WHERE m.deadline < :now AND m.allSupplied <> true")
    List<MaterialSupplement> findOverdueSupplements(@Param("now") LocalDateTime now);

    @Query("SELECT m FROM MaterialSupplement m WHERE m.requesterId = :requesterId")
    List<MaterialSupplement> findByRequesterId(@Param("requesterId") String requesterId);

    Optional<MaterialSupplement> findFirstByClaimIdOrderByCreatedAtDesc(Long claimId);

    boolean existsBySupplementNumber(String supplementNumber);
}
