package com.malerx.mctester.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
public class ChainElement implements Serializable {
    private String direction;
    private String topic_name;
    private String method_http;
    private String url;
    private String format_number;
}
