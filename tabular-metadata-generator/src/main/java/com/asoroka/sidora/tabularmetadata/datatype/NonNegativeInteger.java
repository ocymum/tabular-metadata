
package com.asoroka.sidora.tabularmetadata.datatype;

import static javax.xml.XMLConstants.W3C_XML_SCHEMA_NS_URI;

public class NonNegativeInteger extends XSDAssociatedType<NonNegativeInteger, Integer> {

    public NonNegativeInteger(final ValueTypeSystem system) {
        super(new Integer(system), W3C_XML_SCHEMA_NS_URI + "#nonNegativeInteger", system);
    }

    @Override
    public NonNegativeInteger self() {
        return this;
    }

    @SuppressWarnings("unchecked")
    @Override
    public java.lang.Integer parse(final String s) throws ParsingException {

        final java.lang.Integer value = supertype().parse(s);
        if (value > -1) {
            return value;
        }
        throw new ParsingException("Value was negative!");
    }
}
