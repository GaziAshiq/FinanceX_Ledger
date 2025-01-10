package com.financex.financex_ledger;

import java.sql.*;

public class DatabaseConnection {
    public static String getPasswordByEmail(String email) {
        String query = "SELECT password FROM Users WHERE email = ?";

        try (Connection conn = connect()) {
            assert conn != null;
            try (PreparedStatement statement = conn.prepareStatement(query)) {
                statement.setString(1, email);
                ResultSet rs = statement.executeQuery();

                if (rs.next()) {
                    return rs.getString("password");
                }
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving password: " + e.getMessage());
        }
        return null;
    }

    private static final String URL = "jdbc:sqlite:financex_ledger";

    public static Connection connect() {
        try {
            return DriverManager.getConnection(URL);
        } catch (SQLException e) {
            System.out.println("Connection failed: " + e.getMessage());
            return null;
        }
    }

    public static void initializeDatabase() {
        String createUsersTableSQL = """
                    CREATE TABLE IF NOT EXISTS Users (
                        user_id INTEGER PRIMARY KEY AUTOINCREMENT,
                        name TEXT NOT NULL,
                        email TEXT UNIQUE NOT NULL,
                        password TEXT NOT NULL
                    );
                """;

        String createLoansTableSQL = """
                    CREATE TABLE IF NOT EXISTS Loans (
                        loan_id INTEGER PRIMARY KEY AUTOINCREMENT,
                        user_id INTEGER NOT NULL,
                        principal_amount REAL NOT NULL,
                        interest_rate REAL NOT NULL,
                        repayment_period INTEGER NOT NULL,
                        outstanding_balance REAL NOT NULL,
                        status TEXT NOT NULL,
                        created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                        FOREIGN KEY(user_id) REFERENCES Users(user_id)
                    );
                """;

        try (Connection connection = connect()) {
            assert connection != null;
            try (Statement statement = connection.createStatement()) {
                statement.execute(createUsersTableSQL);
                statement.execute(createLoansTableSQL);
                System.out.println("Database initialized successfully.");
            }
        } catch (SQLException e) {
            System.out.println("Error initializing database: " + e.getMessage());
        }
    }

    public static void createTransactionTableForUser(Connection conn, int userId) {
        String tableName = "user_" + userId + "_transactions";
        String createTableSQL = """
                    CREATE TABLE IF NOT EXISTS %s (
                        transaction_id INTEGER PRIMARY KEY AUTOINCREMENT,
                        transaction_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                        description TEXT NOT NULL,
                        debit REAL DEFAULT 0,
                        credit REAL DEFAULT 0,
                        balance REAL NOT NULL,
                        savings REAL DEFAULT 0,
                        savings_percentages REAL DEFAULT 0
                    );
                """.formatted(tableName);

        try (Statement statement = conn.createStatement()) {
            statement.execute(createTableSQL);
            System.out.println("Transaction table created successfully for user " + userId);
        } catch (SQLException e) {
            System.out.println("Failed to create transaction table for user " + userId + ": " + e.getMessage());
        }
    }

    public static double addTransaction(int userId, String description, double debit, double credit) {
        String tableName = "user_" + userId + "_transactions";

        // Ensure amounts are positive
        if (debit < 0 || credit < 0) {
            System.out.println("Debit or credit amount cannot be negative.");
            return -1; // Indicate failure
        }

        double currentBalance = getUserBalance(userId);
        double newBalance = currentBalance + credit - debit;

        // Check for invalid balances (e.g., overdraft)
        if (newBalance < 0) {
            System.out.println("Transaction would result in a negative balance. Operation not allowed.");
            return -1; // Indicate failure
        }

        try (Connection connection = connect();
             PreparedStatement insertStatement = connection.prepareStatement(
                     "INSERT INTO " + tableName + " (description, debit, credit, balance) VALUES (?, ?, ?, ?)")) {

            // Insert transaction
            insertStatement.setString(1, description);
            insertStatement.setDouble(2, debit);
            insertStatement.setDouble(3, credit);
            insertStatement.setDouble(4, newBalance);
            insertStatement.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Error adding transaction: " + e.getMessage());
            return -1; // Indicate failure
        }

        return newBalance;
    }


    public static String getUserName(int userId) {
        String query = "SELECT name FROM Users WHERE user_id = ?";
        try (Connection connection = connect(); PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, userId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getString("name");
            }
        } catch (SQLException e) {
            System.out.println("Error fetching user name: " + e.getMessage());
        }
        return null;
    }

    public static double getUserBalance(int userId) {
        String query = "SELECT SUM(balance) AS total_balance FROM user_" + userId + "_transactions";
        try (Connection connection = connect(); Statement statement = connection.createStatement(); ResultSet resultSet = statement.executeQuery(query)) {
            if (resultSet.next()) {
                return resultSet.getDouble("total_balance");
            }
        } catch (SQLException e) {
            System.out.println("Error fetching balance: " + e.getMessage());
        }
        return 0.0;
    }

    public static double updateBalance(int userId, double change) {
        String query = "SELECT SUM(balance) AS total_balance FROM user_" + userId + "_transactions";
        double currentBalance = 0.0;

        try (Connection connection = connect(); Statement statement = connection.createStatement(); ResultSet resultSet = statement.executeQuery(query)) {
            if (resultSet.next()) {
                currentBalance = resultSet.getDouble("total_balance");
            }
        } catch (SQLException e) {
            System.out.println("Error fetching balance: " + e.getMessage());
        }

        return currentBalance + change;
    }

    public static double getUserSavings(int userId) {
        String query = "SELECT SUM(savings) AS total_savings FROM user_" + userId + "_transactions";
        try (Connection connection = connect(); Statement statement = connection.createStatement(); ResultSet resultSet = statement.executeQuery(query)) {
            if (resultSet.next()) {
                return resultSet.getDouble("total_savings");
            }
        } catch (SQLException e) {
            System.out.println("Error fetching savings: " + e.getMessage());
        }
        return 0.0;
    }

    public static double getUserLoan(int userId) {
        String query = "SELECT SUM(outstanding_balance) AS total_loan FROM Loans WHERE user_id = ?";
        try (Connection connection = connect(); PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, userId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getDouble("total_loan");
            }
        } catch (SQLException e) {
            System.out.println("Error fetching loan: " + e.getMessage());
        }
        return 0.0;
    }
}