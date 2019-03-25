package app.account;


import app.Entities.Account;
import app.Entities.CT;
import app.db.DB;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class AddNewAccountController {

    TextField newAccountNr;
    TextField newAccountName;

    @FXML
    private void initialize() {

    }

    void addAccount() {

//        don't add
//        if account already is added
        for (Account account : CT.addedAccounts) {
            if (account.getAccountNr().equals(newAccountNr.getText())) {
                return;
            }
        }
//        or if the account is the users
        for (Account account : CT.accounts) {
            if (account.getAccountNr().equals(newAccountNr.getText())) {
                return;
            }
        }

        DB.addNewAccount(newAccountName.getText(), newAccountNr.getText());
    }


}
