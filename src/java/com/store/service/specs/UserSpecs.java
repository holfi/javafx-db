package com.store.service.specs;

import com.store.entity.User;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;

@Component
public class UserSpecs {

    public Specification<User> findByPredicates(User user) {
        return (root, query, cb) -> query
                .where(cb.and(getPredicates(user, cb, root).toArray(new Predicate[] {})))
                .getRestriction();
    }

    public ArrayList<Predicate> getPredicates(User user, CriteriaBuilder cb, Root<User> root) {
        ArrayList<Predicate> predicates = new ArrayList<>(3);

        if (user.getId() != null && !user.getId().toString().equals(""))
            predicates.add(cb.equal(root.get("id"), user.getId()));

        if (!user.getLogin().equals(""))
            predicates.add(cb.like(root.get("login"), "%" + user.getLogin() + "%"));

        if (!user.getName().equals(""))
            predicates.add(cb.like(root.get("name"), "%" + user.getName() + "%"));

        return predicates;
    }

}
