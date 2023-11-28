package com.adventofcode.aoc2022.service;

public interface Puzzle<R,T> {
    T part1(R input);
    T part2(R input);
}
