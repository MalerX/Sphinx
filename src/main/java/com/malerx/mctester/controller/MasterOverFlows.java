package com.malerx.mctester.controller;

import com.malerx.mctester.model.Chain;
import com.malerx.mctester.service.BearerRawTestChain;
import com.malerx.mctester.service.BearerRawTestChainImpl;
import com.malerx.mctester.service.log.ResultTestSave;
import com.malerx.mctester.service.validator.ValidationChain;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;

@Service
@RequiredArgsConstructor
@Slf4j
public class MasterOverFlows {
    private final static int THREADS = 3;

    private final BlockingQueue<Chain> rawData = new LinkedBlockingDeque<>();
    private final BlockingQueue<String> readyData = new LinkedBlockingDeque<>();
    private final Executor executor = Executors.newFixedThreadPool(THREADS);

    private final ValidationChain validation;
    private final BearerRawTestChain bearerChain;
    private final ResultTestSave save;

    void start() {
        setQueues();
        setPoolThreads();
    }

    private void setQueues() {
        bearerChain.setQueue(rawData);
        validation.setQueues(rawData, readyData);
        save.setQueue(readyData);
    }

    private void setPoolThreads() {
        executor.execute(bearerChain);
        executor.execute(validation);
        executor.execute(save);
    }
}
