package dev.sirtimme.scriletio.response;

public class ResponseHelpers {
    public static String withBold(final Object message) {
        return "**" + message + "**";
    }

    public static String withItalic(final Object message) {
        return "_" + message + "_";
    }
}
