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
    private static List<List<String>> testData;

    @BeforeAll
    static void fillTestData() {
        testData = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(pathTestData))) {
            String str;
            int i = 0;
            List<String> tmp = new ArrayList<>(3);
            while ((str = reader.readLine()) != null) {
                tmp.add(str);
                if (++i % 3 == 0) {
                    testData.add(List.copyOf(tmp));
                    tmp.clear();
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
        for (List<String> list : testData) {
            String answer = butcher.butchAndCompare(list.get(0), list.get(1));
            assertEquals(list.get(2), answer);
        }
    }
}