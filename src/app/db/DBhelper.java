package app.db;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DBhelper {
    private static DBhelper singleton = new DBhelper();

    public static DBhelper getDBhelper() {
        return singleton;
    }

    public void transferMoney(double amount, String message, long from, long to) {

        PreparedStatement ps = DB.prep("CALL transfer_money(?, ?, ?, ?)");

        try {
            ps.setDouble(1, amount);
            ps.setString(2, message);
            ps.setDouble(3, from);
            ps.setDouble(4, to);
            ps.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void moveMoneyBetweenAccounts(long from, long to, double amount) {
        transferMoney(amount, "" + from, from, to);
    }
}
