package com.adventofcode.aoc2022.service;

public interface Puzzle<R,T> {

    T solve(R request);
}
