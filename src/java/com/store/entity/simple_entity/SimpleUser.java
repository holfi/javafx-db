package com.store.entity.simple_entity;

import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;

public class SimpleUser {

    @FXML SimpleLongProperty id;
    @FXML SimpleStringProperty login;
    @FXML SimpleStringProperty name;

    public SimpleUser(Long id, String login, String name) {
        this.id = new SimpleLongProperty(id);
        this.login = new SimpleStringProperty(login);
        this.name = new SimpleStringProperty(name);
    }

    public long getId() {
        return id.get();
    }

    public SimpleLongProperty idProperty() {
        return id;
    }

    public String getLogin() {
        return login.get();
    }

    public SimpleStringProperty loginProperty() {
        return login;
    }

    public String getName() {
        return name.get();
    }

    public SimpleStringProperty nameProperty() {
        return name;
    }

    public void setId(long id) {
        this.id.set(id);
    }

    public void setLogin(String login) {
        this.login.set(login);
    }

    public void setName(String name) {
        this.name.set(name);
    }
}
