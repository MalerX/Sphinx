package com.malerx.mctester.service.validator;

import com.fasterxml.jackson.core.JsonProcessingException;

public interface ValidationChain {

    void validate() throws InterruptedException, JsonProcessingException;
}
