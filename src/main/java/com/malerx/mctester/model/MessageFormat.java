package com.malerx.mctester.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MessageFormat {
    private String method;
    private String params;
    private String id;
}
