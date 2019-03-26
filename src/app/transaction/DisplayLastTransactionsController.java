package app.transaction;

import app.Entities.Transaction;
import app.db.DB;
import app.login.LoginController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.List;

public class DisplayLastTransactionsController {

    @FXML
    VBox allTransactions;

    @FXML
    private void initialize() {
        loadTransactions();
    }

    public void loadTransactions() {

        List<Transaction> transactions = DB.getLastTransactions(LoginController.getUser().getId());

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

            allTransactions.getChildren().add(scene.getRoot());
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

}
