package com.malerx.mctester.kafka;

import com.malerx.mctester.exceptions.TopicNotFoundException;
import lombok.NonNull;
import org.apache.kafka.clients.consumer.ConsumerRecord;

public interface PostmanService {
    void send(@NonNull String topicOut, @NonNull String outMessage);

    void consume(@NonNull ConsumerRecord<?, ?> inMessage) throws InterruptedException;

    String getAnswer(@NonNull String topic) throws InterruptedException, TopicNotFoundException;
}
