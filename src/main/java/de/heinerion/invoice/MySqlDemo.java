package de.heinerion.invoice;

import java.sql.*;

public class MySqlDemo {
  public static void main(String... args) {
    withDriver(() -> createStatement(MySqlDemo::queryUpdate));
  }

  private static void withDriver(Runnable func) {
    try {
      Class.forName("com.mysql.jdbc.Driver");

      func.run();
    } catch (ClassNotFoundException e) {
      handleExceptions(e);
    }
  }

  private static void createStatement(StatementConsumer query) {
    String url = "jdbc:mysql://127.0.0.1:3306/test";
    String user = "user";

    try (Connection connection = DriverManager.getConnection(url, user, "password");
         Statement statement = connection.createStatement()) {
      query.accept(statement);
    } catch (SQLException e) {
      handleExceptions(e);
    }
  }

  private static void handleExceptions(Exception e) {
    throw new RuntimeException(e);
  }

  private static void queryUpdate(Statement statement) throws SQLException {
    statement.executeUpdate("INSERT INTO Tabelle (attribut) VALUES ('bla'), ('keks')");

    try (ResultSet resultSet = statement.executeQuery("SELECT * FROM Tabelle")) {
      while (resultSet.next()) {
        System.out.println(resultSet.getString(2));
      }
    }

    statement.executeUpdate("DELETE FROM Tabelle");
  }

  @FunctionalInterface
  private interface StatementConsumer {
    void accept(Statement statement) throws SQLException;
  }
}
