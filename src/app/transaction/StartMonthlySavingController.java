package app.transaction;

import app.Entities.Account;
import app.Entities.CT;
import app.db.DB;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class StartMonthlySavingController {

    @FXML
    TextField savingsAmount;
    @FXML
    ChoiceBox<Account> accountFrom;
    @FXML
    ChoiceBox<Account> accountTo;

    @FXML
    private void initialize() {
        fillAccountBoxes();
    }

    void fillAccountBoxes() {
        for (Account account : CT.accounts) {
            accountTo.getItems().add(account);
            accountFrom.getItems().add(account);
        }
        accountFrom.getSelectionModel().selectFirst();
        accountTo.getSelectionModel().selectLast();
    }

    @FXML
    void submitSaving() {

        if (accountFrom.getValue().getAccountNr() == accountTo.getValue().getAccountNr()) {
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
