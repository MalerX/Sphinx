package com.malerx.mctester.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document("validation")
@Data
public class Chain {
    @Id
    private String id;
    private List<ChainElement> chain;
}
