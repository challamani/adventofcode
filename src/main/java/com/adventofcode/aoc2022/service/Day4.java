package com.adventofcode.aoc2022.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service("day4")
@Slf4j
public class Day4 implements Puzzle<String,Integer>{

    /**
     * I need to find a smart answer
     * [][][][]
     *       [][][][]
     *
     *       [][][]
     * [][][][]
     *
     *  [][][]
     * [][][][][]
     *
     * [][][][][][]
     *     [][][]
     *
     * */
    @Override
    public Integer solve(String input) {

        int finalResult = 0;

        for (String record : input.split("\\n")) {

            String[] sections = record.split(",");
            String[] sectionOne = sections[0].split("-");
            String[] sectionTwo = sections[1].split("-");

            int fMinor = Integer.parseInt(sectionOne[0]);
            int fMajor = Integer.parseInt(sectionOne[1]);
            int sMinor = Integer.parseInt(sectionTwo[0]);
            int sMajor = Integer.parseInt(sectionTwo[1]);

            if (fMinor >= sMinor && fMinor <= sMajor) {
                finalResult += 1;
            } else if (fMajor >= sMinor && fMajor <= sMajor) {
                finalResult += 1;
            } else if (fMinor <= sMinor && fMajor >= sMajor) {
                finalResult += 1;
            } else if (sMinor <= fMinor && sMajor >= fMajor) {
                finalResult += 1;
            }
        }
        return finalResult;
    }


    private Integer part1(String input) {

        int finalResult = 0;

        for (String record : input.split("\\n")) {

            String[] sections = record.split(",");
            String[] sectionOne = sections[0].split("-");
            String[] sectionTwo = sections[1].split("-");

            int fMinor = Integer.parseInt(sectionOne[0]);
            int fMajor = Integer.parseInt(sectionOne[1]);
            int sMinor = Integer.parseInt(sectionTwo[0]);
            int sMajor = Integer.parseInt(sectionTwo[1]);

            if ((fMajor - fMinor) >= (sMajor - sMinor)) {
                if (fMinor <= sMinor && fMajor >= sMajor) {
                    finalResult += 1;
                }
            } else {
                if (sMinor <= fMinor && sMajor >= fMajor) {
                    finalResult += 1;
                }
            }
        }
        return finalResult;
    }
}
