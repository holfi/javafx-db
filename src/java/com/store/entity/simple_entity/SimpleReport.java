package com.store.entity.simple_entity;

import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;

public class SimpleReport {

    @FXML SimpleStringProperty repUrl;
    @FXML SimpleStringProperty repNamespace;
    @FXML SimpleStringProperty repAuthor;
    @FXML SimpleStringProperty repType;
    @FXML SimpleStringProperty repService;
    @FXML SimpleStringProperty repStatus;
    @FXML SimpleStringProperty repDocCode;

    public SimpleReport(String repUrl, String repNamespace, String repAuthor, String repType, String repService, String repStatus, String repDocCode) {
        this.repUrl = new SimpleStringProperty(repUrl);
        this.repNamespace = new SimpleStringProperty(repNamespace);
        this.repAuthor = new SimpleStringProperty(repAuthor);
        this.repType = new SimpleStringProperty(repType);
        this.repService = new SimpleStringProperty(repService);
        this.repStatus = new SimpleStringProperty(repStatus);
        this.repDocCode = new SimpleStringProperty(repDocCode);
    }

    public String getRepUrl() {
        return repUrl.get();
    }

    public SimpleStringProperty repUrlProperty() {
        return repUrl;
    }

    public void setRepUrl(String repUrl) {
        this.repUrl.set(repUrl);
    }

    public String getRepNamespace() {
        return repNamespace.get();
    }

    public SimpleStringProperty repNamespaceProperty() {
        return repNamespace;
    }

    public void setRepNamespace(String repNamespace) {
        this.repNamespace.set(repNamespace);
    }

    public String getRepAuthor() {
        return repAuthor.get();
    }

    public SimpleStringProperty repAuthorProperty() {
        return repAuthor;
    }

    public void setRepAuthor(String repAuthor) {
        this.repAuthor.set(repAuthor);
    }

    public String getRepType() {
        return repType.get();
    }

    public SimpleStringProperty repTypeProperty() {
        return repType;
    }

    public void setRepType(String repType) {
        this.repType.set(repType);
    }

    public String getRepService() {
        return repService.get();
    }

    public SimpleStringProperty repServiceProperty() {
        return repService;
    }

    public void setRepService(String repService) {
        this.repService.set(repService);
    }

    public String getRepStatus() {
        return repStatus.get();
    }

    public SimpleStringProperty repStatusProperty() {
        return repStatus;
    }

    public void setRepStatus(String repStatus) {
        this.repStatus.set(repStatus);
    }

    public String getRepDocCode() {
        return repDocCode.get();
    }

    public SimpleStringProperty repDocCodeProperty() {
        return repDocCode;
    }

    public void setRepDocCode(String repDocCode) {
        this.repDocCode.set(repDocCode);
    }
}
