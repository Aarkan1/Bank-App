package app.transaction;

import app.Entities.Account;
import app.Entities.CT;
import app.account.AllAccountController;
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

    ChoiceBox<String> accountFrom = new ChoiceBox<>();
    ChoiceBox<String> accountTo = new ChoiceBox<>();
    TextField amountField = new TextField();
    TextField messageField = new TextField();
    Label errorLabel = new Label();

    @FXML
    private void initialize() {
        initTransactionBoxes();
    }

    void initTransactionBoxes() {
        Label fromLabel = new Label("Från");
        Label toLabel = new Label("Till");
        Label amountLabel = new Label("Belopp");
        Label messageLabel = new Label("Meddelande");
        toLabel.setPadding(new Insets(10, 0, 0, 0));
        amountLabel.setPadding(new Insets(10, 0, 0, 0));

        fillAccountBoxes();
        nodeSettings();

        HBox buttonsHBox = new HBox();
        Button cancelButton = new Button("Avbryt");
        cancelButton.addEventHandler(MouseEvent.MOUSE_PRESSED, (MouseEvent e) -> CT.navController.loadHome());

        Button submitButton = new Button("Spara");
        submitButton.addEventHandler(MouseEvent.MOUSE_PRESSED, (MouseEvent e) ->
                moveMoneyBetweenAccounts(accountFrom.getValue(), accountTo.getValue(), amountField.getText(), messageField.getText()));

        buttonsHBox.getChildren().addAll(cancelButton, submitButton);
        buttonsHBox.setSpacing(10);
        buttonsHBox.setAlignment(Pos.CENTER_RIGHT);

        payMethods.getChildren().addAll(
                fromLabel,
                accountFrom,
                toLabel,
                accountTo,
                amountLabel,
                amountField,
                messageLabel,
                messageField,
                errorLabel,
                buttonsHBox);
    }

    void fillAccountBoxes() {
        for (Account account : AllAccountController.accounts) {

            String itemText = String.format("%s\t\t%2.2f", account.getAccountNr(), account.getSaldo());

            accountFrom.getItems().add(itemText);
            accountTo.getItems().add(itemText);
        }
    }

    void nodeSettings() {
        payMethods.setAlignment(Pos.TOP_LEFT);
        payMethods.setSpacing(5);

        accountFrom.setPrefWidth(200);
        accountTo.setPrefWidth(200);
        accountFrom.setStyle("-fx-text-alignment: cover;");

        accountFrom.getSelectionModel().selectFirst();
        accountTo.getSelectionModel().selectLast();
    }

    void moveMoneyBetweenAccounts(String from, String to, String amount, String message) {

        if (!amount.matches("^[\\d]+$")) {
            errorLabel.setTextFill(Color.RED);
            errorLabel.setText("Beloppet stämmer inte");
            return;
        }

        String[] fromSplit = from.split("\t");
        String[] toSplit = to.split("\t");

//        long fromAcc = Long.parseLong(fromSplit[0]);
        double fromSaldo = Double.parseDouble(fromSplit[fromSplit.length - 1].replace(",", "."));
//        long toAcc = Long.parseLong(toSplit[0]);
        double inputAmount = Double.parseDouble(amount);

        if (inputAmount > fromSaldo) {
            errorLabel.setTextFill(Color.RED);
            errorLabel.setText("Beloppet stämmer inte");
        } else {
            DB.moveMoneyBetweenAccounts(fromSplit[0], toSplit[0], inputAmount, message);
            CT.navController.loadHome();
        }
    }
}
