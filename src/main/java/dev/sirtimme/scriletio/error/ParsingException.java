package dev.sirtimme.scriletio.error;

public class ParsingException extends RuntimeException {
    private final int column;

    public ParsingException(String message, int column) {
        super(message);
        this.column = column;
    }

    public int getColumn() {
        return column;
    }
}
