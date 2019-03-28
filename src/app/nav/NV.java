package app.nav;

import app.Entities.CT;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

import java.io.IOException;

public class NV {
    private static NV singleton = new NV();

    public static NV get() {
        return singleton;
    }

    public Scene loadFXML(String fxmlRoute) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlRoute));
            Parent fxmlInstance = loader.load();
            return new Scene(fxmlInstance);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @FXML
    public void loadHome() {
        CT.homeController.loadAllAccounts();
    }
}
