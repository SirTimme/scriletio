package dev.sirtimme.scriletio.format;

import dev.sirtimme.scriletio.error.ParsingException;

public class Formatter {
	public static String format(String content, ParsingException exception) {
		final var sb = new StringBuilder();
		sb.append("**Error:**\n");
		sb.append("Incorrect usage of the duration format. Error occurred here:");
		sb.append("\n```\n");
		sb.append(content);
		sb.append("\n");
		sb.repeat(" ", exception.getColumn());
		sb.append("^");
		sb.append("\n```\n");
		sb.append("**Cause:**\n");
		sb.append(exception.getMessage());
		sb.append("\n\n");
		sb.append("**Examples:**\n");
		sb.append("- `4D` or `4d` = 4 days\n");
		sb.append("- `2D3H` or `2d3h` = 2 days 3 hours\n");
		sb.append("- `1D5H3M` or `1d5h3m` = 1 day 5 hours 3 minutes");
		return sb.toString();
	}

	public static String format() {
		return """
				**Disclaimer:**
				In order for Scriletio to work, your `discord user id` gets saved. [What's this?](<https://support.discord.com/hc/en-us/articles/206346498-Where-can-I-find-my-User-Server-Message-ID>)
				This is public available information and you can delete all of your stored data at any time using the `/delete` command.

				**Continue?**
				""";
	}
}