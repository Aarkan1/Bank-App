package app.nav;

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
        changeHomeCenter("/app/transaction/newTransaction.fxml");
    }

    @FXML
    void loadStartSavings() {
        changeHomeCenter("/app/transaction/startMonthlySaving.fxml");
    }

    @FXML
    void loadAutogiro() {
        changeHomeCenter("/app/transaction/startAutogiro.fxml");
    }

    @FXML
    void loadAddAccount() {
        changeHomeCenter("/app/account/addNewAccount.fxml");
    }

    @FXML
    void loadAccountSettings() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/app/account/accountSettings/accountSettings.fxml"));
            Parent fxmlInstance = loader.load();
            Scene scene = new Scene(fxmlInstance);

            CT.homeController.setCenter(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }

        changeNameSettingsScreen();
    }

    Scene loadFXML(String fxmlRoute) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlRoute));
            Parent fxmlInstance = loader.load();
            return new Scene(fxmlInstance);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    <T> T loadAccountSettingsController(String fxmlRoute) {
        loadAccountSettings();
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlRoute));
            Parent fxmlInstance = loader.load();
            Scene scene = new Scene(fxmlInstance);

            CT.homeController.setCenter(scene);

            return loader.getController();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    void changeHomeCenter(String fxmlRoute) {
        CT.homeController.setCenter(loadFXML(fxmlRoute));
    }

    @FXML
    public void changeNameSettingsScreen() {
        changeHomeCenter("/app/account/accountSettings/changeNameSettings.fxml");
    }

    @FXML
    public void changeTypeSettingsScreen() {
        changeHomeCenter("/app/account/accountSettings/changeTypeSettings.fxml");
    }

    @FXML
    public void createNewSettingsScreen() {
        changeHomeCenter("/app/account/accountSettings/createNewSettings.fxml");
    }

    @FXML
    public void deleteSettingsScreen() {
        changeHomeCenter("/app/account/accountSettings/deleteSettings.fxml");
    }

}
