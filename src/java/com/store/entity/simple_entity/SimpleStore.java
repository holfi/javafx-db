package com.store.entity.simple_entity;

import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

public class SimpleStore {

    @FXML SimpleLongProperty id;
    @FXML SimpleLongProperty typeId;
    @FXML SimpleLongProperty serviceId;
    @FXML SimpleLongProperty statusId;
    @FXML SimpleStringProperty url;
    @FXML SimpleStringProperty namespace;
    @FXML SimpleStringProperty docCode;

    public SimpleStore(Long id, Long typeId, Long serviceId, Long statusId, String url, String namespace, String docCode) {
        this.id = new SimpleLongProperty(id);
        this.typeId = new SimpleLongProperty(typeId);
        this.serviceId = serviceId != null ? new SimpleLongProperty(serviceId) : null;
        this.statusId = statusId != null ? new SimpleLongProperty(statusId) : null;
        this.url = new SimpleStringProperty(url);
        this.namespace = new SimpleStringProperty(namespace);
        this.docCode = new SimpleStringProperty(docCode);
    }

    public long getId() {
        return id.get();
    }

    public SimpleLongProperty idProperty() {
        return id;
    }

    public long getTypeId() {
        return typeId.get();
    }

    public SimpleLongProperty typeIdProperty() {
        return typeId;
    }

    public long getServiceId() {
        return serviceId.get();
    }

    public SimpleLongProperty serviceIdProperty() {
        return serviceId;
    }

    public long getStatusId() {
        return statusId.get();
    }

    public SimpleLongProperty statusIdProperty() {
        return statusId;
    }

    public String getUrl() {
        return url.get();
    }

    public SimpleStringProperty urlProperty() {
        return url;
    }

    public String getNamespace() {
        return namespace.get();
    }

    public SimpleStringProperty namespaceProperty() {
        return namespace;
    }

    public String getDocCode() {
        return docCode.get();
    }

    public SimpleStringProperty docCodeProperty() {
        return docCode;
    }

    public void setId(long id) {
        this.id.set(id);
    }

    public void setTypeId(long typeId) {
        this.typeId.set(typeId);
    }

    public void setServiceId(long serviceId) {
        this.serviceId.set(serviceId);
    }

    public void setStatusId(long statusId) {
        this.statusId.set(statusId);
    }

    public void setUrl(String url) {
        this.url.set(url);
    }

    public void setNamespace(String namespace) {
        this.namespace.set(namespace);
    }

    public void setDocCode(String docCode) {
        this.docCode.set(docCode);
    }
}
