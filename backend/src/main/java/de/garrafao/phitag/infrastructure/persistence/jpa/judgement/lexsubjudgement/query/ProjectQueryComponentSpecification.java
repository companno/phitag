package de.garrafao.phitag.infrastructure.persistence.jpa.judgement.lexsubjudgement.query;

import org.springframework.data.jpa.domain.Specification;

import de.garrafao.phitag.domain.judgement.lexsubjudgement.LexSubJudgement;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

public class ProjectQueryComponentSpecification implements Specification<LexSubJudgement> {

    private final String project;

    public ProjectQueryComponentSpecification(final String project) {
        this.project = project;
    }

    @Override
    public Predicate toPredicate(Root<LexSubJudgement> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        return criteriaBuilder.equal(root.get("id").get("instanceid").get("phaseid").get("projectid").get("name"),
                project);
    }

}
