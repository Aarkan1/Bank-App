package app.transaction;

import app.Entities.Account;
import app.Entities.Transaction;
import app.db.DB;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

import java.io.IOException;
import java.util.List;

public class DisplayTransactionsController {

    @FXML
    VBox allTransactions;
    @FXML
    Label accountName;
    @FXML
    Button loadMore;

    private Account account;

    @FXML
    private void initialize() {
        System.out.println("initialize transactions");

        loadMore.addEventHandler(MouseEvent.MOUSE_PRESSED, (MouseEvent e) -> {
            loadMoreTransactions();
        });
    }

    public void loadTransactions(Account account) {
        this.account = account;

        accountName.setText(account.getName());

        List<Transaction> transactions = DB.getTransactions(account.getAccountNr());
        this.account.incrementOffset();

        for (Transaction transaction : transactions) {
            displayTransaction(transaction);
        }

    }

    void loadMoreTransactions() {
        List<Transaction> transactions = DB.getTransactions(account.getAccountNr(), this.account.getOffset());
        this.account.incrementOffset();

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


            TransactionController tc = loader.getController();

            tc.setTransaction(transaction);

            if (transaction.isIncome(this.account.getAccountNr())) {
                tc.amount.setTextFill(Color.GREEN);
                tc.amount.setText("+" + tc.amount.getText());
            } else {
                tc.amount.setTextFill(Color.RED);
                tc.amount.setText("-" + tc.amount.getText());
            }

            allTransactions.getChildren().add(scene.getRoot());
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

}
