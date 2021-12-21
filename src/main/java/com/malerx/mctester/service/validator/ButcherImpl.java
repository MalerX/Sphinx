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
            return String.format("The expected message format does not match the received one: %s -- %s", expected, received);
        } else if (!expectedNode.equals(receivedNode)) {
            return deepCompare(expectedNode, receivedNode);
        }
        return "Successful. Expected and received answers equals.";
    }

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
                        .append("\t");
            }
        } else if (expectedNode.isValueNode()) {
            builder.append(
                    compare(expectedNode, receivedNode)
            ).append("\t");
        }
        return builder.toString().trim();
    }

    private String compare(JsonNode expected, JsonNode received) {
        StringBuilder builder = new StringBuilder();
        String one = expected.textValue();
        String two = received.textValue();

        if (one.equals(two)) {
            builder.append("equals");
        } else
            builder.append(String.format("NON EQUALS: expected = %s -- received = %s", one, two));
        return builder.toString();
    }

    public static void main(String[] args) throws JsonProcessingException {
        Butcher butcher = new ButcherImpl(new ObjectMapper());
        String expected = """
                {"books":{"book":[{"title":"CPP","author":"Milton","year":"2008","price":"456.00"},{"title":"JAVA","author":"Gilson","year":"2002","price":"456.00"}]}}
                """;
        String received = """
                {"books":{"book":[{"title":"CPP","author":"Jhon Boy","year":"2021","price":"456.00"},{"title":"Phyton","author":"Gilson","year":"2002","price":"456.00"}]}}
                """;

        String s = butcher.butchAndCompare(expected, received);
        System.out.println(s);
    }
}
