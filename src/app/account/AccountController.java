package app.account;


import app.Entities.Account;
import app.Entities.Transaction;
import app.db.DB;
import app.transaction.TransactionController;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.List;

public class AccountController {

    @FXML
    VBox transactionBox;
    @FXML
    Label accountName;
    @FXML
    Label accountNr;
    @FXML
    Label accountSaldo;

    @FXML
    private void initialize() {
//        loadMoreTransactions();
    }

    public void setAccount(Account account) {
        accountName.setText(account.getName());
        accountNr.setText("" + account.getAccountNr());
        accountSaldo.setText("" + account.getSaldo());
    }

    void loadMoreTransactions() {
        List<Transaction> transactions = DB.getTransactions(854294967295L);

        for (Transaction transaction : transactions) {
            displayTransaction(transaction);
        }
    }

    void displayTransaction(Transaction transaction) {
        // For every transaction, do the following:
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/app/transaction/transaction.fxml"));
            Parent fxmlInstance = loader.load();
            Scene scene = new Scene(fxmlInstance);

            TransactionController controller = loader.getController();
            controller.setTransaction(transaction);

            transactionBox.getChildren().add(scene.getRoot());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void clickLoadTransactions(Event e) {
        loadMoreTransactions();
    }
}
