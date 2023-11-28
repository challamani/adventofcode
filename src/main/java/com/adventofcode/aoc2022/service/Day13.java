package com.adventofcode.aoc2022.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;


@Service("day13")
@Slf4j
public class Day13 implements Puzzle<String,Object> {
    @Override
    public Object part1(String input) {
        return null;
    }

    @Override
    public Object part2(String input) {

        String[] records = input.split("\\n");
        int len = records.length;

        int index = 0;
        int sumOfIndices = 0;

        for (int i = 0; i < len; i += 3) {
            ++index;
            NumSet left = new NumSet(records[i]);
            NumSet right = new NumSet(records[i + 1]);

            sumOfIndices += left.compareTo(right) < 0 ? index : 0;
        }

        List<NumSet> setOfNumberSets = new ArrayList<>();
        for (int i = 0; i < len; i += 3) {
            ++index;
            NumSet left = new NumSet(records[i]);
            NumSet right = new NumSet(records[i + 1]);
            setOfNumberSets.add(left);
            setOfNumberSets.add(right);
        }

        NumSet dividerOne = new NumSet("[[2]]");
        NumSet dividerTwo = new NumSet("[[6]]");
        setOfNumberSets.add(dividerOne);
        setOfNumberSets.add(dividerTwo);

        Collections.sort(setOfNumberSets);
        String decoderKey = String.valueOf((setOfNumberSets.indexOf(dividerOne) + 1) * (setOfNumberSets.indexOf(dividerTwo) + 1));

        return new Integer[]{sumOfIndices, Integer.parseInt(decoderKey)};
    }

    private boolean compare(String left, String right) {
        Map<String, List<Integer>> leftMap = new TreeMap<>();
        Map<String, List<Integer>> rightMap = new TreeMap<>();

        left  = frameTheInput(left);
        right = frameTheInput(right);

        populateElements("ROOT", left, leftMap);
        populateElements("ROOT", right, rightMap);

        Set<String> keySets =  (leftMap.size() <= rightMap.size() ? leftMap : rightMap).keySet();
        List<String> keyList = new ArrayList<>(keySets);
        Collections.sort(keyList);

        //Collections.reverse(keyList);
        int minSize = Math.min(leftMap.size(), rightMap.size());
        int sameElements = 0;
        boolean isRightMatch = false;

        for (int i = 0; i < minSize; i++) {
            int compareResult = compareListItems(leftMap.get(keyList.get(i)), rightMap.get(keyList.get(i)));
            if (compareResult != 0) {
                isRightMatch = (compareResult < 1);
                break;
            }
            sameElements++;
        }

        if (!isRightMatch && (sameElements == minSize && leftMap.size() < rightMap.size())) {
            isRightMatch = true;
        }
        return isRightMatch;
    }

    private void populateElements(String key, String input, Map<String, List<Integer>> map) {

        input = input.substring(1, input.length() - 1);
        int subsetDepth = 0;
        int index = -1;
        char[] chars = input.toCharArray();
        int len = input.length();
        while (++index < input.length()) {

            if (subsetDepth == 0 && chars[index] == ',') {
                map.putIfAbsent(key, new ArrayList<>());
            } else {
                if (chars[index] == '[') {
                    key = getKeyCount(key, map.keySet());
                    map.putIfAbsent(key, new ArrayList<>());
                    subsetDepth++;
                } else if (chars[index] == ']') {
                    if (key.lastIndexOf(".") > 0) {
                        key = key.substring(0, key.lastIndexOf("."));
                    }
                    subsetDepth--;
                } else if(chars[index] >= '0' && chars[index] <= '9') {
                    StringBuilder number = new StringBuilder();
                    while (index < len && chars[index] >= '0' && chars[index] <= '9') {
                        number.append(chars[index]);
                        ++index;
                    }
                    map.putIfAbsent(key, new ArrayList<>());
                    map.get(key).add(Integer.parseInt(number.toString()));
                    index -= (number.length() > 1) ? 1 : 0;
                }
            }
        }
    }

    private String getKeyCount(final String key, Set<String> keySet) {
        long count = keySet.stream().filter(keyStr -> keyStr.startsWith(key)).count();
        String newKey = key.concat(".").concat(Long.toString(count));
        return newKey;
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
        return temp;
    }

    private int compareListItems(List<Integer> left, List<Integer> right) {

        int size = Math.min((Objects.nonNull(left) ? left.size() : 0), (Objects.nonNull(right) ? right.size():0));
        for (int i = 0; i < size; i++) {
            int val = Integer.compare(left.get(i), right.get(i));
            if (val != 0) {
                return val;
            }
        }
        return Integer.compare(Objects.nonNull(left) ? left.size() : 0, Objects.nonNull(right) ? right.size() : 0);
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

    @Override
    public String toString() {
        return "NumSet{" +
                "subsets=" + subsets +
                ", value=" + value +
                '}';
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

    public String getContent(){
        return content;
    }
}
