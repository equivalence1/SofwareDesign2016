import commands.CommandFactory;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ShellImplTest {

    @Test
    public void testSubstituteVariables() {
        ShellImpl shell = new ShellImpl(CommandFactory.INSTANCE);
        shell.environment.put("x", "123");
        shell.environment.put("some_1variable1", "aba c aba ");

        final Token testToken1 = new Token(Token.TokenType.DOUBLE_QUOTED_STRING, "\"$x$'$$some_1variable1'123'\"");
        final String answer1 = "123$'$aba c aba '123'";
        shell.substituteVariables(testToken1);

        assertEquals(answer1, testToken1.getContent());

        final Token testToken2 = new Token(Token.TokenType.SINGLE_QUOTED_STRING, "\'$x$'$$some_1variable1\"123\"\'");
        final String answer2 = "$x$'$$some_1variable1\"123\"";
        shell.substituteVariables(testToken1);

        assertEquals(answer2, testToken2.getContent());

        final Token testToken3 = new Token(Token.TokenType.DOUBLE_QUOTED_STRING, "\"\\\"\"");
        final String answer3 = "\\\"";
        shell.substituteVariables(testToken3);

        assertEquals(answer3, testToken3.getContent());
    }

    @Test
    public void testParseLine() {
        ShellImpl shell = new ShellImpl(CommandFactory.INSTANCE);

        // it's not a correct bash line, but it should be parsed by our parser
        final String sample1 = "some_var=\"123\"|x=\"$some_var\"|y='$some_var'|z=$y";
        final String answer1 = " |  |  |  ";

        assertEquals(answer1, shell.parseLine(sample1));

        final String sample2 = "x=\"aba c aba\"|echo $x|echo \"\\\"$x\"";
        final String answer2 = " | echo aba c aba | echo \"\\\"aba c aba\" ";

        assertEquals(answer2, shell.parseLine(sample2));
    }

}