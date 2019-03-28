package app.transaction;

import app.Entities.Account;
import app.Entities.CT;
import app.db.DB;
import app.nav.NV;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;

public class NewCardTransactionController {

    @FXML
    ChoiceBox<Account> accountTo;
    @FXML
    TextField amount;
    @FXML
    TextField cardNr;
    @FXML
    Label errorMessage;
    Account cardAccount;

    @FXML
    private void initialize() {
        fillAccountBox();
    }

    private void fillAccountBox() {
        for (Account account : CT.accounts) {
            if (account.getType().equals("card-account"))
                cardAccount = account;
        }
        for (Account account : CT.addedAccounts) {
            accountTo.getItems().add(account);
        }
        accountTo.getSelectionModel().selectFirst();
    }

    @FXML
    void submitPayment() {

        if (accountTo.getValue().getName().equals("---------")) return;

        if (!amount.getText().matches("^[\\d]+$")) {
            errorMessage.setTextFill(Color.RED);
            errorMessage.setText("Beloppet stämmer inte");
            return;
        }

        double inputAmount = Double.parseDouble(amount.getText());
        long cardNR = Long.parseLong(cardNr.getText());

        if (inputAmount > cardAccount.getSaldo()) {
            errorMessage.setTextFill(Color.RED);
            errorMessage.setText("Beloppet stämmer inte");
        } else {
            DB.cardPay(cardNR, accountTo.getValue().getAccountNr(), inputAmount);
            NV.get().loadHome();
        }
    }

    @FXML
    void cancelChange() {
        NV.get().loadHome();
    }
}
