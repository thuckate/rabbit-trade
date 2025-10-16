package com.rabbit.crawl.analyzer.vietlot.dto;

import com.rabbit.crawl.vietlotcrawler.constants.VietlotType;

import java.util.List;

public class LuckySet {
    List<Integer> mainNumbers;
    int specialNumber;
    int ind;

    VietlotType type;

    public LuckySet(List<Integer> mainNumbers, int specialNumber) {
      this.mainNumbers = mainNumbers;
      this.specialNumber = specialNumber;
    }

    public LuckySet(List<Integer> mainNumbers, int specialNumber, int ind) {
      this.mainNumbers = mainNumbers;
      this.specialNumber = specialNumber;
      this.ind = ind;
    }

  public LuckySet(List<Integer> mainNumbers, int specialNumber, int ind, VietlotType type) {
    this.mainNumbers = mainNumbers;
    this.specialNumber = specialNumber;
    this.ind = ind;
    this.type = type;
  }

  @Override
    public String toString() {
      StringBuilder sb = new StringBuilder();
      for (int i = 0; i < mainNumbers.size(); i++) {
        sb.append(String.format("%02d", mainNumbers.get(i)));
        if (i < mainNumbers.size() - 1) {
          sb.append(":");
        }
      }

      if (type == VietlotType.VIETLOT_535) {
        sb.append("|").append(String.format("%02d", specialNumber));
      }

      return sb.toString();
    }

  public List<Integer> getMainNumbers() {
    return mainNumbers;
  }

  public int getSpecialNumber() {
    return specialNumber;
  }

  public int getInd() {
    return ind;
  }

  public VietlotType getType() {
    return type;
  }
}