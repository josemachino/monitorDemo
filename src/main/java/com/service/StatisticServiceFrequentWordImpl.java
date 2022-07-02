package com.service;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class StatisticServiceFrequentWordImpl implements StatisticService {
  String mostUsedWord;

  public StatisticServiceFrequentWordImpl() {
    this.mostUsedWord = "";
  }

  public String getMostUsedWord() {
    return this.mostUsedWord;
  }

  @Override
  public void calculate(String data) {
    Map<String, Long> counterMap = new HashMap<>();

    Stream.of(data.split("[!,.\s%]+")).parallel()
        .collect(Collectors.groupingBy(k -> k, () -> counterMap,
            Collectors.counting()));
    this.mostUsedWord = Collections.max(counterMap.entrySet(), Map.Entry.comparingByValue()).getKey();
  }

  @Override
  public void print() {
    System.out.println("The most used word: " + this.mostUsedWord);
  }

}
