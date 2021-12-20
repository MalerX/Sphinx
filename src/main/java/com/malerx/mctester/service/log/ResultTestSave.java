package com.malerx.mctester.service.log;

import lombok.NonNull;

import java.io.IOException;
import java.util.concurrent.BlockingQueue;

public interface ResultTestSave extends Runnable {
    void save(@NonNull String resultTest) throws IOException;

    void setQueue(@NonNull BlockingQueue<String> queue);
}
