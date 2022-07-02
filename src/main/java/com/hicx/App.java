package com.hicx;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.WatchService;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import com.service.ParserService;
import com.service.PlainTextParserService;
import com.service.StatisticService;
import com.service.StatisticServiceDotImpl;
import com.service.StatisticServiceFrequentWordImpl;
import com.service.StatisticServiceWordImpl;

import java.nio.file.Paths;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;

/**
 * Application that monitor a folder and print statistics of the file
 *
 */
public class App {
    private WatchService watcher;

    private Path path;

    private List<StatisticService> stats;

    private Map<String, ParserService> parsers;

    public App(Path path) throws IOException {
        this.stats = new ArrayList<>();
        this.parsers = new HashMap<>();
        this.path = path;
        watcher = FileSystems.getDefault().newWatchService();
        this.path.register(watcher, StandardWatchEventKinds.OVERFLOW, StandardWatchEventKinds.ENTRY_CREATE,
                StandardWatchEventKinds.ENTRY_DELETE, StandardWatchEventKinds.ENTRY_MODIFY);
        this.stats.add(new StatisticServiceDotImpl());
        this.stats.add(new StatisticServiceWordImpl());
        this.stats.add(new StatisticServiceFrequentWordImpl());
        this.parsers.put("text/plain", new PlainTextParserService());
    }

    /**
     * This method show statistics
     * 
     * @param contentFile
     */
    private void showStats(String contentFile) {
        this.stats.forEach(stat -> {
            stat.calculate(contentFile);
            stat.print();
        });
    }

    /**
     * This method parses the file. If the file is not supported in the parser shows
     * a message that file is not supported.
     * 
     * @param file
     * @throws IOException
     */
    private void processFile(File file) throws IOException {
        String typeFile = Files.probeContentType(file.toPath());
        ParserService parser = this.parsers.get(typeFile);
        if (Objects.nonNull(parser)) {
            parser.processFile(file);
            showStats(parser.getContent());
            parser.moveToSubFolder(file);
        } else {
            System.out.println("No parser for the " + typeFile + " file.");
        }

    }

    public void handleEvents() throws InterruptedException {
        // start to process the data files
        while (true) {
            // start to handle the file change event
            final WatchKey key = watcher.take();
            for (WatchEvent<?> event : key.pollEvents()) {
                // get event type
                final WatchEvent.Kind<?> kind = event.kind();

                // get file name
                @SuppressWarnings("unchecked")
                final WatchEvent<Path> pathWatchEvent = (WatchEvent<Path>) event;
                final Path fileName = pathWatchEvent.context();

                if (kind == StandardWatchEventKinds.ENTRY_CREATE) {
                    // create a new thread to monitor the new file
                    new Thread(new Runnable() {
                        public void run() {
                            File file = new File(path.toFile().getAbsolutePath() + "/" + fileName);
                            boolean exist;
                            long size = 0;
                            long lastModified = 0;
                            int sameCount = 0;
                            while (exist = file.exists()) {
                                // if the 'size' and 'lastModified' attribute keep same for 3 times,
                                // then we think the file was transferred successfully
                                if (size == file.length() && lastModified == file.lastModified()) {
                                    if (++sameCount >= 3) {
                                        break;
                                    }
                                } else {
                                    size = file.length();
                                    lastModified = file.lastModified();
                                }
                                try {
                                    Thread.sleep(500);
                                } catch (InterruptedException e) {
                                    return;
                                }
                            }
                            // if the new file was cancelled or deleted
                            if (!exist) {
                                return;
                            } else {
                                try {
                                    processFile(file);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }).start();
                }
            }

            // IMPORTANT: the key must be reset after processed
            if (!key.reset()) {
                return;
            }
        }
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        if (args.length > 0) {
            System.out.println("Monitoring in " + args[0]);
            new App(Paths.get(args[0])).handleEvents();
        } else
            System.out.println("No folder provided");
    }
}
