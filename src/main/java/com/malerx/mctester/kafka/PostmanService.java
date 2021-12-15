package com.malerx.mctester.kafka;

public interface PostmanService {
    void send(String outMessage);
    void consume(String inMessage);
}
