package com.store.repository;

import com.store.entity.Service;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ServiceRepo extends JpaRepository<Service, Long>, JpaSpecificationExecutor<Service> {
}
