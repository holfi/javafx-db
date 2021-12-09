package com.store;

import com.store.entity.*;
import com.store.entity.simple_entity.*;
import com.store.service.ConvertService;
import com.store.service.DbService;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Component;

import java.sql.*;
import java.text.DecimalFormat;
import java.text.ParsePosition;
import java.util.ArrayList;
import java.util.List;

@Component
@FxmlView("db.fxml")
public class DbController {

    String userLoginForm;

    @Autowired private DbService dbService;
    @Autowired private ConvertService convertService;

    // Type Properties
    @FXML TextField typeId;
    @FXML TextField typeLabel;
    @FXML TextField typeDesc;
    @FXML TextField typeAuthor;
    @FXML TableView<SimpleType> typeTable;
    @FXML TableColumn typeIdCol;
    @FXML TableColumn typeLabelCol;
    @FXML TableColumn typeDescCol;
    @FXML TableColumn typeAuthorCol;

    // Service Properties
    @FXML TextField serviceId;
    @FXML TextField serviceCode;
    @FXML TextField serviceAuthor;
    @FXML TableView<SimpleService> serviceTable;
    @FXML TableColumn serviceIdCol;
    @FXML TableColumn serviceCodeCol;
    @FXML TableColumn serviceAuthorCol;

    // Status Properties
    @FXML TextField statusId;
    @FXML TextField statusCode;
    @FXML TextField statusName;
    @FXML TextField statusAuthor;
    @FXML TableView<SimpleStatusCode> statusTable;
    @FXML TableColumn statusIdCol;
    @FXML TableColumn statusCodeCol;
    @FXML TableColumn statusNameCol;
    @FXML TableColumn statusAuthorCol;

    // Store Properties
    @FXML TextField storeId;
    @FXML TextField storeTypeId;
    @FXML TextField storeServiceId;
    @FXML TextField storeStatusId;
    @FXML TextField storeUrl;
    @FXML TextField storeNamespace;
    @FXML TextField storeDocCode;
    @FXML TextField storeAuthor;
    @FXML TableView<SimpleStore> storeTable;
    @FXML TableColumn storeIdCol;
    @FXML TableColumn storeTypeIdCol;
    @FXML TableColumn storeStatusIdCol;
    @FXML TableColumn storeServiceIdCol;
    @FXML TableColumn storeUrlCol;
    @FXML TableColumn storeNamespaceCol;
    @FXML TableColumn storeDocCodeCol;
    @FXML TableColumn storeAuthorCol;

    // User Properties
    @FXML TextField userId;
    @FXML TextField userLogin;
    @FXML TextField userName;
    @FXML TextField userPassword;
    @FXML TableView<SimpleUser> userTable;
    @FXML TableColumn userIdCol;
    @FXML TableColumn userLoginCol;
    @FXML TableColumn userNameCol;

    @FXML DatePicker dateFrom;
    @FXML DatePicker dateTo;
    @FXML TableColumn repUrl;
    @FXML TableColumn repNamespace;
    @FXML TableColumn repAuthor;
    @FXML TableColumn repType;
    @FXML TableColumn repService;
    @FXML TableColumn repStatus;
    @FXML TableColumn repDocCode;
    @FXML TableView repTable;

    @FXML
    public void initialize() {
        typeTable.setEditable(true);
        serviceTable.setEditable(true);
        storeTable.setEditable(true);
        userTable.setEditable(true);
        statusTable.setEditable(true);

        setUpdatable();
        setNumbersOnly();

        userLoginForm = LoginController.userLogin;
    }

