package com.malerx.mctester.service.validator;

import com.malerx.mctester.kafka.PostmanService;
import com.malerx.mctester.model.Chain;
import com.malerx.mctester.model.ChainElement;
import com.malerx.mctester.repositories.MongoTestDataRepositories;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ValidationChainImpl implements ValidationChain {
    private final MongoTestDataRepositories repositories;
    private final PostmanService postman;

    @Override
    public void validate() {
        for (Chain chain : repositories.findAll()) {
            handle(chain.getChain());
        }
    }

    private void handle(List<ChainElement> chainElements) {
        for (ChainElement element : chainElements) {
            if (element.getDirection().equals("in")) {
                postman.send(element.getTopic_name(), element.getData());
            }
        }
    }
}
