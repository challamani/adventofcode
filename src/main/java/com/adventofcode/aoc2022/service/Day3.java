package com.adventofcode.aoc2022.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@Service("day3")
@Slf4j
public class Day3 implements Puzzle<String,Integer>{

    /**
     * Rucksack's problems - two compartments with common items
     * A,B,C - 65,66,67
     * a,b,c - 97,98,99
     * */
    @Override
    public Integer solve(String input) {

        int finalResult = 0;
        String[] records = input.split("\\n");
        int index=0;

        while (index < records.length) {

            String segment1 = records[index];
            String segment2 = records[index+1];
            String segment3 = records[index+2];

            Map<String, Integer> charMap = new HashMap<>();
            for (char ch : segment1.toCharArray()) {
                charMap.putIfAbsent(ch + "", 1);
            }

            for (char ch : segment2.toCharArray()) {
                if (charMap.containsKey(ch + "")) {
                    charMap.put(ch + "", 2);
                }
            }

            for (char ch : segment3.toCharArray()) {
                if (charMap.containsKey(ch + "") && charMap.get(ch + "") == 2) {
                    if (ch >= 'a' && ch <= 'z') {
                        finalResult += (ch - 96);
                    } else {
                        finalResult += (ch - 38);
                    }
                    break;
                }
            }
            index += 3;
        }
        return finalResult;
    }

    private int part1(String input) {
        AtomicInteger finalResult = new AtomicInteger();
        Arrays.stream(input.split("\\n")).forEach(record -> {
            int len = record.length();
            String compartmentOne = record.substring(0, len / 2);
            String compartmentTwo = record.substring(len / 2);

            log.info("one {} two {}", compartmentOne, compartmentTwo);
            Map<String, Integer> charMap = new HashMap<>();
            for (char ch : compartmentOne.toCharArray()) {
                charMap.putIfAbsent(ch + "", 0);
            }

            for (char ch : compartmentTwo.toCharArray()) {
                if (charMap.containsKey(ch + "")) {
                    if (ch >= 'a' && ch <= 'z') {
                        finalResult.addAndGet((ch - 96));
                    } else {
                        finalResult.addAndGet((ch - 38));
                    }
                    break;
                }
            }
        });
        return finalResult.get();
    }
}
