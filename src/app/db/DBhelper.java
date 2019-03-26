package app.db;

import app.login.LoginController;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DBhelper {
    private static DBhelper singleton = new DBhelper();

    public static DBhelper getDBhelper() {
        return singleton;
    }

    public void transferMoney(double amount, String message, String from, String to) {

        PreparedStatement ps = DB.prep("CALL transfer_money(?, ?, ?, ?)");

        try {
            ps.setDouble(1, amount);
            ps.setString(2, message);
            ps.setString(3, from);
            ps.setString(4, to);
            ps.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void moveMoneyBetweenAccounts(String from, String to, double amount, String message) {
        message = message.trim();
        String sendMsg = message.length() > 0 ? message : "" + from;
        transferMoney(amount, sendMsg, from, to);
    }

    public void createNewAccount(String name, String type) {

//        gets a random account number on creation
        String clearingNr = "8521";
        int accountNrRnd = (int) (Math.random() * 80000000) + 10000000;
        clearingNr += accountNrRnd;
        String userID = LoginController.getUser().getId();

        PreparedStatement ps = DB.prep("INSERT INTO accounts SET account_nr = ?, user_person_nr = ?, `name` = ?, `type` = ?");

        try {
            ps.setString(1, clearingNr);
            ps.setString(2, userID);
            ps.setString(3, name);
            ps.setString(4, type);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteAccount(String targetAccount) {

        PreparedStatement ps = DB.prep("DELETE FROM accounts WHERE account_nr = ?");

        try {
            ps.setString(1, targetAccount);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void changeAccountName(String targetAccount, String newName) {
        PreparedStatement ps = DB.prep("UPDATE accounts SET `name` = ? WHERE account_nr = ?");

        try {
            ps.setString(1, newName);
            ps.setString(2, targetAccount);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void changeAccountType(String targetAccount, String newType) {

        PreparedStatement ps = DB.prep("UPDATE accounts SET `type` = ? WHERE account_nr = ?");

        try {
            ps.setString(1, newType);
            ps.setString(2, targetAccount);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
   public void addNewAccount(String targetAccount, String accountName) {

        PreparedStatement ps = DB.prep("INSERT INTO userXaccounts SET userXaccounts.`user_person_nr` = ?, userXaccounts.`account_nr` = ?, userXaccounts.`name` = ?");

        try {
            ps.setString(1, LoginController.getUser().getId());
            ps.setString(2, targetAccount);
            ps.setString(3, accountName);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
