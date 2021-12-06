package com.store;

import com.store.entity.Type;
import com.store.entity.simple_entity.SimpleType;
import com.store.service.ConvertService;
import com.store.service.DbService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.cell.TextFieldTableCell;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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

    @FXML
    public void initialize() {
        typeTable.setEditable(true);
        typeUpdate();
    }

    public void typeFind(ActionEvent event) {
        typeList = FXCollections.observableArrayList();

        Type type;

        if (!typeId.getText().equals(""))
            type = new Type(Long.valueOf(typeId.getText()), typeLabel.getText(), typeDesc.getText());
        else
            type = new Type(typeLabel.getText(), typeDesc.getText());

        typeTable.setItems(convertService.convertType(dbService.find(type)));
        clearTypeText();
    }

    public void typeUpdate() {
        typeIdCol.setCellValueFactory(new PropertyValueFactory<Type, Long>("id"));
        typeLabelCol.setCellValueFactory(new PropertyValueFactory<Type, String>("label"));
        typeDescCol.setCellValueFactory(new PropertyValueFactory<Type, String>("description"));

        typeLabelCol.setCellFactory(TextFieldTableCell.forTableColumn());
        typeLabelCol.setOnEditCommit(
                (EventHandler<CellEditEvent<SimpleType, String>>) t -> {
                    SimpleType simpleType = t.getTableView().getItems().get(
                            t.getTablePosition().getRow());
                    simpleType.setLabel(t.getNewValue());
                    dbService.saveOrUpdate(new Type(simpleType.getId(), simpleType.getLabel(), simpleType.getDescription()));
                }
        );

        typeDescCol.setCellFactory(TextFieldTableCell.forTableColumn());
        typeDescCol.setOnEditCommit(
                (EventHandler<CellEditEvent<SimpleType, String>>) t -> {
                    SimpleType simpleType = t.getTableView().getItems().get(
                            t.getTablePosition().getRow());
                    simpleType.setDescription(t.getNewValue());
                    dbService.saveOrUpdate(new Type(simpleType.getId(), simpleType.getLabel(), simpleType.getDescription()));
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
        typeTable.setItems(convertService.convertType(dbService.findAll()));
    }

    public void typeDelete(ActionEvent event) {
        if (typeId.getText().matches("\\d+"))
            dbService.delete(Long.valueOf(typeId.getText()));
        else {
            new Alert(Alert.AlertType.NONE, "Поле ID должно быть заполнено числом", new ButtonType("Закрыть")).showAndWait();
        }

        clearTypeText();
        typeTable.setItems(convertService.convertType(dbService.findAll()));
    }

    public void typeClear(ActionEvent event) {
        clearTypeText();
    }

    public void clearTypeText() {
        typeId.setText("");
        typeLabel.setText("");
        typeDesc.setText("");
    }

}
