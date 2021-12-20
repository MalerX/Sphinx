package com.malerx.mctester.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
@RequiredArgsConstructor
public class StartTest {
    private final MasterOverFlows master;

    @GetMapping
    public String start() {
        master.start();
        return "Test already start.";
    }
}
