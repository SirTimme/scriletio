package dev.sirtimme.scriletio.error;

public class ExceptionFormatter {
    public static String format(String content, ParsingException exception) {
        final var sb = new StringBuilder();
        sb.append("Error: Incorrect use");
        sb.append("\n```\n");
        sb.append(content);
        sb.append("\n");
        sb.repeat(" ", exception.getColumn());
        sb.append("^");
        sb.append("\n```\n");
        sb.append("Cause: ");
        sb.append(exception.getMessage());
        return sb.toString();
    }
}
