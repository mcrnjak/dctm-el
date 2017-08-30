package mc.dctm.el.function;

import mc.dctm.el.identifier.context.DctmContext;
import mc.sel.function.Function;
import mc.sel.identifier.context.Context;

import java.util.List;

/**
 * Base Documentum function class. Custom Documentum functions should extend this class.
 *
 * @author Milan Crnjak
 */
public abstract class DctmFunction implements Function {

    @Override
    public Object execute(Context context, List<Object> args) {
        DctmContext ctx = (DctmContext) context;
        return doExecute(ctx, args);
    }

    /**
     * Executes function.
     *
     * @param context context
     * @param args function arguments
     * @return function result
     */
    protected abstract Object doExecute(DctmContext context, List<Object> args);
}
