package ru.spbau.mit;

import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;
import ru.spbau.mit.parsing.Token;
import ru.spbau.mit.parsing.Tokenizer;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;

public class TokenizerTest {

    private static Map<String, Token[]> samples;

    @Rule
    public final Timeout globalTimeout = new Timeout(2, TimeUnit.SECONDS);

    @BeforeClass
    public static void createSamples() {
        samples = new HashMap<>();

        final String sample1 = "echo 123 ' 123' | wc | echo '12 3' \"123 123 12   \"";
        final Token[] answer1 = {
                new Token(Token.TokenType.COMMAND, "echo"),
                new Token(Token.TokenType.WORD, "123"),
                new Token(Token.TokenType.SINGLE_QUOTED_STRING, " 123"),
                new Token(Token.TokenType.PIPE, "|"),
                new Token(Token.TokenType.COMMAND, "wc"),
                new Token(Token.TokenType.PIPE, "|"),
                new Token(Token.TokenType.COMMAND, "echo"),
                new Token(Token.TokenType.SINGLE_QUOTED_STRING, "12 3"),
                new Token(Token.TokenType.DOUBLE_QUOTED_STRING, "123 123 12   ")
        };

        final String sample2 = "FILE=example.txt|echo $FILE   \" $FILE \" '$FILE'";
        final Token[] answer2 = {
                new Token(Token.TokenType.ASSIGNMENT, "FILE=example.txt"),
                new Token(Token.TokenType.PIPE, "|"),
                new Token(Token.TokenType.COMMAND, "echo"),
                new Token(Token.TokenType.WORD, "$FILE"),
                new Token(Token.TokenType.DOUBLE_QUOTED_STRING, " $FILE "),
                new Token(Token.TokenType.SINGLE_QUOTED_STRING, "$FILE")
        };

        final String sample3 = "    X=\"123\"|echo 1  |  Y='123'";
        final Token[] answer3 = {
                new Token(Token.TokenType.ASSIGNMENT, "X=\"123\""),
                new Token(Token.TokenType.PIPE, "|"),
                new Token(Token.TokenType.COMMAND, "echo"),
                new Token(Token.TokenType.WORD, "1"),
                new Token(Token.TokenType.PIPE, "|"),
                new Token(Token.TokenType.ASSIGNMENT, "Y=\'123\'")
        };

        final String sample4 = "";
        final Token[] answer4 = new Token[0];

        samples.put(sample1, answer1);
        samples.put(sample2, answer2);
        samples.put(sample3, answer3);
        samples.put(sample4, answer4);
    }

    @Test
    public void testTokenize() {
        for (String sample : samples.keySet()) {
            Tokenizer tokenizer = new Tokenizer(sample);
            Token[] tokensActual = tokenizer.tokenizeCommands();
            Token[] tokensExpected = samples.get(sample);
            checkEqual(tokensExpected, tokensActual);
        }
    }

    private void checkEqual(Token[] tokensExpected, Token[] tokensActual) {
        assertEquals(tokensExpected.length, tokensActual.length);
        for (int i = 0; i < tokensActual.length; i++) {
            assertEquals(tokensExpected[i].getTokenType(), tokensActual[i].getTokenType());
            assertEquals(tokensExpected[i].getContent(), tokensActual[i].getContent());
        }
    }
}