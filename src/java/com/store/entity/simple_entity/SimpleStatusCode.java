package com.store.entity.simple_entity;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;

public class SimpleStatusCode {

    @FXML SimpleLongProperty id;
    @FXML SimpleLongProperty code;
    @FXML SimpleStringProperty name;
    @FXML SimpleStringProperty author;

    public SimpleStatusCode(Long id, Long code, String name, String author) {
        this.id = new SimpleLongProperty(id);
        this.code = new SimpleLongProperty(code);
        this.name = new SimpleStringProperty(name);
        this.author = new SimpleStringProperty(author);
    }

    public long getId() {
        return id.get();
    }

    public SimpleLongProperty idProperty() {
        return id;
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

    public void setName(String name) {
        this.name.set(name);
    }

    public long getCode() {
        return code.get();
    }

    public SimpleLongProperty codeProperty() {
        return code;
    }

    public void setCode(long code) {
        this.code.set(code);
    }

    public String getAuthor() {
        return author.get();
    }

    public SimpleStringProperty authorProperty() {
        return author;
    }

    public void setAuthor(String author) {
        this.author.set(author);
    }
}
