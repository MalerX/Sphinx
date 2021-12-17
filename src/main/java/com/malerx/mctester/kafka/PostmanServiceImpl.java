package com.malerx.mctester.kafka;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * Функционал класса -- отправка и чтение сообщений в/из Kafka.
 */
@Service
@RequiredArgsConstructor
public class PostmanServiceImpl implements PostmanService {
    private final BlockingQueue<String> queue = new LinkedBlockingQueue<>();
    private final KafkaTemplate<String, String> kafkaTemplate;
    @Value("${kafka.topics.in}")
    private String topicIn;
    @Value("${kafka.topics.out}")
    private String topicOut;
    @Value("${kafka.groupId}")
    private String groupId;

    @SneakyThrows
    @Override
    public void send(@NonNull String topicOutZ, @NonNull String outMessage) {
        kafkaTemplate.send(topicOut, outMessage);
        kafkaTemplate.flush();
    }

    @Override
    @KafkaListener(id = "sphinx", topics = "${kafka.topics.out}", containerFactory = "kafkaListenerContainerFactory", autoStartup = "true")
    public void consume(@NonNull String inMessage) throws InterruptedException {
        queue.put(inMessage);
    }

    @Override
    public String getAnswer() throws InterruptedException {
        int attempt = 0;
        do {
            String str = queue.poll(500, TimeUnit.MILLISECONDS);
            if (str != null) {
                return str;
            }
            attempt++;
        } while (attempt != 3);
        return null;
    }
}
