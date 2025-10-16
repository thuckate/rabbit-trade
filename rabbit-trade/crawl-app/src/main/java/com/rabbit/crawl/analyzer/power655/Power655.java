package com.rabbit.crawl.analyzer.power655;

import com.rabbit.crawl.analyzer.vietlot.service.VietlotRandom;
import com.rabbit.crawl.analyzer.vietlot.service.VietlotRandomImpl;
import com.rabbit.crawl.vietlotcrawler.constants.VietlotType;

import java.sql.SQLException;

public class Power655 {
  public static void main(String[] args) throws SQLException {
    VietlotRandom vietlotRandom = new VietlotRandomImpl();
    vietlotRandom.random(VietlotType.VIETLOT_655);
  }
}
