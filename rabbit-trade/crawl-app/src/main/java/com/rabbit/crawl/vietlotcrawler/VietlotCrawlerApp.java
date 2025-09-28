package com.rabbit.crawl.vietlotcrawler;

import com.rabbit.crawl.vietlotcrawler.constants.VietlotType;
import com.rabbit.crawl.vietlotcrawler.service.VietlotService;
import com.rabbit.crawl.vietlotcrawler.service.impl.VietlotServiceImpl;

public class VietlotCrawlerApp {
  public static void main(String[] args) {
    VietlotService service = new VietlotServiceImpl();
    service.crawl(VietlotType.VIETLOT_655);
    service.crawl(VietlotType.VIETLOT_645);
    service.crawl(VietlotType.VIETLOT_535);

//    service.migrate(VietlotType.VIETLOT_535);
//    service.migrate(VietlotType.VIETLOT_645);
//    service.migrate(VietlotType.VIETLOT_655);
  }
}
