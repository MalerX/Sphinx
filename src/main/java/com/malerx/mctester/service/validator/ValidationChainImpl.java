package com.malerx.mctester.service.validator;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.malerx.mctester.kafka.PostmanService;
import com.malerx.mctester.model.Chain;
import com.malerx.mctester.model.ChainElement;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * Функционал класса -- прочитать цепочку из БД, отправить то что должно быть отправлено, получить то, что должно быть
 * получено. Создать из ожидаемого/полученого сообщений JsonNode, передать в Butcher ноды для глубокого сравнения.
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class ValidationChainImpl implements ValidationChain {
    private final PostmanService postman;
    private final Butcher butcher;
    private BlockingQueue<Chain> rawData;
    private BlockingQueue<String> readyData;

    @Override
    public void validate() {
        int i = 0;
        while (!rawData.isEmpty()) {
            try {
                Chain chain = Objects.requireNonNull(rawData.poll(500, TimeUnit.MILLISECONDS));
                log.info("Read from queue in Validate chain {}", i++);
                for (ChainElement element : chain.getChain()) {
                    if (element.getDirection().equals("in")) {
                        postman.send(element.getTopic_name(), element.getData());
                    } else if (element.getDirection().equals("out")) {
                        String received = postman.getAnswer();
                        if (received == null) {
                            log.info("Chain {} was skipped,", chain.getId());
                            continue;
                        }
                        readyData.put(butcher.butchAndCompare(element.getData(), received));
                    }
                }
            } catch (InterruptedException e) {
                log.info("Process has been interrupt", e);
            } catch (JsonProcessingException e) {
                log.warn("Failed convert string to JsonNode", e);
            }
        }
    }

    @Override
    public void setQueues(@NonNull BlockingQueue<Chain> queueRaw, @NonNull BlockingQueue<String> queueReady) {
        this.rawData = queueRaw;
        this.readyData = queueReady;
    }

    @Override
    public void run() {
        log.info("Start read test chain from DB");
        validate();
    }
}
