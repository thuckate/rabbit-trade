package com.rabbit.crawl.vietlotcrawler;

import java.sql.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class DbUtils {
  private static String url = "jdbc:mysql://localhost:3306/vietlott?allowPublicKeyRetrieval=true&useSSL=false"; // Replace with your DB name
  private static String user = "root";
  private static String password = "root";
  private DbUtils() {}

  public static boolean executeStr(String sql, String ...params) {
    try (Connection conn = DriverManager.getConnection(url, user, password)) {
      PreparedStatement stmt = conn.prepareStatement(sql);
      int index = 1;
      for (String param : params) {
        stmt.setString(index++, param);
      }
      stmt.executeUpdate();
    } catch (SQLException e) {
      return false;
    }

    return true;
  }

  public static boolean executeBulk(String sql, List<String[]> params) {
    try (Connection conn = DriverManager.getConnection(url, user, password)) {
      PreparedStatement stmt = conn.prepareStatement(sql);
      for (String[] param : params) {
        int index = 1;
        for (String pr : param) {
          stmt.setString(index++, pr);
        }
        stmt.addBatch();
      }
      stmt.executeBatch();
    } catch (SQLException e) {
      return false;
    }

    return true;
  }

  public static String executeMaxId(String sql, String ...params) {
    Map<String, String> rs = new HashMap<>();
    try (Connection conn = DriverManager.getConnection(url, user, password)) {
      PreparedStatement stmt = conn.prepareStatement(sql);
      int index = 1;
      for (String param : params) {
        stmt.setString(index++, param);
      }

      ResultSet result = stmt.executeQuery();

      while (result.next()) {
        return result.getString("max");
      }
    } catch (SQLException e) {
      return null;
    }
    return null;
  }
}
