package com.adventofcode.aoc2022.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.IntStream;

@Service("day5")
@Slf4j
public class Day5 implements Puzzle<String,String> {

    /**
     * Problem - stack pop/push operations
     * this is bruteforce solution - will try to find optimal solution.
     * Input - https://adventofcode.com/2022/day/5/input
     */

    private final Map<Integer, Stack<String>> stackMap;

    @Autowired
    public Day5() {
        this.stackMap = new HashMap<>();
        IntStream.range(1, 10).forEach(i -> stackMap.put(i, new Stack<>()));

        /**
         *                         [R] [J] [W]
         *             [R] [N]     [T] [T] [C]
         * [R]         [P] [G]     [J] [P] [T]
         * [Q]     [C] [M] [V]     [F] [F] [H]
         * [G] [P] [M] [S] [Z]     [Z] [C] [Q]
         * [P] [C] [P] [Q] [J] [J] [P] [H] [Z]
         * [C] [T] [H] [T] [H] [P] [G] [L] [V]
         * [F] [W] [B] [L] [P] [D] [L] [N] [G]
         *  1   2   3   4   5   6   7   8   9*/

        load(1, "FCPGQR");
        load(2, "WTCP");
        load(3, "BHPMC");
        load(4, "LTQSMPR");
        load(5, "PHJZVGN");
        load(6, "DPJ");
        load(7, "LGPZFJTR");
        load(8, "NLHCFPTJ");
        load(9, "GVZQHTCW");
    }

    private void load(int key, String stackItems) {
        for (char ch : stackItems.toCharArray()) {
            stackMap.get(key).push(ch + "");
        }
    }

    @Override
    public String part2(String input) {

        Pattern pattern = Pattern.compile("move (\\d+) from (\\d+) to (\\d+)");
        Arrays.stream(input.split("\\n")).forEach(record -> {

            Matcher matcher = pattern.matcher(record);
            if (matcher.find()) {
                int moves = Integer.parseInt(matcher.group(1));
                int from = Integer.parseInt(matcher.group(2));
                int to = Integer.parseInt(matcher.group(3));

                log.info("moves {} from {} to {}", moves, from, to);
                log.info("stack {}", stackMap);

                //Dumb bruteforce solution - I'll update with optimal solution
                Stack<String> tempStack = new Stack<>();
                IntStream.range(0, moves).forEach(move -> {
                    String popItem = stackMap.get(from).pop();
                    tempStack.push(popItem);
                });
                IntStream.range(0, moves).forEach(move -> stackMap.get(to).push(tempStack.pop()));
            }
        });

        StringBuffer stringBuffer = new StringBuffer("");
        IntStream.range(1, 10).forEach(key -> {
            stringBuffer.append(stackMap.get(key).peek());
        });

        log.info("final output {}", stringBuffer);
        return stringBuffer.toString();
    }


    public String part1(String input) {

        Pattern pattern = Pattern.compile("move (\\d+) from (\\d+) to (\\d+)");

        Arrays.stream(input.split("\\n")).forEach(record -> {
            Matcher matcher = pattern.matcher(record);
            if (matcher.find()) {
                int moves = Integer.parseInt(matcher.group(1));
                int from = Integer.parseInt(matcher.group(2));
                int to = Integer.parseInt(matcher.group(3));

                log.info("moves {} from {} to {}", moves, from, to);
                log.info("stack {}", stackMap);

                IntStream.range(0, moves).forEach(move -> {
                    String popItem = stackMap.get(from).pop();
                    stackMap.get(to).push(popItem);
                });
            }
        });

        StringBuffer stringBuffer = new StringBuffer("");
        IntStream.range(1, 10).forEach(key -> {
            stringBuffer.append(stackMap.get(key).peek());
        });

        log.info("final output {}", stringBuffer);
        return stringBuffer.toString();
    }
}
