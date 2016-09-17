import commands.CommandFactory;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by equi on 17.09.16.
 *
 * @author Kravchenko Dima
 */
public class ShellImplTest {

    @Test
    public void testSubstituteVariables() {
        ShellImpl shell = new ShellImpl(CommandFactory.INSTANCE);
        shell.environment.put("x", "123");
        shell.environment.put("y", "aba c aba ");

        Token testToken1 = new Token(Token.TokenType.DOUBLE_QUOTED_STRING, "\"$x$'$$y'123'\"");
        shell.substituteVariables(testToken1);
        String rightAnswer1 = "123$'$aba c aba '123'";

        assertEquals(rightAnswer1, testToken1.getContent());

        Token testToken2 = new Token(Token.TokenType.SINGLE_QUOTED_STRING, "\'$x$'$$y\"123\"\'");
        shell.substituteVariables(testToken1);
        String rightAnswer2 = "$x$'$$y\"123\"";

        assertEquals(rightAnswer2, testToken2.getContent());
    }

}