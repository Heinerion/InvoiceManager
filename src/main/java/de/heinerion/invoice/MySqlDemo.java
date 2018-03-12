package de.heinerion.invoice;

import java.sql.*;

public class MySqlDemo {
  public static void main(String... args) {
    try {
      Class.forName("com.mysql.jdbc.Driver");

      try (Connection connection = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/test", "user", "password")) {
        try (Statement statement = connection.createStatement()) {
          statement.executeUpdate("INSERT INTO Tabelle (attribut) VALUES ('bla'), ('keks')");

          try (ResultSet resultSet = statement.executeQuery("SELECT * FROM Tabelle")) {
            while (resultSet.next()) {
              System.out.println(resultSet.getString(2));
            }
          }

          statement.executeUpdate("DELETE FROM Tabelle");
        }
      } catch (SQLException e) {
        handleExceptions(e);
      }
    } catch (ClassNotFoundException e) {
      handleExceptions(e);
    }
  }

  private static void handleExceptions(Exception e) {
    e.printStackTrace();
  }
}
