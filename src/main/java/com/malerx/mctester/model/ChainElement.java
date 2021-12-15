package com.malerx.mctester.model;

import lombok.Data;

import java.io.Serializable;

@Data
public class ChainElement implements Serializable {
    private String direction;
    private String topic_name;
    private String method_http;
    private String url;
    private String format_number;
    private String data;
}
