package com.malerx.mctester.exceptions;

public class TopicNotFoundException extends Throwable {

    public TopicNotFoundException(String format) {
        super(format);
    }

    public TopicNotFoundException() {
        super();
    }
}
