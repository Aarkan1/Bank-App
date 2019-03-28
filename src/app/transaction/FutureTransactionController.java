package app.transaction;

import app.Entities.Account;
import app.Entities.CT;
import app.db.DB;
import app.nav.NV;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;

import java.sql.Timestamp;
import java.time.LocalDate;

public class FutureTransactionController {

    @FXML
    ChoiceBox<Account> accountTo;
    @FXML
    ChoiceBox<Account> accountFrom;
    @FXML
    TextField amount;
    @FXML
    TextField ocrNr;
    @FXML
    Label errorMessage;
    @FXML
    DatePicker datePicker;

    @FXML
    private void initialize() {
        fillAccountBox();
    }

    private void fillAccountBox() {
        for (Account account : CT.accounts) {
            accountFrom.getItems().add(account);

        }
        for (Account account : CT.addedAccounts) {
            accountTo.getItems().add(account);
        }
        accountFrom.getSelectionModel().selectFirst();
        accountTo.getSelectionModel().selectFirst();
    }

    @FXML
    void submitPayment() {
        if (!amount.getText().matches("^[\\d]+$")) {
            errorMessage.setTextFill(Color.RED);
            errorMessage.setText("Beloppet stämmer inte");
            return;
        }
        if (datePicker.getValue() == null) {
            errorMessage.setTextFill(Color.RED);
            errorMessage.setText("Måste ange ett datum");
            return;
        }

        Timestamp timestamp = Timestamp.valueOf(datePicker.getValue().atStartOfDay());

        double inputAmount = Double.parseDouble(amount.getText());

        if (inputAmount > accountFrom.getValue().getSaldo()) {
            errorMessage.setTextFill(Color.RED);
            errorMessage.setText("Beloppet stämmer inte");
        } else {
            DB.futureTransaction(ocrNr.getText().trim(), inputAmount, accountFrom.getValue().getAccountNr(), accountTo.getValue().getAccountNr(), timestamp);
            NV.get().loadHome();
        }
    }

    @FXML
    void cancelChange() {
        NV.get().loadHome();
    }
}
