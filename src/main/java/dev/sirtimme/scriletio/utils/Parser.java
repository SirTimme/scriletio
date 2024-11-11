package dev.sirtimme.scriletio.utils;

import dev.sirtimme.scriletio.exceptions.ParsingException;

import java.time.Duration;
import java.util.ArrayList;

public class Parser {
    private ArrayList<Token> tokens;
    private int index;

    public Parser() {
        this.tokens = new ArrayList<>();
        this.index = 0;
    }

    public long parse(final String content) {
        var lexer = new Lexer(content);
        tokens = lexer.lex();
        var totalDuration = 0L;

        while (index < tokens.size()) {
            var currentToken = tokens.get(index);

            if (index == 0 && !(currentToken instanceof Token.Digit)) {
                throw new ParsingException("The duration format must start with a digit!", index);
            }

            if (currentToken instanceof Token.Digit digit) {
                var duration = getDuration(digit);
                totalDuration += duration.toMinutes();
            }

            index++;
        }

        return totalDuration;
    }

    private Duration getDuration(Token.Digit digit) {
        final var nextToken = index + 1 >= tokens.size() ? new Token.EOF(index) : tokens.get(index + 1);
        return switch (nextToken) {
            case Token.Day day -> Duration.ofDays(digit.value());
            case Token.Hour hour -> Duration.ofHours(digit.value());
            case Token.Minute minute -> Duration.ofMinutes(digit.value());
            default -> throw new ParsingException("A digit must be followed by one of [ `D, d, H, h, M, m` ]", digit.column());
        };
    }
}