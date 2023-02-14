package com.joelallison.user;

import java.sql.*;

public class Database {
    static Connection connection;
    static Statement statement;

    //specific values for my server so I don't have to type them out more than once!
    public static String jdbcURL = "jdbc:postgresql://localhost:5432/levelgentool";
    public static String username = "postgres";
    public static String password = "password";

    public static void makeConnection(String jdbcURL, String username, String password) {
        try {
            connection = DriverManager.getConnection(jdbcURL, username, password);
            System.out.println("Connected to PostgreSQL server");

        } catch (SQLException e) {
            System.out.println("PostgreSQL Server Connection error");
            e.printStackTrace();
        }
    }

    public static ResultSet doSqlQuery(String sql) {
        try {
            statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(sql);
            return rs;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static String doSqlStatement(String sql) {
        try {
            statement = connection.createStatement();
            int rows = statement.executeUpdate(sql);
            if (rows > 0) {
                return "Statement successfully executed.";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "PostgreSQL Server Connection error";
        }

        return null; // if the sql is right, this line should never be executed
    }

    public static void closeConnection() throws SQLException {
        connection.close();
    }
}