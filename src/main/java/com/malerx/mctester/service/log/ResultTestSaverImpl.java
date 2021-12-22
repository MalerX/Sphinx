package com.malerx.mctester.service.log;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Функционал клаасса -- сохранять результыаты тестирования в текстовый файл.
 */
@Service
@Slf4j
public class ResultTestSaverImpl implements ResultTestSave {
    private final BufferedWriter bf;

    public ResultTestSaverImpl(@Value("${key.pathResult}") String pathResult) throws IOException {
        this.bf = new BufferedWriter(init(pathResult));
    }

    private FileWriter init(String pathResult) throws IOException {
        SimpleDateFormat dateFormatter = new SimpleDateFormat("dd.MM.yyyy-kk:mm");
        String fileName = "result_test_" + dateFormatter.format(new Date()) + ".txt";
        String pathLogFile = pathResult + fileName;
        return new FileWriter(pathLogFile);
    }

    @Override
    public void save(@NonNull String resultTest) throws IOException {
        bf.write(resultTest);
    }

    public void close() {
        try {
            bf.close();
        } catch (IOException e) {
            log.error("Failed close writer.", e);
        }
    }
}
