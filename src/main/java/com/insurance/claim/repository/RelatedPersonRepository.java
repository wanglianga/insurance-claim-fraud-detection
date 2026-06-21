package com.insurance.claim.repository;

import com.insurance.claim.entity.RelatedPerson;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RelatedPersonRepository extends JpaRepository<RelatedPerson, Long> {

    @Query("SELECT r FROM RelatedPerson r WHERE r.personIdCard = :idCard")
    List<RelatedPerson> findByPersonIdCard(@Param("idCard") String idCard);

    @Query("SELECT r FROM RelatedPerson r WHERE r.relatedPersonIdCard = :idCard")
    List<RelatedPerson> findByRelatedPersonIdCard(@Param("idCard") String idCard);

    @Query("SELECT r FROM RelatedPerson r WHERE " +
           "(r.personIdCard = :idCard OR r.relatedPersonIdCard = :idCard) " +
           "AND r.relationDegree <= :maxDepth")
    List<RelatedPerson> findRelationsUpToDepth(
            @Param("idCard") String idCard, @Param("maxDepth") Integer maxDepth);

    @Query("SELECT r FROM RelatedPerson r WHERE " +
           "r.personIdCard = :idCard1 AND r.relatedPersonIdCard = :idCard2")
    List<RelatedPerson> findDirectRelation(
            @Param("idCard1") String idCard1, @Param("idCard2") String idCard2);

    @Query("SELECT COUNT(r) > 0 FROM RelatedPerson r WHERE " +
           "r.personIdCard = :idCard1 AND r.relatedPersonIdCard = :idCard2 " +
           "AND r.relationDegree <= :maxDepth")
    boolean areRelatedWithinDepth(
            @Param("idCard1") String idCard1,
            @Param("idCard2") String idCard2,
            @Param("maxDepth") Integer maxDepth);
}