    public void getReport() throws SQLException {
        List<Report> reports = new ArrayList<>();
        String query = "select s.url, s.namespace, s.author, count(t.label), count(s2.service_code),\n" +
                "       count(sc.name), count(s.doc_code)\n" +
                "    from store s\n" +
                "        join type t on s.type_id = t.id\n" +
                "        join service s2 on s2.id = s.service_id\n" +
                "        join status_code sc on sc.id = s.status_id\n" +
                " %s " +
                "    group by s.author, s.url, s.namespace";

        String bothDates = "where s.created > '" + dateFrom.getValue() + "' and s.created < '" + dateTo.getValue() + "'\n";
        String fromDate = "where s.created > '" + dateFrom.getValue() + "'\n";
        String toDate = "where s.created < '" + dateTo.getValue() + "'\n";

        if (dateFrom.getValue() != null && dateTo.getValue() != null) {
            query = String.format(query, bothDates);
        }
        else if (dateFrom.getValue() != null && dateTo.getValue() == null) {
            query = String.format(query, fromDate);
        }
        else if(dateFrom.getValue() == null && dateTo.getValue() != null)
            query = String.format(query, toDate);
        else
            query = String.format(query, "");

        Connection con = DriverManager.getConnection("jdbc:postgresql://localhost:5432/xsd_store", "postgres", "1");
        Statement statement = con.createStatement();
        ResultSet rs = statement.executeQuery(query);

        while (rs.next()) {
            reports.add(new Report(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4),
                    rs.getString(5), rs.getString(6), rs.getString(7)));
        }

