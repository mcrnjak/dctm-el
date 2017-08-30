package mc.dctm.el.eval;

import com.documentum.fc.client.IDfSession;
import mc.dctm.el.identifier.context.DctmContext;
import mc.dctm.el.parsetree.visitor.DctmParseTreeInterpreter;
import mc.dctm.el.tokenizer.DctmRegexTokenizer;
import mc.sel.identifier.context.ContextObject;
import mc.sel.parser.DefaultParser;
import mc.sel.parser.Parser;
import mc.sel.parsetree.ParseTreeNode;
import mc.sel.parsetree.visitor.ParseTreeVisitor;
import mc.sel.token.Token;
import mc.sel.tokenizer.Tokenizer;

import java.util.List;

/**
 * Holds methods for evaluating Documentum expressions.
 *
 * @author Milan Crnjak
 */
public class DctmExpressionEvaluator {

    /**
     * Evaluates expression.
     *
     * @param expression expression
     * @param ctxObj context object
     * @param session repository session
     * @return evaluated expression result
     */
    public static Object evaluate(String expression, ContextObject ctxObj, IDfSession session) {
        DctmContext ctx = new DctmContext(ctxObj);
        ctx.setSession(session);

        Tokenizer tokenizer = new DctmRegexTokenizer();
        List<Token> tokens = tokenizer.tokenize(expression);

        Parser parser = new DefaultParser();
        ParseTreeNode parseTree = parser.parse(tokens);

        ParseTreeVisitor<Object> interpreter = new DctmParseTreeInterpreter(ctx);
        return interpreter.visit(parseTree);
    }
}
