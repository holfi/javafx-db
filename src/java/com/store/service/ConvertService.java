package com.store.service;

import com.store.entity.Type;
import com.store.entity.simple_entity.SimpleType;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ConvertService {

    public ObservableList<SimpleType> convertType(List<Type> list) {
        ObservableList<SimpleType> oList = FXCollections.observableArrayList();

        for (Type t : list) {
            oList.add(new SimpleType(t.getId(), t.getLabel(), t.getDescription()));
        }

        return oList;
    }

}
