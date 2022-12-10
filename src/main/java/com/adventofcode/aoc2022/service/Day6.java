package com.adventofcode.aoc2022.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@Service("day6")
@Slf4j
public class Day6 implements Puzzle<String,Integer> {


    /**
     * Problem - first Occurrence of distinct 4/14 characters in a string
     */
    @Override
    public Integer solve(String input) {
        AtomicInteger result = new AtomicInteger();
        Arrays.stream(input.split("\\n")).forEach(record -> {
            int len = record.length();
            int index = 0;
            while (index + 14 <= len) {
                String subStr = record.substring(index, index + 14);
                if (isUniqueSet(subStr)) {
                    break;
                }
                index++;
            }
            log.info("record {} index {}", record, (index + 14));
            result.addAndGet((index + 14));
        });
        return result.get();
    }

    private boolean isUniqueSet(String subStr) {
        log.info("sub string {}", subStr);
        Map<String, Integer> charMap = new HashMap<>();
        for (char ch : subStr.toCharArray()) {
            charMap.putIfAbsent(ch + "", 1);
        }
        return (charMap.keySet().size() == 14);
    }

}
