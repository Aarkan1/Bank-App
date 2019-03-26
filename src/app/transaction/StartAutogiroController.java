package app.transaction;

import app.Entities.Account;
import app.Entities.CT;
import app.db.DB;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;

public class StartAutogiroController {

    @FXML
    TextField savingsAmount;
    @FXML
    ChoiceBox<Account> accountFrom;
    @FXML
    ChoiceBox<Account> accountTo;
    @FXML
    Label errorLabel;

    @FXML
    private void initialize() {
        fillAccountBoxes();
        addedAccounts();
    }

    private void fillAccountBoxes() {
        for (Account account : CT.accounts) {
            accountTo.getItems().add(account);
            accountFrom.getItems().add(account);
        }
        accountFrom.getSelectionModel().selectFirst();

        Account seperator = new Account("---------");
        seperator.setAddedAccount();
        accountTo.getItems().add(seperator);

    }

    private void addedAccounts() {
        for (Account account : CT.addedAccounts) {
            accountTo.getItems().add(account);
        }
        accountTo.getSelectionModel().selectFirst();
    }

    @FXML
    void submitSaving() {
        if (accountTo.getValue().getName().equals("---------")) return;

        if (accountFrom.getValue().getAccountNr().equals(accountTo.getValue().getAccountNr())) {
            return;
        }

        String strAmount = savingsAmount.getText().trim();

        if (!strAmount.matches("^[\\d]+$")) {
            errorLabel.setTextFill(Color.RED);
            errorLabel.setText("Beloppet stämmer inte");
            return;
        }

        double inputAmount = Double.parseDouble(strAmount);

        if (inputAmount > accountTo.getValue().getSaldo()) {
            errorLabel.setTextFill(Color.RED);
            errorLabel.setText("Beloppet stämmer inte");
        } else {

            DB.startAutogiro(inputAmount, accountFrom.getValue().getAccountNr(), accountTo.getValue().getAccountNr());
            CT.navController.loadHome();
        }
    }

    @FXML
    void cancelChange() {
        CT.navController.loadHome();
    }

}
