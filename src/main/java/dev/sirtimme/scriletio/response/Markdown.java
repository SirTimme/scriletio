package dev.sirtimme.scriletio.response;

public class Markdown {
    public static String bold(final Object message) {
        return "**" + message + "**";
    }

    public static String italic(final Object message) {
        return "_" + message + "_";
    }

    public static String codeBlock(final Object message) {
        return "```\n" + message + "```\n";
    }
}