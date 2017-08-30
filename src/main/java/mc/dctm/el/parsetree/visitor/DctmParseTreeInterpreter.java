package mc.dctm.el.parsetree.visitor;

import mc.dctm.el.identifier.context.DctmContext;
import mc.sel.parsetree.visitor.ParseTreeInterpreter;

/**
 * Specialized {@link ParseTreeInterpreter} which forces usage of Documentum specific context {@link DctmContext}.
 *
 * @author Milan Crnjak
 */
public class DctmParseTreeInterpreter extends ParseTreeInterpreter {

    public DctmParseTreeInterpreter(DctmContext context) {
        super(context);
    }
}
