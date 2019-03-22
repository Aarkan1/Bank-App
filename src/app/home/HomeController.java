package app.home;

import app.Entities.CT;
import app.Main;
import app.db.DB;
import app.login.LoginController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;

import java.io.IOException;

public class HomeController {

    @FXML
    public BorderPane homePane;

    @FXML
    void initialize() {
        // load accounts from db using LoginController.user.getId() and display them
        System.out.println("initialize home");

        loadNavbar();
        loadAllAccounts();

        System.out.println(LoginController.getUser());

//        DB.changeAccountType(852147483647L,"salary-account");

//        test for paying with card
//        DB.cardPay(1111222233334444L, 852147483647L, 20.5f);
    }

    void loadNavbar() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/app/nav/nav.fxml"));
        Parent fxmlNav = null;
        try {
            fxmlNav = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        CT.setNavController(loader.getController());

        Scene scene = new Scene(fxmlNav);
        homePane.setLeft(scene.getRoot());
        BorderPane.setAlignment(homePane.getLeft(), Pos.TOP_CENTER);
    }

    public void loadAllAccounts() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/app/account/allAccounts.fxml"));
        Parent fxmlAccounts = null;
        try {
            fxmlAccounts = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Scene scene = new Scene(fxmlAccounts);

        CT.setAllAccountController(loader.getController());

        setCenter(scene);
    }

    public void setCenter(Scene scene) {
        homePane.setCenter(scene.getRoot());
        BorderPane.setAlignment(homePane.getCenter(), Pos.TOP_CENTER);

    }
}
