package com.store.repository;

import com.store.entity.StatusCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface StatusRepo extends JpaRepository<StatusCode, Long>, JpaSpecificationExecutor<StatusCode> {
}
