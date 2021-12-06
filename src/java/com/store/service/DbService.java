package com.store.service;

import com.store.entity.Type;
import com.store.repository.*;
import com.store.service.specs.TypeSpecs;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DbService {

    private final TypeRepo typeRepo;
    private final ServiceRepo serviceRepo;
    private final StoreRepo storeRepo;
    private final UserRepo userRepo;
    private final StatusRepo statusRepo;

    private final TypeSpecs typeSpecs;


    public List<Type> find(Type type) {
        return typeRepo.findAll(typeSpecs.findByPredicates(type));
    }

    public List<Type> findAll() {
        return typeRepo.findAll();
    }

    public void saveOrUpdate(Type type) {
        typeRepo.save(type);
    }

    public void delete(Long id) {
        typeRepo.deleteById(id);
    }


}
