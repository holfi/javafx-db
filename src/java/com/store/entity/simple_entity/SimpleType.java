package com.store.entity.simple_entity;

import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import lombok.Setter;

public class SimpleType {

    @FXML SimpleLongProperty id;
    @FXML SimpleStringProperty label;
    @FXML SimpleStringProperty description;
    @FXML SimpleStringProperty author;

    public SimpleType(long id, String label, String description, String author) {
        this.id = new SimpleLongProperty(id);
        this.label = new SimpleStringProperty(label);
        this.description = new SimpleStringProperty(description);
        this.author = new SimpleStringProperty(author);
    }

    public long getId() {
        return id.get();
    }

    public SimpleLongProperty idProperty() {
        return id;
    }

    public String getLabel() {
        return label.get();
    }

    public SimpleStringProperty labelProperty() {
        return label;
    }

    public String getDescription() {
        return description.get();
    }

    public SimpleStringProperty descriptionProperty() {
        return description;
    }

    public void setId(long id) {
        this.id.set(id);
    }

    public void setLabel(String label) {
        this.label.set(label);
    }

    public void setDescription(String description) {
        this.description.set(description);
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
