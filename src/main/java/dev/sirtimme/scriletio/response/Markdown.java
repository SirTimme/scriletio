package dev.sirtimme.scriletio.response;

public class Markdown {
    public static String bold(final Object content) {
        return "**" + content + "**";
    }

    public static String channel(final Object content) {
        return "<#" + content + ">";
    }

    public static String monospace(final Object content) {
        return "`" + content + "`";
    }

    public static String italic(final Object content) {
        return "_" + content + "_";
    }

    public static String codeBlock(final Object content) {
        return "```\n" + content + "```\n";
    }

    public static String list(final Object... items) {
        final var sb = new StringBuilder();
        for (final var item : items) {
            sb.append("- ").append(item).append("\n");
        }
        return sb.toString();
    }
}