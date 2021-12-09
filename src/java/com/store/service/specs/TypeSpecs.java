package com.store.service.specs;

import com.store.entity.Type;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;

@Component
public class TypeSpecs {

    public Specification<Type> findByPredicates(Type type) {
        return (root, query, cb) -> query
                .where(cb.and(getPredicates(type, cb, root).toArray(new Predicate[] {})))
                .getRestriction();
    }

    public ArrayList<Predicate> getPredicates(Type type, CriteriaBuilder cb, Root<Type> root) {
        ArrayList<Predicate> predicates = new ArrayList<>(4);

        if (type.getId() != null && !type.getId().toString().equals(""))
            predicates.add(cb.equal(root.get("id"), type.getId()));

        if (!type.getLabel().equals(""))
            predicates.add(cb.like(root.get("label"), "%" + type.getLabel() + "%"));

        if (!type.getDescription().equals(""))
            predicates.add(cb.like(root.get("description"), "%" + type.getDescription() + "%"));

        if (!type.getAuthor().equals(""))
            predicates.add(cb.like(root.get("author"), "%" + type.getAuthor() + "%"));

        return predicates;
    }

}
