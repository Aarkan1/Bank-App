package app.account.accountSettings;

import app.Entities.Account;
import app.Entities.CT;
import app.account.AllAccountController;
import app.db.DB;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;

public class DeleteSettingsController {

    @FXML
    ChoiceBox<Account> accountToDelete;

    @FXML
    private void initialize() {
        fillAccountBoxes();
    }

    void fillAccountBoxes() {
        for (Account account : AllAccountController.accounts) {
            accountToDelete.getItems().add(account);
        }
        accountToDelete.getSelectionModel().selectFirst();
    }

    @FXML
    void deleteAccount(){
        DB.deleteAccount(accountToDelete.getValue().getAccountNr());
        CT.navController.loadHome();
    }

    @FXML
    void cancelDelete(){
        CT.navController.loadHome();
    }

}
