package mc.dctm.el.identifier.context;

import com.documentum.fc.client.IDfSession;
import mc.sel.identifier.context.ContextObject;
import mc.sel.identifier.context.DefaultContextImpl;

/**
 * Documentum specific expression context.
 *
 * @author Milan Crnjak
 */
public class DctmContext extends DefaultContextImpl {

    public DctmContext(ContextObject rootContextObject) {
        super(rootContextObject);
    }

    /**
     * Gets the repository session.
     *
     * @return repository session
     */
    public IDfSession getSession() {
        return (IDfSession) getProperty("session");
    }

    /**
     * Sets the repository session.
     *
     * @param session repository session
     */
    public void setSession(IDfSession session) {
        setProperty("session", session);
    }
}
