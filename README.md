# dctm-el
Documentum Expression Language is an expression language library for Documentum DFC based 
on [SEL](https://github.com/mcrnjak/sel).

* [Usage](#usage)
* [Functions](#functions)
* [Identifiers](#identifiers)
* [Aspect Attributes](#aspect-attributes)

## Usage

```java
public void dctmElDemo(String objId, IDfSession session) throws DfException {
    
    // expression
    String input = "this.title = 'Title set with dctm-el'";

    try {
        // context object
        IDfSysObject sysObj = (IDfSysObject) session.getObject(new DfId(objId));
        ContextObject ctxObj = new TypedObjectContextObject(sysObj);

        // evaluate expression
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

## Functions
Custom functions can be implemented by extending abstract `mc.dctm.el.function.DctmFunction` class.

New functions must be added to the global functions registry.

```java
FunctionsRegistry.registerFunction("funcname", new FuncClass());
```

## Identifiers
Custom identifiers can be implemented by extending abstract `mc.dctm.el.identifier.DctmObjectIdentifier` class.

New identifiers must be added to the global identifiers registry.

```java
ObjectIdentifiersRegistry.registerObjectIdentifier("this", new ThisIdentifier());
```

The library provides `parent` identifier which returns the parent folder of the context object. It can be used
for accessing parent folder's attributes.

```
parent.r_folder_path
parent.parent.object_name
```

## Aspect Attributes
Aspect attributes can be accessed in the same way as normal attributes are accessed but the aspect name and
aspect attribute are separated with `->` symbol instead of `.` because `.` is used by 
[SEL](https://github.com/mcrnjak/sel) for accessing object properties and calling methods.

```
this.aspect_name->aspect_attribute
```

The `->` symbol is defined in `dctm-el.properties` file hence can be overridden if necessary.

```
aspect.attribute.separator = ->
```