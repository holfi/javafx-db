package com.store.service.specs;

import com.store.entity.Service;
import com.store.entity.Type;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;

@Component
public class ServiceSpecs {

    public Specification<Type> findByPredicates(Service service) {
        return (root, query, cb) -> query
                .where(cb.and(getPredicates(service, cb, root).toArray(new Predicate[] {})))
                .getRestriction();
    }

    public ArrayList<Predicate> getPredicates(Service service, CriteriaBuilder cb, Root<Type> root) {
        ArrayList<Predicate> predicates = new ArrayList<>(2);

        if (service.getId() != null && !service.getId().toString().equals(""))
            predicates.add(cb.equal(root.get("id"), service.getId()));

        if (!service.getServiceCode().equals(""))
            predicates.add(cb.like(root.get("serviceCode"), "%" + service.getServiceCode() + "%"));

        return predicates;
    }

}
