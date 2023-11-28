package com.adventofcode.aoc2022.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.IntStream;

@Service("day8")
@Slf4j
public class Day8 implements Puzzle<String,Integer> {


    /**
     * Day 8: Treetop Tree House - double dimensional array should solve the problem
     * Note: This is not an optimal solution, there can N of solutions might be available and achievable in less than o(n*n)
     * Below solution consumes more memory but less than o(n*n) time-complexity
     */

    /**
     * Sorry for this bruteforce solution */
    public Integer part2(String input) {

        String[] rows = input.split("\\n");

        Map<Integer, Map<Integer, Integer>> treeHeights = new HashMap<>();
        final int[] rowIndex = {0};
        while (rowIndex[0] < rows.length) {
            treeHeights.put(rowIndex[0], new HashMap<>());
            int colIndex = 0;
            for (char ch : rows[rowIndex[0]].toCharArray()) {
                int treeHeight = Integer.parseInt(ch + "");
                treeHeights.get(rowIndex[0]).put(colIndex, treeHeight);
                colIndex += 1;
            }
            rowIndex[0]++;
        }

        final int[] maxScenicScore = {Integer.MIN_VALUE};
        int len = rows[0].length();
        IntStream.range(1,len-1).forEach(colSpot -> {
            int rowInd = 0;
            while (++rowInd < (rows.length - 1)) {

                int spotTreeHeight = treeHeights.get(rowInd).get(colSpot);
                int top = topScenicTrees(treeHeights, rowInd, colSpot, spotTreeHeight);
                int bottom = bottomScenicTrees(treeHeights, rowInd, colSpot, spotTreeHeight);
                int left = leftScenicTrees(treeHeights, rowInd, colSpot, spotTreeHeight);
                int right = rightScenicTrees(treeHeights, rowInd, colSpot, spotTreeHeight);

                int scenicScore = (top * bottom * left * right);

                if (maxScenicScore[0] < scenicScore) {
                    log.info("tree-height {} col [{},{}] top {} bottom {}, left {}, right {} score {}",rowInd, colSpot,spotTreeHeight, top ,bottom , left , right, scenicScore);
                    maxScenicScore[0] = scenicScore;
                }
            }
        });

        return maxScenicScore[0];
    }

    private int topScenicTrees(Map<Integer, Map<Integer, Integer>> treeHeights, int row, int col, int spotTreeHeight) {

        int cnt = 0;
        int rowIndex = row;
        while (--rowIndex >= 0) {
            int height = treeHeights.get(rowIndex).get(col);
            if (spotTreeHeight <= height) {
                cnt++;
                break;
            }
            cnt++;
        }
        return cnt;
    }

    private int bottomScenicTrees(Map<Integer, Map<Integer, Integer>> treeHeights, int row, int col, int spotTreeHeight) {

        int cnt = 0;
        int rowIndex = row;
        while (++rowIndex < treeHeights.size()) {
            int height = treeHeights.get(rowIndex).get(col);
            if (spotTreeHeight <= height) {
                cnt++;
                break;
            }
            cnt++;
        }
        return cnt;
    }

    private int leftScenicTrees(Map<Integer, Map<Integer, Integer>> treeHeights, int row, int col, int spotTreeHeight) {
        int colIndex = col;
        int cnt = 0;
        while (--colIndex >= 0) {
            int height = treeHeights.get(row).get(colIndex);
            if (spotTreeHeight <= height) {
                cnt++;
                break;
            }
            cnt++;
        }
        return cnt;
    }

    private int rightScenicTrees(Map<Integer, Map<Integer, Integer>> treeHeights, int row, int col, int spotTreeHeight) {
        int colIndex = col;
        int cnt = 0;
        while (++colIndex < treeHeights.get(row).size()) {
            int height = treeHeights.get(row).get(colIndex);
            if (spotTreeHeight <= height) {
                cnt++;
                break;
            }
            cnt++;
        }
        return cnt;
    }



    public Integer part1(String input) {

        String[] rows = input.split("\\n");
        int rowsCnt = rows.length;
        int colCnt = rows[0].length();
        int visibleOnEdge = ((2 * rowsCnt) + (2 * colCnt) - 4);

        Map<String, Map<Integer, Integer>> maxValue = new HashMap<>();
        maxValue.put("TOP", new HashMap<>());
        maxValue.put("LEFT", new HashMap<>());

        int topIndex = 0;
        for (char ch : rows[0].toCharArray()) {
            maxValue.get("TOP").putIfAbsent(topIndex++, Integer.parseInt(ch + ""));
        }

        Map<Integer, Map<Integer, Integer>> topPositionMax = new HashMap<>();
        Map<Integer, Map<Integer, Integer>> bottomPositionMax = new HashMap<>();

        int rowIndex = rows.length - 1;
        while (rowIndex >= 0) {
            StringBuilder stringBuilder = new StringBuilder(rows[rowIndex]);
            topPositionMax.put(rowIndex, new HashMap<>());
            bottomPositionMax.put(rowIndex, new HashMap<>());

            int colIndex = rows[rowIndex].length() - 1;
            String reverseStr = stringBuilder.reverse().toString();

            int maxHeight = Integer.MIN_VALUE;
            int bottomMaxHeight = Integer.MIN_VALUE;
            for (char ch : reverseStr.toCharArray()) {
                int treeHeight = Integer.parseInt(ch + "");
                maxHeight = maxHeight < treeHeight ? treeHeight : maxHeight;
                topPositionMax.get(rowIndex).put(colIndex, maxHeight);

                if (rowIndex != rows.length - 1) {
                    bottomMaxHeight = bottomPositionMax.get(rowIndex + 1).get(colIndex);
                } else {
                    bottomMaxHeight = treeHeight;
                }
                bottomMaxHeight = bottomMaxHeight < treeHeight ? treeHeight : bottomMaxHeight;
                bottomPositionMax.get(rowIndex).put(colIndex, bottomMaxHeight);
                colIndex -= 1;
            }
            rowIndex--;
        }

        int interiorTreesCnt = 0;
        rowIndex = 1;

        while (rowIndex < (rowsCnt - 1)) {
            int colIndex = 0;
            int rowLen = rows[rowIndex].length();
            maxValue.get("LEFT").putIfAbsent(rowIndex, Integer.parseInt(rows[rowIndex].charAt(0) + ""));

            for (char ch : rows[rowIndex].toCharArray()) {

                if (colIndex == 0 || colIndex == (rowLen - 1)) {
                    colIndex++;
                    continue;
                }
                boolean isVisible = false;

                int treeHeight = Integer.parseInt(ch + "");
                if (treeHeight > maxValue.get("LEFT").get(rowIndex)) {
                    maxValue.get("LEFT").put(rowIndex, treeHeight);
                    isVisible = true;
                }

                if (treeHeight > maxValue.get("TOP").get(colIndex)) {
                    maxValue.get("TOP").put(colIndex, treeHeight);
                    isVisible = true;
                }

                if (colIndex != (rowLen - 1) && treeHeight > topPositionMax.get(rowIndex).get(colIndex + 1)) {
                    isVisible = true;
                }
                if (rowIndex != (rowsCnt - 1) && treeHeight > bottomPositionMax.get(rowIndex + 1).get(colIndex)) {
                    isVisible = true;
                }

                if (isVisible) {
                    interiorTreesCnt += 1;
                }
                colIndex++;
            }
            rowIndex++;
        }
        log.info("{} + {} ", visibleOnEdge, interiorTreesCnt);
        return (visibleOnEdge + interiorTreesCnt);
    }
}
