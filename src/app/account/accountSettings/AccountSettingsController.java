package app.account.accountSettings;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;

public class AccountSettingsController {

    @FXML
    BorderPane accountSettings;

    @FXML
    private void initialize() {

    }

    public void setAccountSettings(Scene scene) {
        accountSettings.setLeft(scene.getRoot());
    }

}
