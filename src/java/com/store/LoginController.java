package com.store;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import net.rgielen.fxweaver.core.FxWeaver;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.stereotype.Component;

@Component
@FxmlView("login.fxml")
public class LoginController {

    private final String login = "1";
    private final String password = "1";

    @FXML TextField textLogin;
    @FXML PasswordField passwordLogin;

    @FXML
    public void initialize() {
        // Этап инициализации JavaFX
        textLogin.setEditable(true);
        passwordLogin.setEditable(true);

        textLogin.setText(login);
        passwordLogin.setText(password);
    }

    public void loginHandle(ActionEvent event) {
        if (!textLogin.getText().equals(login) || !passwordLogin.getText().equals(password)) {
            new Alert(Alert.AlertType.NONE, "Неправильный логин или пароль", new ButtonType("Закрыть")).showAndWait();
            return;
        }

        Stage stage = new Stage();
        stage.setTitle("XSD Store");

        FxWeaver fxWeaver = JavaFxApplication.applicationContext.getBean(FxWeaver.class);
        Parent root = fxWeaver.loadView(DbController.class);
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();

        ((Node) (event.getSource())).getScene().getWindow().hide();
    }


}
