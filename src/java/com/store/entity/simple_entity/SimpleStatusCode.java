package com.store.entity.simple_entity;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;

public class SimpleStatusCode {

    @FXML SimpleLongProperty id;
    @FXML SimpleIntegerProperty code;
    @FXML SimpleStringProperty name;

    public SimpleStatusCode(Long id, int code, String name) {
        this.id = new SimpleLongProperty(id);
        this.code = new SimpleIntegerProperty(code);
        this.name = new SimpleStringProperty(name);
    }

    public long getId() {
        return id.get();
    }

    public SimpleLongProperty idProperty() {
        return id;
    }

    public int getCode() {
        return code.get();
    }

    public SimpleIntegerProperty codeProperty() {
        return code;
    }

    public String getName() {
        return name.get();
    }

    public SimpleStringProperty nameProperty() {
        return name;
    }
}
