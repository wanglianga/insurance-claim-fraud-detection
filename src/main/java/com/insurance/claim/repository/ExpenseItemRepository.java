package com.insurance.claim.repository;

import com.insurance.claim.entity.ExpenseItem;
import com.insurance.claim.enums.MaterialType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExpenseItemRepository extends JpaRepository<ExpenseItem, Long> {

    @Query("SELECT e FROM ExpenseItem e WHERE e.sourceType = :sourceType AND e.sourceId = :sourceId")
    List<ExpenseItem> findBySource(@Param("sourceType") String sourceType, @Param("sourceId") Long sourceId);

    List<ExpenseItem> findByItemCode(String itemCode);

    @Query("SELECT e FROM ExpenseItem e WHERE e.abnormalDetected = true")
    List<ExpenseItem> findAbnormalItems();

    @Query("SELECT e FROM ExpenseItem e WHERE e.sourceType = :sourceType AND e.sourceId = :sourceId " +
           "AND e.materialType = :materialType")
    List<ExpenseItem> findBySourceAndMaterialType(
            @Param("sourceType") String sourceType,
            @Param("sourceId") Long sourceId,
            @Param("materialType") MaterialType materialType);

    @Query("SELECT AVG(e.unitPrice) FROM ExpenseItem e WHERE e.itemCode = :itemCode AND e.verified = true")
    java.math.BigDecimal findAveragePriceByItemCode(@Param("itemCode") String itemCode);

    @Query("SELECT COUNT(e) FROM ExpenseItem e WHERE e.itemCode = :itemCode AND e.verified = true")
    long countVerifiedItemsByItemCode(@Param("itemCode") String itemCode);
}
