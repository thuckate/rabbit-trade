package com.rabbit.crawl.analyzer.lotto535;
import com.rabbit.crawl.analyzer.lotto535.dto.DrawResult;
import com.rabbit.crawl.analyzer.lotto535.dto.ProbabilityAnalysis;
import com.rabbit.crawl.vietlotcrawler.constants.VietlotType;

import java.sql.*;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Vietlot535Analyzer {

  private static final String DB_URL = "jdbc:mysql://localhost:3306/vietlott?allowPublicKeyRetrieval=true&useSSL=false";
  private static final String DB_USER = "root";
  private static final String DB_PASSWORD = "root";

  public static void main(String[] args) {
    try {
      List<DrawResult> history = loadHistoricalData(VietlotType.VIETLOT_535);
      if (history.isEmpty()) {
        System.out.println("Không có dữ liệu lịch sử");
        return;
      }

      ProbabilityAnalysis analysis = analyzeNumbers(history);

      System.out.println("=== PHÂN TÍCH XÁC SUẤT VIETLOTT 5/35 ===");
      System.out.printf("Tổng số kỳ quay phân tích: %d%n", history.size());

      System.out.println("\nTop 15 số xuất hiện nhiều nhất:");
      printTopNumbers(analysis.getNumberFrequency());

      System.out.println("\nTop 10 cặp số thường đi cùng nhau:");
      printTopPairs(analysis.getPairFrequency());

      System.out.println("\nTop 5 bộ 3 số thường xuất hiện cùng nhau:");
      printTopTriples(analysis.getTripleFrequency());

      System.out.println("\nBộ số đề xuất cho lần quay tới:");
      List<Integer> suggestedNumbers = generateSuggestedNumbers(analysis);
      System.out.println("Đề xuất 1: " + suggestedNumbers);

      List<Integer> coldNumbers = generateColdNumbers(analysis, history.size());
      System.out.println("Đề xuất 2 (số lâu chưa về): " + coldNumbers);

    } catch (SQLException e) {
      System.err.println("Lỗi kết nối database: " + e.getMessage());
    }
  }

  private static List<DrawResult> loadHistoricalData(VietlotType type) throws SQLException {
    List<DrawResult> results = new ArrayList<>();

    String query = "SELECT type, date, drawId, draw FROM %s ORDER BY date DESC".formatted(type.getTable());

    try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
         Statement stmt = conn.createStatement();
         ResultSet rs = stmt.executeQuery(query)) {

      while (rs.next()) {
        String draw = rs.getString("draw");
        String[] full = draw.split("\\|");
        String[] strBall = full[0].split(":");
        results.add(new DrawResult(
                Integer.parseInt(full[1]),
                Integer.parseInt(strBall[0]),
                Integer.parseInt(strBall[1]),
                Integer.parseInt(strBall[2]),
                Integer.parseInt(strBall[3]),
                Integer.parseInt(strBall[4])
        ));
      }
    }
    return results;
  }

  private static ProbabilityAnalysis analyzeNumbers(List<DrawResult> history) {
    // 1. Khởi tạo các Map để lưu thống kê
    Map<Integer, Integer> numberFrequency = new HashMap<>();     // Tần suất số thường (1-35)
    Map<Integer, Integer> specialNumberFreq = new HashMap<>();   // Tần suất số đặc biệt (1-10)
    Map<String, Integer> pairFrequency = new HashMap<>();        // Tần suất cặp số
    Map<String, Integer> tripleFrequency = new HashMap<>();      // Tần suất bộ 3 số
    Map<Integer, Integer> lastAppearance = new HashMap<>();      // Lần cuối số xuất hiện
    Map<Integer, Integer> lastSpecialAppearance = new HashMap<>(); // Lần cuối số đặc biệt xuất hiện

    // 2. Khởi tạo giá trị mặc định
    IntStream.rangeClosed(1, 35).forEach(i -> {
      numberFrequency.put(i, 0);
      lastAppearance.put(i, 0);
    });

    IntStream.rangeClosed(1, 12).forEach(i -> {
      specialNumberFreq.put(i, 0);
      lastSpecialAppearance.put(i, 0);
    });

    // 3. Phân tích từng kết quả
    int drawCount = 0;
    for (DrawResult draw : history) {
      drawCount++;
      int[] numbers = draw.getNumbers();
      int specialNum = draw.getSpecialNumber();

      // 3.1. Cập nhật số thường
      for (int num : numbers) {
        numberFrequency.merge(num, 1, Integer::sum);
        lastAppearance.put(num, drawCount);
      }

      // 3.2. Cập nhật số đặc biệt
      specialNumberFreq.merge(specialNum, 1, Integer::sum);
      lastSpecialAppearance.put(specialNum, drawCount);

      // 3.3. Thống kê cặp số (thường + thường)
      for (int i = 0; i < numbers.length; i++) {
        for (int j = i + 1; j < numbers.length; j++) {
          String pairKey = orderedKey(numbers[i], numbers[j]);
          pairFrequency.merge(pairKey, 1, Integer::sum);
        }
      }

      // 3.4. Thống kê bộ 3 số
      for (int i = 0; i < numbers.length; i++) {
        for (int j = i + 1; j < numbers.length; j++) {
          for (int k = j + 1; k < numbers.length; k++) {
            String tripleKey = orderedKey(numbers[i], numbers[j], numbers[k]);
            tripleFrequency.merge(tripleKey, 1, Integer::sum);
          }
        }
      }
    }

    // 4. Trả về đối tượng phân tích
    return new ProbabilityAnalysis(
            numberFrequency,
            specialNumberFreq,
            pairFrequency,
            tripleFrequency,
            lastAppearance,
            lastSpecialAppearance,
            drawCount
    );
  }

  // Helper method tạo key có thứ tự
  private static String orderedKey(int... numbers) {
    return Arrays.stream(numbers)
            .sorted()
            .mapToObj(String::valueOf)
            .collect(Collectors.joining("-"));
  }

  private static List<Integer> generateSuggestedNumbers(ProbabilityAnalysis analysis) {
    // Lấy các số có tần suất cao và thường xuất hiện cùng nhau
    Map<Integer, Integer> frequency = analysis.getNumberFrequency();
    Map<String, Integer> pairs = analysis.getPairFrequency();

    // Chọn top 20 số phổ biến
    List<Integer> topNumbers = frequency.entrySet().stream()
            .sorted(Map.Entry.<Integer, Integer>comparingByValue().reversed())
            .limit(20)
            .map(Map.Entry::getKey)
            .toList();

    // Tìm các cặp phổ biến trong top số
    Map<Integer, Integer> numberScores = new HashMap<>();
    for (Integer num : topNumbers) {
      int score = frequency.get(num);
      // Tăng điểm cho các số có nhiều cặp phổ biến
      for (Integer otherNum : topNumbers) {
        if (!num.equals(otherNum)) {
          String pairKey = Math.min(num, otherNum) + "-" + Math.max(num, otherNum);
          score += pairs.getOrDefault(pairKey, 0);
        }
      }
      numberScores.put(num, score);
    }

    // Chọn 10 số có điểm cao nhất
    List<Integer> selected = numberScores.entrySet().stream()
            .sorted(Map.Entry.<Integer, Integer>comparingByValue().reversed())
            .limit(10)
            .map(Map.Entry::getKey)
            .collect(Collectors.toList());

    // Xáo trộn và chọn 5 số
    Collections.shuffle(selected);
    return selected.stream()
            .sorted()
            .limit(5)
            .collect(Collectors.toList());
  }

  private static List<Integer> generateColdNumbers(ProbabilityAnalysis analysis, int totalDraws) {
    // Chọn các số lâu chưa về (cold numbers)
    Map<Integer, Integer> lastAppearance = analysis.getLastAppearance();
    int currentDraw = analysis.getTotalDraws();

    return lastAppearance.entrySet().stream()
            .sorted(Comparator.comparingInt(Map.Entry::getValue))
            .limit(15) // Lấy top 15 số lâu nhất chưa về
            .map(Map.Entry::getKey)
            .sorted()
            .limit(5)
            .collect(Collectors.toList());
  }

  private static void printTopNumbers(Map<Integer, Integer> frequency) {
    int totalDraws = frequency.values().stream().mapToInt(Integer::intValue).sum() / 5;

    frequency.entrySet().stream()
            .sorted(Map.Entry.<Integer, Integer>comparingByValue().reversed())
            .limit(15)
            .forEach(entry -> System.out.printf("Số %2d: %4d lần (%.2f%%) - Cách đây %3d kỳ%n",
                    entry.getKey(),
                    entry.getValue(),
                    (entry.getValue() * 100.0 / totalDraws),
                    totalDraws - entry.getValue()));
  }

  private static void printTopPairs(Map<String, Integer> frequency) {
    frequency.entrySet().stream()
            .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
            .limit(10)
            .forEach(entry -> System.out.printf("Cặp %5s: %3d lần%n",
                    entry.getKey(),
                    entry.getValue()));
  }

  private static void printTopTriples(Map<String, Integer> frequency) {
    frequency.entrySet().stream()
            .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
            .limit(5)
            .forEach(entry -> System.out.printf("Bộ %8s: %2d lần%n",
                    entry.getKey(),
                    entry.getValue()));
  }
}

