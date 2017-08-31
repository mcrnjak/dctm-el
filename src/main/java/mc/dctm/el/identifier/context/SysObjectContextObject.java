package mc.dctm.el.identifier.context;

import com.documentum.fc.client.IDfSysObject;
import com.documentum.fc.common.DfException;
import com.documentum.fc.common.DfValue;
import com.documentum.fc.common.IDfAttr;
import com.documentum.fc.common.IDfValue;
import mc.dctm.el.util.DctmElProperties;
import mc.sel.identifier.context.ContextObject;

/**
 * Context object wrapper for {@link IDfSysObject} objects.
 *
 * @author Milan Crnjak
 */
public class SysObjectContextObject implements ContextObject {

    private IDfSysObject sysObject;

    public SysObjectContextObject(IDfSysObject sysObject) {
        this.sysObject = sysObject;
    }

    @Override
    public Object getProperty(String s) {
        String attrName = replaceAspectAttrSep(s);

        try {
            IDfValue dfValue = sysObject.getValue(attrName);
            return extractValue(dfValue, sysObject.getAttrDataType(s));
        } catch (DfException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Object getPropertyAtIndex(String s, int i) {
        try {
            String attr = replaceAspectAttrSep(s);
            IDfValue dfValue = sysObject.getRepeatingValue(attr, i);
            return extractValue(dfValue, sysObject.getAttrDataType(s));
        } catch (DfException e) {
            throw new RuntimeException(e);
        }
    }

    private Object extractValue(IDfValue dfValue, int attrDataType) {
        switch (attrDataType) {
            case IDfAttr.DM_BOOLEAN:
                return dfValue.asBoolean();
            case IDfAttr.DM_INTEGER:
                return dfValue.asInteger();
            case IDfAttr.DM_DOUBLE:
                return dfValue.asDouble();
            case IDfAttr.DM_TIME:
                return dfValue.asTime().getDate();
            default:
                return dfValue.asString();
        }
    }

    @Override
    public void setProperty(String s, Object o) {
        try {
            String attr = replaceAspectAttrSep(s);
            int attrDataType = sysObject.getAttrDataType(s);
            sysObject.setValue(attr, new DfValue(o, attrDataType));
        } catch (DfException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void setPropertyAtIndex(String s, Object o, int i) {
        try {
            String attr = replaceAspectAttrSep(s);
            int attrDataType = sysObject.getAttrDataType(s);
            sysObject.setRepeatingValue(attr, i, new DfValue(o, attrDataType));
        } catch (DfException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Object getObject() {
        return sysObject;
    }

    private String replaceAspectAttrSep(String s) {
        String aspectAttrSep = DctmElProperties.getInstance().getProperty("aspect.attribute.separator");
        return s.replace(aspectAttrSep, ".");
    }
}
