package com.store.entity.simple_entity;


import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;

public class SimpleService {

    @FXML SimpleLongProperty id;
    @FXML SimpleStringProperty serviceCode;
    @FXML SimpleStringProperty author;

    public SimpleService(Long id, String serviceCode, String author) {
        this.id = new SimpleLongProperty(id);
        this.serviceCode = new SimpleStringProperty(serviceCode);
        this.author = new SimpleStringProperty(author);
    }

    public long getId() {
        return id.get();
    }

    public SimpleLongProperty idProperty() {
        return id;
    }

    public String getServiceCode() {
        return serviceCode.get();
    }

    public SimpleStringProperty serviceCodeProperty() {
        return serviceCode;
    }

    public void setId(long id) {
        this.id.set(id);
    }

    public void setServiceCode(String serviceCode) {
        this.serviceCode.set(serviceCode);
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
