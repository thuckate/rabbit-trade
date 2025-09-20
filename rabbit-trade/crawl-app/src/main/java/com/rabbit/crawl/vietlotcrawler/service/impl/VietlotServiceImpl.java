package com.rabbit.crawl.vietlotcrawler.service.impl;

import com.rabbit.crawl.vietlotcrawler.DbUtils;
import com.rabbit.crawl.vietlotcrawler.constants.VietlotType;
import com.rabbit.crawl.vietlotcrawler.dto.Result;
import com.rabbit.crawl.vietlotcrawler.service.VietlotService;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class VietlotServiceImpl implements VietlotService {
  String url = "jdbc:mysql://localhost:3306/vietlott?allowPublicKeyRetrieval=true&useSSL=false"; // Replace with your DB name
  String user = "root";
  String password = "root";

  @Override
  public void crawl(VietlotType type) {
    bulkInsert(type);
  }

  private void sequenceInsert(VietlotType type) {
    if (type == null) return;

    Result result = getInfoBasedOnUrl(type.getBasedUrl());

    String maxDrawId = result.getDrawId();
    int dbMaxId = getMaxDbDrawId(type);
    for (int i = dbMaxId+1; i <= Integer.parseInt(maxDrawId); i++) {
      String drawId = String.format("%05d", i);
      result = getInfoByDrawId(type.getWinUrl(), drawId);
      result.setType(type.getType());

      insertIntoDB(result, type);
      System.out.println(result);
    }
  }

  private Result getInfoBasedOnUrl(String url) {
    try {
      Document doc = Jsoup.connect(url).get();
      return processingData(doc);
    } catch (IOException e) {
      System.out.println("ERROR getInfoBasedOnUrl: " + e.getMessage());
      return null;
    }
  }

  private Result getInfoByDrawId(String winUrl, String drawId) {
    try {
      String url = String.format(winUrl, drawId);
      Document doc = Jsoup.connect(url).get();
      return processingData(doc);
    } catch (IOException e) {
      System.out.println("ERROR getInfoByDrawId: " + e.getMessage());
      return null;
    }
  }

  private Result processingData(Document doc) {
    // Extract drawId, date
    Element row = doc.select("div.chitietketqua_title h5").first();

    if (row == null) return null;

    String drawId = row.select("b").first().text();
    String date = row.select("b").last().text();
    String newDate = LocalDate.parse(date, DateTimeFormatter.ofPattern("dd/MM/yyyy"))
            .format(DateTimeFormatter.ofPattern("yyyyMMdd"));

    // Extract drawData
    String drawData = doc.select("div.day_so_ket_qua_v2").first().text();

    return new Result(newDate, drawId.replace("#", ""), drawData);
  }

  private void insertIntoDB(Result result, VietlotType type) {
    String insertSql = "INSERT INTO %s (type, date, drawId, draw) VALUES (?, ?, ?, ?)".formatted(type.getTable());
    DbUtils.executeStr(insertSql, result.getType(), result.getDate(), result.getDrawId(), result.getDraw());
  }

  private void bulkInsert(VietlotType type) {
    if (type == null) return;

    Result result = getInfoBasedOnUrl(type.getBasedUrl());
    List<String[]> bulkResult = new ArrayList<>();
    String maxDrawId = result.getDrawId();
    int dbMaxId = getMaxDbDrawId(type);
    for (int i = dbMaxId+1; i <= Integer.parseInt(maxDrawId); i++) {
      String drawId = String.format("%05d", i);
      result = getInfoByDrawId(type.getWinUrl(), drawId);
      result.setType(type.getType());

      if (bulkResult.size() < 100) {
        bulkResult.add(new String[] {result.getType(), result.getDate(), result.getDrawId(), result.getDraw()});
      } else {
        String sql = "INSERT INTO %s (type, date, drawId, draw) VALUES (?, ?, ?, ?)".formatted(type.getTable());
        DbUtils.executeBulk(sql, bulkResult);
        bulkResult.clear();
      }
    }
  }

  private int getMaxDbDrawId(VietlotType type) {
    String query = "SELECT MAX(CAST(drawId AS UNSIGNED)) max FROM %s".formatted(type.getTable());
    String rs = DbUtils.executeMaxId(query);
    return rs == null ? 0 : Integer.parseInt(rs);
  }
}
