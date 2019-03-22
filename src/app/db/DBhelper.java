package app.db;

import app.Entities.Account;
import app.account.AllAccountController;
import app.login.LoginController;

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

    public void moveMoneyBetweenAccounts(long from, long to, double amount, String message) {
        message = message.trim();
        String sendMsg = message.length() > 0 ? message : "" + from;
        transferMoney(amount, sendMsg, from, to);
    }

    public void createNewAccount(String name, String type) {

//        gets a random account number on creation
        String clearingNr = "8521";
        int accountNrRnd = (int) (Math.random() * 80000000) + 10000000;
        clearingNr += accountNrRnd;
        long accountNrLong = Long.parseLong(clearingNr);
        int userID = (int) LoginController.getUser().getId();

        PreparedStatement ps = DB.prep("INSERT INTO accounts SET account_nr = ?, user_id = ?, `name` = ?, `type` = ?");

        try {
            ps.setLong(1, accountNrLong);
            ps.setInt(2, userID);
            ps.setString(3, name);
            ps.setString(4, type);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteAccount(long targetAccount) {

        PreparedStatement ps = DB.prep("DELETE FROM accounts WHERE account_nr = ?");

        try {
            ps.setLong(1, targetAccount);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void changeAccountName(long targetAccount, String newName) {
        PreparedStatement ps = DB.prep("UPDATE accounts SET `name` = ? WHERE account_nr = ?");

        try {
            ps.setString(1, newName);
            ps.setLong(2, targetAccount);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void changeAccountType(long targetAccount, String newType) {

        //        may only have 1 salary account
        if (newType.equals("salary-account")) {
            changeSalaryAccount(newType);
        }
//        must have a salary account
        if (checkAccountType(targetAccount, "salary-account")) {
            return;
        }

        PreparedStatement ps = DB.prep("UPDATE accounts SET `type` = ? WHERE account_nr = ?");

        try {
            ps.setString(1, newType);
            ps.setLong(2, targetAccount);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    void changeSalaryAccount(String type) {
        AllAccountController.accounts.forEach(a -> {
            if (a.getType().equals(type))
                changeAccountType(a.getAccountNr(), "savings");
        });
    }

    boolean checkAccountType(long targetAccount, String type) {
        for (Account a : AllAccountController.accounts) {
            if (a.getAccountNr() == targetAccount && a.getType().equals(type))
                return true;
        }
        return false;
    }
}
