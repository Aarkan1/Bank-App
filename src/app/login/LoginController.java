package app.login;


import app.Entities.User;
import app.Main;
import app.db.DB;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;

import java.io.IOException;

public class LoginController {

    @FXML
    TextField input_username;
    @FXML
    TextField input_password;

    // Use this in other Controllers to get "the currently logged in user".
    private static User user = null;

    public static User getUser() {
        return user;
    }

    @FXML
    private void initialize() {
        System.out.println("initialize login");
    }

    @FXML
    void login() {

//        user = DB.getMatchingUser("Kalle", "abc123?");

        user = DB.getMatchingUser(input_username.getText(), input_password.getText());
        // if null display error
        if (user == null) {
            System.err.println("ERROR: User doesn't exist");
            // else switchScene to Home
        } else {
            switchScene("/app/home/home.fxml");
        }
    }

    void switchScene(String pathname) {
        try {
            Parent bla = FXMLLoader.load(getClass().getResource(pathname));
            Scene scene = new Scene(bla, 800, 600);
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
