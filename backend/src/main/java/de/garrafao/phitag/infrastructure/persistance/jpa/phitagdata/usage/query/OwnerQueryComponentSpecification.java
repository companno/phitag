package de.garrafao.phitag.infrastructure.persistance.jpa.phitagdata.usage.query;

import org.springframework.data.jpa.domain.Specification;

import de.garrafao.phitag.domain.phitagdata.usage.Usage;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

public class OwnerQueryComponentSpecification implements Specification<Usage> {

    private final String owner;

    public OwnerQueryComponentSpecification(final String owner) {
        this.owner = owner;
    }

    @Override
    public Predicate toPredicate(Root<Usage> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        return criteriaBuilder.equal(root.get("id").get("projectid").get("ownername"), owner);
    }
    
    
}
