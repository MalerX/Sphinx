package com.malerx.mctester.kafka;

import com.malerx.mctester.exceptions.TopicNotFoundException;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * Функционал класса -- отправка и чтение сообщений в/из Kafka.
 */
@Service
@RequiredArgsConstructor
public class PostmanServiceImpl implements PostmanService {
    //    Топики из которых ожидаем ответы на сообщения.
    private final static String TOPIC_03 = "validation_terms_03";
    private final static String TOPIC_04 = "validation_terms_04";
    private final static String TOPIC_08 = "validation_verification-steps_08";
    private final static String TOPIC_D10 = "validation_deposit-requests_10";
    private final static String TOPIC_W10 = "validation_withdraw-requests_10";
    private final static String TOPIC_F10 = "validation_fast-exchange-requests_10";
    private final static String TOPIC_D07 = "validation_deposit-requests_07";
    private final static String TOPIC_W07 = "validation_withdraw-requests_07";
    private final static String TOPIC_F07 = "validation_fast-exchange-requests_07";
    private final static String TOPIC_11 = "errors_11";
    private final static String[] consumerTestTopics = new String[]{
            TOPIC_03, TOPIC_04, TOPIC_08, TOPIC_D10, TOPIC_W10, TOPIC_F10, TOPIC_D07, TOPIC_W07, TOPIC_F07, TOPIC_11
    };

    //Мапа с очередями, куда будут складываться ответы. Ключём в мапе служит имя топика.
    private static Map<String, BlockingQueue<String>> blockingQueueMap;

    private final KafkaTemplate<String, String> kafkaTemplate;

    @Value("${kafka.groupId}")
    private String groupId;


    @PostConstruct
    private void init() {
        blockingQueueMap = new HashMap<>();
        Arrays.asList(consumerTestTopics).forEach(s -> blockingQueueMap.put(s, new LinkedBlockingQueue<>()));
    }


    @SneakyThrows
    @Override
    public void send(@NonNull String topicOut, @NonNull String outMessage) {
        kafkaTemplate.send(topicOut, outMessage);
        kafkaTemplate.flush();
    }

    @Override
    @KafkaListener(id = "sphinx",
            topics = {TOPIC_03, TOPIC_04, TOPIC_08, TOPIC_D10, TOPIC_W10, TOPIC_F10, TOPIC_D07, TOPIC_W07, TOPIC_11},
            containerFactory = "kafkaListenerContainerFactory",
            autoStartup = "true")
    public void consume(@NonNull ConsumerRecord<?, ?> inMessage) throws InterruptedException {
        String message = ((String) inMessage.value());
        String topic = inMessage.topic();
        BlockingQueue<String> queue = blockingQueueMap.get(topic);
        if (queue != null) {
            queue.put(message);
        }
    }

    @Override
    public String getAnswer(@NonNull String topic) throws InterruptedException, TopicNotFoundException {
        String answer;
        BlockingQueue<String> queue = blockingQueueMap.get(topic);
        if (queue != null) {
//            Если с третьей поптыки в топик ничего не пришло -- возвращаем null
            for (int i = 0; i < 3; i++) {
                answer = queue.poll(500, TimeUnit.MILLISECONDS);
                if (answer != null) {
                    return answer;
                }
            }
        } else throw new TopicNotFoundException(String.format("Topic with name %s not exists.", topic));
        return null;
    }
}
