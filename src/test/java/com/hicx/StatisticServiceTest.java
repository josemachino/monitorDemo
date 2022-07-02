package com.hicx;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;

import com.service.StatisticServiceDotImpl;
import com.service.StatisticServiceFrequentWordImpl;
import com.service.StatisticServiceWordImpl;

public class StatisticServiceTest {
    private StatisticServiceWordImpl serviceNumberWord;
    private StatisticServiceDotImpl serviceNumberDots;
    private StatisticServiceFrequentWordImpl serviceFrequentWord;
    private String data;

    @BeforeEach
    void setUp() throws Exception {
        serviceNumberWord = new StatisticServiceWordImpl();
        serviceNumberDots = new StatisticServiceDotImpl();
        serviceFrequentWord = new StatisticServiceFrequentWordImpl();
        data = ".El catolicismo es mi religion, .... mi senior%mi.Rey. es Jesus.";
    }

    @org.junit.jupiter.api.Test
    public void numberOfCorrectWords() {
        serviceNumberWord.calculate(data);
        assertEquals(serviceNumberWord.getNumberOfWords(), 11);
    }

    @org.junit.jupiter.api.Test
    public void numberOfCorrectDots() {
        serviceNumberDots.calculate(data);
        assertEquals(serviceNumberDots.getNumberDots(), 8);
    }

    @org.junit.jupiter.api.Test
    void frequentCorrectWord() {
        serviceFrequentWord.calculate(data);
        assertEquals(serviceFrequentWord.getMostUsedWord(), "mi");
    }

    @org.junit.jupiter.api.Test
    public void numberOfCorrectWordsEmptyString() {
        serviceNumberWord.calculate("");
        assertEquals(serviceNumberWord.getNumberOfWords(), 0);
    }

    @org.junit.jupiter.api.Test
    public void numberOfDotsEmptyString() {
        serviceNumberDots.calculate("");
        assertEquals(serviceNumberDots.getNumberDots(), 0);
    }

    @org.junit.jupiter.api.Test
    void frequentCorrectWordLastOfThem() {
        serviceFrequentWord.calculate("ma ma ma pa pa pa");
        assertEquals(serviceFrequentWord.getMostUsedWord(), "pa");
    }
}
