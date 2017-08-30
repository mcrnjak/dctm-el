package mc.dctm.el.identifier;

import mc.dctm.el.identifier.context.DctmContext;
import mc.sel.identifier.ObjectIdentifier;
import mc.sel.identifier.context.Context;
import mc.sel.identifier.context.ContextObject;

/**
 * Base Documentum object identifier. Custom Documentum identifiers should extend this class.
 *
 * @author Milan Crnjak
 */
public abstract class DctmObjectIdentifier implements ObjectIdentifier {

    @Override
    public ContextObject execute(Context context) {
        ContextObject obj = doExecute((DctmContext) context);
        context.setContextObject(obj);
        return obj;
    }

    /**
     * Executes identifier.
     *
     * @param context context
     * @return identifier result
     */
    protected abstract ContextObject doExecute(DctmContext context);
}
