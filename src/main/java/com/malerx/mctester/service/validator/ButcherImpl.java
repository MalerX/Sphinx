package com.malerx.mctester.service.validator;

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

    @Override
    public @NonNull String butchAndCompare(@NonNull String expected, @NonNull String received) {
        log.info("{} -- {}", expected.toString(), received.toString());
        return "processed";
    }
}
