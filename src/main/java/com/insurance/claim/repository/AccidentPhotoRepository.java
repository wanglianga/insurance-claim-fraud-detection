package com.insurance.claim.repository;

import com.insurance.claim.entity.AccidentPhoto;
import com.insurance.claim.enums.MaterialType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AccidentPhotoRepository extends JpaRepository<AccidentPhoto, Long> {

    List<AccidentPhoto> findByClaimId(Long claimId);

    List<AccidentPhoto> findByClaimIdAndMaterialType(Long claimId, MaterialType materialType);

    Optional<AccidentPhoto> findByPhotoHash(String photoHash);

    @Query("SELECT p FROM AccidentPhoto p WHERE p.photoHash = :hash AND p.id <> :excludeId")
    List<AccidentPhoto> findByPhotoHashExcludingId(@Param("hash") String hash, @Param("excludeId") Long excludeId);

    @Query("SELECT p FROM AccidentPhoto p WHERE p.tamperDetected = true AND p.claim.id = :claimId")
    List<AccidentPhoto> findTamperedPhotosByClaimId(@Param("claimId") Long claimId);

    @Query("SELECT COUNT(p) > 0 FROM AccidentPhoto p WHERE p.photoHash = :hash")
    boolean existsByPhotoHash(@Param("hash") String hash);

    List<AccidentPhoto> findByClaimIdAndVerified(Long claimId, Boolean verified);
}
