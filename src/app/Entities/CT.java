package app.Entities;

import app.account.AllAccountController;
import app.account.accountSettings.ChangeNameSettingsController;
import app.account.accountSettings.ChangeTypeSettingsController;
import app.account.accountSettings.CreateNewSettingsController;
import app.account.accountSettings.DeleteSettingsController;
import app.home.HomeController;
import app.nav.NavController;
import app.transaction.DisplayTransactionsController;

/**
 * A Helper class for global access to controllers
 */
public abstract class CT {

    public static AllAccountController allAccountController;
    public static HomeController homeController;
    public static NavController navController;
    public static DisplayTransactionsController displayTransactionsController;
    public static ChangeNameSettingsController changeNameSettingsController;
    public static CreateNewSettingsController createNewSettingsController;
    public static ChangeTypeSettingsController changeTypeSettingsController;

    public static void setChangeNameSettingsController(ChangeNameSettingsController changeNameSettingsController) {
        CT.changeNameSettingsController = changeNameSettingsController;
    }

    public static void setCreateNewSettingsController(CreateNewSettingsController createNewSettingsController) {
        CT.createNewSettingsController = createNewSettingsController;
    }

    public static void setChangeTypeSettingsController(ChangeTypeSettingsController changeTypeSettingsController) {
        CT.changeTypeSettingsController = changeTypeSettingsController;
    }

    public static void setDeleteSettingsController(DeleteSettingsController deleteSettingsController) {
        CT.deleteSettingsController = deleteSettingsController;
    }

    public static DeleteSettingsController deleteSettingsController;

    public static void setDisplayTransactionsController(DisplayTransactionsController displayTransactionsController) {
        CT.displayTransactionsController = displayTransactionsController;
    }

    public static void setAllAccountController(AllAccountController allAccountController) {
        CT.allAccountController = allAccountController;
    }

    public static void setHomeController(HomeController homeController) {
        CT.homeController = homeController;
    }

    public static void setNavController(NavController navController) {
        CT.navController = navController;
    }

    public static String getAccountType(String type){
        switch (type) {
            case "Sparkonto":
                return "savings";
            case "Kortkonto":
                return "card-account";
            case "Lönkonto":
                return "salary-account";
            default:
                return  "savings";
        }
    }
}
