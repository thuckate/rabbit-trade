package com.rabbit.crawl;

import java.time.LocalDate;

public class Result {
  private LocalDate date; // Ngày
  private String period;  // Kỳ
  private String numbers; // Bộ số trúng thưởng

  public Result(LocalDate date, String period, String numbers) {
    this.date = date;
    this.period = period;
    this.numbers = numbers;
  }

  public LocalDate getDate() {
    return date;
  }

  public void setDate(LocalDate date) {
    this.date = date;
  }

  public String getPeriod() {
    return period;
  }

  public void setPeriod(String period) {
    this.period = period;
  }

  public String getNumbers() {
    return numbers;
  }

  public void setNumbers(String numbers) {
    this.numbers = numbers;
  }

  @Override
  public String toString() {
    return "Result{" +
            "date=" + date +
            ", period='" + period + '\'' +
            ", numbers='" + numbers + '\'' +
            '}';
  }
}
