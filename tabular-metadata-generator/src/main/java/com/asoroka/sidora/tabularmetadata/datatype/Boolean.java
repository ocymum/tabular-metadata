
package com.asoroka.sidora.tabularmetadata.datatype;

import static java.util.regex.Pattern.CASE_INSENSITIVE;
import static java.util.regex.Pattern.compile;
import static javax.xml.XMLConstants.W3C_XML_SCHEMA_NS_URI;

import java.util.regex.Pattern;

public class Boolean extends XSDAssociatedType<Boolean, Lex> {

    private Boolean(final ValueTypeSystem system) {
        super(new Lex(system), W3C_XML_SCHEMA_NS_URI + "#boolean", system);
    }

    @Override
    public Boolean self() {
        return this;
    }

    @SuppressWarnings("unchecked")
    @Override
    public java.lang.Boolean parse(final java.lang.String s) throws ParsingException {
        if (BOOLEAN_TRUE.matcher(s).matches()) {
            return true;
        }
        if (BOOLEAN_FALSE.matcher(s).matches()) {
            return false;
        }
        throw new ParsingException("Could not parse as Boolean!");
    }

    /**
     * How to recognize a Boolean lex for true.
     */
    public static final Pattern BOOLEAN_TRUE = compile("^true|t$", CASE_INSENSITIVE);

    /**
     * How to recognize a Boolean lex for false.
     */
    public static final Pattern BOOLEAN_FALSE = compile("^false|f$", CASE_INSENSITIVE);

}
