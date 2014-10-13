
package com.asoroka.sidora.tabularmetadata.datatype;

import static java.lang.Integer.parseInt;
import static javax.xml.XMLConstants.W3C_XML_SCHEMA_NS_URI;

public class Integer extends XSDAssociatedType<Integer, Decimal> {

    public Integer(final ValueTypeSystem system) {
        super(new Decimal(system), W3C_XML_SCHEMA_NS_URI + "#integer", system);
    }

    @Override
    public Integer self() {
        return this;
    }

    @SuppressWarnings("unchecked")
    @Override
    public java.lang.Integer parse(final String s) throws ParsingException {
        try {
            return parseInt(s.trim());
        } catch (final NumberFormatException e) {
            throw new ParsingException("Could not parse as Integer!", e);
        }
    }

}
