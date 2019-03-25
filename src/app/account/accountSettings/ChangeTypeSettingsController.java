package app.account.accountSettings;

import app.Entities.Account;
import app.Entities.CT;
import app.db.DB;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;

public class ChangeTypeSettingsController {

    @FXML
    ChoiceBox<Account> chooseAccount;
    @FXML
    ChoiceBox<String> chooseType;
    @FXML
    Label errorMessage;

    @FXML
    private void initialize() {
        fillAccountBoxes();
        chooseType.getItems().addAll("Sparkonto", "Kortkonto", "Lönkonto");
        chooseType.getSelectionModel().selectFirst();
    }

    void fillAccountBoxes() {
        for (Account account : CT.accounts) {
            chooseAccount.getItems().add(account);
        }
        chooseAccount.getSelectionModel().selectFirst();
    }

    @FXML
    void submitChange() {
        DB.changeAccountType(chooseAccount.getValue().getAccountNr(), CT.getAccountType(chooseType.getValue()));
        CT.navController.loadHome();
    }

    @FXML
    void cancelChange() {
        CT.navController.loadHome();
    }


}
