package com.store.service;

import com.store.entity.*;
import com.store.entity.simple_entity.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.List;

@org.springframework.stereotype.Service
public class ConvertService {

    public ObservableList<SimpleType> convertType(List<Type> list) {
        ObservableList<SimpleType> oList = FXCollections.observableArrayList();

        for (Type t : list) {
            oList.add(new SimpleType(t.getId(), t.getLabel(), t.getDescription(), t.getAuthor()));
        }

        return oList;
    }

    public ObservableList<SimpleService> convertService(List<Service> list) {
        ObservableList<SimpleService> oList = FXCollections.observableArrayList();

        for (Service s : list) {
            oList.add(new SimpleService(s.getId(), s.getServiceCode(), s.getAuthor()));
        }

        return oList;
    }

    public ObservableList<SimpleStore> convertStore(List<Store> list) {
        ObservableList<SimpleStore> oList = FXCollections.observableArrayList();

        for (Store s : list) {
            oList.add(new SimpleStore(s.getId(), s.getTypeId(), s.getServiceId(), s.getStatusId(), s.getUrl(), s.getNamespace(), s.getDocCode(), s.getAuthor()));
        }

        return oList;
    }

    public ObservableList<SimpleUser> convertUser(List<User> list) {
        ObservableList<SimpleUser> oList = FXCollections.observableArrayList();

        for (User u : list) {
            oList.add(new SimpleUser(u.getId(), u.getLogin(), u.getName()));
        }

        return oList;
    }

    public ObservableList<SimpleStatusCode> convertStatus(List<StatusCode> list) {
        ObservableList<SimpleStatusCode> oList = FXCollections.observableArrayList();

        for (StatusCode s : list) {
            oList.add(new SimpleStatusCode(s.getId(), s.getCode(), s.getName(), s.getAuthor()));
        }

        return oList;
    }

    public ObservableList<SimpleReport> convertReport(List<Report> list) {
        ObservableList<SimpleReport> oList = FXCollections.observableArrayList();

        for (Report r : list) {
            oList.add(new SimpleReport(r.getRepUrl(), r.getRepNamespace(), r.getRepAuthor(),
                    r.getRepType(), r.getRepService(), r.getRepStatus(), r.getRepDocCode()));
        }

        return oList;
    }

}
