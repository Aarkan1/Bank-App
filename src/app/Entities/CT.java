package app.Entities;

import app.home.HomeController;
import app.nav.NavController;
import app.transaction.DisplayTransactionsController;
import java.util.List;

/**
 * A Helper class for global access to controllers
 */
public abstract class CT {

    public static List<Account> accounts;
    public static List<Account> addedAccounts;
    public static HomeController homeController;
    public static NavController navController;
    public static DisplayTransactionsController displayTransactionsController;

    public static void setDisplayTransactionsController(DisplayTransactionsController displayTransactionsController) {
        CT.displayTransactionsController = displayTransactionsController;
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
