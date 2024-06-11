package dev.sirtimme.scriletio.utils;

public sealed interface Token {
    record Digit(int value, int column) implements Token {}

    record Day(int column) implements Token {}

    record Hour(int column) implements Token {}

    record Minute(int column) implements Token {}

    record EOF(int column) implements Token {}
}