package ru.spbau.mit.parsing;

import org.jetbrains.annotations.NotNull;

/**
 * Tokens in which we split our input string.
 * {@link Token#getTokenType} tells us the type of current token
 * and {@link Token#getContent} returns its content
 * (just the string which user typed without any modification).
 */
public final class Token {

    public static final String VARIABLE_PATTERN = "[a-zA-Z](\\w*)";

    @NotNull private final TokenType type;
    @NotNull private String content;

    public Token(@NotNull TokenType type, @NotNull String content) {
        this.type = type;
        this.content = content;

        checkContent();
    }

    /**
     * get type of this token
     * @return Token's type
     */
    @NotNull
    public TokenType getTokenType() {
        return type;
    }

    /**
     * get content of this token
     * @return Token's content
     */
    @NotNull
    public String getContent() {
        return content;
    }

    public void changeContent(@NotNull String newContent) {
        content = newContent;
    }

    /**
     * @return content as it was in user input
     */
    @NotNull
    public String getContentWithSurrounding() {
        switch (type) {
            case DOUBLE_QUOTED_STRING:
                return "\"" + content + "\"";
            case SINGLE_QUOTED_STRING:
                return "'" + content + "'";
            default:
                return content;
        }
    }

    private void checkContent() {
        if (type == TokenType.PIPE && !content.equals("|")) {
            throw new IllegalArgumentException("ru.spbau.mit.Pipe should be only symbol `|`, not `" + content + "`");
        }

        if (((type == TokenType.COMMAND) || (type == TokenType.WORD))
                && (content.contains("|") || content.contains("\"") || content.contains("'"))) {
            throw new IllegalArgumentException("invalid token `" + content + "`");
        }

        if (type == TokenType.ASSIGNMENT) {
            String pattern = VARIABLE_PATTERN + "=.*";
            if (!content.matches(pattern)) {
                throw new IllegalArgumentException("invalid assignment `" + content + "`\n"
                        + "it should match regex `" + pattern + "`");
            }
        }
    }

    public enum TokenType {
        COMMAND,
        ASSIGNMENT,
        WORD, // everything which is not command/string/pipe
        SINGLE_QUOTED_STRING,
        DOUBLE_QUOTED_STRING,
        PIPE
    }

}
