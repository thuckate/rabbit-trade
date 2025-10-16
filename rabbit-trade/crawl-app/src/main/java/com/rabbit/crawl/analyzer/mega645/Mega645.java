package com.rabbit.crawl.analyzer.mega645;

import com.rabbit.crawl.analyzer.vietlot.service.VietlotRandom;
import com.rabbit.crawl.analyzer.vietlot.service.VietlotRandomImpl;
import com.rabbit.crawl.vietlotcrawler.constants.VietlotType;
import com.rabbit.crawl.vietlotcrawler.service.VietlotService;
import com.rabbit.crawl.vietlotcrawler.service.impl.VietlotServiceImpl;

import java.sql.SQLException;

public class Mega645 {
  public static void main(String[] args) throws SQLException {
    VietlotService service = new VietlotServiceImpl();
    service.crawl(VietlotType.VIETLOT_645);
    VietlotRandom vietlotRandom = new VietlotRandomImpl();
    vietlotRandom.random(VietlotType.VIETLOT_645);
  }
}
