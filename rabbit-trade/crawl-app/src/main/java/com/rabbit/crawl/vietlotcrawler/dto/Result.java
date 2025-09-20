package com.rabbit.crawl.vietlotcrawler.dto;


import java.util.Objects;

public class Result {
  private String date; // Ngày
  private String drawId;  // Kỳ
  private String draw; // Bộ số trúng thưởng
  private String type;

  public Result(String date, String drawId, String draw) {
    this.date = date;
    this.drawId = drawId;
    this.draw = draw;
  }

  public String getDate() {
    return date;
  }

  public void setDate(String date) {
    this.date = date;
  }

  public String getDrawId() {
    return drawId;
  }

  public void setDrawId(String drawId) {
    this.drawId = drawId;
  }

  public String getDraw() {
    return draw;
  }

  public void setDraw(String draw) {
    this.draw = draw;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  @Override
  public String toString() {
    return "Result{" +
            "type = " + type +
            ", date=" + date +
            ", drawId='" + drawId + '\'' +
            ", draw='" + draw + '\'' +
            '}';
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Result result = (Result) o;
    return Objects.equals(date, result.date) && Objects.equals(drawId, result.drawId) && Objects.equals(type, result.type);
  }

  @Override
  public int hashCode() {
    return Objects.hash(date, drawId, type);
  }
}
