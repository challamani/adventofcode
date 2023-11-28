package com.adventofcode.aoc2022.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.TreeSet;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

@Service("day1")
@Slf4j
public class Day1 implements Puzzle<String,Integer> {

    /**
     * Question : https://adventofcode.com/2022/day/1
     * Input : https://adventofcode.com/2022/day/1/input
     * Calculate max calories from the given calories - sum of top 3, segment-sum
     */
    public Integer part2(String input) {
        AtomicInteger caloriesSegSum = new AtomicInteger(0);
        TreeSet<Integer> treeSet = new TreeSet<>();
        AtomicInteger maxCalories = new AtomicInteger(0);
        AtomicInteger finalMaxCalories = maxCalories;

        Arrays.stream(input.split("\\n")).forEach(calories -> {
            try {
                caloriesSegSum.addAndGet(Integer.parseInt(calories));
            } catch (Exception exception) {
                if (finalMaxCalories.get() < caloriesSegSum.get()) {
                    finalMaxCalories.set(caloriesSegSum.get());
                }
                log.info("segment-sum {} maxCal {}", caloriesSegSum, finalMaxCalories.get());
                treeSet.add(caloriesSegSum.get());
                caloriesSegSum.set(0);
            }
        });

        treeSet.add(caloriesSegSum.get());
        if (maxCalories.get() < caloriesSegSum.get()) {
            maxCalories = caloriesSegSum;
        }

        log.info("final result - segment-sum {} maxCal {}", caloriesSegSum, maxCalories);
        TreeSet<Integer> reverseSet = (TreeSet<Integer>) treeSet.descendingSet();
        log.info("TreeSet {}", reverseSet);

        AtomicReference<Integer> finalResult = new AtomicReference<>(0);
        reverseSet.stream().limit(3).forEach(sumOfSegCalories ->
                finalResult.updateAndGet(v -> v + sumOfSegCalories));
        return finalResult.get();
    }

    public Integer part1(String input) {
        AtomicInteger caloriesSegSum = new AtomicInteger(0);
        AtomicInteger maxCalories = new AtomicInteger(0);
        AtomicInteger finalMaxCalories = maxCalories;

        Arrays.stream(input.split("\\n")).forEach(calories -> {
            try {
                caloriesSegSum.addAndGet(Integer.parseInt(calories));
            } catch (Exception exception) {
                if (finalMaxCalories.get() < caloriesSegSum.get()) {
                    finalMaxCalories.set(caloriesSegSum.get());
                }
                log.info("segment-sum {} maxCal {}", caloriesSegSum, finalMaxCalories.get());
                caloriesSegSum.set(0);
            }
        });

        if (maxCalories.get() < caloriesSegSum.get()) {
            maxCalories = caloriesSegSum;
        }
        return maxCalories.get();
    }
}
