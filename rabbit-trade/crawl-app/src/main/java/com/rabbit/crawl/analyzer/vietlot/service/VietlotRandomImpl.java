package com.rabbit.crawl.analyzer.vietlot.service;

import com.rabbit.crawl.analyzer.vietlot.dto.LuckySet;
import com.rabbit.crawl.vietlotcrawler.DbUtils;
import com.rabbit.crawl.vietlotcrawler.constants.VietlotType;
import java.security.SecureRandom;
import java.sql.*;
import java.util.*;

public class VietlotRandomImpl implements VietlotRandom {
  private static final int SPECIAL_POOL = 12;
  // Hiển thị random 5 bộ số trong toàn bộ TOTAL_SETS
  private static final int SELECTED_SETS = 10;

  // Hàm sinh n số không trùng từ 1 đến max
  private static List<Integer> generateUniqueNumbers(SecureRandom random, VietlotType type) {
    List<Integer> pool = new ArrayList<>();
    int max = type.getPool(), count = type.getCount();

    for (int i = 1; i <= max; i++) {
      pool.add(i);
    }

    Collections.shuffle(pool, random);
    List<Integer> result = new ArrayList<>(pool.subList(0, count));
    Collections.sort(result);
    return result;
  }

  // Hàm chọn ngẫu nhiên k phần tử từ list
  private static List<LuckySet> selectRandomSets(List<LuckySet> allSets, int count, SecureRandom random) {
    List<LuckySet> copy = new ArrayList<>(allSets);
    Collections.shuffle(copy, random);
    return new ArrayList<>(copy.subList(0, count));
  }

  @Override
  public void random(VietlotType type) throws SQLException {
    SecureRandom random = new SecureRandom();
    List<LuckySet> allSets = new ArrayList<>();

    Map<Integer, String> gen = generateSetLuckyDraw(allSets, type, random);

    Integer luckyIndex = gen.keySet().stream().findFirst().orElse(-1);
    String lucky = gen.get(luckyIndex);

    // Chọn ngẫu nhiên 5 bộ số trong 100 bộ
    List<LuckySet> selectedSets = selectRandomSets(allSets, SELECTED_SETS, random);

    // In 5 bộ được chọn
    System.out.println("\n🎯 5 bộ số được chọn ngẫu nhiên:");
    String goodLucky = "";
    int goodLuckyInd = -1;
    for (LuckySet set : selectedSets) {
      System.out.printf("Top %d: | Số đặc biệt: %s\n", set.getInd(), set);
      if (set.toString().equals(lucky)) {
        goodLuckyInd = set.getInd();
        goodLucky = set.toString();
      }
    }
    System.out.println("####################");
    System.out.println("####################");
    System.out.printf("#### [%d]: LUCKY = %s%n", luckyIndex, lucky);
    System.out.printf("#### [%d]: GOOD LUCKY = %s%n", goodLuckyInd, goodLucky);
    System.out.println("####################");
    System.out.println("####################");
  }

  private Map<Integer, String> generateSetLuckyDraw(List<LuckySet> allSets, VietlotType type, SecureRandom random) throws SQLException {
    Map<Integer, String> rs = new HashMap<>();
    String newestDraw = DbUtils.loadNewestDraw(type);
    String sql = "SELECT type, date, drawId, draw FROM %s".formatted(type.getTable());
    Map<String, String> dbData = DbUtils.getAllMap(sql);
    for (int i = 0; i < type.getTotalSet();) {
      List<Integer> mainNumbers = generateUniqueNumbers(random, type);
      int specialNumber = random.nextInt(SPECIAL_POOL) + 1;
      LuckySet set = new LuckySet(mainNumbers, specialNumber, i+1, type);

      if (set.toString().equals(newestDraw)) {
        rs.put(set.getInd(), set.toString());
      }

      if (dbData.get(set.toString()) == null) {
        allSets.add(set);
        i++;
      }
    }

    return rs;
  }
}
