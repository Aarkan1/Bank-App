package app.account;


import app.Entities.Account;
import app.db.DB;
import app.login.LoginController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import java.io.IOException;
import java.util.List;

public class AllAccountController {

    @FXML
    VBox allAccounts;

    public static List<Account> accounts;

    @FXML
    private void initialize() {

//        print all accounts on load
        accounts = DB.getAccounts(LoginController.getUser().getId());

        for (Account account : accounts) {
            printAccount(account);
        }

    }

    void printAccount(Account account) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/app/account/account.fxml"));
            Parent fxmlInstance = loader.load();
            Scene scene = new Scene(fxmlInstance);

            AccountController controller = loader.getController();
            controller.setAccount(account);

            allAccounts.getChildren().add(scene.getRoot());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
