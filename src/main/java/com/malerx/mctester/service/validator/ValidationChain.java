package com.malerx.mctester.service.validator;

import com.malerx.mctester.model.Chain;
import lombok.NonNull;

public interface ValidationChain {

    @NonNull String validate(@NonNull Chain chain);
}
