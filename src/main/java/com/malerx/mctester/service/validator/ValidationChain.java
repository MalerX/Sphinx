package com.malerx.mctester.service.validator;

import com.malerx.mctester.model.Chain;
import lombok.NonNull;

import java.util.concurrent.BlockingQueue;

public interface ValidationChain extends Runnable {

    void validate();

    void setQueues(@NonNull BlockingQueue<Chain> queueRaw, @NonNull BlockingQueue<String> queueReady);
}
