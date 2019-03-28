package app.db;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;

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

    public void futureTransaction(String message, double amount, String accountFrom, String accountTo, Timestamp timestamp){

        PreparedStatement ps = DB.prep("INSERT INTO future_transactions SET `message` = ?,`amount` = ?, `account_from` = ?, `account_to` = ?, `date_to_send` = ?");

        try {
            ps.setString(1, message);
            ps.setDouble(2, amount);
            ps.setString(3, accountFrom);
            ps.setString(4, accountTo);
            ps.setTimestamp(5, timestamp);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
