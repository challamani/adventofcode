package com.adventofcode.aoc2022.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

@Service("day11")
@Slf4j
public class Day11 implements Puzzle<String,Object> {

    private Map<Integer, Integer> itemWorryValue;
    private Map<Integer, Queue<Integer>> monkeyItems;
    private Map<Integer, Map<String, String>> monkeyRules;
    private String opVar = "operation";
    private String divisibleVar = "divisible";
    private String trueVar = "true";
    private String falseVar = "false";

    private int LCM = 9699690; //LCM of 3*11*19*5*2*7*17*13

    @Override
    public Object solve(String input) {

        itemWorryValue = new HashMap<>();
        monkeyItems = new HashMap<>();
        monkeyRules = new HashMap<>();

        int rounds = 10000;
        parseInputData(input);

        log.info("after parsing input data{}", monkeyRules);
        log.info("item worry-levels {}", itemWorryValue);
        log.info("money holding item-indexes {}", monkeyItems);

        Map<Integer, Integer> inspectedItems = new HashMap<>();

        IntStream.range(0, rounds).forEach(round -> monkeyItems.entrySet().stream().forEach(monkeyQueueEntry -> {
            int currentMonkeyId = monkeyQueueEntry.getKey();
            Queue<Integer> queuedItems = monkeyQueueEntry.getValue();

            inspectedItems.putIfAbsent(currentMonkeyId, 0);
            inspectedItems.put(currentMonkeyId, inspectedItems.get(currentMonkeyId) + queuedItems.size());
            while (queuedItems.size() > 0) {
                int itemIndex = queuedItems.remove();
                int targetMonkeyId = deriveTargetMoney(currentMonkeyId, itemIndex);
                monkeyItems.get(targetMonkeyId).add(itemIndex);
            }
        }));

        monkeyItems.entrySet().stream().forEach(monkeyQueueEntry -> {
            StringBuilder stringBuilder = new StringBuilder();
            monkeyQueueEntry.getValue().forEach(itemIndex -> stringBuilder.append(itemWorryValue.get(itemIndex)).append(","));
            log.info("monkey:: {} items :: {}", monkeyQueueEntry.getKey(), stringBuilder);
        });
        log.info("inspected Items {}", inspectedItems);
        return inspectedItems;
    }

    private int deriveTargetMoney(int monkeyId, int itemIndex) {

        int worryValue = itemWorryValue.get(itemIndex);
        String operation = monkeyRules.get(monkeyId).get(opVar);

        int derivedWorryValue = performOperation(operation, worryValue);
        int divisibleVal = Integer.parseInt(monkeyRules.get(monkeyId).get(divisibleVar));

        int targetMonkeyId;
        if (derivedWorryValue % divisibleVal == 0) {
            targetMonkeyId = Integer.parseInt(monkeyRules.get(monkeyId).get(trueVar));
        } else {
            targetMonkeyId = Integer.parseInt(monkeyRules.get(monkeyId).get(falseVar));
        }
        itemWorryValue.put(itemIndex, derivedWorryValue);
        return targetMonkeyId;
    }

    private int performOperation(String operation, int worryValue) {

        String operand = operation.substring(operation.lastIndexOf(" ") + 1).trim();
        int temp;
        int actual;
        if (operand.equals("old")) {
            temp = worryValue;
        } else {
            temp = Integer.parseInt(operand);
        }
        if (operation.contains("+")) {
            actual = (worryValue + temp);
        } else {
            BigInteger derived = new BigInteger(Integer.toString(temp))
                    .multiply(new BigInteger(Integer.toString(worryValue)));

            if (derived.compareTo(new BigInteger(Integer.toString(LCM))) == 1) {
                actual = LCM + derived.mod(new BigInteger(Integer.toString(LCM))).intValue();
            } else {
                actual = (temp * worryValue);
            }
        }

        if (actual > LCM) {
            actual = LCM + (actual % LCM);
        }
        return actual;
    }

    private void parseInputData(String input) {

        AtomicInteger currentMoneyId = new AtomicInteger();
        AtomicInteger itemIndex = new AtomicInteger(1);

        Arrays.stream(input.split("\\n")).forEach(record -> {
            if (record.startsWith("Monkey ")) {
                int monkeyId = Integer.parseInt(record.substring(record.indexOf(" "), record.indexOf(":")).trim());
                monkeyItems.put(monkeyId, new LinkedList<>());
                currentMoneyId.set(monkeyId);
            } else if (record.contains("Starting items:")) {
                String items = record.substring(record.indexOf(":") + 1);
                log.info("monkey {} and items {}", currentMoneyId.get(), items);
                Arrays.stream(items.split(",")).map(str -> Integer.parseInt(str.trim())).forEach(
                        worryLevel -> {
                            monkeyItems.get(currentMoneyId.get()).add(itemIndex.get());
                            itemWorryValue.put(itemIndex.getAndAdd(1), worryLevel);
                        });
            } else if (record.contains("Operation:")) {
                monkeyRules.put(currentMoneyId.get(), new HashMap<>());
                String operation = record.substring(record.indexOf("=") + 1).trim();
                log.info("monkey {} and operation {}", currentMoneyId.get(), operation);
                monkeyRules.get(currentMoneyId.get()).put("operation", operation);
            } else if (record.contains("Test:")) {
                String divisible = (record.substring(record.lastIndexOf(" ")).trim());
                log.info("monkey {} and divisible {}", currentMoneyId.get(), divisible);
                monkeyRules.get(currentMoneyId.get()).put("divisible", divisible);
            } else if (record.contains("If true")) {
                String trueMoney = (record.substring(record.lastIndexOf(" ")).trim());
                log.info("monkey {} and true-rule {}", currentMoneyId.get(), trueMoney);
                monkeyRules.get(currentMoneyId.get()).put("true", trueMoney);
            } else if (record.contains("If false")) {
                String falseMoney = (record.substring(record.lastIndexOf(" ")).trim());
                log.info("monkey {} and false-rule {}", currentMoneyId.get(), falseMoney);
                monkeyRules.get(currentMoneyId.get()).put("false", falseMoney);
            }
        });
    }
}

