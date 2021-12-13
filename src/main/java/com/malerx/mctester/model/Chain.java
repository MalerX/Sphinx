package com.malerx.mctester.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document("test")
@Getter
@Setter
@NoArgsConstructor
public class Chain {

    @Id
    private String id;
    private List<ChainElement> chain;
}
