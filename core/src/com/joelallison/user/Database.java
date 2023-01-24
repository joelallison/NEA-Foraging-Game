package com.joelallison.user;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Database {

    static String sql;

    public static void main(String[] args) {
        sql = "INSERT INTO users(username, hashed_password, email)" +
                "VALUES ('joelallison', 'asjdlansdasd', 'thatjoelallison@gmail.com')";

        makeConnection("jdbc:postgresql://localhost:5432/levelgentool", "postgres", "password");
    }

    public static void makeConnection(String jdbcURL, String username, String password) {
        try (Connection connection = DriverManager.getConnection(jdbcURL, username, password)) {
            System.out.println("Connected to PostgreSQL server");

            Statement statement = connection.createStatement();

            int rows = statement.executeUpdate(sql);
            if (rows > 0) {
                System.out.println("New user added.");
            }

            connection.close();

        } catch (SQLException e) {
            System.out.println("PostgreSQL Server Connection error");
            e.printStackTrace();
        }
    }
}