package app.Entities;

import app.account.AllAccountController;
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

}
