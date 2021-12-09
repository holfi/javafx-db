package com.store;

import com.store.entity.User;
import com.store.repository.UserRepo;
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
import lombok.RequiredArgsConstructor;
import net.rgielen.fxweaver.core.FxWeaver;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@FxmlView("login.fxml")
@RequiredArgsConstructor
public class LoginController {

    private String login = "1";
    private String password = "1";

    public static Stage dbStage;
    public static Scene dbScene;

    @Autowired
    private final UserRepo userRepo;

    public static String userLogin;

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
        User user = userRepo.findByLogin(textLogin.getText());
        if (user == null)
            new Alert(Alert.AlertType.NONE, "Пользователь с таким логином не существует", new ButtonType("Закрыть")).showAndWait();
        if (!textLogin.getText().equals(user.getLogin()) || !passwordLogin.getText().equals(user.getPassword())) {
            new Alert(Alert.AlertType.NONE, "Неправильный логин или пароль", new ButtonType("Закрыть")).showAndWait();
            return;
        }

        userLogin = user.getName() == null ? textLogin.getText() : user.getName();

        Stage stage = new Stage();
        dbStage = stage;
        stage.setTitle("XSD Store");

        FxWeaver fxWeaver = JavaFxApplication.applicationContext.getBean(FxWeaver.class);
        Parent root = fxWeaver.loadView(DbController.class);
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();

        ((Node) (event.getSource())).getScene().getWindow().hide();
    }


}
