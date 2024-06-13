package de.garrafao.phitag.infrastructure.persistence.jpa.phase.query;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

import de.garrafao.phitag.domain.phase.Phase;

public class IsTutorialQueryComponentSpecification implements Specification<Phase> {

    private final boolean isTutorial;

    public IsTutorialQueryComponentSpecification(boolean isTutorial) {
        this.isTutorial = isTutorial;
    }

    @Override
    public Predicate toPredicate(Root<Phase> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        return criteriaBuilder.equal(root.get("isTutorial"), isTutorial);
    }

}
