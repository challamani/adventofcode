package com.adventofcode.aoc2022.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;


@Service("day13")
@Slf4j
public class Day13 implements Puzzle<String,Object> {
    @Override
    public Object solve(String input) {

        String[] records = input.split("\\n");
        int len = records.length;

        int index = 0;
        int sumOfIndices = 0;

        for (int i = 0; i < len; i += 3) {
            ++index;
            NumSet left = new NumSet(records[i]);
            NumSet right = new NumSet(records[i + 1]);
            sumOfIndices += left.compareTo(right) < 0 ? index : 0;
            log.info("left {} right {}",left,right);
        }
        return sumOfIndices;
    }

    private boolean compare(String left, String right) {

        Map<String, List<Integer>> leftMap = new TreeMap<>();
        Map<String, List<Integer>> rightMap = new TreeMap<>();

        List<List<Integer>> leftSets = new ArrayList<>();
        List<List<Integer>> rightSets = new ArrayList<>();

        //left = frameTheInput(left);
        //right = frameTheInput(right);

        //Populate keys - using hashmap <root>.<0>.<1>.<2>...<n level>

        log.info("left elements {}", leftSets);
        log.info("right elements {}", rightSets);
        boolean isRightMatch = false;


        int minSize = Math.min(leftSets.size(), rightSets.size());
        int sameElements = 0;
        for (int i = 0; i < minSize; i++) {
            boolean[] result = compareListItems(leftSets.get(i), rightSets.get(i));
            if (result[0]) {
                isRightMatch = true;
                break;
            } else if (!result[1]) {
                isRightMatch = false;
                break;
            } else {
                sameElements++;
            }
        }

        if (!isRightMatch && sameElements == minSize && leftSets.size() < rightSets.size()) {
            isRightMatch = true;
        }
        return isRightMatch;
    }

    private String frameTheInput(String input) {
        int len = input.length();
        String temp = input;
        int index = 0;

        while (index < len && index != -1) {

            if (temp.substring(index).indexOf("],") > 0) {
                index = temp.substring(index).indexOf("],") + index;
                index += 2;
            }else {
                index = -1;
            }

            if (index > 0 && temp.charAt(index) >= '0' && temp.charAt(index) <= '9') {
                temp = temp.substring(0, index).concat("[").concat(temp.substring(index));
                index += temp.substring(index).indexOf("]");
                temp = temp.substring(0, index).concat("]").concat(temp.substring(index));
            } else if (index > 0) {
                index++;
            }
        }
        log.info("frameTheInput before {} after {}", input, temp);
        return temp;
    }

    private boolean[] compareListItems(List<Integer> left, List<Integer> right) {

        if (right != null && right.size() == 0 && left.size() == 0) {
            return new boolean[]{true, true};
        } else if ((right == null || right.size() == 0) && left != null && left.size() >= 0) {
            return new boolean[]{false, false};
        }

        int size = Math.min(left.size(), right.size());
        ;
        boolean isRightMatch = false;

        int equalMatch = 0;
        for (int i = 0; i < size; i++) {
            if (right.get(i) > left.get(i)) {
                isRightMatch = true;
                break;
            } else if (right.get(i) == left.get(i)) {
                equalMatch++;
            } else {
                break;
            }
        }
        return new boolean[]{(isRightMatch || (equalMatch == left.size() && left.size() < right.size())),
                (equalMatch == left.size() && left.size() == right.size())};
    }

}

@Slf4j
class NumSet implements Comparable<NumSet>{

    private String content;
    private List<NumSet> subsets = new ArrayList<>();
    private int value;
    private boolean number = true;

    public NumSet(String input) {
        this.content = input;
        if (input.startsWith("[")) {
            populateElements(input);
        } else {
            value = Integer.parseInt(input);
        }
    }

    private void populateElements(String input){

        input = input.substring(1, input.length() - 1);
        number = false;
        int subsetDepth = 0;

        StringBuilder subset = new StringBuilder();
        for (char ch: input.toCharArray()) {
            if (subsetDepth == 0 && ch == ','){
                subsets.add(new NumSet(subset.toString()));
                subset = new StringBuilder();
            }
            else {
                if (ch == '[') {
                    subsetDepth++;
                } else if (ch == ']') {
                    subsetDepth--;
                }
                subset.append(ch);
            }
        }
        if (!subset.isEmpty()) {
            subsets.add(new NumSet(subset.toString()));
        }
    }

    public boolean isNumber(){
        return number;
    }

    @Override
    public int compareTo(NumSet numberSet) {
        if (this.isNumber() && numberSet.isNumber()) {
            return Integer.compare(this.getValue(), numberSet.getValue());
        } else if (this.isNumber()) {
            return new NumSet("[" + getValue() + "]").compareTo(numberSet);
        } else if (!this.isNumber() && !numberSet.isNumber()) {
            int minSize = Math.min(this.getSubsets().size(), numberSet.getSubsets().size());
            for (int i = 0; i < minSize; i++) {
                log.info("left {} right {}", this.getSubsets(), numberSet.getSubsets());
                int compare = this.getSubsets().get(i).compareTo(numberSet.getSubsets().get(i));
                if (compare != 0) {
                    return compare;
                }
            }
            return Integer.compare(this.getSubsets().size(), numberSet.getSubsets().size());
        } else {
            return this.compareTo(new NumSet("[" + numberSet.getValue() + "]"));
        }
    }

    public Integer getValue(){
        return value;
    }

    public List<NumSet> getSubsets() {
        return subsets;
    }

    public void setSubsets(List<NumSet> subsets) {
        this.subsets = subsets;
    }
}
