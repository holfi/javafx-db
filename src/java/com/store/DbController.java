package com.store;

import com.store.entity.*;
import com.store.entity.simple_entity.*;
import com.store.service.ConvertService;
import com.store.service.DbService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.cell.PropertyValueFactory;
import net.rgielen.fxweaver.core.FxmlView;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;

import java.text.DecimalFormat;
import java.text.ParsePosition;

@Component
@FxmlView("db.fxml")
public class DbController {

    @Autowired private DbService dbService;
    @Autowired private ConvertService convertService;

    // Type Properties
    @FXML TextField typeId;
    @FXML TextField typeLabel;
    @FXML TextField typeDesc;
    @FXML TableView<SimpleType> typeTable;
    @FXML TableColumn typeIdCol;
    @FXML TableColumn typeLabelCol;
    @FXML TableColumn typeDescCol;
    ObservableList<SimpleType> typeList;

    // Service Properties
    @FXML TextField serviceId;
    @FXML TextField serviceCode;
    @FXML TableView<SimpleService> serviceTable;
    @FXML TableColumn serviceIdCol;
    @FXML TableColumn serviceCodeCol;
    ObservableList<SimpleService> serviceList;

    // Status Properties
    @FXML TextField statusId;
    @FXML TextField statusCode;
    @FXML TextField statusName;
    @FXML TableView<SimpleStatusCode> statusTable;
    @FXML TableColumn statusIdCol;
    @FXML TableColumn statusCodeCol;
    @FXML TableColumn statusNameCol;
    ObservableList<SimpleStatusCode> statusList;

    // Store Properties
    @FXML TextField storeId;
    @FXML TextField storeTypeId;
    @FXML TextField storeServiceId;
    @FXML TextField storeStatusId;
    @FXML TextField storeUrl;
    @FXML TextField storeNamespace;
    @FXML TextField storeDocCode;
    @FXML TableView<SimpleStore> storeTable;
    @FXML TableColumn storeIdCol;
    @FXML TableColumn storeTypeIdCol;
    @FXML TableColumn storeStatusIdCol;
    @FXML TableColumn storeServiceIdCol;
    @FXML TableColumn storeUrlCol;
    @FXML TableColumn storeNamespaceCol;
    @FXML TableColumn storeDocCodeCol;
    ObservableList<SimpleStore> storeList;

    // User Properties
    @FXML TextField userId;
    @FXML TextField userLogin;
    @FXML TextField userName;
    @FXML TableView<SimpleUser> userTable;
    @FXML TableColumn userIdCol;
    @FXML TableColumn userLoginCol;
    @FXML TableColumn userNameCol;
    ObservableList<SimpleUser> userList;

    @FXML
    public void initialize() {
        typeTable.setEditable(true);
        serviceTable.setEditable(true);
        storeTable.setEditable(true);
        userTable.setEditable(true);
        statusTable.setEditable(true);

        setUpdatable();
        setNumbersOnly();
    }

    public void typeFind(ActionEvent event) {
        typeList = FXCollections.observableArrayList();

        typeTable.setItems(convertService.convertType(
                dbService.find(new Type(parseToLong(typeId.getText()), typeLabel.getText(), typeDesc.getText()))));
        clearTypeText();
    }

    public void serviceFind(ActionEvent event) {
        serviceList = FXCollections.observableArrayList();

        serviceTable.setItems(convertService.convertService(
                dbService.find(new Service(parseToLong(serviceId.getText()), serviceCode.getText()))));
        clearServiceText();
    }

    public void storeFind(ActionEvent event) {
        storeList = FXCollections.observableArrayList();

        storeTable.setItems(convertService.convertStore(
                dbService.find(new Store(parseToLong(storeId.getText()),
                        parseToLong(storeTypeId.getText()), parseToLong(storeServiceId.getText()),
                        parseToLong(storeStatusId.getText()), storeUrl.getText(), storeNamespace.getText(), storeDocCode.getText()))));
        clearServiceText();
    }

    public void userFind(ActionEvent event) {
        userList = FXCollections.observableArrayList();

        userTable.setItems(convertService.convertUser(
                dbService.find(new User(parseToLong(userId.getText()), userLogin.getText(), userName.getText()))));
        clearServiceText();
    }

    public void statusFind(ActionEvent event) {
        statusList = FXCollections.observableArrayList();

        statusTable.setItems(convertService.convertStatus(
                dbService.find(new StatusCode(parseToLong(statusId.getText()), parseToLong(statusCode.getText()), statusName.getText()))));
        clearServiceText();
    }

