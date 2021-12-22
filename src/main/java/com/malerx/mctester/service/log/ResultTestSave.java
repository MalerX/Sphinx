package com.malerx.mctester.service.log;

import lombok.NonNull;

import java.io.IOException;

public interface ResultTestSave {
    void save(@NonNull String resultTest) throws IOException;
    void close();
}
