package com.malerx.mctester.service;

import com.malerx.mctester.model.Chain;
import lombok.NonNull;

import java.util.concurrent.BlockingQueue;

public interface BearerRawTestChain extends Runnable {
    void loadTestChain();

    void setQueue(@NonNull BlockingQueue<Chain> queue);
}
