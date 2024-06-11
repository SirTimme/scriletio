package dev.sirtimme.scriletio.utils;

public class TimeUtils {
    public static String createReadableDuration(final long minutes) {
        final var days = minutes / (24 * 60);
        final var hours = (minutes % (24 * 60)) / 60;
        final var remaining = minutes % 60;

        return "Duration: " + days + " days, " + hours + " hours, " + remaining + " minutes";
    }
}