    public void setUpdatable() {
        typeIdCol.setCellValueFactory(new PropertyValueFactory<Type, Long>("id"));
        typeLabelCol.setCellValueFactory(new PropertyValueFactory<Type, String>("label"));
        typeDescCol.setCellValueFactory(new PropertyValueFactory<Type, String>("description"));

        statusIdCol.setCellValueFactory(new PropertyValueFactory<StatusCode, Long>("id"));
        statusCodeCol.setCellValueFactory(new PropertyValueFactory<StatusCode, Long>("code"));
        statusNameCol.setCellValueFactory(new PropertyValueFactory<StatusCode, String>("name"));

        storeIdCol.setCellValueFactory(new PropertyValueFactory<Store, Long>("id"));
        storeTypeIdCol.setCellValueFactory(new PropertyValueFactory<Store, Long>("typeId"));
        storeServiceIdCol.setCellValueFactory(new PropertyValueFactory<Store, Long>("serviceId"));
        storeStatusIdCol.setCellValueFactory(new PropertyValueFactory<Store, Long>("statusId"));
        storeUrlCol.setCellValueFactory(new PropertyValueFactory<Store, String>("url"));
        storeDocCodeCol.setCellValueFactory(new PropertyValueFactory<Store, String>("docCode"));
        storeNamespaceCol.setCellValueFactory(new PropertyValueFactory<Store, String>("namespace"));

        userIdCol.setCellValueFactory(new PropertyValueFactory<User, Long>("id"));
        userLoginCol.setCellValueFactory(new PropertyValueFactory<User, String>("login"));
        userNameCol.setCellValueFactory(new PropertyValueFactory<User, String>("name"));

        serviceIdCol.setCellValueFactory(new PropertyValueFactory<Service, Long>("id"));
        serviceCodeCol.setCellValueFactory(new PropertyValueFactory<Service, String>("code"));

        typeLabelCol.setOnEditCommit(
                (EventHandler<CellEditEvent<SimpleType, String>>) t -> {
                    SimpleType simpleType = t.getTableView().getItems().get(
                            t.getTablePosition().getRow());
                    simpleType.setLabel(t.getNewValue());
                    dbService.saveOrUpdate(new Type(simpleType.getId(), simpleType.getLabel(), simpleType.getDescription()));
                }
        );

        typeDescCol.setOnEditCommit(
                (EventHandler<CellEditEvent<SimpleType, String>>) t -> {
                    SimpleType simpleType = t.getTableView().getItems().get(
                            t.getTablePosition().getRow());
                    simpleType.setDescription(t.getNewValue());
                    dbService.saveOrUpdate(new Type(simpleType.getId(), simpleType.getLabel(), simpleType.getDescription()));
                }
        );

        statusCodeCol.setOnEditCommit(
                (EventHandler<CellEditEvent<SimpleStatusCode, Integer>>) t -> {
                    SimpleStatusCode simpleStatusCode = t.getTableView().getItems().get(
                            t.getTablePosition().getRow());
                    simpleStatusCode.setCode(t.getNewValue());
                    dbService.saveOrUpdate(new StatusCode(simpleStatusCode.getId(), simpleStatusCode.getCode(), simpleStatusCode.getName()));
                }
        );

        statusCodeCol.setOnEditCommit(
                (EventHandler<CellEditEvent<SimpleStatusCode, String>>) t -> {
                    SimpleStatusCode simpleStatusCode = t.getTableView().getItems().get(
                            t.getTablePosition().getRow());
                    simpleStatusCode.setName(t.getNewValue());
                    dbService.saveOrUpdate(new StatusCode(simpleStatusCode.getId(), simpleStatusCode.getCode(), simpleStatusCode.getName()));
                }
        );

        storeTypeIdCol.setOnEditCommit(
                (EventHandler<CellEditEvent<SimpleStore, Long>>) t -> {
                    SimpleStore simpleStore = t.getTableView().getItems().get(
                            t.getTablePosition().getRow());
                    simpleStore.setTypeId(t.getNewValue());
                    dbService.saveOrUpdate(new Store(simpleStore.getId(), simpleStore.getTypeId(), simpleStore.getServiceId(),
                            simpleStore.getStatusId(), simpleStore.getUrl(), simpleStore.getNamespace(), simpleStore.getDocCode()));
                }
        );

        storeServiceIdCol.setOnEditCommit(
                (EventHandler<CellEditEvent<SimpleStore, Long>>) t -> {
                    SimpleStore simpleStore = t.getTableView().getItems().get(
                            t.getTablePosition().getRow());
                    simpleStore.setServiceId(t.getNewValue());
                    dbService.saveOrUpdate(new Store(simpleStore.getId(), simpleStore.getTypeId(), simpleStore.getServiceId(),
                            simpleStore.getStatusId(), simpleStore.getUrl(), simpleStore.getNamespace(), simpleStore.getDocCode()));
                }
        );

        storeStatusIdCol.setOnEditCommit(
                (EventHandler<CellEditEvent<SimpleStore, Long>>) t -> {
                    SimpleStore simpleStore = t.getTableView().getItems().get(
                            t.getTablePosition().getRow());
                    simpleStore.setStatusId(t.getNewValue());
                    dbService.saveOrUpdate(new Store(simpleStore.getId(), simpleStore.getTypeId(), simpleStore.getServiceId(),
                            simpleStore.getStatusId(), simpleStore.getUrl(), simpleStore.getNamespace(), simpleStore.getDocCode()));
                }
        );

        storeUrlCol.setOnEditCommit(
                (EventHandler<CellEditEvent<SimpleStore, String>>) t -> {
                    SimpleStore simpleStore = t.getTableView().getItems().get(
                            t.getTablePosition().getRow());
                    simpleStore.setUrl(t.getNewValue());
                    dbService.saveOrUpdate(new Store(simpleStore.getId(), simpleStore.getTypeId(), simpleStore.getServiceId(),
                            simpleStore.getStatusId(), simpleStore.getUrl(), simpleStore.getNamespace(), simpleStore.getDocCode()));
                }
        );

        storeNamespaceCol.setOnEditCommit(
                (EventHandler<CellEditEvent<SimpleStore, String>>) t -> {
                    SimpleStore simpleStore = t.getTableView().getItems().get(
                            t.getTablePosition().getRow());
                    simpleStore.setNamespace(t.getNewValue());
                    dbService.saveOrUpdate(new Store(simpleStore.getId(), simpleStore.getTypeId(), simpleStore.getServiceId(),
                            simpleStore.getStatusId(), simpleStore.getUrl(), simpleStore.getNamespace(), simpleStore.getDocCode()));
                }
        );

        storeDocCodeCol.setOnEditCommit(
                (EventHandler<CellEditEvent<SimpleStore, String>>) t -> {
                    SimpleStore simpleStore = t.getTableView().getItems().get(
                            t.getTablePosition().getRow());
                    simpleStore.setDocCode(t.getNewValue());
                    dbService.saveOrUpdate(new Store(simpleStore.getId(), simpleStore.getTypeId(), simpleStore.getServiceId(),
                            simpleStore.getStatusId(), simpleStore.getUrl(), simpleStore.getNamespace(), simpleStore.getDocCode()));
                }
        );

        userLoginCol.setOnEditCommit(
                (EventHandler<CellEditEvent<SimpleUser, String>>) t -> {
                    SimpleUser simpleUser = t.getTableView().getItems().get(
                            t.getTablePosition().getRow());
                    simpleUser.setLogin(t.getNewValue());
                    dbService.saveOrUpdate(new User(simpleUser.getId(), simpleUser.getLogin(), simpleUser.getName()));
                }
        );

        userNameCol.setOnEditCommit(
                (EventHandler<CellEditEvent<SimpleUser, String>>) t -> {
                    SimpleUser simpleUser = t.getTableView().getItems().get(
                            t.getTablePosition().getRow());
                    simpleUser.setName(t.getNewValue());
                    dbService.saveOrUpdate(new User(simpleUser.getId(), simpleUser.getLogin(), simpleUser.getName()));
                }
        );

        serviceCodeCol.setOnEditCommit(
                (EventHandler<CellEditEvent<SimpleService, String>>) t -> {
                    SimpleService simpleService = t.getTableView().getItems().get(
                            t.getTablePosition().getRow());
                    simpleService.setServiceCode(t.getNewValue());
                    dbService.saveOrUpdate(new Service(simpleService.getId(), simpleService.getServiceCode()));
                }
        );
    }

