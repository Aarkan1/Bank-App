package app.db;

import app.login.LoginController;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DBschedules {
    private static DBschedules singleton = new DBschedules();

    public static DBschedules getSingleton() {
        return singleton;
    }


    public void startScheduledTransfer(double amount, String fromAccount, String toAccount) {
        String query =
                "CREATE EVENT IF NOT EXISTS `" + LoginController.getUser().getId() + "_auto_saving` ON SCHEDULE EVERY 1 MONTH STARTS CURRENT_TIMESTAMP ON COMPLETION PRESERVE ENABLE DO CALL transfer_money(?, 'MONTHLY SAVING', ?, ?);";

        PreparedStatement ps = DB.prep(query);

        try {
            ps.setDouble(1, amount);
            ps.setString(2, fromAccount);
            ps.setString(3, toAccount);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void removeSchedule(String userID) {
        PreparedStatement ps = DB.prep("DROP EVENT IF EXISTS `" + userID + "_auto_saving`");
        try {
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
