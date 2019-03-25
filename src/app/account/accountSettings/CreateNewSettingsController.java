package app.account.accountSettings;

import app.Entities.Account;
import app.Entities.CT;
import app.account.AllAccountController;
import app.db.DB;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class CreateNewSettingsController {

    @FXML
    TextField newName;
    @FXML
    ChoiceBox<String> chooseType;
    @FXML
    Label errorMessage;

    @FXML
    private void initialize() {
        chooseType.getItems().addAll("Sparkonto", "Kortkonto", "Lönkonto");
        chooseType.getSelectionModel().selectFirst();
    }

    @FXML
    void submitChange() {

        String newNameText = newName.getText().trim();

        for (Account account : AllAccountController.accounts) {
            if (account.getName().equals(newNameText)) {
                errorMessage.setText("Namnet används redan");
                return;
            }
        }

        DB.createNewAccount(newNameText, CT.getAccountType(chooseType.getValue()));
        CT.navController.loadHome();
    }

    @FXML
    void cancelChange() {
        CT.navController.loadHome();
    }


}
