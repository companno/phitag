package de.garrafao.phitag.infrastructure.persistance.jpa.samplingorder.query;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

import de.garrafao.phitag.domain.annotationprocessinformation.AnnotationProcessInformation;

public class AnnotatorQueryComponentSpecification implements Specification<AnnotationProcessInformation> {

    private final String annotator;

    public AnnotatorQueryComponentSpecification(String annotator) {
        this.annotator = annotator;
    }

    @Override
    public Predicate toPredicate(Root<AnnotationProcessInformation> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        return criteriaBuilder.equal(root.get("id").get("annotatorid").get("username"), annotator);
    }
    
}
