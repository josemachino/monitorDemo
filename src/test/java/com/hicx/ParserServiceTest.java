package com.hicx;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.nio.file.Path;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.service.ParserService;
import com.service.PlainTextParserService;

public class ParserServiceTest {
    ParserService parserService;
    Path workingDir;

    @BeforeEach
    void setUp() {
        parserService = new PlainTextParserService();
        this.workingDir = Path.of("", "src/test/resources");
    }

    @Test
    public void processFile() throws IOException {
        Path file = this.workingDir.resolve("test.txt");
        parserService.processFile(file.toFile());
        String content = parserService.getContent();
        assertEquals(content, "sedevacante");
    }

    @Test
    void moveFile() throws IOException {
        Path file = this.workingDir.resolve("test2.txt");
        parserService.moveToSubFolder(file.toFile());
        Path newfile = this.workingDir.resolve("processed/test2.txt");
        parserService.processFile(newfile.toFile());
        String content = parserService.getContent();
        assertEquals(content, "sedevacante");
    }

}
