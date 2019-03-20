package app.login;


import app.Entities.User;
import app.Main;
import app.db.DB;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;

import java.io.IOException;

public class LoginController {

    @FXML
    TextField input_email;
    @FXML
    TextField input_password;
    @FXML
    Label errorMessage;

    // Use this in other Controllers to get "the currently logged in user".
    private static User user = null;

    public static User getUser() {
        return user;
    }

    @FXML
    private void initialize() {
        System.out.println("initialize login");
        errorMessage.setTextFill(Color.RED);
    }

    @FXML
    void login() {
        if (!input_email.getText().matches("^[\\w-]{2,}@[a-z]{2,8}\\.[a-z]{2,8}[\\.a-z]*$") ||
                !input_password.getText().matches("^[\\w-@!#%&?]{8,20}$")) {
            errorMessage.setText("E-mail or password is wrong");
        } else {
            user = DB.getMatchingUser(input_email.getText(), input_password.getText());
            // if null display error
            if (user == null) {
                errorMessage.setText("E-mail or password is wrong");
                // else switchScene to Home
            } else {
                switchScene("/app/home/home.fxml");
            }
        }
    }

    void switchScene(String pathname) {
        try {
            Parent bla = FXMLLoader.load(getClass().getResource(pathname));
            Scene scene = new Scene(bla, 600, 400);
            Main.stage.setScene(scene);
            Main.stage.show();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

    @FXML
    void goToHome() {
        switchScene("/app/home/home.fxml");
    }
}
