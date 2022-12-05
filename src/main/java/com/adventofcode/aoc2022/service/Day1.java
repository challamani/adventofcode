package com.adventofcode.aoc2022.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.TreeSet;
import java.util.concurrent.atomic.AtomicReference;

@Service("day1")
@Slf4j
public class Day1 implements Puzzle<String,Integer> {

    private final RestTemplate restTemplate;

    @Autowired
    public Day1(RestTemplate restTemplate){
        this.restTemplate=restTemplate;
    }

    /**
     * Question : https://adventofcode.com/2022/day/1
     * Input : https://adventofcode.com/2022/day/1/input
     * */
    public Integer solve(String input) {
        String[] inputData = input.split("\\n");
        log.info("Length {} ", inputData.length);

        Integer caloriesSegSum = 0;
        Integer maxCalories = 0;
        TreeSet<Integer> treeSet = new TreeSet<>();
        for (String calories : inputData) {
            try {
                caloriesSegSum += Integer.parseInt(calories);
            } catch (Exception ex) {
                log.info("exception at converting string to int {}", calories);
                if (maxCalories < caloriesSegSum) {
                    maxCalories = caloriesSegSum;
                }
                log.info("segment-sum {} maxCal {}", caloriesSegSum, maxCalories);
                treeSet.add(caloriesSegSum);
                caloriesSegSum = 0;
            }
        }

        treeSet.add(caloriesSegSum);
        if (maxCalories < caloriesSegSum) {
            maxCalories = caloriesSegSum;
        }
        //part-1, return maxCalories
        log.info("final result - segment-sum {} maxCal {}", caloriesSegSum, maxCalories);

        TreeSet<Integer> reverseSet = (TreeSet<Integer>) treeSet.descendingSet();
        log.info("TreeSet {}", reverseSet);

        AtomicReference<Integer> finalResult = new AtomicReference<>(0);
        reverseSet.stream().limit(3).forEach(sumOfSegCalories ->
                finalResult.updateAndGet(v -> v + sumOfSegCalories));

        //return part-2 sum of top 3 seg sum - 212520
        return finalResult.get();
    }
}
