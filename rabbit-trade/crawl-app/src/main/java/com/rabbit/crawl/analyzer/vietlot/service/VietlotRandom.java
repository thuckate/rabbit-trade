package com.rabbit.crawl.analyzer.vietlot.service;

import com.rabbit.crawl.vietlotcrawler.constants.VietlotType;

import java.sql.SQLException;

public interface VietlotRandom {
  void random(VietlotType type) throws SQLException;
}
