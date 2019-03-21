package app.nav;

import app.Entities.CT;
import javafx.fxml.FXML;

public class NavController {

    @FXML
    void loadHome() {
        CT.homeController.loadAllAccounts();
    }

}
