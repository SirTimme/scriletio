package dev.sirtimme.scriletio.parse;

import dev.sirtimme.scriletio.error.ParsingException;

import java.util.ArrayList;

public class Lexer {
    private final String content;
    private int index;

    public Lexer(final String content) {
        this.content = content;
        this.index = 0;
    }

    public ArrayList<Token> lex() throws ParsingException {
        var tokens = new ArrayList<Token>();
        while (index < content.length()) {
            if (Character.isDigit(current())) {
                var digitStart = index;
                while (Character.isDigit(current())) {
                    index++;
                }
                var digitValue = content.substring(digitStart, index);
                tokens.add(new Token.Digit(Integer.parseInt(digitValue), index));
                continue;
            }
            final var currentToken = current();
            switch (currentToken) {
                case 'D':
                case 'd':
                    tokens.add(new Token.Day(index));
                    break;
                case 'H':
                case 'h':
                    tokens.add(new Token.Hour(index));
                    break;
                case 'M':
                case 'm':
                    tokens.add(new Token.Minute(index));
                    break;
                default:
                    throw new ParsingException("Expected one of D, d, H, h, M, m got " + currentToken + " instead", index);
            }
            index++;
        }

        return tokens;
    }

    private char current() {
        return index >= content.length() ? ' ' : content.charAt(index);
    }
}