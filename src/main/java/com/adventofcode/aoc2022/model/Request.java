package com.adventofcode.aoc2022.model;

public class Request {

    private String input;

    public Request() {
    }

    public Request(String input) {
        this.input = input;
    }

    @Override
    public String toString() {
        return "Request{" +
                "input=" + input +
                '}';
    }

    public String getInput() {
        return input;
    }

    public void setInput(String input) {
        this.input = input;
    }
}
