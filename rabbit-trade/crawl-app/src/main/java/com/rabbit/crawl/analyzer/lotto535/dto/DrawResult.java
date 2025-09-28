package com.rabbit.crawl.analyzer.lotto535.dto;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class DrawResult {
  private final int[] numbers;
  private final int specialNumber;
  private final Date drawDate;

  public DrawResult(int specialNumber, int... numbers) {
    this.numbers = Arrays.copyOf(numbers, numbers.length);
    this.specialNumber = specialNumber;
    this.drawDate = new Date();
  }

  public DrawResult(Date drawDate, int specialNumber, int... numbers) {
    this.numbers = Arrays.copyOf(numbers, numbers.length);
    this.specialNumber = specialNumber;
    this.drawDate = drawDate;
  }

  public int[] getNumbers() {
    return Arrays.copyOf(numbers, numbers.length);
  }

  public int getSpecialNumber() {
    return specialNumber;
  }

  public Date getDrawDate() {
    return drawDate;
  }

  public List<Integer> getNumbersAsList() {
    return Arrays.stream(numbers).boxed().collect(Collectors.toList());
  }

  public boolean containsNumber(int number) {
    return Arrays.stream(numbers).anyMatch(n -> n == number) || specialNumber == number;
  }

  public boolean containsSpecialNumber(int number) {
    return specialNumber == number;
  }

  @Override
  public String toString() {
    return Arrays.stream(numbers)
            .mapToObj(String::valueOf)
            .collect(Collectors.joining("-"))
            + " | Special: " + specialNumber;
  }
}
