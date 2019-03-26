package app.transaction;

import app.Entities.Account;
import app.Entities.CT;
import app.db.DB;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

public class NewTransactionController {

    @FXML
    VBox payMethods;

    private ChoiceBox<Account> accountFrom = new ChoiceBox<>();
    private ChoiceBox<Account> accountTo = new ChoiceBox<>();
    private TextField amountField = new TextField();
    private TextField messageField = new TextField();
    private Label errorLabel = new Label();

    @FXML
    private void initialize() {
        initTransactionBoxes();
    }

    private void initTransactionBoxes() {
        Label fromLabel = new Label("Från");
        Label toLabel = new Label("Till");
        Label amountLabel = new Label("Belopp");
        Label messageLabel = new Label("Meddelande");
        toLabel.setPadding(new Insets(10, 0, 0, 0));
        amountLabel.setPadding(new Insets(10, 0, 0, 0));

        fillAccountBoxes();
        nodeSettings();

        payMethods.getChildren().addAll(
                fromLabel,
                accountFrom,
                toLabel,
                accountTo,
                amountLabel,
                amountField,
                messageLabel,
                messageField,
                errorLabel);

        addButtons();
    }

    private void addButtons() {
        HBox buttonsHBox = new HBox();
        Button cancelButton = new Button("Avbryt");
        cancelButton.addEventHandler(MouseEvent.MOUSE_PRESSED, (MouseEvent e) -> CT.navController.loadHome());

        Button submitButton = new Button("Spara");
        submitButton.addEventHandler(MouseEvent.MOUSE_PRESSED, (MouseEvent e) ->
                moveMoneyBetweenAccounts(accountFrom.getValue(), accountTo.getValue(), amountField.getText(), messageField.getText()));

        buttonsHBox.getChildren().addAll(cancelButton, submitButton);
        buttonsHBox.setSpacing(10);
        buttonsHBox.setAlignment(Pos.CENTER_LEFT);

        payMethods.getChildren().add(buttonsHBox);
    }

    private void fillAccountBoxes() {
        for (Account account : CT.accounts) {
            accountFrom.getItems().add(account);
            accountTo.getItems().add(account);
        }
        Account seperator = new Account("---------");
        seperator.setAddedAccount();
        accountTo.getItems().add(seperator);

        addedAccounts();
    }

    private void addedAccounts() {
        for (Account account : CT.addedAccounts) {
            accountTo.getItems().add(account);
        }
    }

    private void nodeSettings() {
        payMethods.setAlignment(Pos.TOP_LEFT);
        payMethods.setSpacing(5);

        accountFrom.setPrefWidth(200);
        accountTo.setPrefWidth(200);
        accountFrom.setStyle("-fx-text-alignment: cover;");

        accountFrom.getSelectionModel().selectFirst();
        accountTo.getSelectionModel().selectLast();
    }

    private void moveMoneyBetweenAccounts(Account accFrom, Account accTo, String amount, String message) {

        if (accTo.getName().equals("---------")) return;

        if (!amount.matches("^[\\d]+$")) {
            errorLabel.setTextFill(Color.RED);
            errorLabel.setText("Beloppet stämmer inte");
            return;
        }

        double inputAmount = Double.parseDouble(amount);

        if (inputAmount > accFrom.getSaldo()) {
            errorLabel.setTextFill(Color.RED);
            errorLabel.setText("Beloppet stämmer inte");
        } else {
            DB.moveMoneyBetweenAccounts(accFrom.getAccountNr(), accTo.getAccountNr(), inputAmount, message);
            CT.navController.loadHome();
        }
    }
}
