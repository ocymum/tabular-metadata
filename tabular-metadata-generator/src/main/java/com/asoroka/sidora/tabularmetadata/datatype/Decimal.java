
package com.asoroka.sidora.tabularmetadata.datatype;

import static java.lang.Float.parseFloat;
import static javax.xml.XMLConstants.W3C_XML_SCHEMA_NS_URI;

public class Decimal extends XSDAssociatedType<Decimal, Lex> {

    public Decimal(final ValueTypeSystem system) {
        super(new Lex(system), W3C_XML_SCHEMA_NS_URI + "#decimal", system);
    }

    @SuppressWarnings("unchecked")
    @Override
    public Float parse(final java.lang.String s) throws ParsingException {
        try {
            return parseFloat(s.trim());
        } catch (final NumberFormatException e) {
            throw new ParsingException("Could not parse as Decimal!", e);
        }
    }

    @Override
    public Decimal self() {
        return this;
    }

}
