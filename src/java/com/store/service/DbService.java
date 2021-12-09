package com.store.service;

import com.store.entity.*;
import com.store.repository.*;
import com.store.service.specs.*;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;

import javax.transaction.Transactional;
import java.sql.SQLException;
import java.util.List;

@org.springframework.stereotype.Service
@RequiredArgsConstructor
public class DbService {

    private final TypeRepo typeRepo;
    private final ServiceRepo serviceRepo;
    private final StoreRepo storeRepo;
    private final UserRepo userRepo;
    private final StatusRepo statusRepo;

    private final TypeSpecs typeSpecs;
    private final ServiceSpecs serviceSpecs;
    private final StoreSpecs storeSpecs;
    private final UserSpecs userSpecs;
    private final StatusSpecs statusSpecs;


    // find
    public List<Type> find(Type type) {
        return typeRepo.findAll(typeSpecs.findByPredicates(type));
    }
    public List<Service> find(Service service) { return serviceRepo.findAll(serviceSpecs.findByPredicates(service)); }
    public List<Store> find(Store store) { return storeRepo.findAll(storeSpecs.findByPredicates(store)); }
    public List<User> find(User user) { return userRepo.findAll(userSpecs.findByPredicates(user)); }
    public List<StatusCode> find(StatusCode statusCode) { return statusRepo.findAll(statusSpecs.findByPredicates(statusCode)); }

    // find All
    public List<Type> findAllTypes() {
        return typeRepo.findAll();
    }
    public List<Service> findAllServices() { return serviceRepo.findAll(); }
    public List<Store> findAllStores() { return storeRepo.findAll(); }
    public List<User> findAllUsers() { return userRepo.findAll(); }
    public List<StatusCode> findAllStatuses() { return statusRepo.findAll(); }


    // saveOrUpdate
    public void saveOrUpdate(Type type) {
        typeRepo.saveAndFlush(type);
        typeRepo.save(type);
    }
    public void saveOrUpdate(Service service) {
        serviceRepo.saveAndFlush(service);
        serviceRepo.save(service);
    }

    public void saveOrUpdate(Store store) {
        storeRepo.saveAndFlush(store);
        storeRepo.save(store);
    }
    public void saveOrUpdate(User user) { userRepo.save(user); }
    public void saveOrUpdate(StatusCode statusCode) {
        statusRepo.save(statusCode);
        statusRepo.saveAndFlush(statusCode);
    }


    // delete
    public void deleteType(Long id) { typeRepo.deleteById(id); }
    public void deleteService(Long id) { serviceRepo.deleteById(id); }
    public void deleteStore(Long id) { storeRepo.deleteById(id); }
    public void deleteUser(Long id) { userRepo.deleteById(id); }
    public void deleteStatus(Long id) { statusRepo.deleteById(id); }


}
