package app.home;

import app.Main;
import app.login.LoginController;
import app.nav.NV;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;

public class HomeController {

    @FXML
    public BorderPane homePane;

    @FXML
    void initialize() {
        loadNavbar();
        loadAllAccounts();

        System.out.println(LoginController.getUser());
    }

    @FXML
    void logout() {
        Main.stage.setScene(NV.get().loadFXML("/app/login/login.fxml"));
        Main.stage.show();
    }

    private void loadNavbar() {
        Scene scene = NV.get().loadFXML("/app/nav/nav.fxml");
        homePane.setLeft(scene.getRoot());
        BorderPane.setAlignment(homePane.getLeft(), Pos.TOP_CENTER);
    }

    public void loadAllAccounts() {
        setCenter(NV.get().loadFXML("/app/account/allAccounts.fxml"));
        loadLastTransactions();
    }

    public void setCenter(Scene scene) {
        homePane.setCenter(scene.getRoot());
        BorderPane.setAlignment(homePane.getCenter(), Pos.TOP_CENTER);
    }

    private void loadLastTransactions() {
        homePane.setRight(NV.get().loadFXML("/app/transaction/displayLastTransactions.fxml").getRoot());
    }
}
