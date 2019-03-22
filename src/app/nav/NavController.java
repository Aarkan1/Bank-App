package app.nav;

import app.Entities.CT;
import app.account.accountSettings.AccountSettingsController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

import java.io.IOException;

public class NavController {
    AccountSettingsController accountSettingsController;

    @FXML
    public void loadHome() {
        CT.homeController.loadAllAccounts();
    }

    @FXML
    void loadNewTransactions() {
        changeHomeCenter("/app/transaction/newTransaction.fxml");
    }

    @FXML
    void loadAccountSettings() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/app/account/accountSettings/accountSettings.fxml"));
            Parent fxmlInstance = loader.load();
            Scene scene = new Scene(fxmlInstance);

            accountSettingsController = loader.getController();

            CT.homeController.setCenter(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
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
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlRoute));
            Parent fxmlInstance = loader.load();
            Scene scene = new Scene(fxmlInstance);

            accountSettingsController.setAccountSettings(scene);
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
    void changeNameSettingsScreen() {
        CT.setChangeNameSettingsController(loadAccountSettingsController("/app/account/accountSettings/changeNameSettings.fxml"));
    }

    @FXML
    void changeTypeSettingsScreen() {
        CT.setChangeTypeSettingsController(loadAccountSettingsController("/app/account/accountSettings/changeTypeSettings.fxml"));
    }

    @FXML
    void createNewSettingsScreen() {
        CT.setCreateNewSettingsController(loadAccountSettingsController("/app/account/accountSettings/createNewSettings.fxml"));
    }

    @FXML
    void deleteSettingsScreen() {
        CT.setDeleteSettingsController(loadAccountSettingsController("/app/account/accountSettings/deleteSettings.fxml"));
    }

}
