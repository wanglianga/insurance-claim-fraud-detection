package com.insurance.claim.repository;

import com.insurance.claim.entity.ThirdPartyCallback;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ThirdPartyCallbackRepository extends JpaRepository<ThirdPartyCallback, Long> {

    Optional<ThirdPartyCallback> findByCallbackId(String callbackId);

    List<ThirdPartyCallback> findByRelatedClaimNumber(String relatedClaimNumber);

    List<ThirdPartyCallback> findBySourceSystem(String sourceSystem);

    @Query("SELECT t FROM ThirdPartyCallback t WHERE t.processed <> true ORDER BY t.callbackTime ASC")
    List<ThirdPartyCallback> findPendingCallbacks();

    @Query("SELECT t FROM ThirdPartyCallback t WHERE t.processed <> true AND t.callbackTime < :timeout")
    List<ThirdPartyCallback> findTimeoutCallbacks(@Param("timeout") LocalDateTime timeout);

    @Query("SELECT t FROM ThirdPartyCallback t WHERE t.relatedClaimNumber = :claimNumber AND t.callbackType = :type")
    List<ThirdPartyCallback> findByClaimAndType(
            @Param("claimNumber") String claimNumber, @Param("type") String type);

    @Query("SELECT COUNT(t) FROM ThirdPartyCallback t WHERE t.processed <> true AND t.retryCount < :maxRetries")
    long countPendingWithRetryLimit(@Param("maxRetries") Integer maxRetries);

    boolean existsByCallbackId(String callbackId);
}
