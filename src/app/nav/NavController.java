package app.nav;

import app.login.LoginController;
import javafx.fxml.FXML;

public class NavController {

    @FXML
    void loadHome() {
        LoginController.homeController.loadAllAccounts();
    }

}
