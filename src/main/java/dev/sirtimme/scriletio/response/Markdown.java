package dev.sirtimme.scriletio.response;

public class Markdown {
    public static String bold(final Object content) {
        return "**" + content + "**";
    }

    public static String channel(final Object content) {
        return "<#" + content + ">";
    }

    public static String command(final String commandName, final Long commandId) {
        return "</" + commandName + ":" + commandId + ">";
    }

    public static String h2(final Object content) {
        return "## " + content;
    }

    public static String h3(final Object content) {
        return "### " + content;
    }
}