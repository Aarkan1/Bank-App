package app.account.accountSettings;

import app.Entities.Account;
import app.Entities.CT;
import app.db.DB;
import app.nav.NV;
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

    private void fillAccountBoxes() {
        for (Account account : CT.accounts) {
            chooseAccount.getItems().add(account);
        }
        chooseAccount.getSelectionModel().selectFirst();
    }

    @FXML
    void submitChange() {
        DB.changeAccountType(chooseAccount.getValue().getAccountNr(), CT.getAccountType(chooseType.getValue()));
        NV.get().loadHome();
    }

    @FXML
    void cancelChange() {
        NV.get().loadHome();
    }


}
