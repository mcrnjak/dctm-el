package mc.dctm.el;

import com.documentum.com.DfClientX;
import com.documentum.fc.client.IDfClient;
import com.documentum.fc.client.IDfSession;
import com.documentum.fc.client.IDfSessionManager;
import com.documentum.fc.client.IDfSysObject;
import com.documentum.fc.client.aspect.IDfAspects;
import com.documentum.fc.common.DfException;
import com.documentum.fc.common.DfLoginInfo;
import com.documentum.fc.common.DfTime;
import mc.dctm.el.eval.DctmExpressionEvaluator;
import mc.dctm.el.identifier.context.SysObjectContextObject;
import mc.dctm.el.identifier.impl.ParentIdentifier;
import mc.sel.identifier.ObjectsRegistry;
import mc.sel.identifier.context.ContextObject;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.util.Date;
import java.util.Properties;

import static org.junit.Assert.*;

public class DctmElIT {

    private static IDfSessionManager sessionManager;
    private static IDfSession session;
    private static IDfSysObject sysObject;

    @BeforeClass
    public static void setUp() throws DfException, IOException {
        Properties properties = new Properties();
        properties.load(Thread.currentThread().getContextClassLoader().getResourceAsStream("dctm.properties"));

        String repository = properties.getProperty("dctm.repository");
        String username = properties.getProperty("dctm.username");
        String password = properties.getProperty("dctm.password");

        sessionManager = createSessionManager(repository, username, password);
        session = sessionManager.getSession(repository);

        ObjectsRegistry.registerObjectIdentifier("parent", new ParentIdentifier());

        sysObject = (IDfSysObject) session.newObject("dm_document");
        sysObject.link("/Temp");

        ((IDfAspects) sysObject).attachAspect("dm_smart_object", null);

        sysObject.save();
    }

    @AfterClass
    public static void tearDown() throws DfException {
        sysObject.destroy();

        if (sessionManager != null && session != null) {
            sessionManager.release(session);
        }
    }

    @Test
    public void testSetString() throws DfException {
        String expr = "this.title = 'dctm-el title'";
        executeExpression(expr);
        assertEquals("dctm-el title", sysObject.getTitle());
    }

    @Test
    public void testNullifyString() throws DfException {
        sysObject.setTitle("title");
        sysObject.save();
        assertEquals("title", sysObject.getTitle());

        String expr = "this.title = null";
        executeExpression(expr);
        assertEquals("", sysObject.getTitle());
    }

    @Test
    public void testSetBoolean() throws DfException {
        sysObject.setBoolean("a_is_template", false);
        sysObject.save();
        assertEquals(false, sysObject.getBoolean("a_is_template"));

        String expr = "this.a_is_template = true";
        executeExpression(expr);

        assertEquals(true, sysObject.getBoolean("a_is_template"));

        expr = "this.a_is_template = false";
        executeExpression(expr);

        assertEquals(false, sysObject.getBoolean("a_is_template"));
    }

    @Test
    public void testSetDate() throws DfException {
        sysObject.setTime("a_last_review_date", DfTime.DF_NULLDATE);
        assertEquals(null, sysObject.getTime("a_last_review_date").getDate());

        Date date = (Date) executeExpression("date('02/09/2017', 'dd/MM/yyyy')");

        String expr = "this.a_last_review_date = date('02/09/2017', 'dd/MM/yyyy')";
        executeExpression(expr);

        assertEquals(date, sysObject.getTime("a_last_review_date").getDate());
    }

    @Test
    public void testNullifyDate() throws DfException {
        Date date = new Date();
        sysObject.setTime("a_last_review_date", new DfTime(date));
        sysObject.save();
        assertNotNull(sysObject.getTime("a_last_review_date").getDate());

        String expr = "this.a_last_review_date = null";
        executeExpression(expr);
        assertNull(sysObject.getTime("a_last_review_date").getDate());
    }

    @Test
    public void testAppendDate() throws DfException {
        int cnt = sysObject.getValueCount("a_effective_date");
        String expr = "this.a_effective_date [ this.getValueCount('a_effective_date') ] = date()";
        executeExpression(expr);
        assertEquals(cnt + 1, sysObject.getValueCount("a_effective_date"));
    }

    @Test
    public void testTruncate() throws DfException {
        sysObject.truncate("a_extended_properties", 0);
        sysObject.appendString("a_extended_properties", "123");
        sysObject.appendString("a_extended_properties", "456");
        sysObject.save();

        assertEquals(2, sysObject.getValueCount("a_extended_properties"));

        String expr = "this.truncate('a_extended_properties', 0)";
        executeExpression(expr);

        assertEquals(0, sysObject.getValueCount("a_extended_properties"));
    }

    @Test
    public void testAspectAttr() throws DfException {
        sysObject.appendString("dm_smart_object.member_logical_name", "Test 1");
        sysObject.appendString("dm_smart_object.member_logical_name", "Test 2");

        String expr = "this.dm_smart_object->member_logical_name[1]";
        Object result = executeExpression(expr);
        assertEquals("Test 2", result);

        expr = "this.dm_smart_object->member_logical_name[2] = 'Test 3'";
        executeExpression(expr);
        assertEquals("Test 3", sysObject.getRepeatingString("dm_smart_object.member_logical_name", 2));
    }

    private Object executeExpression(String expr) throws DfException {
        ContextObject ctxObj = new SysObjectContextObject(sysObject);
        Object result = DctmExpressionEvaluator.evaluate(expr, ctxObj, session);

        if (sysObject.isDirty()) {
            sysObject.save();
        }

        return result;
    }

    private static IDfSessionManager createSessionManager(String docbase, String username, String password)
            throws DfException {

        IDfClient client = new DfClientX().getLocalClient();
        IDfSessionManager sessMgr = client.newSessionManager();
        sessMgr.setIdentity(docbase, new DfLoginInfo(username, password));
        sessMgr.authenticate(docbase);
        return sessMgr;
    }
}
