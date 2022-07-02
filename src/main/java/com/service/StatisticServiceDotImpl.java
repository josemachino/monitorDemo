package com.service;

public class StatisticServiceDotImpl implements StatisticService {
    long numberDots;

    public StatisticServiceDotImpl() {
        this.numberDots = 0;
    }

    public long getNumberDots() {
        return this.numberDots;
    }

    @Override
    public void calculate(String data) {
        this.numberDots = data.chars().mapToObj(strInt -> (char) strInt).filter(chr -> chr.equals('.')).count();
    }

    @Override
    public void print() {
        System.out.println("Number of dots: " + this.numberDots);
    }

}
