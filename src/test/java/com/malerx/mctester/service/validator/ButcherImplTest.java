package com.malerx.mctester.service.validator;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;


class ButcherImplTest {
    private static final String pathTestData = "src/main/resources/test/ButcherImplTestData.txt";
    private static final List<List<String>> testData = new ArrayList<>();

    @BeforeAll
    static void fillTestData() {
        try (BufferedReader reader = new BufferedReader(new FileReader(pathTestData))) {
            String str;
            int i = 0;
            List<String> tmp = new ArrayList<>(3);
            while ((str = reader.readLine()) != null) {
                i++;
                tmp.add(str);
                if (i % 3 == 0) {
                    testData.add(tmp);
                    tmp = new ArrayList<>(3);
                }
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException("Not found file with test data");
        } catch (IOException e) {
            throw new RuntimeException("Failed read data from file.");
        }
    }

    @Test
    void butchAndCompare() throws JsonProcessingException {
        Butcher butcher = new ButcherImpl(new ObjectMapper());
        for (List<String> test : testData) {
            String answer = butcher.butchAndCompare(test.get(0), test.get(1));
            assertEquals(test.get(2), answer);
        }
    }
}