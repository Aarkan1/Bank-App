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

    public static PreparedStatement prep(String SQLQuery) {
        return Database.getInstance().prepareStatement(SQLQuery);
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
        return getTransactions(accountNr, 0, 10);
    }

    public static List<Transaction> getTransactions(long accountNr, int offset) {
        return getTransactions(accountNr, offset, offset + 10);
    }

    public static List<Transaction> getTransactions(long accountNr, int offset, int limit) {
        List<Transaction> result = null;
        // TODO: Fix SQL syntax error
        PreparedStatement ps = prep("SELECT * FROM transactions WHERE account_from = " + accountNr + " OFFSET " + offset + " LIMIT " + limit);
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

    public static void cardPay(long cardNr, long targetAccount, float amount) {

       CallableStatement cs = Database.getInstance().cardPay(cardNr, targetAccount, amount);

        try {
            cs.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

}