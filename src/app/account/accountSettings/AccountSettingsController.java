package app.account.accountSettings;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;

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