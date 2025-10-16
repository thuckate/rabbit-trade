package com.rabbit.crawl.vietlotcrawler.constants;

public enum VietlotType {
  VIETLOT_535("535"),
  VIETLOT_645("645"),
  VIETLOT_655("655");

  private String type;
  private static final String BASED_URL = "https://vietlott.vn/vi/trung-thuong/ket-qua-trung-thuong/%s";
  private static final String WIN_URL = "https://vietlott.vn/vi/trung-thuong/ket-qua-trung-thuong/%s?id=%s&nocatche=1";

  VietlotType(String type) {
    this.type = type;
  }

  public String getType() {
    return type;
  }

  public String getBasedUrl() {
    return String.format(BASED_URL, type);
  }

  public String getWinUrl() {
    return String.format(WIN_URL, type, "%s");
  }

  public int getTotalSet() {
    return switch (type) {
      case "655" -> 8_000_000;
      case "645" -> 5_000_000;
      default -> 3_000_000;
    };
  }

  public int getPool() {
    return switch (type) {
      case "655" -> 55;
      case "645" -> 45;
      default -> 35;
    };
  }

  public int getCount() {
    if (type == "535") {
      return 5;
    }

    return 6;
  }

  public static void main(String[] args) {
    VietlotType.VIETLOT_655.getTotalSet();
  }

  public String getTable() {
    return switch (type) {
      case "535" -> "lotto535";
      case "645" -> "mega645";
      case "655" -> "power655";
      default -> null;
    };
  }
}
