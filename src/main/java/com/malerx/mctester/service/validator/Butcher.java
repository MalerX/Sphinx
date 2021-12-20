package com.malerx.mctester.service.validator;

import lombok.NonNull;

public interface Butcher {
    @NonNull
    String butchAndCompare(@NonNull String expected, @NonNull String received);
}
