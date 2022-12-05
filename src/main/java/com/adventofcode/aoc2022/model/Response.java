package com.adventofcode.aoc2022.model;

public class Response<T> {

    private T result;

    public Response() {
    }

    @Override
    public String toString() {
        return "Response{" +
                "result=" + result +
                '}';
    }

    public Response(T result) {
        this.result = result;
    }

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }
}
