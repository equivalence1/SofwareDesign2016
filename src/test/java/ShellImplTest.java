import commands.CommandFactory;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ShellImplTest {

    @Test
    public void testSubstituteVariables() {
        ShellImpl shell = new ShellImpl(CommandFactory.INSTANCE);
        shell.environment.put("x", "123");
        shell.environment.put("some_1variable1", "aba c aba ");

        Token testToken1 = new Token(Token.TokenType.DOUBLE_QUOTED_STRING, "\"$x$'$$some_1variable1'123'\"");
        shell.substituteVariables(testToken1);
        String answer1 = "123$'$aba c aba '123'";

        assertEquals(answer1, testToken1.getContent());

        Token testToken2 = new Token(Token.TokenType.SINGLE_QUOTED_STRING, "\'$x$'$$some_1variable1\"123\"\'");
        shell.substituteVariables(testToken1);
        String answer2 = "$x$'$$some_1variable1\"123\"";

        assertEquals(answer2, testToken2.getContent());

        Token testToken3 = new Token(Token.TokenType.DOUBLE_QUOTED_STRING, "\"\\\"\"");
        shell.substituteVariables(testToken3);
        String answer3 = "\\\"";

        assertEquals(answer3, testToken3.getContent());
    }

    @Test
    public void testParseLine() {
        ShellImpl shell = new ShellImpl(CommandFactory.INSTANCE);

        // it's not a correct bash line, but it should be parsed by our parser
        String line = "some_var=\"123\"|x=\"$some_var\"|y='$some_var'|z=$y";
        String rightAnswer = " |  |  |  ";

        assertEquals(rightAnswer, shell.parseLine(line));
    }

}