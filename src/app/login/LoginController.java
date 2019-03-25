package app.login;


import app.Entities.CT;
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
    TextField input_userID;
    @FXML
    TextField input_password;
    @FXML
    Label loginError;

    // Use this in other Controllers to get "the currently logged in user".
    private static User user = null;

    public static User getUser() {
        return user;
    }

    @FXML
    private void initialize() {
        System.out.println("initialize login");
        loginError.setTextFill(Color.RED);
    }

    @FXML
    void login() {
        if (!input_userID.getText().matches("^[\\d]{6}-[\\d]{4}$") ||
                !input_password.getText().matches("^[\\w-@!#%&?]{8,20}$")) {
            loginError.setText("Personnr eller lösenord är fel");
        } else {
            user = DB.getMatchingUser(input_userID.getText(), input_password.getText());
            // if null display error
            if (user == null) {
                loginError.setText("Personnr eller lösenord är fel");
                // else switchScene to Home
            } else {
                switchScene("/app/home/home.fxml");
            }
        }
    }

    void switchScene(String pathname) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(pathname));
            Parent fxmlHome = loader.load();

            CT.setHomeController(loader.getController());

            Scene scene = new Scene(fxmlHome, 700, 500);
            Main.stage.setScene(scene);
            Main.stage.show();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }
}
