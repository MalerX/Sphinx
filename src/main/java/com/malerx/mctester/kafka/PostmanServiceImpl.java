package com.malerx.mctester.kafka;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

@Service
@RequiredArgsConstructor
public class PostmanServiceImpl implements PostmanService {
    private final static BlockingQueue<String> queue = new LinkedBlockingQueue<>();
    private final KafkaTemplate<String, String> kafkaTemplate;
    @Value("${kafka.topics.in}")
    private String topicIn;
    @Value("${kafka.topics.out}")
    private String topicOut;
    @Value("${kafka.groupId}")
    private String groupId;

    @Override
    public void send(String outMessage) {
        kafkaTemplate.send(topicOut, outMessage);
    }

    @Override
    @KafkaListener(id = "sphinx", topics = "outcoming", containerFactory = "singleFactory")
    public void consume(String inMessage) {
        System.out.println(inMessage);
        try {
            queue.put(inMessage);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
