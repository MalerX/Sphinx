package com.malerx.mctester.service.log;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Функционал клаасса -- сохранять результыаты тестирования в текстовый файл.
 */
@Service
@Slf4j
public class ResultTestSaverImpl implements ResultTestSave {
    private static final SimpleDateFormat dateFormatter = new SimpleDateFormat("dd.MM.yyyy-kk:mm");
    private final PrintWriter printWriter;

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

    public void close() {
        printWriter.close();
    }
}
