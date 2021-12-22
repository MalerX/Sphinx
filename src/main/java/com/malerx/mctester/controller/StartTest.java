package com.malerx.mctester.controller;

import com.malerx.mctester.service.BearerRawTestChain;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
@RequiredArgsConstructor
public class StartTest {
    private final BearerRawTestChain bearerRawTestChain;

    @GetMapping
    public String start() {
        new Thread(bearerRawTestChain).start();
        return "Test already start.";
    }
}
