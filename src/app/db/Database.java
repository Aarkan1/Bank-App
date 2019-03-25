package app.db;

import java.sql.*;
import java.util.HashMap;

public class Database {
    private static Database ourInstance = new Database();

    public static Database getInstance() {
        return ourInstance;
    }

    private Database() {
        connectToDb();
    }

    final String connectionURL = "jdbc:mysql://localhost/bank_app?user=root&password=mysql&serverTimezone=UTC";
    private Connection conn = null;
    private HashMap<String, PreparedStatement> preparedStatements = new HashMap<>();


    /**
     * Returns a cached PreparedStatement if possible, else caches it for future use
     */
    public PreparedStatement prepareStatement(String SQLQuery) {
        PreparedStatement ps = preparedStatements.get(SQLQuery);
        if (ps == null) {
            try {
                ps = conn.prepareStatement(SQLQuery);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return ps;
    }

    public CallableStatement cardPay(long cardNr, String targetAccount, double amount) {

        CallableStatement stmt = null;
        try {
            stmt = conn.prepareCall("{call card_pay(?, ?, ?)}");
            stmt.setLong(1, cardNr);
            stmt.setString(2, targetAccount);
            stmt.setDouble(3, amount);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return stmt;
    }

    public CallableStatement callableStatement(String procedure, String[] params) {

        String param = String.join(", ", params);

        System.out.println(param);

        CallableStatement stmt = null;
        try {
            stmt = conn.prepareCall("{call " + procedure + "(" + param + ")}");
//            stmt.setString(1, param);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return stmt;
    }

    private void connectToDb() {
        try {
            conn = DriverManager.getConnection(connectionURL);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