    public void typeAdd(ActionEvent event) {
        if (typeLabel.getText().equals("") || typeDesc.getText().equals("")) {
            new Alert(Alert.AlertType.NONE, "Поле 'Название типа' и 'Описание' должны быть заполнены", new ButtonType("Закрыть")).showAndWait();
            return;
        }

        dbService.saveOrUpdate(new Type(typeLabel.getText(), typeDesc.getText()));
        clearTypeText();
        typeTable.setItems(convertService.convertType(dbService.findAllTypes()));
    }

    public void statusAdd(ActionEvent event) {
        if (statusCode.getText().equals("") || statusName.getText().equals("")) {
            new Alert(Alert.AlertType.NONE, "Поле 'Код' и 'Название статуса' должны быть заполнены", new ButtonType("Закрыть")).showAndWait();
            return;
        }

        dbService.saveOrUpdate(new StatusCode(parseToLong(statusCode.getText()), statusName.getText()));
        clearStatusText();
        statusTable.setItems(convertService.convertStatus(dbService.findAllStatuses()));
    }

    public void storeAdd(ActionEvent event) {
        if (storeTypeId.getText().equals("") || storeUrl.getText().equals("")) {
            new Alert(Alert.AlertType.NONE, "Поле 'ID типа' и 'URL' должны быть заполнены", new ButtonType("Закрыть")).showAndWait();
            return;
        }

        try {
            dbService.saveOrUpdate(new Store(parseToLong(storeTypeId.getText()), parseToLong(storeServiceId.getText()),
                    parseToLong(storeStatusId.getText()), storeUrl.getText(), storeNamespace.getText(), storeDocCode.getText()));
        }
        catch (DataAccessException d) {
            String error = d.getRootCause().toString();
            new Alert(Alert.AlertType.NONE, "Ошибка: " + error.substring(error.indexOf("Подробности: ") + 13), new ButtonType("Закрыть")).showAndWait();
        }
        clearStoreText();
        storeTable.setItems(convertService.convertStore(dbService.findAllStores()));
    }

