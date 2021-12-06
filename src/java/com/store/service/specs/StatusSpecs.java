package com.store.service.specs;

import com.store.entity.StatusCode;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;

@Component
public class StatusSpecs {

    public Specification<StatusCode> findByPredicates(StatusCode statusCode) {
        return (root, query, cb) -> query
                .where(cb.and(getPredicates(statusCode, cb, root).toArray(new Predicate[] {})))
                .getRestriction();
    }

    public ArrayList<Predicate> getPredicates(StatusCode status, CriteriaBuilder cb, Root<StatusCode> root) {
        ArrayList<Predicate> predicates = new ArrayList<>(3);

        if (status.getId() != null && !status.getId().toString().equals(""))
            predicates.add(cb.equal(root.get("id"), status.getId()));

        if (status.getCode() != null)
            predicates.add(cb.equal(root.get("code"), status.getCode()));

        if (!status.getName().equals(""))
            predicates.add(cb.like(root.get("name"), "%" + status.getName() + "%"));

        return predicates;
    }

}
