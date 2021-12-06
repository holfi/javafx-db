package com.store.repository;

import com.store.entity.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface StoreRepo extends JpaRepository<Store, Long>, JpaSpecificationExecutor<Store> {
}
