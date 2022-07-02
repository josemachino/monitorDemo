package com.service;

import java.io.File;
import java.io.IOException;

public abstract class ParserService {
    String content;

    public abstract String getContent();

    public abstract void processFile(File file) throws IOException;

    public abstract void moveToSubFolder(File file) throws IOException;
}
