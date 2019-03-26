package app.account.accountSettings;

import app.Entities.Account;
import app.Entities.CT;
import app.db.DB;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class ChangeNameSettingsController {

    @FXML
    TextField newName;
    @FXML
    ChoiceBox<Account> accountBox;
    @FXML
    Label errorMessage;

    @FXML
    private void initialize() {
        fillAccountBoxes();
    }

    private void fillAccountBoxes() {
        for (Account account : CT.accounts) {
            accountBox.getItems().add(account);
        }
        accountBox.getSelectionModel().selectFirst();
    }

    @FXML
    void submitChange() {

        String newNameText = newName.getText().trim();

        for (Account account : CT.accounts) {
            if (account.getName().equals(newNameText)) {
                errorMessage.setText("Namnet används redan");
                return;
            }
        }

        DB.changeAccountName(accountBox.getValue().getAccountNr(), newNameText);
        CT.navController.loadHome();
    }

    @FXML
    void cancelChange() {
        CT.navController.loadHome();
    }

}
