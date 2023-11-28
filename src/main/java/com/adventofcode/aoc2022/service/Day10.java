package com.adventofcode.aoc2022.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service("day10")
@Slf4j
public class Day10 implements Puzzle<String, List<String>>{

    /**
     * Input : https://adventofcode.com/2022/day/10/input
     * Day 10 - Cathode-Ray Tube
     * */
    public List<String> part2(String input) {

        StringBuilder[] screen = new StringBuilder[6];
        IntStream.range(0, 6).forEach(index -> {
            StringBuilder stringBuilder = new StringBuilder("");
            IntStream.range(0, 40).forEach(position -> stringBuilder.append("."));
            screen[index] = stringBuilder;
        });

        AtomicInteger cycleCount = new AtomicInteger(0);
        int instructionCost = 2;

        AtomicInteger registerValue = new AtomicInteger(1);
        AtomicInteger spriteBegin = new AtomicInteger(0);
        AtomicInteger spriteEnd = new AtomicInteger(2);
        int screenWidth = 40;

        Arrays.stream(input.split("\\n")).forEach(record -> {

            if (record.equals("noop")) {
                cycleCount.addAndGet(1);
                if (isCycleCountInSpriteRange(cycleCount.get(), spriteBegin.get(), spriteEnd.get(), screenWidth)) {
                    maskTheMatch(cycleCount.get(), screen, screenWidth);
                }
            } else {
                int instructionAddValue = Integer.parseInt(record.substring(5));
                IntStream.range(0, instructionCost).forEach(cycle -> {
                    cycleCount.addAndGet(1);
                    if (isCycleCountInSpriteRange(cycleCount.get(), spriteBegin.get(), spriteEnd.get(), screenWidth)) {
                        maskTheMatch(cycleCount.get(), screen, screenWidth);
                    }
                });
                registerValue.addAndGet(instructionAddValue);
                spriteBegin.set(registerValue.get() - 1);
                spriteEnd.set(registerValue.get() + 1);
            }
        });

        Arrays.stream(screen).forEach(System.out::println);
        return Arrays.stream(screen).map(stringBuilder -> stringBuilder.toString()).collect(Collectors.toList());
    }

    private boolean isCycleCountInSpriteRange(int cycleCount, int spriteBegin, int spriteEnd, int screenWidth) {
        int cycleCountPosition = (cycleCount-1) % screenWidth;
        return (cycleCountPosition >= spriteBegin && cycleCountPosition <= spriteEnd);
    }

    private void maskTheMatch(int cycleCount, StringBuilder[] screen, int screenWidth) {
        int row = (cycleCount-1) / screenWidth;
        int position = (cycleCount-1) % screenWidth;
        screen[row].setCharAt(position, '#');
    }

    public List<String> part1(String input) {

        AtomicInteger signalStrength = new AtomicInteger(0);
        AtomicInteger cycleCount = new AtomicInteger(0);

        Set<Integer> signalStrengthCalPosition =  new HashSet<>(Arrays.asList(20,60,100,140,180,220));
        int instructionCost = 2;
        AtomicInteger registerValue = new AtomicInteger(1);

        Arrays.stream(input.split("\\n")).forEach(record -> {

            if (record.equals("noop")) {
                cycleCount.addAndGet(1);
                if(signalStrengthCalPosition.contains(cycleCount.get())) {
                    signalStrength.addAndGet(returnSignalStrength(cycleCount.get(),registerValue.get()));
                }
            } else {
                int instructionAddValue = Integer.parseInt(record.substring(5));
                IntStream.range(0, instructionCost).forEach(cycle -> {
                    cycleCount.addAndGet(1);
                    if(signalStrengthCalPosition.contains(cycleCount.get())) {
                        signalStrength.addAndGet(returnSignalStrength(cycleCount.get(),registerValue.get()));
                    }
                });
                registerValue.addAndGet(instructionAddValue);
            }
        });
        return List.of(Integer.toString(signalStrength.get()));
    }

    private int returnSignalStrength(int cycleCount, int registerValue){
        return (cycleCount * registerValue);
    }
}
