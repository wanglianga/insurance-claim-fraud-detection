package com.insurance.claim.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Entity
@Table(name = "related_persons")
@EqualsAndHashCode(callSuper = true)
public class RelatedPerson extends BaseEntity {

    @Column(name = "person_id_card", nullable = false, length = 30)
    private String personIdCard;

    @Column(name = "person_name", length = 100)
    private String personName;

    @Column(name = "person_phone", length = 20)
    private String personPhone;

    @Column(name = "related_person_id_card", nullable = false, length = 30)
    private String relatedPersonIdCard;

    @Column(name = "related_person_name", length = 100)
    private String relatedPersonName;

    @Column(name = "relation_type", length = 50)
    private String relationType;

    @Column(name = "relation_degree")
    private Integer relationDegree;

    @Column(name = "relation_description", columnDefinition = "TEXT")
    private String relationDescription;

    @Column(name = "data_source", length = 100)
    private String dataSource;

    @Column(name = "verified")
    private Boolean verified;

    @Column(name = "remark", columnDefinition = "TEXT")
    private String remark;
}
