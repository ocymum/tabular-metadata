
package com.asoroka.sidora.tabularmetadata.datatype;

import static javax.xml.XMLConstants.W3C_XML_SCHEMA_NS_URI;

public class PositiveInteger extends XSDAssociatedType<PositiveInteger, NonNegativeInteger> {

    private PositiveInteger(final ValueTypeSystem system) {
        super(new NonNegativeInteger(system), W3C_XML_SCHEMA_NS_URI + "#positiveInteger", system);
    }

    @Override
    public PositiveInteger self() {
        return this;
    }

    @SuppressWarnings("unchecked")
    @Override
    public java.lang.Integer parse(final String s) throws ParsingException {
        final java.lang.Integer value = supertype().parse(s);
        if (value > 0) {
            return value;
        }
        throw new ParsingException("Value was negative!");
    }
}
