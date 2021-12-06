package com.store.service.specs;

import com.store.entity.Service;
import com.store.entity.Store;
import com.store.entity.Type;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;

@Component
public class StoreSpecs {

    public Specification<Store> findByPredicates(Store store) {
        return (root, query, cb) -> query
                .where(cb.and(getPredicates(store, cb, root).toArray(new Predicate[] {})))
                .getRestriction();
    }

    public ArrayList<Predicate> getPredicates(Store store, CriteriaBuilder cb, Root<Store> root) {
        ArrayList<Predicate> predicates = new ArrayList<>(7);

        if (store.getId() != null && !store.getId().toString().equals(""))
            predicates.add(cb.equal(root.get("id"), store.getId()));

        if (store.getServiceId() != null && !store.getServiceId().toString().equals(""))
            predicates.add(cb.equal(root.get("serviceId"), store.getServiceId()));

        if (store.getTypeId() != null && !store.getTypeId().toString().equals(""))
            predicates.add(cb.equal(root.get("typeId"), store.getTypeId()));

        if (store.getStatusId() != null && !store.getStatusId().toString().equals(""))
            predicates.add(cb.equal(root.get("statusId"), store.getStatusId()));

        if (!store.getDocCode().equals(""))
            predicates.add(cb.like(root.get("docCode"), "%" + store.getDocCode() + "%"));

        if (!store.getNamespace().equals(""))
            predicates.add(cb.like(root.get("namespace"), "%" + store.getNamespace() + "%"));

        if (!store.getUrl().equals(""))
            predicates.add(cb.like(root.get("url"), "%" + store.getUrl() + "%"));

        return predicates;
    }

}
