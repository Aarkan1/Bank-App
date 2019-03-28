package app.account.accountSettings;

import app.Entities.Account;
import app.Entities.CT;
import app.db.DB;
import app.nav.NV;
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

    private void fillAccountBoxes() {
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
        NV.get().loadHome();
    }

    @FXML
    void cancelDelete() {
        NV.get().loadHome();
    }

}