    public void userAdd(ActionEvent event) {
        if (userLogin.getText().equals("") || userName.getText().equals("")) {
            new Alert(Alert.AlertType.NONE, "Поле 'Логин' и 'Имя' должны быть заполнены", new ButtonType("Закрыть")).showAndWait();
            return;
        }

        dbService.saveOrUpdate(new User(userLogin.getText(), userName.getText()));
        clearUserText();
        userTable.setItems(convertService.convertUser(dbService.findAllUsers()));
    }

    public void serviceAdd(ActionEvent event) {
        if (serviceCode.getText().equals("")) {
            new Alert(Alert.AlertType.NONE, "Поле 'Код услуги' должно быть заполнено", new ButtonType("Закрыть")).showAndWait();
            return;
        }

        dbService.saveOrUpdate(new Service(serviceCode.getText()));
        clearServiceText();
        serviceTable.setItems(convertService.convertService(dbService.findAllServices()));
    }

    public void typeDelete(ActionEvent event) {
        dbService.deleteType(Long.valueOf(typeId.getText()));

        clearTypeText();
        typeTable.setItems(convertService.convertType(dbService.findAllTypes()));
    }

    public void statusDelete(ActionEvent event) {
        dbService.deleteStatus(Long.valueOf(statusId.getText()));

        clearStatusText();
        statusTable.setItems(convertService.convertStatus(dbService.findAllStatuses()));
    }

    public void storeDelete(ActionEvent event) {
        dbService.deleteStore(Long.valueOf(storeId.getText()));

        clearStoreText();
        storeTable.setItems(convertService.convertStore(dbService.findAllStores()));
    }

    public void userDelete(ActionEvent event) {
        dbService.deleteUser(Long.valueOf(userId.getText()));

        clearUserText();
        userTable.setItems(convertService.convertUser(dbService.findAllUsers()));
    }

    public void serviceDelete(ActionEvent event) {
        dbService.deleteService(Long.valueOf(serviceId.getText()));

        clearServiceText();
        serviceTable.setItems(convertService.convertService(dbService.findAllServices()));
    }

    public void typeClear(ActionEvent event) {
        clearTypeText();
    }

    public void clearTypeText() {
        typeId.setText("");
        typeLabel.setText("");
        typeDesc.setText("");
    }

    public void clearServiceText() {
        serviceId.setText("");
        serviceCode.setText("");
    }

