package com.malerx.mctester.service.validator;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.malerx.mctester.kafka.PostmanService;
import com.malerx.mctester.model.Chain;
import com.malerx.mctester.model.ChainElement;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
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
    private static final SimpleDateFormat dateFormatter = new SimpleDateFormat("dd.MM.yyyy-kk:mm");

    private final Butcher butcher;
    private final PostmanService postman;

    @Override
    public @NonNull String validate(@NonNull Chain chain) {
        StringBuilder result = new StringBuilder();
        try {
            for (ChainElement element : chain.getChain()) {
                if (element.getDirection().equals("in")) {
                    postman.send(element.getTopic_name(), element.getData());
                } else if (element.getDirection().equals("out")) {
                    String received = postman.getAnswer();
                    if (received == null) {
                        log.info("Chain {} was skipped,", chain.getId());
                        continue;
                    }
                    result.append(dateFormatter.format(new Date()))
                            .append("\t")
                            .append(butcher.butchAndCompare(element.getData(), received))
                            .append("\n");
                }
            }
        } catch (InterruptedException e) {
            log.info("Process has been interrupt", e);
        } catch (JsonProcessingException e) {
            log.warn("Failed convert string to JsonNode", e);
        }
        return result.toString();
    }
}
