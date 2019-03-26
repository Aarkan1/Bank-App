package app.transaction;

import app.Entities.Account;
import app.Entities.CT;
import app.db.DB;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;

public class StartAutogiroController {

    @FXML
    TextField savingsAmount;
    @FXML
    ChoiceBox<Account> accountFrom;
    @FXML
    ChoiceBox<Account> accountTo;

    @FXML
    private void initialize() {
        fillAccountBoxes();
        addedAccounts();
    }

    void fillAccountBoxes() {
        for (Account account : CT.accounts) {
            accountTo.getItems().add(account);
            accountFrom.getItems().add(account);
        }
        accountFrom.getSelectionModel().selectFirst();

        Account seperator = new Account("---------");
        seperator.setAddedAccount();
        accountTo.getItems().add(seperator);

    }

    void addedAccounts() {
        for (Account account : CT.addedAccounts) {
            accountTo.getItems().add(account);
        }
        accountTo.getSelectionModel().selectFirst();
    }

    @FXML
    void submitSaving() {

        if (accountFrom.getValue().getAccountNr() == accountTo.getValue().getAccountNr() ||
                accountTo.getValue().equals("---------")) {
            return;
        }

        String strAmount = savingsAmount.getText().trim();
        double amount = Double.parseDouble(strAmount);

        DB.startAutogiro(amount, accountFrom.getValue().getAccountNr(), accountTo.getValue().getAccountNr());
        CT.navController.loadHome();
    }

    @FXML
    void cancelChange() {
        CT.navController.loadHome();
    }

}
