package com.rabbit.crawl.vietlotcrawler.service;

import com.rabbit.crawl.vietlotcrawler.constants.VietlotType;

public interface VietlotService {
  void crawl(VietlotType type);
  void migrate(VietlotType type);
}
