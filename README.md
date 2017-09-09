# dctm-el
Documentum Expression Language is based on [SEL](https://github.com/mcrnjak/sel).

## Usage

```java
public void dctmElDemo(String objId, IDfSession session) throws DfException {
    
    String input = "this.title = 'Title set with dctm-el'";

    try {
        IDfSysObject sysObj = (IDfSysObject) session.getObject(new DfId(objId));
        ContextObject ctxObj = new TypedObjectContextObject(sysObj);

        Object result = DctmExpressionEvaluator.evaluate(input, ctxObj, session);

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
```
