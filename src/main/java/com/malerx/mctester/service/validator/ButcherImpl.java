package com.malerx.mctester.service.validator;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
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
    public @NonNull String butchAndCompare(@NonNull String expected, @NonNull String received) throws JsonProcessingException {
        JsonNode expectedNode = mapper.readTree(expected);
        JsonNode receivedNode = mapper.readTree(received);
        if (expectedNode.size() != receivedNode.size()) {
            return String.format("Fail.\tThe expected message format does not match the received one: %s -- %s", expected, received);
        } else if (!expectedNode.equals(receivedNode)) {
            return "Fail.\t" + deepCompare(expectedNode, receivedNode);
        }
        return "Successful";
    }

    /**
     * Рекурсиваная функция для обхода дерева.
     *
     * @param expectedNode -- ожидаемое сообщение. Фоормат {@link JsonNode}
     * @param receivedNode -- полученный от микросервиса ответ. Формат {@link JsonNode}
     * @return возвращается {@link String}, в которой перечислены все поля дерева с результатами сравнения.
     */
    @NonNull
    private String deepCompare(JsonNode expectedNode, JsonNode receivedNode) {
        StringBuilder builder = new StringBuilder();

        if (expectedNode.isContainerNode() && !expectedNode.isArray()) {
            Iterator<Map.Entry<String, JsonNode>> iterator = expectedNode.fields();
            while (iterator.hasNext()) {
                Map.Entry<String, JsonNode> entry = iterator.next();
                builder
                        .append(String.format("%s: ", entry.getKey()))
                        .append(
                                deepCompare(entry.getValue(), receivedNode.findValue(entry.getKey()))
                        ).append("\t");
            }
        } else if (expectedNode.isArray()) {
            ArrayNode arrayNodeExpected = ((ArrayNode) expectedNode);
            ArrayNode arrayNodeReceived = ((ArrayNode) receivedNode);
            int arraySize = arrayNodeExpected.size();
            for (int i = 0; i < arraySize; i++) {
                JsonNode nodeExp = arrayNodeExpected.get(i);
                JsonNode nodeRec = arrayNodeReceived.get(i);
                builder
                        .append(String.format("array[%d]: ", i))
                        .append(deepCompare(nodeExp, nodeRec))
                        .append("\s");
            }
        } else if (expectedNode.isValueNode()) {

            if (expectedNode.equals(receivedNode)) {
                builder.append("equal.");
            } else
                builder.append(String.format("NON EQUALS expected '%s' != received '%s'.",
                        expectedNode.asText(), receivedNode.asText()));
        }
        return builder.toString().trim();
    }


    public static void main(String[] args) throws JsonProcessingException {
        Butcher butcher = new ButcherImpl(new ObjectMapper());
//        String expected = """
//                {"books":{"book":[{"title":"CPP","author":"Milton","year":"2008","price":"456.00"},{"title":"JAVA","author":"Gilson","year":"2002","price":"456.00"}]}}
//                """;
//        String received = """
//                {"books":{"book":[{"title":"CPP","author":"Jhon Boy","year":"2021","price":"456.00"},{"title":"Phyton","author":"Gilson","year":"2002","price":"456.00"}]}}
//                """;

        String ex = """
                {"method":"fees_limits","params":{"operation_type":"1","currency_in":"BTC","provider_in":"advcash_card"},"id":"a141905a-7d47-4f52-a8e9-10891155d8a2"}
                """;
        String rec = """
                {"method":"fees_limits","params":{"operation_type":"2","currency_in":"BTC","provider_in":"advcash_card"},"id":"a141005a-7d47-4f52-a8e9-10891155d8a2"}
                """;

        String s = butcher.butchAndCompare(ex, rec);
        System.out.println(s);
    }
}
