package com.malerx.mctester.service;

import com.malerx.mctester.model.Chain;
import com.malerx.mctester.repositories.MongoTestDataRepositories;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.concurrent.BlockingQueue;

@Service
@RequiredArgsConstructor
@Slf4j
public class BearerRawTestChainImpl implements BearerRawTestChain {
    private final static int QUANTITY = 1000;

    private final MongoTestDataRepositories repositories;
    private BlockingQueue<Chain> rawData;

    @Override
    public void loadTestChain() {
        Page<Chain> page = repositories.findAll(Pageable.ofSize(QUANTITY));
        while (page.hasNext()) {
            page.stream().forEach(chain -> {
                try {
                    rawData.put(chain);
                } catch (InterruptedException e) {
                    log.info("Process has been interrupt.", e);
                    throw new RuntimeException("Process has been interrupt", e);
                }
            });
        }
    }

    @Override
    public void setQueue(@NonNull BlockingQueue<Chain> queue) {
        this.rawData = queue;
    }

    @Override
    public void run() {
        loadTestChain();
    }
}
