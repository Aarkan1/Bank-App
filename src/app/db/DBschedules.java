package app.db;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DBschedules {
    private static DBschedules singleton = new DBschedules();

    public static DBschedules getSingleton() {
        return singleton;
    }


    public void startAutogiro(double amount, String fromAccount, String toAccount) {

        PreparedStatement ps = DB.prep("INSERT INTO autogiro SET `account_from` = ?, `account_to` = ?, `amount` = ?");

        try {
            ps.setString(1, fromAccount);
            ps.setString(2, toAccount);
            ps.setDouble(3, amount);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void removeAutogiro(String targetAccount) {

        PreparedStatement ps = DB.prep("DELETE FROM autogiro WHERE `account_from` = ? LIMIT 1");
        try {
            ps.setString(1, targetAccount);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void paySalary(double amount, String userID) {
        PreparedStatement ps = DB.prep("CALL pay_salary(?, ?)");
        try {
            ps.setDouble(1, amount);
            ps.setString(2, userID);
            ps.executeQuery();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
