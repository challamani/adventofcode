package com.adventofcode.aoc2022.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service("day2")
@Slf4j
public class Day2 implements Puzzle<String,Integer>{
    @Override
    public Integer solve(String input) {
        /**
         * A,B,C - 65,66,67
         * X,Y,Z - 88,89,90
         * a,b,c - 97
         * */
        int score = 0;
        for (String round : input.split("\\n")) {

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
                score += 3;
            } else {
                //win
                if (elfVal == 67) {
                    youChoose = 65;
                } else {
                    youChoose = (elfVal + 1);
                }
                score += 6;
            }
            score += (youChoose - 64);
        }
        return score;
    }

    private int part1(String input) {

        int score = 0;
        for (String round : input.split("\\n")) {

            int yourVal = round.charAt(2);
            int diff = (yourVal - 23) - (int) round.charAt(0);
            if (diff == 0) {
                score += 3;
            } else if (diff == 1 || diff == -2) {
                score += 6;
            }
            score += (yourVal - 87);
        }
        return score;
    }
}
