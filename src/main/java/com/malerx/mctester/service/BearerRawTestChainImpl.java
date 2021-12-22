package com.malerx.mctester.service;

import com.malerx.mctester.model.Chain;
import com.malerx.mctester.repositories.MongoTestDataRepositories;
import com.malerx.mctester.service.log.ResultTestSave;
import com.malerx.mctester.service.validator.ValidationChain;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Service
@RequiredArgsConstructor
@Slf4j
public class BearerRawTestChainImpl implements BearerRawTestChain {

    private final MongoTestDataRepositories repositories;
    private final ValidationChain validationChain;
    private final ResultTestSave resultTestSave;

    @Override
    public void loadTestChain() {
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
        long start = new Date().getTime();
        SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy-kk:mm:ss:S");
        log.info("Start test. Chains in db {}. Timestamp start: {} .", repositories.count(), format.format(new Date()));
        loadTestChain();
        resultTestSave.close();
        long finish = new Date().getTime();
        log.info("Finish test. Timestamp finish: {}. Duration test: {}", format.format(new Date()), (finish - start) / 1000);
    }
}
