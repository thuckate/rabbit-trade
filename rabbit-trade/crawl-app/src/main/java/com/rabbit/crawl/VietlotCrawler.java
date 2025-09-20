package com.rabbit.crawl;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class VietlotCrawler {
  private static final String URL_535 = "https://vietlott.vn/vi/trung-thuong/ket-qua-trung-thuong/winning-number-535";
// https://vietlott.vn/vi/trung-thuong/ket-qua-trung-thuong/535?id=00012&nocatche=1
  // current: https://vietlott.vn/vi/trung-thuong/ket-qua-trung-thuong/535
  public static void main(String[] args) throws IOException {
    List<Result> results = crawlData(URL_535);
    System.out.println("Kết quả xổ số Vietlott (Power 6/55):");
    for (Result result : results) {
      System.out.println(result.toString());
    }
  }

  public static List<Result> crawlData(final String url) throws IOException {
    List<Result> results = new ArrayList<>();

    // Kết nối tới trang web và lấy HTML
    Document doc = Jsoup.connect(url).get();

    // Lấy bảng kết quả (dựa trên cấu trúc HTML của trang)
    Elements rows = doc.select("table tbody tr");

    for (Element row : rows) {
      Elements columns = row.select("td");

      if (columns.size() >= 3) {
        // Lấy dữ liệu từ các cột
        String dateStr = columns.get(0).text().trim(); // Ngày
        String period = columns.get(1).text().trim();  // Kỳ
        String numbers = columns.get(2).text().trim(); // Bộ số

        // Chuyển đổi ngày từ String sang LocalDate
        LocalDate date = parseDate(dateStr);

        // Tạo đối tượng Result và thêm vào danh sách
        results.add(new Result(date, period, numbers));
      }
    }

    return results;
  }

  // Hàm chuyển đổi chuỗi ngày sang LocalDate
  private static LocalDate parseDate(String dateStr) {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy", new Locale("vi", "VN"));
    return LocalDate.parse(dateStr, formatter);
  }
}
