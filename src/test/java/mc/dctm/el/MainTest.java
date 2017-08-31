package mc.dctm.el;

import com.documentum.com.DfClientX;
import com.documentum.fc.client.IDfClient;
import com.documentum.fc.client.IDfSession;
import com.documentum.fc.client.IDfSessionManager;
import com.documentum.fc.client.IDfSysObject;
import com.documentum.fc.common.DfException;
import com.documentum.fc.common.DfId;
import com.documentum.fc.common.DfLoginInfo;
import mc.dctm.el.eval.DctmExpressionEvaluator;
import mc.dctm.el.identifier.context.SysObjectContextObject;
import mc.dctm.el.identifier.impl.ParentIdentifier;
import mc.sel.exception.ParseTreeVisitorException;
import mc.sel.exception.ParserException;
import mc.sel.exception.TokenizerException;
import mc.sel.identifier.ObjectsRegistry;
import mc.sel.identifier.context.ContextObject;
import mc.sel.util.ExceptionUtils;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class MainTest {

    private static IDfSessionManager sessionManager;
    private static IDfSession session;

    @BeforeClass
    public static void setUp() throws DfException {
        String docbase = "rep01";
        String username = "dmadmin";
        String password = "Dmadm1n";;

        sessionManager = createSessionManager(docbase, username, password);
        session = sessionManager.getSession(docbase);

        ObjectsRegistry.registerObjectIdentifier("parent", new ParentIdentifier());
    }

    @AfterClass
    public static void tearDown() {
        if (sessionManager != null && session != null) {
            sessionManager.release(session);
        }
    }

    @Test
    public void testRun() throws DfException {
        String objId = "09000539800aacc9";

        String input = "this.r_creation_date";

        try {
            IDfSysObject sysObj = (IDfSysObject) session.getObject(new DfId(objId));
            ContextObject ctxObj = new SysObjectContextObject(sysObj);

            System.out.println(DctmExpressionEvaluator.evaluate(input, ctxObj, session));

            if (sysObj.isDirty()) {
                sysObj.save();
            }

        } catch (TokenizerException e) {
            System.err.println(ExceptionUtils.underlineError(input, e));
            e.printStackTrace();
        } catch (ParserException e) {
            System.err.println(ExceptionUtils.underlineError(input, e));
            e.printStackTrace();
        } catch (ParseTreeVisitorException e) {
            System.err.println(ExceptionUtils.underlineError(input, e));
            e.printStackTrace();
        }
    }

    private static IDfSessionManager createSessionManager(String docbase, String username, String password) throws DfException {
        IDfClient client = new DfClientX().getLocalClient();
        IDfSessionManager sessMgr = client.newSessionManager();
        sessMgr.setIdentity(docbase, new DfLoginInfo(username, password));
        sessMgr.authenticate(docbase);
        return sessMgr;
    }
}
