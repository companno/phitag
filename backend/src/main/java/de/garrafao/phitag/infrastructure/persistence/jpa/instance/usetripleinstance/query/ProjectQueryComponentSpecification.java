package de.garrafao.phitag.infrastructure.persistence.jpa.instance.usetripleinstance.query;

import org.springframework.data.jpa.domain.Specification;

import de.garrafao.phitag.domain.instance.usetripleinstance.UseTripleInstance;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

public class ProjectQueryComponentSpecification implements Specification<UseTripleInstance> {

    private final String project;

    public ProjectQueryComponentSpecification(String project) {
        this.project = project;
    }

    @Override
    public Predicate toPredicate(Root<UseTripleInstance> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        return criteriaBuilder.equal(root.get("id").get("phaseid").get("projectid").get("name"), project);
    }
    
}
