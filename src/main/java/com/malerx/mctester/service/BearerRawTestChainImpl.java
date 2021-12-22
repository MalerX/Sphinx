package com.malerx.mctester.service;

import com.malerx.mctester.model.Chain;
import com.malerx.mctester.repositories.MongoTestDataRepositories;
import com.malerx.mctester.service.log.ResultTestSave;
import com.malerx.mctester.service.validator.ValidationChain;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@RequiredArgsConstructor
@Slf4j
public class BearerRawTestChainImpl implements BearerRawTestChain {

    private final MongoTestDataRepositories repositories;
    private final ValidationChain validationChain;
    private final ResultTestSave resultTestSave;

    @Override
    public void loadTestChain() {
        log.info("Start read test chain from db. In db {} test chains.", repositories.count());
        for (Chain chain : repositories.findAll()) {
            String resultTestChain = validationChain.validate(chain);
            try {
                resultTestSave.save(resultTestChain);
            } catch (IOException e) {
                log.warn("Fail save result test", e);
            }
        }
    }

    @Override
    public void run() {
        loadTestChain();
    }
}
