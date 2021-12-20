package com.malerx.mctester.service.validator;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.NonNull;

public interface Butcher {
    @NonNull
    String butchAndCompare(@NonNull String expected, @NonNull String received) throws JsonProcessingException;
}
