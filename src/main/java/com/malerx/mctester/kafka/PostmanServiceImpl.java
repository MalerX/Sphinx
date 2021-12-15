package com.malerx.mctester.kafka;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Функционал класса -- отправка и чтение сообщений в/из Kafka.
 */
@Service
@RequiredArgsConstructor
public class PostmanServiceImpl implements PostmanService {
    @Getter
    private final static BlockingQueue<String> queue = new LinkedBlockingQueue<>();
    private final KafkaTemplate<String, String> kafkaTemplate;
    @Value("${kafka.topics.in}")
    private String topicIn;
    @Value("${kafka.groupId}")
    private String groupId;

    @Override
    public void send(@NonNull String topicOut, @NonNull String outMessage) {
        kafkaTemplate.send(topicOut, outMessage);
    }

    @Override
    @KafkaListener(id = "sphinx", topics = "${kafka.topics.in}", containerFactory = "singleFactory")
    public void consume(@NonNull String inMessage) throws InterruptedException {
        queue.put(inMessage);
    }
}
