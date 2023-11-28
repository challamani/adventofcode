package com.adventofcode.aoc2022.controller;

import com.adventofcode.aoc2022.model.Request;
import com.adventofcode.aoc2022.model.Response;
import com.adventofcode.aoc2022.service.Day1;
import com.adventofcode.aoc2022.service.Puzzle;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@Slf4j
public class Aoc2022Controller {

    private final Map<String, Puzzle> puzzleMap;

    @Autowired
    public Aoc2022Controller(Map<String, Puzzle> puzzleMap, Day1 day1){
        this.puzzleMap=puzzleMap;
    }

    @PostMapping("/2022/{day}/part1")
    public ResponseEntity<Response<Object>> solvePart1(
            @PathVariable(name = "day", required = false) String day,
            @RequestBody String request) {

        //log.info("list of keys {}", puzzleMap.keySet());
        if (puzzleMap.containsKey(day)) {
            Object result = puzzleMap.get(day).part1(request);
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new Response<>(result));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new Response<>("Puzzle implementation not found"));
        }
    }

    @PostMapping("/2022/{day}/part2")
    public ResponseEntity<Response<Object>> solvePart2(
            @PathVariable(name = "day", required = false) String day,
            @RequestBody String request) {

        //log.info("list of keys {}", puzzleMap.keySet());
        if (puzzleMap.containsKey(day)) {
            Object result = puzzleMap.get(day).part2(request);
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new Response<>(result));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new Response<>("Puzzle implementation not found"));
        }
    }

}
