package mc.dctm.el.identifier.impl;

import com.documentum.fc.client.IDfSession;
import com.documentum.fc.client.IDfSysObject;
import com.documentum.fc.common.DfException;
import com.documentum.fc.common.DfId;
import mc.dctm.el.identifier.DctmObjectIdentifier;
import mc.dctm.el.identifier.context.DctmContext;
import mc.dctm.el.identifier.context.TypedObjectContextObject;
import mc.sel.identifier.context.ContextObject;

/**
 * Object identifier which returns context object's parent folder.
 *
 * @author Milan Crnjak
 */
public class ParentIdentifier extends DctmObjectIdentifier {

    @Override
    protected ContextObject doExecute(DctmContext context) {
        IDfSession session = context.getSession();

        ContextObject ctxObj = context.getContextObject();

        String parentId = (String) ctxObj.getPropertyAtIndex("i_folder_id", 0);

        try {
            IDfSysObject parentSysObj = (IDfSysObject) session.getObject(new DfId(parentId));
            return new TypedObjectContextObject(parentSysObj);
        } catch (DfException e) {
            throw new RuntimeException(e);
        }
    }
}
