package com.rabbit.crawl.analyzer.lotto535.dto;

import java.util.*;
import java.util.stream.Collectors;

public class ProbabilityAnalysis {
  private final Map<Integer, Integer> numberFrequency;
  private final Map<Integer, Integer> specialNumberFrequency;
  private final Map<String, Integer> pairFrequency;
  private final Map<String, Integer> tripleFrequency;
  private final Map<Integer, Integer> lastAppearance;
  private final Map<Integer, Integer> lastSpecialAppearance;
  private final int totalDraws;

  public ProbabilityAnalysis(Map<Integer, Integer> numberFrequency,
                             Map<Integer, Integer> specialNumberFrequency,
                             Map<String, Integer> pairFrequency,
                             Map<String, Integer> tripleFrequency,
                             Map<Integer, Integer> lastAppearance,
                             Map<Integer, Integer> lastSpecialAppearance,
                             int totalDraws) {
    this.numberFrequency = numberFrequency;
    this.specialNumberFrequency = specialNumberFrequency;
    this.pairFrequency = pairFrequency;
    this.tripleFrequency = tripleFrequency;
    this.lastAppearance = lastAppearance;
    this.lastSpecialAppearance = lastSpecialAppearance;
    this.totalDraws = totalDraws;
  }

  // Getter methods
  public Map<Integer, Integer> getNumberFrequency() {
    return Collections.unmodifiableMap(numberFrequency);
  }

  public Map<Integer, Integer> getSpecialNumberFrequency() {
    return Collections.unmodifiableMap(specialNumberFrequency);
  }

  public Map<String, Integer> getPairFrequency() {
    return Collections.unmodifiableMap(pairFrequency);
  }

  public Map<String, Integer> getTripleFrequency() {
    return Collections.unmodifiableMap(tripleFrequency);
  }

  public Map<Integer, Integer> getLastAppearance() {
    return Collections.unmodifiableMap(lastAppearance);
  }

  public Map<Integer, Integer> getLastSpecialAppearance() {
    return Collections.unmodifiableMap(lastSpecialAppearance);
  }

  public int getTotalDraws() {
    return totalDraws;
  }

  // Phân tích số thường
  public List<Integer> getHotNumbers(int count) {
    return numberFrequency.entrySet().stream()
            .sorted(Map.Entry.<Integer, Integer>comparingByValue().reversed())
            .limit(count)
            .map(Map.Entry::getKey)
            .collect(Collectors.toList());
  }

  public List<Integer> getColdNumbers(int count) {
    return lastAppearance.entrySet().stream()
            .sorted(Comparator.comparingInt(Map.Entry::getValue))
            .limit(count)
            .map(Map.Entry::getKey)
            .collect(Collectors.toList());
  }

  public double getNumberProbability(int number) {
    if (!numberFrequency.containsKey(number)) return 0;
    return (double) numberFrequency.get(number) / totalDraws;
  }

  // Phân tích số đặc biệt
  public List<Integer> getHotSpecialNumbers(int count) {
    return specialNumberFrequency.entrySet().stream()
            .sorted(Map.Entry.<Integer, Integer>comparingByValue().reversed())
            .limit(count)
            .map(Map.Entry::getKey)
            .collect(Collectors.toList());
  }

  public List<Integer> getColdSpecialNumbers(int count) {
    return lastSpecialAppearance.entrySet().stream()
            .sorted(Comparator.comparingInt(Map.Entry::getValue))
            .limit(count)
            .map(Map.Entry::getKey)
            .collect(Collectors.toList());
  }

  public double getSpecialNumberProbability(int number) {
    if (!specialNumberFrequency.containsKey(number)) return 0;
    return (double) specialNumberFrequency.get(number) / totalDraws;
  }

  // Phân tích cặp số
  public double getPairProbability(int num1, int num2) {
    String pairKey = Math.min(num1, num2) + "-" + Math.max(num1, num2);
    return (double) pairFrequency.getOrDefault(pairKey, 0) / totalDraws;
  }

  // Phân tích mối quan hệ số thường và số đặc biệt
  public Map<Integer, Integer> getNumbersWithSpecial() {
    Map<Integer, Integer> result = new HashMap<>();
    // Triển khai logic thống kê số thường xuất hiện cùng số đặc biệt
    return result;
  }
}
