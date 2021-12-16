package com.malerx.mctester.service.validator;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.NonNull;

public interface Butcher {
    void butchAndCompare(@NonNull JsonNode expected, @NonNull JsonNode received);
}
