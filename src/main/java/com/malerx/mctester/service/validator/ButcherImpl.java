package com.malerx.mctester.service.validator;

import com.fasterxml.jackson.databind.JsonNode;
import com.malerx.mctester.service.log.ResultTestSave;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;

@Service
@Slf4j
@RequiredArgsConstructor
public class ButcherImpl implements Butcher {
    private final SimpleDateFormat formattedDate = new SimpleDateFormat("dd.MM.yyyy-kk:mm::ss");
    private final ResultTestSave logKeeper;

    @Override
    public void butchAndCompare(@NonNull JsonNode expected, @NonNull JsonNode received) {
      log.info("{} -- {}", expected.toString(), received.toString());
    }
}
