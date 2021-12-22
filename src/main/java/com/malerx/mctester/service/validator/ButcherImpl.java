package com.malerx.mctester.service.validator;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.malerx.mctester.exceptions.NotEqualsFormatMessageException;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Iterator;
import java.util.Map;

/**
 * Функционал класса -- глубокое сравнение JSON при помощи рекурсивной функции обхода сложного вложенного дерева.
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class ButcherImpl implements Butcher {
    private final ObjectMapper mapper;

    @Override
    public @NonNull String butchAndCompare(@NonNull String expected, @NonNull String received)
            throws JsonProcessingException {
        JsonNode expectedNode = mapper.readTree(expected);
        JsonNode receivedNode = mapper.readTree(received);
        if (expectedNode.size() != receivedNode.size()) {
            return String.format("Fail.\tThe expected message format does not match the received one: %s -- %s",
                    expected, received);
        } else if (!expectedNode.equals(receivedNode)) {
            try {
                return "Fail.\t" + deepCompare(expectedNode, receivedNode);
            } catch (NotEqualsFormatMessageException e) {
                return String.format("Fail.\tThe expected message format does not match the received one: %s -- %s",
                        expected, received);
            }
        }
        return "Successful";
    }

    /**
     * Рекурсиваная функция для обхода дерева. Проваливаемся всё глубже и глубже, пока не дойдём до элемента ValueNode
     * (не мапы и не массива), сравниваем, пишем в StringBuilder результат (только equal если поля равны и подробно,
     * с значениями если не равны) и передаём строку-результат-сравнения на верх из рекурсивного метода, где всё
     * конкатенируется в к имени поля.
     *
     * @param expectedNode -- ожидаемое сообщение. Фоормат {@link JsonNode}
     * @param receivedNode -- полученный от микросервиса ответ. Формат {@link JsonNode}
     * @return возвращается {@link String}, в которой перечислены все поля дерева с результатами сравнения.
     */
    @NonNull
    private String deepCompare(JsonNode expectedNode, JsonNode receivedNode) throws NotEqualsFormatMessageException {
        StringBuilder builder = new StringBuilder();

        if (expectedNode.isContainerNode() && !expectedNode.isArray()) {
            Iterator<Map.Entry<String, JsonNode>> iterator = expectedNode.fields();
            while (iterator.hasNext()) {
                Map.Entry<String, JsonNode> entry = iterator.next();
                if (receivedNode.get(entry.getKey()) == null) {
                    throw new NotEqualsFormatMessageException();
                }
                builder
                        .append(String.format("%s: ", entry.getKey()))
                        .append(
                                deepCompare(entry.getValue(), receivedNode.findValue(entry.getKey()))
                        ).append("\t");
            }
        } else if (expectedNode.isArray()) {
            ArrayNode arrayNodeExpected;
            ArrayNode arrayNodeReceived;
            if (expectedNode instanceof ArrayNode expected) {
                arrayNodeExpected = expected;
            } else
                throw new NotEqualsFormatMessageException();
            if (receivedNode instanceof ArrayNode received) {
                arrayNodeReceived = received;
            } else
                throw new NotEqualsFormatMessageException();
            int arraySize = arrayNodeExpected.size();
            for (int i = 0; i < arraySize; i++) {
                builder
                        .append(String.format("array[%d]: ", i))
                        .append(
                                deepCompare(arrayNodeExpected.get(i), arrayNodeReceived.get(i))
                        ).append("\t");
            }
        } else if (expectedNode.isValueNode() && receivedNode != null) {
            if (expectedNode.equals(receivedNode)) {
                builder.append("equal.");
            } else
                builder.append(String.format("NON EQUALS expected '%s' != received '%s'.",
                        expectedNode.asText(), receivedNode.asText()));
        } else if (receivedNode == null) {
            throw new NotEqualsFormatMessageException();
        }
        return builder.toString().trim();
    }
}
