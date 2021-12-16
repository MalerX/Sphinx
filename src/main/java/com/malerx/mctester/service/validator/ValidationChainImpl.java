package com.malerx.mctester.service.validator;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.malerx.mctester.kafka.PostmanService;
import com.malerx.mctester.model.Chain;
import com.malerx.mctester.model.ChainElement;
import com.malerx.mctester.repositories.MongoTestDataRepositories;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * Функционал класса -- прочитать цепочку из БД, отправить то что должно быть отправлено, получить то, что должно быть
 * получено. Создать из ожидаемого/полученого сообщений JsonNode, передать в Butcher ноды для глубокого сравнения.
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class ValidationChainImpl implements ValidationChain {
    private final MongoTestDataRepositories repositories;
    private final PostmanService postman;
    private final ObjectMapper mapper;
    private final Butcher butcher;
    private final LinkedBlockingQueue<Chain> queue = new LinkedBlockingQueue<>();

    @Override
    public void validate() throws JsonProcessingException, InterruptedException {
        initRead();
        long quantity = repositories.count();
        for (long i = 0; i < quantity; i++) {
                handle(queue.take().getChain());
        }
    }

    /**
     * Вот такая самодельная асинхронность. Чтобы не ждать перед началом обработки цепочек, пока они все выгрузятся из
     * БД, запускаем чтение в новом потоке и кладём цепочки в блокирующую очередь. И параллельно в основном потоке
     * берём и обрабатываем цепочки.
     */
    private void initRead() {
        new Thread(() -> {
            try {
                for (Chain chain : repositories.findAll()) {
                    queue.put(chain);
                    log.info("Add in lambda: -- {}, in queue -- {}", chain.getId(), queue.size());
                }
            } catch (InterruptedException e) {
                log.warn("Failed read/put chain.", e);
            }
        }).start();
    }

    private void handle(List<ChainElement> chainElements) throws InterruptedException, JsonProcessingException {
        for (ChainElement element : chainElements) {
            if (element.getDirection().equals("in")) {
                postman.send(element.getTopic_name(), element.getData());
            } else if (element.getDirection().equals("out")) {
                String str = postman.getAnswer();
                if (str == null) {
                    str = "{\"test\":\"test\"}";
                }
                JsonNode received = mapper.readTree(str);
                JsonNode expected = mapper.readTree(element.getData());
                butcher.butchAndCompare(expected, received);
            }
        }
    }
}
