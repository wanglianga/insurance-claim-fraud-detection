package com.insurance.claim.repository;

import com.insurance.claim.entity.InsurancePolicy;
import com.insurance.claim.enums.PolicyStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface InsurancePolicyRepository extends JpaRepository<InsurancePolicy, Long> {

    Optional<InsurancePolicy> findByPolicyNumber(String policyNumber);

    List<InsurancePolicy> findByInsuredIdCard(String insuredIdCard);

    List<InsurancePolicy> findByPolicyHolderIdCard(String policyHolderIdCard);

    List<InsurancePolicy> findByStatusAndEffectiveDateBeforeAndExpiryDateAfter(
            PolicyStatus status, LocalDate effectiveDate, LocalDate expiryDate);

    @Query("SELECT p FROM InsurancePolicy p WHERE p.insuredIdCard = :idCard " +
           "AND p.status = 'ACTIVE' AND :date BETWEEN p.effectiveDate AND p.expiryDate")
    List<InsurancePolicy> findActivePoliciesByIdCardAndDate(
            @Param("idCard") String idCard, @Param("date") LocalDate date);

    @Query("SELECT p FROM InsurancePolicy p WHERE p.insuredItemIdentifier = :identifier " +
           "AND p.status = 'ACTIVE' AND :date BETWEEN p.effectiveDate AND p.expiryDate")
    List<InsurancePolicy> findActivePoliciesByItemIdentifierAndDate(
            @Param("identifier") String identifier, @Param("date") LocalDate date);

    boolean existsByPolicyNumber(String policyNumber);
}
