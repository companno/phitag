package de.garrafao.phitag.infrastructure.persistence.jpa.instance.usetripleinstance.query;

import org.springframework.data.jpa.domain.Specification;

import de.garrafao.phitag.domain.instance.usetripleinstance.UseTripleInstance;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

public class OwnerQueryComponentSpecification implements Specification<UseTripleInstance> {

    private final String owner;

    public OwnerQueryComponentSpecification(String owner) {
        this.owner = owner;
    }

    @Override
    public Predicate toPredicate(Root<UseTripleInstance> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        return criteriaBuilder.equal(root.get("id").get("phaseid").get("projectid").get("ownername"), owner);
    }
    
}
