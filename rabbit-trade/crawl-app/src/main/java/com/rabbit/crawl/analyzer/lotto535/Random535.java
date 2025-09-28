package com.rabbit.crawl.analyzer.lotto535;

import com.rabbit.crawl.analyzer.lotto535.dto.DrawResult;
import com.rabbit.crawl.vietlotcrawler.DbUtils;
import com.rabbit.crawl.vietlotcrawler.constants.VietlotType;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.sql.*;
import java.util.*;

public class Random535 {
  private static final int MAIN_NUMBERS = 5;
  private static final int MAIN_POOL = 35;
  private static final int SPECIAL_POOL = 12;
  private static final int TOTAL_SETS = 3_000_000;
  private static final int SELECTED_SETS = 5;

  private static final String DB_URL = "jdbc:mysql://localhost:3306/vietlott?allowPublicKeyRetrieval=true&useSSL=false";
  private static final String DB_USER = "root";
  private static final String DB_PASSWORD = "root";

  public static void main(String[] args) throws SQLException, NoSuchAlgorithmException {
    SecureRandom random = new SecureRandom();

    List<LottoSet> allSets = new ArrayList<>();
    String newestDraw = loadNewestDraw(VietlotType.VIETLOT_535);
    String lucky = "";
    int luckyIndex = 0;
    String sql = "SELECT type, date, drawId, draw FROM %s".formatted(VietlotType.VIETLOT_535.getTable());
    Map<String, String> dbData = DbUtils.getAllMap(sql);
    for (int i = 0; i < TOTAL_SETS;) {
      List<Integer> mainNumbers = generateUniqueNumbers(random, MAIN_POOL, MAIN_NUMBERS);
      int specialNumber = random.nextInt(SPECIAL_POOL) + 1; // 1–12
      LottoSet set = new LottoSet(mainNumbers, specialNumber, i+1);
      if (set.toString().equals(newestDraw)) {
        lucky = set.toString();
        luckyIndex = set.ind;
      }
      if (dbData.get(set.toString()) == null) {
        allSets.add(set);
        i++;
      }
    }

    // In toàn bộ 100 bộ số
//    System.out.println("📄 100 bộ số được tạo:");
//    int count = 1;
//    for (LottoSet set : allSets) {
//      System.out.printf("Bộ %3d: %s | SB: %2d\n", count++, set.mainNumbers, set.specialNumber);
//    }

    // Chọn ngẫu nhiên 5 bộ số trong 100 bộ
    List<LottoSet> selectedSets = selectRandomSets(allSets, SELECTED_SETS, random);

    // In 5 bộ được chọn
    System.out.println("\n🎯 5 bộ số được chọn ngẫu nhiên:");
    String goodLucky = "";
    int goodLuckyInd = -1;
    for (LottoSet set : selectedSets) {
      System.out.printf("Top %d: | Số đặc biệt: %s\n", set.ind, set);
      if (set.toString().equals(lucky)) {
        goodLuckyInd = set.ind;
        goodLucky = set.toString();
      }
    }
    System.out.println("####################");
    System.out.println("####################");
    System.out.println("#### [%d]: LUCKY = %s".formatted(luckyIndex, lucky));
    System.out.println("#### [%d]: GOOD LUCKY = %s".formatted(goodLuckyInd, goodLucky));
    System.out.println("####################");
    System.out.println("####################");
  }

  // Hàm sinh n số không trùng từ 1 đến max
  private static List<Integer> generateUniqueNumbers(SecureRandom random, int max, int count) {
    List<Integer> pool = new ArrayList<>();
    for (int i = 1; i <= max; i++) {
      pool.add(i);
    }
    Collections.shuffle(pool, random);
    List<Integer> result = new ArrayList<>(pool.subList(0, count));
    Collections.sort(result);
    return result;
  }

  // Hàm chọn ngẫu nhiên k phần tử từ list
  private static List<LottoSet> selectRandomSets(List<LottoSet> allSets, int count, SecureRandom random) {
    List<LottoSet> copy = new ArrayList<>(allSets);
    Collections.shuffle(copy, random);
    return new ArrayList<>(copy.subList(0, count));
  }

  // Class đại diện cho 1 bộ số Lotto 5/35 + số đặc biệt
  private static class LottoSet {
    List<Integer> mainNumbers;
    int specialNumber;
    int ind;

    public LottoSet(List<Integer> mainNumbers, int specialNumber) {
      this.mainNumbers = mainNumbers;
      this.specialNumber = specialNumber;
    }

    public LottoSet(List<Integer> mainNumbers, int specialNumber, int ind) {
      this.mainNumbers = mainNumbers;
      this.specialNumber = specialNumber;
      this.ind = ind;
    }

    @Override
    public String toString() {
      StringBuilder sb = new StringBuilder();
      for (int i = 0; i < mainNumbers.size(); i++) {
        sb.append(String.format("%02d", mainNumbers.get(i)));
        if (i < mainNumbers.size() - 1) {
          sb.append(":");
        }
      }

      sb.append("|").append(String.format("%02d", specialNumber));
      return sb.toString();
    }
  }

  private static String loadNewestDraw(VietlotType type) throws SQLException {

    String query = "SELECT draw FROM %s ORDER BY drawId DESC LIMIT 1".formatted(type.getTable());

    try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
         Statement stmt = conn.createStatement();
         ResultSet rs = stmt.executeQuery(query)) {

      while (rs.next()) {
        return rs.getString("draw");
      }
    }

    return null;
  }
}
