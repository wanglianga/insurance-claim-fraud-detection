package com.insurance.claim.repository;

import com.insurance.claim.entity.HospitalInvoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface HospitalInvoiceRepository extends JpaRepository<HospitalInvoice, Long> {

    List<HospitalInvoice> findByClaimId(Long claimId);

    Optional<HospitalInvoice> findByInvoiceNumber(String invoiceNumber);

    Optional<HospitalInvoice> findByInvoiceHash(String invoiceHash);

    @Query("SELECT h FROM HospitalInvoice h WHERE h.invoiceHash = :hash AND h.id <> :excludeId")
    List<HospitalInvoice> findByInvoiceHashExcludingId(@Param("hash") String hash, @Param("excludeId") Long excludeId);

    @Query("SELECT h FROM HospitalInvoice h WHERE h.invoiceNumber = :invoiceNumber AND h.id <> :excludeId")
    List<HospitalInvoice> findByInvoiceNumberExcludingId(@Param("invoiceNumber") String invoiceNumber, @Param("excludeId") Long excludeId);

    @Query("SELECT h FROM HospitalInvoice h WHERE h.patientIdCard = :patientIdCard " +
           "AND h.admissionDate BETWEEN :startDate AND :endDate AND h.id <> :excludeId")
    List<HospitalInvoice> findByPatientAndDateRange(
            @Param("patientIdCard") String patientIdCard,
            @Param("startDate") java.time.LocalDate startDate,
            @Param("endDate") java.time.LocalDate endDate,
            @Param("excludeId") Long excludeId);

    @Query("SELECT h FROM HospitalInvoice h WHERE h.duplicateFound = true OR h.amountAbnormal = true")
    List<HospitalInvoice> findSuspiciousInvoices();

    @Query("SELECT COUNT(h) > 0 FROM HospitalInvoice h WHERE h.invoiceHash = :hash")
    boolean existsByInvoiceHash(@Param("hash") String hash);

    @Query("SELECT COUNT(h) > 0 FROM HospitalInvoice h WHERE h.invoiceNumber = :invoiceNumber")
    boolean existsByInvoiceNumber(@Param("invoiceNumber") String invoiceNumber);
}
