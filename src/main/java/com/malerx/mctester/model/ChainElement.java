package com.malerx.mctester.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class ChainElement implements Serializable {
    private String direction;
    private String topic_name;
    private String method_http;
    private String url;
    private String format_number;
    private MessageFormat data;
}
