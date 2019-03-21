package app.db;

import app.Entities.Account;
import app.Entities.Transaction;
import app.Entities.User;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

/**
 * A Helper class for interacting with the Database using short-commands
 */
public abstract class DB {


    public static void moveMoneyBetweenAccounts(long from, long to, double amount) {
        DBhelper.getDBhelper().moveMoneyBetweenAccounts(from, to, amount);
    }

    public static void createNewAccount(String name, String type) {
        DBhelper.getDBhelper().createNewAccount(name, type);
    }

    public static void changeAccountName(long targetAccount, String newName) {
        DBhelper.getDBhelper().changeAccountName(targetAccount, newName);
    }

    public static void changeAccountType(long targetAccount, String newType) {
        DBhelper.getDBhelper().changeAccountType(targetAccount, newType);
    }

    public static void deleteAccount(long targetAccount) {
        DBhelper.getDBhelper().deleteAccount(targetAccount);
    }

    public static PreparedStatement prep(String SQLQuery) {
        return Database.getInstance().prepareStatement(SQLQuery);
    }

    public static CallableStatement call(String procedure, String[] params) {
        return Database.getInstance().callableStatement(procedure, params);
    }

    public static User getMatchingUser(String email, String password) {
        User result = null;
        PreparedStatement ps = prep("SELECT * FROM users WHERE email = ? AND password = ?");
        try {
            ps.setString(1, email);
            ps.setString(2, password);
            result = (User) new ObjectMapper<>(User.class).mapOne(ps.executeQuery());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result; // return User;
    }

    //        Example method with default parameters
    public static List<Transaction> getTransactions(long accountNr) {
        return getTransactions(accountNr, 0);
    }

    public static List<Transaction> getTransactions(long accountNr, int offset) {

        List<Transaction> result = null;
        PreparedStatement ps = prep("SELECT * FROM transactions WHERE account_from = " + accountNr + " OR account_to = " + accountNr + " LIMIT 10 OFFSET " + offset);
        try {
            result = (List<Transaction>) new ObjectMapper<>(Transaction.class).map(ps.executeQuery());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result; // return Transactions;
    }

    public static List<Account> getAccounts(long userId) {
        List<Account> result = null;
        PreparedStatement ps = prep("SELECT * FROM accounts WHERE user_id = " + userId);
        try {
            result = (List<Account>) new ObjectMapper<>(Account.class).map(ps.executeQuery());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result; // return Accounts;
    }

    public static void cardPay(long cardNr, long targetAccount, double amount) {

        CallableStatement cs = Database.getInstance().cardPay(cardNr, targetAccount, amount);

        try {
            cs.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

}