package de.garrafao.phitag.infrastructure.persistance.jpa.judgement.wssimjudgement.query;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.lang.Nullable;

import de.garrafao.phitag.domain.judgement.wssimjudgement.WSSIMJudgement;

public class OwnerQueryComponentSpecification implements Specification<WSSIMJudgement> {

    private final String owner;

    public OwnerQueryComponentSpecification(final String owner) {
        this.owner = owner;
    }

    @Override
    @Nullable
    public Predicate toPredicate(Root<WSSIMJudgement> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        return criteriaBuilder.equal(root.get("id").get("instanceid").get("phaseid").get("projectid").get("ownername"), owner);
    }
    
}