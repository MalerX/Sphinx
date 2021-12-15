package com.malerx.mctester.kafka;

import lombok.NonNull;

public interface PostmanService {
    void send(@NonNull String topicOut, @NonNull String outMessage);
    void consume( @NonNull String inMessage) throws InterruptedException;
}
