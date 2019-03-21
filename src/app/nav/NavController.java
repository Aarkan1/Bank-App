package app.nav;

import app.Entities.Account;
import app.Entities.CT;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

import java.io.IOException;

public class NavController {

    @FXML
    public void loadHome() {
        CT.homeController.loadAllAccounts();
    }

    @FXML
    void loadNewTransactions() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/app/transaction/newTransaction.fxml"));
            Parent fxmlInstance = loader.load();
            Scene scene = new Scene(fxmlInstance);

            CT.homeController.setCenter(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
