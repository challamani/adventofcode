package com.adventofcode.aoc2022.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;

@Service("day2")
@Slf4j
public class Day2 implements Puzzle<String,Integer> {
    @Override
    public Integer part2(String input) {
        /**
         * A,B,C - 65,66,67
         * X,Y,Z - 88,89,90
         * a,b,c - 97
         * */
        AtomicInteger score = new AtomicInteger();
        Arrays.stream(input.split("\\n")).forEach(round -> {

            int decidingFactor = round.charAt(2) - 88;
            int elfVal = round.charAt(0);

            int youChoose = 0;
            if (decidingFactor == 0) {
                //loss
                if (elfVal == 65) {
                    youChoose = 67;
                } else {
                    youChoose = (elfVal - 1);
                }
            } else if (decidingFactor == 1) {
                //draw
                youChoose = elfVal;
                score.addAndGet(3);
            } else {
                //win
                if (elfVal == 67) {
                    youChoose = 65;
                } else {
                    youChoose = (elfVal + 1);
                }
                score.addAndGet(6);
            }
            score.addAndGet((youChoose - 64));
        });
        return score.get();
    }

    public Integer part1(String input) {
        AtomicInteger score = new AtomicInteger();
        Arrays.stream(input.split("\\n")).forEach(round -> {
            int yourVal = round.charAt(2);
            int diff = (yourVal - 23) - (int) round.charAt(0);
            if (diff == 0) {
                score.addAndGet(3);
            } else if (diff == 1 || diff == -2) {
                score.addAndGet(6);
            }
            score.addAndGet((yourVal - 87));
        });
        return score.get();
    }
}
