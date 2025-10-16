package com.rabbit.crawl.vietlotcrawler;

import com.rabbit.crawl.vietlotcrawler.constants.VietlotType;
import com.rabbit.crawl.vietlotcrawler.dto.Result;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

  public static List<Result> getAll(String sql) {
    List<Result> rs = new ArrayList<>();
    try (Connection conn = DriverManager.getConnection(url, user, password)) {
      PreparedStatement stmt = conn.prepareStatement(sql);
      ResultSet resultSet = stmt.executeQuery();
      while (resultSet.next()) {
        String type = resultSet.getString("type");
        String date = resultSet.getString("date");
        String drawId = resultSet.getString("drawId");
        String draw = resultSet.getString("draw");
        rs.add(new Result(date, drawId, draw, type));
      }
    } catch (SQLException e) {
      return new ArrayList<>();
    }

    return rs;
  }

  public static Map<String, String> getAllMap(String sql) {
    Map<String, String> rs = new HashMap<>();
    try (Connection conn = DriverManager.getConnection(url, user, password)) {
      PreparedStatement stmt = conn.prepareStatement(sql);
      ResultSet resultSet = stmt.executeQuery();
      while (resultSet.next()) {
        String type = resultSet.getString("type");
        String date = resultSet.getString("date");
        String drawId = resultSet.getString("drawId");
        String draw = resultSet.getString("draw");
        rs.put(draw, drawId);
      }
    } catch (SQLException e) {
      return new HashMap<>();
    }

    return rs;
  }

  public static String loadNewestDraw(VietlotType type) throws SQLException {

    String query = "SELECT draw FROM %s ORDER BY drawId DESC LIMIT 1".formatted(type.getTable());

    try (Connection conn = DriverManager.getConnection(url, user, password);
         Statement stmt = conn.createStatement();
         ResultSet rs = stmt.executeQuery(query)) {

      while (rs.next()) {
        return rs.getString("draw");
      }
    }

    return null;
  }
}
