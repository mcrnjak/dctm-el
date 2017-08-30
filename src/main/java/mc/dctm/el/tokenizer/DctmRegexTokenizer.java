package mc.dctm.el.tokenizer;

import mc.dctm.el.util.DctmElProperties;
import mc.sel.token.TokenType;
import mc.sel.tokenizer.RegexTokenizer;

/**
 * Extends default tokenizer to add support for Documentum aspect attributes.
 *
 * @author Milan Crnjak
 */
public class DctmRegexTokenizer extends RegexTokenizer {

    @Override
    protected void initialize() {
        super.initialize();

        /*
         * support for aspect attributes - need a special separator since '.'
         * is used for properties/methods access
         */
        String regex = "[a-zA-Z][a-zA-Z0-9_]*(%s[a-zA-Z][a-zA-Z0-9_]*)?";
        String aspectAttrSep = DctmElProperties.getInstance().getProperty("aspect.attribute.separator");

        addTokenDescription(TokenType.ID, String.format(regex, aspectAttrSep));
    }
}
