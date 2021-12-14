package com.store;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.store.entity.*;
import com.store.entity.simple_entity.*;
import com.store.service.ConvertService;
import com.store.service.DbService;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.KeyCode;
import javafx.util.converter.LongStringConverter;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.*;
import java.text.DecimalFormat;
import java.text.ParsePosition;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@FxmlView("db.fxml")
public class DbController {

    static String arial = "C:\\Windows\\Fonts\\Arial.ttf";
    static BaseFont bf;

    static {
        try {
            bf = BaseFont.createFont(arial, BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static Font arialFontBig = new Font(bf, 30, Font.NORMAL);
    static Font arialFont = new Font(bf, 14, Font.NORMAL);

    String userLoginForm;

    @Autowired private DbService dbService;
    @Autowired private ConvertService convertService;

    // Type Properties
    @FXML TextField typeId;
    @FXML TextField typeLabel;
    @FXML TextField typeDesc;
    @FXML TextField typeAuthor;
    @FXML TableView<SimpleType> typeTable;
    @FXML TableColumn typeLabelCol;
    @FXML TableColumn typeDescCol;
    @FXML TableColumn typeAuthorCol;

    // Service Properties
    @FXML TextField serviceId;
    @FXML TextField serviceCode;
    @FXML TextField serviceAuthor;
    @FXML TableView<SimpleService> serviceTable;
    @FXML TableColumn serviceCodeCol;
    @FXML TableColumn serviceAuthorCol;

    // Status Properties
    @FXML TextField statusId;
    @FXML TextField statusCode;
    @FXML TextField statusName;
    @FXML TextField statusAuthor;
    @FXML TableView<SimpleStatusCode> statusTable;
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
    @FXML TableColumn userLoginCol;
    @FXML TableColumn userNameCol;

    // Report properties
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

    public void getReport() throws SQLException, IOException, InterruptedException {
        List<Report> reports = new ArrayList<>();
        String query = "select s.url, s.namespace, s.author, count(t.label), count(s2.service_code),\n" +
                "       count(sc.name), count(s.doc_code)\n" +
                "    from store s\n" +
                "        join type t on s.type_id = t.id\n" +
                "        join service s2 on s2.id = s.service_id\n" +
                "        join status_code sc on sc.id = s.status_id\n" +
                " %s " +
                "    group by s.author, s.url, s.namespace\n" +
                "    order by s.url";

        String bothDates = "where s.created > '" + dateFrom.getValue() + "' and s.created < '" + dateTo.getValue() + "'\n";

        if (dateFrom.getValue() != null && dateTo.getValue() != null) {
            query = String.format(query, bothDates);
        }
        else {
            new Alert(Alert.AlertType.NONE, "Заполните обе даты", new ButtonType("Закрыть")).showAndWait();
            return;
        }

        Connection con = DriverManager.getConnection("jdbc:postgresql://localhost:5432/xsd_store",
                "postgres", "1");
        Statement statement = con.createStatement();
        ResultSet rs = statement.executeQuery(query);

        while (rs.next()) {
            reports.add(new Report(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4),
                    rs.getString(5), rs.getString(6), rs.getString(7)));
        }

        rs = statement.executeQuery("select count(*) from type");
        rs.next();
        long countTypes = rs.getLong(1);

        rs = statement.executeQuery("select count(*) from service");
        rs.next();
        long countServices = rs.getLong(1);

        rs = statement.executeQuery("select count(*) from status_code");
        rs.next();
        long countStatuses = rs.getLong(1);

        statement.close();
        con.close();

        repTable.setItems(convertService.convertReport(reports));
        dateFrom.setValue(null);
        dateTo.setValue(null);

        String file = "otchet.pdf";

        try {
            Document document = new Document();
            PdfWriter.getInstance(document, new FileOutputStream(file));
            document.open();
            addMetaData(document);
            addTitlePage(document, reports, countTypes, countServices, countStatuses);
            document.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if ((new File("D:\\IdeaProjects\\javafx-db\\otchet.pdf")).exists()) {
            Process p = Runtime
                    .getRuntime()
                    .exec("rundll32 url.dll,FileProtocolHandler D:\\IdeaProjects\\javafx-db\\otchet.pdf");
            p.waitFor();

        } else {
            System.out.println("File is not exists");
        }

    }

    private static void addMetaData(Document document) {
        document.addTitle("Отчет ");
        document.addAuthor("Свяжин Н. О.");
        document.addCreator("Свяжин Н. О.");
    }

    private static void addTitlePage(Document document, List<Report> reports, long types, long services, long statuses)
            throws DocumentException {
        Paragraph preface = new Paragraph();
        addEmptyLine(preface, 3);
        Paragraph paragraph = new Paragraph("Отчет по документообороту", arialFontBig);
        paragraph.setAlignment(Element.ALIGN_CENTER);
        preface.add(paragraph);

        PdfPTable table = new PdfPTable(5);

        PdfPCell c1 = new PdfPCell(new Phrase("Кол. типов", arialFont));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(c1);

        PdfPCell c2 = new PdfPCell(new Phrase("Кол. статусов", arialFont));
        c2.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(c2);

        PdfPCell c3 = new PdfPCell(new Phrase("Кол. услуг", arialFont));
        c3.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(c3);

        PdfPCell c4 = new PdfPCell(new Phrase("URL", arialFont));
        c4.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(c4);

        PdfPCell c5 = new PdfPCell(new Phrase("Неймспейс", arialFont));
        c5.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(c5);

        table.setHeaderRows(1);

        Map<String, Map<String, List<Report>>> collect = reports.stream()
                .collect(Collectors.groupingBy(Report::getRepAuthor, Collectors.groupingBy(Report::getRepUrl)));

        for (String a : collect.keySet()) {
            PdfPCell cell = new PdfPCell(new Phrase("Автор: " + a, arialFont));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setColspan(5);
            table.addCell(cell);

            for (String u : collect.get(a).keySet()) {
                Report report = collect.get(a).get(u).get(0);

                PdfPCell cell1 = new PdfPCell(new Phrase(report.getRepType(), arialFont));
                cell1.setHorizontalAlignment(Element.ALIGN_CENTER);

                PdfPCell cell2 = new PdfPCell(new Phrase(report.getRepStatus(), arialFont));
                cell2.setHorizontalAlignment(Element.ALIGN_CENTER);

                PdfPCell cell3 = new PdfPCell(new Phrase(report.getRepService(), arialFont));
                cell3.setHorizontalAlignment(Element.ALIGN_CENTER);

                PdfPCell cell4 = new PdfPCell(new Phrase(report.getRepUrl(), arialFont));
                cell4.setHorizontalAlignment(Element.ALIGN_CENTER);

                PdfPCell cell5 = new PdfPCell(new Phrase(report.getRepNamespace(), arialFont));
                cell5.setHorizontalAlignment(Element.ALIGN_CENTER);

                table.addCell(cell1);
                table.addCell(cell2);
                table.addCell(cell3);
                table.addCell(cell4);
                table.addCell(cell5);
           }
        }

        PdfPCell cell6 = new PdfPCell(new Phrase("Всего типов: " + types, arialFont));
        cell6.setColspan(5);

        PdfPCell cell7 = new PdfPCell(new Phrase("Всего статусов: " + statuses, arialFont));
        cell7.setColspan(5);

        PdfPCell cell8 = new PdfPCell(new Phrase("Всего услуг: " + services, arialFont));
        cell8.setColspan(5);

        table.addCell(cell6);
        table.addCell(cell7);
        table.addCell(cell8);

        document.add(preface);

        Paragraph p = new Paragraph();
        addEmptyLine(p, 3);

        document.add(p);
        document.add(table);
    }

    private static void addEmptyLine(Paragraph paragraph, int number) {
        for (int i = 0; i < number; i++) {
            paragraph.add(new Paragraph(" "));
        }
    }

    public void typeFind() {
        typeTable.setItems(convertService.convertType(
                dbService.find(new Type(parseToLong(typeId.getText()), typeLabel.getText(), typeDesc.getText(), typeAuthor.getText()))));
        clearTypeText();
    }

    public void serviceFind() {
        serviceTable.setItems(convertService.convertService(
                dbService.find(new Service(parseToLong(serviceId.getText()), serviceCode.getText(), serviceAuthor.getText()))));
        clearServiceText();
    }

    public void storeFind() {
        storeTable.setItems(convertService.convertStore(
                dbService.find(new Store(parseToLong(storeId.getText()),
                        parseToLong(storeTypeId.getText()), parseToLong(storeServiceId.getText()),
                        parseToLong(storeStatusId.getText()), storeUrl.getText(), storeNamespace.getText(), storeDocCode.getText(), storeAuthor.getText()))));
        clearServiceText();
    }

    public void userFind() {
        userTable.setItems(convertService.convertUser(
                dbService.find(new User(parseToLong(userId.getText()), userLogin.getText(), userName.getText(), ""))));
        clearServiceText();
    }

    public void statusFind() {
        statusTable.setItems(convertService.convertStatus(
                dbService.find(new StatusCode(parseToLong(statusId.getText()), parseToLong(statusCode.getText()), statusName.getText(), statusAuthor.getText()))));
        clearServiceText();
    }

    public void typeAdd() {
        if (typeLabel.getText().equals("") || typeDesc.getText().equals("")) {
            new Alert(Alert.AlertType.NONE, "Поле 'Название типа' и 'Описание' должны быть заполнены", new ButtonType("Закрыть")).showAndWait();
            return;
        }

        try {
            dbService.saveOrUpdate(new Type(typeLabel.getText(), typeDesc.getText(), userLoginForm));
        }
        catch (DataAccessException d) {
            showDbError(d);
            return;
        }
        clearTypeText();
        typeTable.setItems(convertService.convertType(dbService.findAllTypes()));
    }

    public void statusAdd() {
        if (statusCode.getText().equals("") || statusName.getText().equals("")) {
            new Alert(Alert.AlertType.NONE, "Поле 'Код' и 'Название статуса' должны быть заполнены", new ButtonType("Закрыть")).showAndWait();
            return;
        }

        try {
            dbService.saveOrUpdate(new StatusCode(parseToLong(statusCode.getText()), statusName.getText(), userLoginForm));
        }
        catch (DataAccessException d) {
            showDbError(d);
            return;
        }
        clearStatusText();
        statusTable.setItems(convertService.convertStatus(dbService.findAllStatuses()));
    }

    public void storeAdd() {
        if (storeTypeId.getText().equals("") || storeUrl.getText().equals("")) {
            new Alert(Alert.AlertType.NONE, "Поле 'ID типа' и 'URL' должны быть заполнены", new ButtonType("Закрыть")).showAndWait();
            return;
        }

        try {
            dbService.saveOrUpdate(new Store(parseToLong(storeTypeId.getText()), parseToLong(storeServiceId.getText()),
                    parseToLong(storeStatusId.getText()), storeUrl.getText(), storeNamespace.getText(), storeDocCode.getText(), userLoginForm));
        }
        catch (DataAccessException d) {
            showDbError(d);
            return;
        }
        clearStoreText();
        storeTable.setItems(convertService.convertStore(dbService.findAllStores()));
    }

    public void userAdd() {
        if (userLogin.getText().equals("") || userName.getText().equals("") || userPassword.getText().equals("")) {
            new Alert(Alert.AlertType.NONE, "Поле 'Логин', 'Имя' и 'Пароль' должны быть заполнены", new ButtonType("Закрыть")).showAndWait();
            return;
        }
        try {
            dbService.saveOrUpdate(new User(userLogin.getText(), userName.getText(), userPassword.getText()));
        }
        catch (DataAccessException d) {
            showDbError(d);
            return;
        }
        clearUserText();
        userTable.setItems(convertService.convertUser(dbService.findAllUsers()));
    }

    public void serviceAdd() {
        if (serviceCode.getText().equals("")) {
            new Alert(Alert.AlertType.NONE, "Поле 'Код услуги' должно быть заполнено", new ButtonType("Закрыть")).showAndWait();
            return;
        }

        try {
            dbService.saveOrUpdate(new Service(serviceCode.getText(), userLoginForm));
        }
        catch (DataAccessException d) {
            showDbError(d);
            return;
        }
        clearServiceText();
        serviceTable.setItems(convertService.convertService(dbService.findAllServices()));
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

        typeLabelCol.setCellFactory(TextFieldTableCell.forTableColumn());
        typeLabelCol.setOnEditCommit(
                (EventHandler<CellEditEvent<SimpleType, String>>) t -> {
                    SimpleType simpleType = t.getTableView().getItems().get(
                            t.getTablePosition().getRow());
                    simpleType.setLabel(t.getNewValue());
                    dbService.saveOrUpdate(new Type(simpleType.getId(), simpleType.getLabel(), simpleType.getDescription(), simpleType.getAuthor()));
                }
        );

        typeDescCol.setCellFactory(TextFieldTableCell.forTableColumn());
        typeDescCol.setOnEditCommit(
                (EventHandler<CellEditEvent<SimpleType, String>>) t -> {
                    SimpleType simpleType = t.getTableView().getItems().get(
                            t.getTablePosition().getRow());
                    simpleType.setDescription(t.getNewValue());
                    dbService.saveOrUpdate(new Type(simpleType.getId(), simpleType.getLabel(), simpleType.getDescription(), simpleType.getAuthor()));
                }
        );

        statusCodeCol.setCellFactory(TextFieldTableCell.forTableColumn(new LongStringConverter()));
        statusCodeCol.setOnEditCommit(
                (EventHandler<CellEditEvent<SimpleStatusCode, Long>>) t -> {
                    SimpleStatusCode simpleStatusCode = t.getTableView().getItems().get(
                            t.getTablePosition().getRow());
                    simpleStatusCode.setCode(t.getNewValue());
                    dbService.saveOrUpdate(new StatusCode(simpleStatusCode.getId(), simpleStatusCode.getCode(), simpleStatusCode.getName(), simpleStatusCode.getAuthor()));
                }
        );

        statusNameCol.setCellFactory(TextFieldTableCell.forTableColumn());
        statusNameCol.setOnEditCommit(
                (EventHandler<CellEditEvent<SimpleStatusCode, String>>) t -> {
                    SimpleStatusCode simpleStatusCode = t.getTableView().getItems().get(
                            t.getTablePosition().getRow());
                    simpleStatusCode.setName(t.getNewValue());
                    dbService.saveOrUpdate(new StatusCode(simpleStatusCode.getId(), simpleStatusCode.getCode(), simpleStatusCode.getName(), simpleStatusCode.getAuthor()));
                }
        );

        storeTypeIdCol.setCellFactory(TextFieldTableCell.forTableColumn(new LongStringConverter()));
        storeTypeIdCol.setOnEditCommit(
                (EventHandler<CellEditEvent<SimpleStore, Long>>) t -> {
                    SimpleStore simpleStore = t.getTableView().getItems().get(
                            t.getTablePosition().getRow());
                    simpleStore.setTypeId(t.getNewValue());
                    dbService.saveOrUpdate(new Store(simpleStore.getId(), simpleStore.getTypeId(), simpleStore.getServiceId(),
                            simpleStore.getStatusId(), simpleStore.getUrl(), simpleStore.getNamespace(), simpleStore.getDocCode(), simpleStore.getAuthor()));
                }
        );

        storeServiceIdCol.setCellFactory(TextFieldTableCell.forTableColumn(new LongStringConverter()));
        storeServiceIdCol.setOnEditCommit(
                (EventHandler<CellEditEvent<SimpleStore, Long>>) t -> {
                    SimpleStore simpleStore = t.getTableView().getItems().get(
                            t.getTablePosition().getRow());
                    simpleStore.setServiceId(t.getNewValue());
                    dbService.saveOrUpdate(new Store(simpleStore.getId(), simpleStore.getTypeId(), simpleStore.getServiceId(),
                            simpleStore.getStatusId(), simpleStore.getUrl(), simpleStore.getNamespace(), simpleStore.getDocCode(), simpleStore.getAuthor()));
                }
        );

        storeStatusIdCol.setCellFactory(TextFieldTableCell.forTableColumn(new LongStringConverter()));
        storeStatusIdCol.setOnEditCommit(
                (EventHandler<CellEditEvent<SimpleStore, Long>>) t -> {
                    SimpleStore simpleStore = t.getTableView().getItems().get(
                            t.getTablePosition().getRow());
                    simpleStore.setStatusId(t.getNewValue());
                    dbService.saveOrUpdate(new Store(simpleStore.getId(), simpleStore.getTypeId(), simpleStore.getServiceId(),
                            simpleStore.getStatusId(), simpleStore.getUrl(), simpleStore.getNamespace(), simpleStore.getDocCode(), simpleStore.getAuthor()));
                }
        );

        storeUrlCol.setCellFactory(TextFieldTableCell.forTableColumn());
        storeUrlCol.setOnEditCommit(
                (EventHandler<CellEditEvent<SimpleStore, String>>) t -> {
                    SimpleStore simpleStore = t.getTableView().getItems().get(
                            t.getTablePosition().getRow());
                    simpleStore.setUrl(t.getNewValue());
                    dbService.saveOrUpdate(new Store(simpleStore.getId(), simpleStore.getTypeId(), simpleStore.getServiceId(),
                            simpleStore.getStatusId(), simpleStore.getUrl(), simpleStore.getNamespace(), simpleStore.getDocCode(), simpleStore.getAuthor()));
                }
        );

        storeNamespaceCol.setCellFactory(TextFieldTableCell.forTableColumn());
        storeNamespaceCol.setOnEditCommit(
                (EventHandler<CellEditEvent<SimpleStore, String>>) t -> {
                    SimpleStore simpleStore = t.getTableView().getItems().get(
                            t.getTablePosition().getRow());
                    simpleStore.setNamespace(t.getNewValue());
                    dbService.saveOrUpdate(new Store(simpleStore.getId(), simpleStore.getTypeId(), simpleStore.getServiceId(),
                            simpleStore.getStatusId(), simpleStore.getUrl(), simpleStore.getNamespace(), simpleStore.getDocCode(), simpleStore.getAuthor()));
                }
        );

        storeDocCodeCol.setCellFactory(TextFieldTableCell.forTableColumn());
        storeDocCodeCol.setOnEditCommit(
                (EventHandler<CellEditEvent<SimpleStore, String>>) t -> {
                    SimpleStore simpleStore = t.getTableView().getItems().get(
                            t.getTablePosition().getRow());
                    simpleStore.setDocCode(t.getNewValue());
                    dbService.saveOrUpdate(new Store(simpleStore.getId(), simpleStore.getTypeId(), simpleStore.getServiceId(),
                            simpleStore.getStatusId(), simpleStore.getUrl(), simpleStore.getNamespace(), simpleStore.getDocCode(), simpleStore.getAuthor()));
                }
        );

        userLoginCol.setCellFactory(TextFieldTableCell.forTableColumn());
        userLoginCol.setOnEditCommit(
                (EventHandler<CellEditEvent<SimpleUser, String>>) t -> {
                    SimpleUser simpleUser = t.getTableView().getItems().get(
                            t.getTablePosition().getRow());
                    simpleUser.setLogin(t.getNewValue());
                    dbService.saveOrUpdate(new User(simpleUser.getId(), simpleUser.getLogin(), simpleUser.getName(), simpleUser.getLogin()));
                }
        );

        userNameCol.setCellFactory(TextFieldTableCell.forTableColumn());
        userNameCol.setOnEditCommit(
                (EventHandler<CellEditEvent<SimpleUser, String>>) t -> {
                    SimpleUser simpleUser = t.getTableView().getItems().get(
                            t.getTablePosition().getRow());
                    simpleUser.setName(t.getNewValue());
                    dbService.saveOrUpdate(new User(simpleUser.getId(), simpleUser.getLogin(), simpleUser.getName()));
                }
        );

        serviceCodeCol.setCellFactory(TextFieldTableCell.forTableColumn());
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

    public void showDbError(DataAccessException d) {
        String error = d.getRootCause().toString();
        new Alert(Alert.AlertType.NONE, "Ошибка: " + error.substring(error.indexOf("Подробности: ") + 13), new ButtonType("Закрыть")).showAndWait();
    }

}