        repTable.setItems(convertService.convertReport(reports));
        dateFrom.setValue(null);
        dateTo.setValue(null);
    }

    public void typeFind(ActionEvent event) {
        typeTable.setItems(convertService.convertType(
                dbService.find(new Type(parseToLong(typeId.getText()), typeLabel.getText(), typeDesc.getText(), typeAuthor.getText()))));
        clearTypeText();
    }

    public void serviceFind(ActionEvent event) {
        serviceTable.setItems(convertService.convertService(
                dbService.find(new Service(parseToLong(serviceId.getText()), serviceCode.getText(), serviceAuthor.getText()))));
        clearServiceText();
    }

    public void storeFind(ActionEvent event) {
        storeTable.setItems(convertService.convertStore(
                dbService.find(new Store(parseToLong(storeId.getText()),
                        parseToLong(storeTypeId.getText()), parseToLong(storeServiceId.getText()),
                        parseToLong(storeStatusId.getText()), storeUrl.getText(), storeNamespace.getText(), storeDocCode.getText(), storeAuthor.getText()))));
        clearServiceText();
    }

    public void userFind(ActionEvent event) {
        userTable.setItems(convertService.convertUser(
                dbService.find(new User(parseToLong(userId.getText()), userLogin.getText(), userName.getText(), ""))));
        clearServiceText();
    }

    public void statusFind(ActionEvent event) {
        statusTable.setItems(convertService.convertStatus(
                dbService.find(new StatusCode(parseToLong(statusId.getText()), parseToLong(statusCode.getText()), statusName.getText(), statusAuthor.getText()))));
        clearServiceText();
    }

    public void typeAdd(ActionEvent event) {
        if (typeLabel.getText().equals("") || typeDesc.getText().equals("")) {
            new Alert(Alert.AlertType.NONE, "Поле 'Название типа' и 'Описание' должны быть заполнены", new ButtonType("Закрыть")).showAndWait();
            return;
        }

        dbService.saveOrUpdate(new Type(typeLabel.getText(), typeDesc.getText(), userLoginForm));
        clearTypeText();
        typeTable.setItems(convertService.convertType(dbService.findAllTypes()));
    }

    public void statusAdd(ActionEvent event) {
        if (statusCode.getText().equals("") || statusName.getText().equals("")) {
            new Alert(Alert.AlertType.NONE, "Поле 'Код' и 'Название статуса' должны быть заполнены", new ButtonType("Закрыть")).showAndWait();
            return;
        }

        try {
            dbService.saveOrUpdate(new StatusCode(parseToLong(statusCode.getText()), statusName.getText(), userLoginForm));
        }
        catch (DataAccessException d) {
            String error = d.getRootCause().toString();
            new Alert(Alert.AlertType.NONE, "Ошибка: " + error.substring(error.indexOf("Подробности: ") + 13), new ButtonType("Закрыть")).showAndWait();
        }
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
                    parseToLong(storeStatusId.getText()), storeUrl.getText(), storeNamespace.getText(), storeDocCode.getText(), userLoginForm));
        }
        catch (DataAccessException d) {
            String error = d.getRootCause().toString();
            new Alert(Alert.AlertType.NONE, "Ошибка: " + error.substring(error.indexOf("Подробности: ") + 13), new ButtonType("Закрыть")).showAndWait();
        }
        clearStoreText();
        storeTable.setItems(convertService.convertStore(dbService.findAllStores()));
    }

    public void userAdd(ActionEvent event) {
        if (userLogin.getText().equals("") || userName.getText().equals("") || userPassword.getText().equals("")) {
            new Alert(Alert.AlertType.NONE, "Поле 'Логин', 'Имя' и 'Пароль' должны быть заполнены", new ButtonType("Закрыть")).showAndWait();
            return;
        }
        try {
            dbService.saveOrUpdate(new User(userLogin.getText(), userName.getText(), userPassword.getText()));
        }
        catch (DataAccessException d) {
            String error = d.getRootCause().toString();
            new Alert(Alert.AlertType.NONE, "Ошибка: " + error.substring(error.indexOf("Подробности: ") + 13), new ButtonType("Закрыть")).showAndWait();
        }
        clearUserText();
        userTable.setItems(convertService.convertUser(dbService.findAllUsers()));
    }

    public void serviceAdd(ActionEvent event) {
        if (serviceCode.getText().equals("")) {
            new Alert(Alert.AlertType.NONE, "Поле 'Код услуги' должно быть заполнено", new ButtonType("Закрыть")).showAndWait();
            return;
        }

        try {
            dbService.saveOrUpdate(new Service(serviceCode.getText(), userLoginForm));
        }
        catch (DataAccessException d) {
            String error = d.getRootCause().toString();
            new Alert(Alert.AlertType.NONE, "Ошибка: " + error.substring(error.indexOf("Подробности: ") + 13), new ButtonType("Закрыть")).showAndWait();
        }
        clearServiceText();
        serviceTable.setItems(convertService.convertService(dbService.findAllServices()));
    }

    public void typeDelete(ActionEvent event) {
        dbService.deleteType(Long.valueOf(typeId.getText()));

        clearTypeText();
        typeTable.setItems(convertService.convertType(dbService.findAllTypes()));
    }

    public void statusDelete(ActionEvent event) {
        System.out.println("inside");

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

    public void serviceDelete(KeyEvent event) {
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
        typeAuthor.setText("");
    }

    public void clearServiceText() {
        serviceId.setText("");
        serviceCode.setText("");
        serviceAuthor.setText("");
    }

    public void clearStoreText() {
        storeId.setText("");
        storeTypeId.setText("");
        storeServiceId.setText("");
        storeStatusId.setText("");
        storeUrl.setText("");
        storeNamespace.setText("");
        storeDocCode.setText("");
        storeAuthor.setText("");
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
        statusAuthor.setText("");
    }

    public void setUpdatable() {
        typeLabelCol.setCellValueFactory(new PropertyValueFactory<Type, String>("label"));
        typeDescCol.setCellValueFactory(new PropertyValueFactory<Type, String>("description"));
        typeAuthorCol.setCellValueFactory(new PropertyValueFactory<Type, String>("author"));

        statusCodeCol.setCellValueFactory(new PropertyValueFactory<StatusCode, Long>("code"));
        statusNameCol.setCellValueFactory(new PropertyValueFactory<StatusCode, String>("name"));
        statusAuthorCol.setCellValueFactory(new PropertyValueFactory<StatusCode, String>("author"));

        storeTypeIdCol.setCellValueFactory(new PropertyValueFactory<Store, Long>("typeId"));
        storeServiceIdCol.setCellValueFactory(new PropertyValueFactory<Store, Long>("serviceId"));
        storeStatusIdCol.setCellValueFactory(new PropertyValueFactory<Store, Long>("statusId"));
        storeUrlCol.setCellValueFactory(new PropertyValueFactory<Store, String>("url"));
        storeDocCodeCol.setCellValueFactory(new PropertyValueFactory<Store, String>("docCode"));
        storeNamespaceCol.setCellValueFactory(new PropertyValueFactory<Store, String>("namespace"));
        storeAuthorCol.setCellValueFactory(new PropertyValueFactory<Store, String>("author"));

        userLoginCol.setCellValueFactory(new PropertyValueFactory<User, String>("login"));
        userNameCol.setCellValueFactory(new PropertyValueFactory<User, String>("name"));

        serviceCodeCol.setCellValueFactory(new PropertyValueFactory<Service, String>("serviceCode"));
        serviceAuthorCol.setCellValueFactory(new PropertyValueFactory<Service, String>("author"));

        repUrl.setCellValueFactory(new PropertyValueFactory<Report, String>("repUrl"));
        repNamespace.setCellValueFactory(new PropertyValueFactory<Report, String>("repNamespace"));
        repAuthor.setCellValueFactory(new PropertyValueFactory<Report, String>("repAuthor"));
        repType.setCellValueFactory(new PropertyValueFactory<Report, String>("repType"));
        repService.setCellValueFactory(new PropertyValueFactory<Report, String>("repService"));
        repStatus.setCellValueFactory(new PropertyValueFactory<Report, String>("repStatus"));
        repDocCode.setCellValueFactory(new PropertyValueFactory<Report, String>("repDocCode"));

        typeLabelCol.setOnEditCommit(
                (EventHandler<CellEditEvent<SimpleType, String>>) t -> {
                    SimpleType simpleType = t.getTableView().getItems().get(
                            t.getTablePosition().getRow());
                    simpleType.setLabel(t.getNewValue());
                    dbService.saveOrUpdate(new Type(simpleType.getId(), simpleType.getLabel(), simpleType.getDescription(), simpleType.getAuthor()));
                }
        );

        typeDescCol.setOnEditCommit(
                (EventHandler<CellEditEvent<SimpleType, String>>) t -> {
                    SimpleType simpleType = t.getTableView().getItems().get(
                            t.getTablePosition().getRow());
                    simpleType.setDescription(t.getNewValue());
                    dbService.saveOrUpdate(new Type(simpleType.getId(), simpleType.getLabel(), simpleType.getDescription(), simpleType.getAuthor()));
                }
        );

        statusCodeCol.setOnEditCommit(
                (EventHandler<CellEditEvent<SimpleStatusCode, Integer>>) t -> {
                    SimpleStatusCode simpleStatusCode = t.getTableView().getItems().get(
                            t.getTablePosition().getRow());
                    simpleStatusCode.setCode(t.getNewValue());
                    dbService.saveOrUpdate(new StatusCode(simpleStatusCode.getId(), simpleStatusCode.getCode(), simpleStatusCode.getName(), simpleStatusCode.getAuthor()));
                }
        );

        statusCodeCol.setOnEditCommit(
                (EventHandler<CellEditEvent<SimpleStatusCode, String>>) t -> {
                    SimpleStatusCode simpleStatusCode = t.getTableView().getItems().get(
                            t.getTablePosition().getRow());
                    simpleStatusCode.setName(t.getNewValue());
                    dbService.saveOrUpdate(new StatusCode(simpleStatusCode.getId(), simpleStatusCode.getCode(), simpleStatusCode.getName(), simpleStatusCode.getAuthor()));
                }
        );

        storeTypeIdCol.setOnEditCommit(
                (EventHandler<CellEditEvent<SimpleStore, Long>>) t -> {
                    SimpleStore simpleStore = t.getTableView().getItems().get(
                            t.getTablePosition().getRow());
                    simpleStore.setTypeId(t.getNewValue());
                    dbService.saveOrUpdate(new Store(simpleStore.getId(), simpleStore.getTypeId(), simpleStore.getServiceId(),
                            simpleStore.getStatusId(), simpleStore.getUrl(), simpleStore.getNamespace(), simpleStore.getDocCode(), simpleStore.getAuthor()));
                }
        );

        storeServiceIdCol.setOnEditCommit(
                (EventHandler<CellEditEvent<SimpleStore, Long>>) t -> {
                    SimpleStore simpleStore = t.getTableView().getItems().get(
                            t.getTablePosition().getRow());
                    simpleStore.setServiceId(t.getNewValue());
                    dbService.saveOrUpdate(new Store(simpleStore.getId(), simpleStore.getTypeId(), simpleStore.getServiceId(),
                            simpleStore.getStatusId(), simpleStore.getUrl(), simpleStore.getNamespace(), simpleStore.getDocCode(), simpleStore.getAuthor()));
                }
        );

        storeStatusIdCol.setOnEditCommit(
                (EventHandler<CellEditEvent<SimpleStore, Long>>) t -> {
                    SimpleStore simpleStore = t.getTableView().getItems().get(
                            t.getTablePosition().getRow());
                    simpleStore.setStatusId(t.getNewValue());
                    dbService.saveOrUpdate(new Store(simpleStore.getId(), simpleStore.getTypeId(), simpleStore.getServiceId(),
                            simpleStore.getStatusId(), simpleStore.getUrl(), simpleStore.getNamespace(), simpleStore.getDocCode(), simpleStore.getAuthor()));
                }
        );

        storeUrlCol.setOnEditCommit(
                (EventHandler<CellEditEvent<SimpleStore, String>>) t -> {
                    SimpleStore simpleStore = t.getTableView().getItems().get(
                            t.getTablePosition().getRow());
                    simpleStore.setUrl(t.getNewValue());
                    dbService.saveOrUpdate(new Store(simpleStore.getId(), simpleStore.getTypeId(), simpleStore.getServiceId(),
                            simpleStore.getStatusId(), simpleStore.getUrl(), simpleStore.getNamespace(), simpleStore.getDocCode(), simpleStore.getAuthor()));
                }
        );

        storeNamespaceCol.setOnEditCommit(
                (EventHandler<CellEditEvent<SimpleStore, String>>) t -> {
                    SimpleStore simpleStore = t.getTableView().getItems().get(
                            t.getTablePosition().getRow());
                    simpleStore.setNamespace(t.getNewValue());
                    dbService.saveOrUpdate(new Store(simpleStore.getId(), simpleStore.getTypeId(), simpleStore.getServiceId(),
                            simpleStore.getStatusId(), simpleStore.getUrl(), simpleStore.getNamespace(), simpleStore.getDocCode(), simpleStore.getAuthor()));
                }
        );

        storeDocCodeCol.setOnEditCommit(
                (EventHandler<CellEditEvent<SimpleStore, String>>) t -> {
                    SimpleStore simpleStore = t.getTableView().getItems().get(
                            t.getTablePosition().getRow());
                    simpleStore.setDocCode(t.getNewValue());
                    dbService.saveOrUpdate(new Store(simpleStore.getId(), simpleStore.getTypeId(), simpleStore.getServiceId(),
                            simpleStore.getStatusId(), simpleStore.getUrl(), simpleStore.getNamespace(), simpleStore.getDocCode(), simpleStore.getAuthor()));
                }
        );

        userLoginCol.setOnEditCommit(
                (EventHandler<CellEditEvent<SimpleUser, String>>) t -> {
                    SimpleUser simpleUser = t.getTableView().getItems().get(
                            t.getTablePosition().getRow());
                    simpleUser.setLogin(t.getNewValue());
                    dbService.saveOrUpdate(new User(simpleUser.getId(), simpleUser.getLogin(), simpleUser.getName(), simpleUser.getLogin()));
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
                    dbService.saveOrUpdate(new Service(simpleService.getId(), simpleService.getServiceCode(), simpleService.getAuthor()));
                }
        );

        statusTable.setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode().equals(KeyCode.DELETE)) {
                final SimpleStatusCode selected = statusTable.getSelectionModel().getSelectedItem();
                dbService.deleteStatus(selected.getId());

                clearStatusText();
                statusTable.setItems(convertService.convertStatus(dbService.findAllStatuses()));
            }
        });

        storeTable.setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode().equals(KeyCode.DELETE)) {
                final SimpleStore selected = storeTable.getSelectionModel().getSelectedItem();
                dbService.deleteStore(selected.getId());

                clearStoreText();
                storeTable.setItems(convertService.convertStore(dbService.findAllStores()));
            }
        });

        typeTable.setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode().equals(KeyCode.DELETE)) {
                final SimpleType selected = typeTable.getSelectionModel().getSelectedItem();
                dbService.deleteType(selected.getId());

                clearTypeText();
                typeTable.setItems(convertService.convertType(dbService.findAllTypes()));
            }
        });

        userTable.setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode().equals(KeyCode.DELETE)) {
                final SimpleUser selected = userTable.getSelectionModel().getSelectedItem();
                dbService.deleteUser(selected.getId());

                clearUserText();
                userTable.setItems(convertService.convertUser(dbService.findAllUsers()));
            }
        });

        serviceTable.setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode().equals(KeyCode.DELETE)) {
                final SimpleService selected = serviceTable.getSelectionModel().getSelectedItem();
                dbService.deleteService(selected.getId());

                clearServiceText();
                serviceTable.setItems(convertService.convertService(dbService.findAllServices()));
            }
        });
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