    public void clearStoreText() {
        storeId.setText("");
        storeTypeId.setText("");
        storeServiceId.setText("");
        storeStatusId.setText("");
        storeUrl.setText("");
        storeNamespace.setText("");
        storeDocCode.setText("");
    }

    public void clearUserText() {
        userId.setText("");
        userLogin.setText("");
        userName.setText("");
    }

    public void clearStatusText() {
        statusId.setText("");
        statusCode.setText("");
        statusName.setText("");
    }

    public void setNumbersOnly() {
        DecimalFormat format = new DecimalFormat( "#.0" );

        typeId.setTextFormatter(new TextFormatter<>(c -> {
            if (c.getControlNewText().isEmpty()) {
                return c;
            }

            ParsePosition parsePosition = new ParsePosition(0);
            Object object = format.parse(c.getControlNewText(), parsePosition);

            if (object == null || parsePosition.getIndex() < c.getControlNewText().length()) {
                return null;
            }
            else {
                return c;
            }
        }));

        storeId.setTextFormatter(new TextFormatter<>(c -> {
            if (c.getControlNewText().isEmpty()) {
                return c;
            }

            ParsePosition parsePosition = new ParsePosition(0);
            Object object = format.parse(c.getControlNewText(), parsePosition);

            if (object == null || parsePosition.getIndex() < c.getControlNewText().length()) {
                return null;
            }
            else {
                return c;
            }
        }));

        storeStatusId.setTextFormatter(new TextFormatter<>(c -> {
            if (c.getControlNewText().isEmpty()) {
                return c;
            }

            ParsePosition parsePosition = new ParsePosition(0);
            Object object = format.parse(c.getControlNewText(), parsePosition);

            if (object == null || parsePosition.getIndex() < c.getControlNewText().length()) {
                return null;
            }
            else {
                return c;
            }
        }));

        storeServiceId.setTextFormatter(new TextFormatter<>(c -> {
            if (c.getControlNewText().isEmpty()) {
                return c;
            }

            ParsePosition parsePosition = new ParsePosition(0);
            Object object = format.parse(c.getControlNewText(), parsePosition);

            if (object == null || parsePosition.getIndex() < c.getControlNewText().length()) {
                return null;
            }
            else {
                return c;
            }
        }));

        storeTypeId.setTextFormatter(new TextFormatter<>(c -> {
            if (c.getControlNewText().isEmpty()) {
                return c;
            }

            ParsePosition parsePosition = new ParsePosition(0);
            Object object = format.parse(c.getControlNewText(), parsePosition);

            if (object == null || parsePosition.getIndex() < c.getControlNewText().length()) {
                return null;
            }
            else {
                return c;
            }
        }));

        statusId.setTextFormatter(new TextFormatter<>(c -> {
            if (c.getControlNewText().isEmpty()) {
                return c;
            }

            ParsePosition parsePosition = new ParsePosition(0);
            Object object = format.parse(c.getControlNewText(), parsePosition);

            if (object == null || parsePosition.getIndex() < c.getControlNewText().length()) {
                return null;
            }
            else {
                return c;
            }
        }));

        statusCode.setTextFormatter(new TextFormatter<>(c -> {
            if (c.getControlNewText().isEmpty()) {
                return c;
            }

            ParsePosition parsePosition = new ParsePosition(0);
            Object object = format.parse(c.getControlNewText(), parsePosition);

            if (object == null || parsePosition.getIndex() < c.getControlNewText().length()) {
                return null;
            }
            else {
                return c;
            }
        }));

        userId.setTextFormatter(new TextFormatter<>(c -> {
            if (c.getControlNewText().isEmpty()) {
                return c;
            }

            ParsePosition parsePosition = new ParsePosition(0);
            Object object = format.parse(c.getControlNewText(), parsePosition);

            if (object == null || parsePosition.getIndex() < c.getControlNewText().length()) {
                return null;
            }
            else {
                return c;
            }
        }));

        serviceId.setTextFormatter(new TextFormatter<>(c -> {
            if (c.getControlNewText().isEmpty()) {
                return c;
            }

            ParsePosition parsePosition = new ParsePosition(0);
            Object object = format.parse(c.getControlNewText(), parsePosition);

            if (object == null || parsePosition.getIndex() < c.getControlNewText().length()) {
                return null;
            }
            else {
                return c;
            }
        }));
    }

    public Long parseToLong(String s) {
        try {
            return Long.valueOf(s);
        }
        catch (Exception e) {
            return null;
        }
    }

}
