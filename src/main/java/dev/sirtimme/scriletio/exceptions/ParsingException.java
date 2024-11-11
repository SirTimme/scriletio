package dev.sirtimme.scriletio.exceptions;

public class ParsingException extends RuntimeException {
    private final int index;

    public ParsingException(final String message, final int index) {
        super(message);
        this.index = index;
    }

    public int getIndex() {
        return index;
    }
}