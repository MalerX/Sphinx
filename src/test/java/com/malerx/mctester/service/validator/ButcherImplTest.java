package com.malerx.mctester.service.validator;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ButcherImplTest {
    private static final String pathTestData = "test/ButcherImplTestData.txt";

    private final List<List<String>> testData = new ArrayList<>();

    @BeforeAll
    void fillTestData() {

    }

    @Test
    void butchAndCompare() {
        Butcher butcher = new ButcherImpl(new ObjectMapper());

        String answer = butcher.butchAndCompare();
    }
}