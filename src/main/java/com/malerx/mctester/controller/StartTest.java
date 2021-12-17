package com.malerx.mctester.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.malerx.mctester.service.validator.ValidationChain;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
@RequiredArgsConstructor
public class StartTest {
    private final ValidationChain validation;
    private Thread validationThread;

    @GetMapping
    public String start() {
        if (validationThread == null || !validationThread.isAlive()) {
            this.validationThread = new Thread(() -> {
                try {
                    validation.validate();
                } catch (InterruptedException | JsonProcessingException e) {
                    e.printStackTrace();
                }
            });
            validationThread.start();
            return "start test";
        }
        return "Test already start.";
    }
}
