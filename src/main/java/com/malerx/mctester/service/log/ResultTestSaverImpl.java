package com.malerx.mctester.service.log;

import lombok.NonNull;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * Функционал клаасса -- сохранять результыаты тестирования в текстовый файл.
 */
@Service
@Slf4j
public class ResultTestSaverImpl implements ResultTestSave {
    private static final SimpleDateFormat dateFormatter = new SimpleDateFormat("dd.MM.yyyy-kk:mm");
    private final PrintWriter printWriter;
    private BlockingQueue<String> readyData;

    public ResultTestSaverImpl(@Value("${key.pathResult}") String pathResult) throws IOException {
        this.printWriter = new PrintWriter(new BufferedWriter(init(pathResult)));
    }

    private FileWriter init(String pathResult) throws IOException {
        String fileName = "result_test_" + dateFormatter.format(new Date()) + ".txt";
        String pathLogFile = pathResult + fileName;
        return new FileWriter(pathLogFile);
    }

    @Override
    public void save(@NonNull String resultTest) throws IOException {
        printWriter.println(resultTest);
    }

    @Override
    public void setQueue(@NonNull BlockingQueue<String> queue) {
        this.readyData = queue;
    }

    public void close() {
        printWriter.close();
    }

    @Override
    public void run() {
        try {
            while (!readyData.isEmpty()) {
                try {
                    String result = Objects.requireNonNull(readyData.poll(500, TimeUnit.MILLISECONDS));
                    save(result);
                } catch (InterruptedException | IOException e) {
                    log.warn("Fail processed", e);
                }
            }
        } finally {
            close();
        }
    }
}
