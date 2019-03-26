package app.account.accountSettings;

import app.Entities.Account;
import app.Entities.CT;
import app.db.DB;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;

public class DeleteSettingsController {

    @FXML
    ChoiceBox<Account> accountToDelete;
    @FXML
    TextField reenterName;
    @FXML
    Label warningMessage;

    @FXML
    private void initialize() {
        fillAccountBoxes();
    }

    void fillAccountBoxes() {
        for (Account account : CT.accounts) {
            accountToDelete.getItems().add(account);
        }
        accountToDelete.getSelectionModel().selectFirst();
    }

    @FXML
    void deleteAccount() {
        if (!reenterName.getText().equals(accountToDelete.getValue().getName())) {
            warningMessage.setTextFill(Color.RED);
            warningMessage.setText("Namnet matchar inte kontot");
            return;
        }

        DB.deleteAccount(accountToDelete.getValue().getAccountNr());
        CT.navController.loadHome();
    }

    @FXML
    void cancelDelete() {
        CT.navController.loadHome();
    }

}
