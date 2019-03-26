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


    public static void moveMoneyBetweenAccounts(String from, String to, double amount, String message) {
        DBhelper.getDBhelper().moveMoneyBetweenAccounts(from, to, amount, message);
    }

    public static void createNewAccount(String name, String type) {
        DBhelper.getDBhelper().createNewAccount(name, type);
    }

    public static void changeAccountName(String targetAccount, String newName) {
        DBhelper.getDBhelper().changeAccountName(targetAccount, newName);
    }

    public static void changeAccountType(String targetAccount, String newType) {
        DBhelper.getDBhelper().changeAccountType(targetAccount, newType);
    }
    public static void addNewAccount(String targetAccount, String accountName) {
        DBhelper.getDBhelper().addNewAccount(targetAccount, accountName);
    }
   public static List<Transaction> getLastTransactions(String userID) {
        return DBhelper.getDBhelper().getLastTransactions(userID);
    }

    public static void startAutogiro(double amount, String fromAccount, String toAccount){
        DBschedules.getSingleton().startAutogiro(amount,fromAccount,toAccount);
    }

    public static void deleteAccount(String targetAccount) {
        DBhelper.getDBhelper().deleteAccount(targetAccount);
    }

    public static PreparedStatement prep(String SQLQuery) {
        return Database.getInstance().prepareStatement(SQLQuery);
    }

    public static CallableStatement call(String procedure, String[] params) {
        return Database.getInstance().callableStatement(procedure, params);
    }

    public static User getMatchingUser(String userID, String password) {
        User result = null;
        PreparedStatement ps = prep("SELECT * FROM users WHERE person_nr = ? AND password = ?");
        try {
            ps.setString(1, userID);
            ps.setString(2, password);
            result = (User) new ObjectMapper<>(User.class).mapOne(ps.executeQuery());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result; // return User;
    }

    //        Example method with default parameters
    public static List<Transaction> getTransactions(String accountNr) {
        return getTransactions(accountNr, 0);
    }

    public static List<Transaction> getTransactions(String accountNr, int offset) {

        List<Transaction> result = null;
        PreparedStatement ps = prep("SELECT * FROM transactions WHERE account_from = ? OR account_to = ? LIMIT 10 OFFSET ?");
        try {
            ps.setString(1, accountNr);
            ps.setString(2, accountNr);
            ps.setInt(3, offset);
            result = (List<Transaction>) new ObjectMapper<>(Transaction.class).map(ps.executeQuery());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result; // return Transactions;
    }

    public static List<Account> getAccounts(String userId) {
        List<Account> result = null;
        PreparedStatement ps = prep("SELECT * FROM accounts WHERE user_person_nr = ?");
        try {
            ps.setString(1, userId);
            result = (List<Account>) new ObjectMapper<>(Account.class).map(ps.executeQuery());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result; // return Accounts;
    }

    public static List<Account> getAddedAccounts(String userId) {
        List<Account> result = null;
        PreparedStatement ps = prep("SELECT * FROM addedaccounts WHERE user_person_nr = ?");
        try {
            ps.setString(1, userId);
            result = (List<Account>) new ObjectMapper<>(Account.class).map(ps.executeQuery());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result; // return added Accounts;
    }

    public static void cardPay(long cardNr, String targetAccount, double amount) {

        CallableStatement cs = Database.getInstance().cardPay(cardNr, targetAccount, amount);

        try {
            cs.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

}