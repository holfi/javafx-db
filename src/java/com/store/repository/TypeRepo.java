package com.store.repository;

import com.store.entity.Type;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface TypeRepo extends JpaRepository<Type, Long>, JpaSpecificationExecutor<Type> {
}
