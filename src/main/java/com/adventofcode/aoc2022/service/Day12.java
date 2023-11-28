package com.adventofcode.aoc2022.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.IntStream;

@Service("day12")
@Slf4j
public class Day12 implements Puzzle<String,Object> {

    private char[][] hills;

    private int[][] adjacentNode = {{1, 0}, {-1, 0}, {0, 1}, {0, -1}};

    @Override
    public Object part1(String input) {
        return null;
    }

    @Override
    public Object part2(String input) {
        return partOneAndTwo(input);
    }

    private int[] findAndReturn(char target, char[][] hills) {
        return IntStream.range(0, hills.length).mapToObj(row -> row)
                .flatMap(row -> IntStream.range(0, hills[row].length)
                        .filter(col -> hills[row][col] == target)
                        .mapToObj(col -> new int[]{row, col}))
                .findFirst()
                .orElse(null);
    }

    public Integer[] partOneAndTwo(String input) {

        int len = input.split("\\n")[0].length();
        char[][] map = Arrays.stream(input.split("\\n"))
                .map(line -> line.toCharArray())
                .toArray(r -> new char[r][len]);

        int[] start = findAndReturn('S', map);
        int[] target = findAndReturn('E', map);

        map[start[0]][start[1]] = 'a';
        map[target[0]][target[1]] = 'z';

        int[][] distance = distance(map, start);
        log.info("distance {}", distance);
        int partOne = distance[target[0]][target[1]];

        int partTwo = IntStream.range(0, map.length)
                .flatMap(row -> IntStream.range(0, map[0].length)
                        .filter(col -> map[row][col] == 'z')
                        .map(col -> distance[row][col]))
                .filter(x -> x != -1)
                .min().orElse(Integer.MIN_VALUE);

        return new Integer[]{partOne, partTwo};
    }

    private int[][] distance(char[][] map, int[] destination) {
        int maxRows = map.length;
        int maxCols = map[0].length;

        boolean visited[][] = new boolean[maxRows][maxCols];
        int distance[][] = new int[maxRows][maxCols];

        for (int[] row : distance) {
            Arrays.fill(row, -1);
        }

        PriorityQueue<int[]> priorityQueue = new PriorityQueue<>(Comparator.comparingInt(e -> e[0]));
        priorityQueue.add(new int[]{0, destination[0], destination[1], destination[0], destination[1]});
        log.info("queue items {}", priorityQueue.toArray());

        while (!priorityQueue.isEmpty()) {
            int[] record = priorityQueue.poll();
            int targetRow = record[1];
            int targetColumn = record[2];

            log.info("queue items {}", priorityQueue.toArray());
            if (!(0 <= targetRow && targetRow < maxRows && 0 <= targetColumn && targetColumn < maxCols)) {
                continue;
            }

            if (visited[targetRow][targetColumn]) {
                continue;
            }

            int sourceRow = record[3];
            int sourceCol = record[4];

            int newHeight = map[targetRow][targetColumn];
            int oldHeight = map[sourceRow][sourceCol];
            if (!(newHeight <= oldHeight + 1)) {
                continue;
            }

            int targetDistance = record[0];
            visited[targetRow][targetColumn] = true;
            distance[targetRow][targetColumn] = targetDistance;

            priorityQueue.add(new int[]{targetDistance + 1, targetRow + 0, targetColumn + 1, targetRow, targetColumn});
            priorityQueue.add(new int[]{targetDistance + 1, targetRow + 0, targetColumn - 1, targetRow, targetColumn});

            priorityQueue.add(new int[]{targetDistance + 1, targetRow + 1, targetColumn + 0, targetRow, targetColumn});
            priorityQueue.add(new int[]{targetDistance + 1, targetRow - 1, targetColumn + 0, targetRow, targetColumn});

            IntStream.range(0, maxRows).forEach(range -> log.info("visited {}", visited[range]));
            IntStream.range(0, maxRows).forEach(range -> log.info("distance {}", distance[range]));
        }
        return distance;
    }
}
