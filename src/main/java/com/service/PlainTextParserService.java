package com.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Scanner;

public class PlainTextParserService extends ParserService {

    @Override
    public void processFile(File file) throws IOException {
        FileInputStream inputStream = null;
        Scanner sc = null;
        StringBuilder stringBuilder = new StringBuilder();
        try {
            inputStream = new FileInputStream(file);
            sc = new Scanner(inputStream, "UTF-8");
            while (sc.hasNextLine()) {
                stringBuilder.append(sc.nextLine());
            }
            if (sc.ioException() != null) {
                throw sc.ioException();
            }
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
            if (sc != null) {
                sc.close();
            }
        }
        this.content = stringBuilder.toString();

    }

    @Override
    public String getContent() {
        return this.content;
    }

    @Override
    public void moveToSubFolder(File file) throws IOException {
        String sourceDirectory = file.getParent();
        String targetDirectory = sourceDirectory + "/processed";
        File directory = new File(targetDirectory);
        if (!directory.exists()) {
            directory.mkdir();
        }
        targetDirectory = targetDirectory + "/" + file.getName();
        Files.move(Paths.get(file.getAbsolutePath()), Paths.get(targetDirectory), StandardCopyOption.REPLACE_EXISTING);
    }

}
