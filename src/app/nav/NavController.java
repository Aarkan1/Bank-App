package app.nav;

import app.Entities.CT;
import app.db.DB;
import javafx.fxml.FXML;

public class NavController {

    @FXML
    void initialize() {
    }

    @FXML
    public void loadHome() {
        NV.get().loadHome();
    }

    private void changeHomeCenter(String fxmlRoute) {
        CT.homeController.setCenter(NV.get().loadFXML(fxmlRoute));
    }

    @FXML
    void paySalary() {
        DB.paySalary(10000, "750312-3453");
        loadHome();
    }

    @FXML
    void loadNewTransactions() {
        changeHomeCenter("/app/transaction/newTransaction.fxml");
    }

    @FXML
    void loadFutureTransactions() {
        changeHomeCenter("/app/transaction/futureTransaction.fxml");
    }

    @FXML
    void loadStartSavings() {
        changeHomeCenter("/app/transaction/startMonthlySaving.fxml");
    }

    @FXML
    void loadAutogiro() {
        changeHomeCenter("/app/transaction/startAutogiro.fxml");
    }

    @FXML
    void loadAddAccount() {
        changeHomeCenter("/app/account/addNewAccount.fxml");
    }

    @FXML
    public void changeNameSettingsScreen() {
        changeHomeCenter("/app/account/accountSettings/changeNameSettings.fxml");
    }

    @FXML
    public void changeTypeSettingsScreen() {
        changeHomeCenter("/app/account/accountSettings/changeTypeSettings.fxml");
    }

    @FXML
    public void createNewSettingsScreen() {
        changeHomeCenter("/app/account/accountSettings/createNewSettings.fxml");
    }

    @FXML
    public void deleteSettingsScreen() {
        changeHomeCenter("/app/account/accountSettings/deleteSettings.fxml");
    }

    @FXML
    public void cardPayScreen() {
        changeHomeCenter("/app/transaction/newCardTransaction.fxml");
    }

}
