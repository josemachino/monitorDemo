package com.service;

import java.util.Arrays;

public class StatisticServiceWordImpl implements StatisticService {
    long numberOfWords;

    public StatisticServiceWordImpl() {
        this.numberOfWords = 0;
    }

    public long getNumberOfWords() {
        return this.numberOfWords;
    }

    @Override
    public void calculate(String data) {

        this.numberOfWords = Arrays.stream(data.split("[!,.\s%]+")) // split the sentence by space, special character
                .filter(s -> s.matches("[\\w-]+")) // filter only matching words
                .count();
    }

    @Override
    public void print() {
        System.out.println("Number of words: " + numberOfWords);
    }

}