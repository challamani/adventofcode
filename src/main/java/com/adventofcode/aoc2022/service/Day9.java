package com.adventofcode.aoc2022.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.RequestScope;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.IntStream;

@Service("day9")
@Slf4j
@RequestScope
public class Day9 implements Puzzle<String,Integer> {

    @Override
    public Integer solve(String input) {
        return part1And2(input);
    }

    private Integer part1And2(String input) {

        /**
         * Part -1
         * int[][] rope = {{10000, 10000}, {10000, 10000}};
         * int lastTailIndex = 1;
         * */

        Set<String> coveredPositions = new HashSet<>();
        coveredPositions.add("10000-10000");

        int[][] rope = {{10000, 10000}, {10000, 10000}, {10000, 10000}, {10000, 10000}, {10000, 10000}, {10000, 10000}, {10000, 10000}, {10000, 10000}, {10000, 10000}, {10000, 10000}};
        int lastTailIndex = 9;

        for (String record : input.split("\\n")) {

            String direction = record.split(" ")[0];
            int moves = Integer.parseInt(record.split(" ")[1]);
            IntStream.range(0, moves).forEach(move -> {
                switch (direction) {
                    case "R":
                        rope[0][0] += 1;
                        break;
                    case "L":
                        rope[0][0] -= 1;
                        break;
                    case "U":
                        rope[0][1] += 1;
                        break;
                    case "D":
                        rope[0][1] -= 1;
                        break;
                }

                IntStream.range(0, lastTailIndex).forEach(index -> {
                    int[] head=rope[index];
                    int[] tail=rope[index + 1];
                    if (!isAdjacentOne(head, tail)) {
                        tail[0] = tail[0] + addSingleMoveToRelativeDirection(head[0],tail[0]);
                        tail[1] = tail[1] + addSingleMoveToRelativeDirection(head[1],tail[1]);
                    }
                });
                populateCoveredPositions(rope[lastTailIndex], coveredPositions);
                printLogs(record, move, rope);
            });
            log.info("after [{}] rope {}", record, rope);
        }
        log.info("tail positions {}", coveredPositions);
        return coveredPositions.size();
    }

    private int addSingleMoveToRelativeDirection(int headPosition, int tailPosition) {
        if (headPosition - tailPosition <= -1)
            return -1;
        else if (headPosition - tailPosition >= 1)
            return 1;
        else
            return 0;
    }
    private void printLogs(String record, int numOfMove, int[][] rope) {
        log.info("after [{}] moves :: {} head ::{}", record, numOfMove + 1, rope[0]);
        int index = 0;
        for (int[] ropePos : rope) {
            log.info("Knot {} Position {}", index++, ropePos);
        }
    }

    private void populateCoveredPositions(int[] tail, Set<String> coveredPositions) {
        String position = Integer.toString(tail[0]).concat("-").concat(Integer.toString(tail[1]));
        if (!coveredPositions.contains(position)) {
            coveredPositions.add(position);
        }
    }

    private String returnRecentPreviousKnotMove(int index, int[][] rope, int[][] previousPosition) {

        int[] head = rope[index];
        int[] tail = previousPosition[index];

        if (tail[0] < head[0] && tail[1] > head[1]) { //Position => UP-LEFT, Direction => D
            return "DR";
        } else if (tail[0] > head[0] && tail[1] > head[1]) {//UP-RIGHT
            return "DL";
        } else if (tail[0] > head[0] && tail[1] < head[1]) {//Position => DOWN-RIGHT, Direction => U
            return "UL";
        } else if (tail[0] < head[0] && tail[1] < head[1]) {//DOWN-LEFT
            return "UR";
        }
        return "";
    }

    private void positionBasedMove(int[] head, int[] tail, String direction) {

        if (tail[0] < head[0] && tail[1] > head[1]) {//UP-LEFT
            tail[0] = tail[0] + 1;
            tail[1] = tail[1] - 1;
        } else if (tail[0] > head[0] && tail[1] > head[1]) {//UP-RIGHT
            tail[0] = tail[0] - 1;
            tail[1] = tail[1] - 1;
        } else if (tail[0] > head[0] && tail[1] < head[1]) {//DOWN-RIGHT
            tail[0] = tail[0] - 1;
            tail[1] = tail[1] + 1;
        } else if (tail[0] < head[0] && tail[1] < head[1]) {//DOWN-LEFT
            tail[0] = tail[0] + 1;
            tail[1] = tail[1] + 1;
        }
    }

    private boolean isAdjacentOne(int[] head, int[] tail) {

        boolean isAdjacent = false;
        if (head[0] == tail[0] && tail[1] == head[1]) {
            isAdjacent = true;
        } else if (head[0] == tail[0] && Math.abs(head[1] - tail[1]) == 1) {
            isAdjacent = true;
        } else if (head[1] == tail[1] && Math.abs(head[0] - tail[0]) == 1) {
            isAdjacent = true;
        } else if (Math.abs(head[0] - tail[0]) == 1 && Math.abs(head[1] - tail[1]) == 1) {
            isAdjacent = true;
        }
        return isAdjacent;
    }

    private boolean isFarKnot(int[] head, int[] tail) {
        return (Math.abs(head[0] - tail[0]) > 1 && Math.abs(head[1] - tail[1]) > 1);
    }

}
