package app.home;

import app.Entities.Account;
import app.Main;
import app.account.AccountController;
import app.db.DB;
import app.login.LoginController;
import app.transaction.TransactionController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;

import java.io.IOException;
import java.util.List;

public class HomeController {

    @FXML
    BorderPane homePane;

    @FXML
    void initialize() {
        // load accounts from db using LoginController.user.getId() and display them
        System.out.println("initialize home");

        loadNavbar();
        loadAllAccounts();

        System.out.println(LoginController.getUser());
        System.out.println("User: " + LoginController.getUser().getId());

        DB.cardPay(1111222233334444L, 852147483647L, 20.5f);
    }

    void loadNavbar() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/app/nav/nav.fxml"));
        Parent fxmlNav = null;
        try {
            fxmlNav = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
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

        homePane.setCenter(scene.getRoot());
        BorderPane.setAlignment(homePane.getCenter(), Pos.TOP_CENTER);
    }

    @FXML
    void goToAccount() throws IOException {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/app/account/account.fxml"));
        Parent fxmlInstance = loader.load();
        Scene scene = new Scene(fxmlInstance, 800, 600);

        // Make sure that you display "the correct account" based on which one you clicked on
//            AccountController controller = loader.getController();
//            controller.setAccount(accountFromDB);

        // If you don't want to have/use the static variable Main.stage
//        Stage window = (Stage)((Node)e.getSource()).getScene().getWindow();
        Main.stage.setScene(scene);
        Main.stage.show();

    }
}
