package app.account;


import app.Entities.Account;
import app.Entities.CT;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import java.io.IOException;

public class AccountController {

    @FXML
    Pane accountPane;
    @FXML
    Label accountName;
    @FXML
    Label accountNr;
    @FXML
    Label accountSaldo;

    @FXML
    private void initialize() {
    }

    public void setAccount(Account account) {
        accountName.setText(account.getName());
        accountNr.setText("" + account.getAccountNr());
        accountSaldo.setText("" + account.getSaldo());

        accountPane.addEventHandler(MouseEvent.MOUSE_PRESSED, (MouseEvent e) -> {
            displayTransactions(account);
        });
    }

    void displayTransactions(Account account) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/app/transaction/displayTransactions.fxml"));
            Parent fxmlInstance = loader.load();
            Scene scene = new Scene(fxmlInstance);

            CT.homeController.setCenter(scene);
            CT.setDisplayTransactionsController(loader.getController());

            CT.displayTransactionsController.loadTransactions(account);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
