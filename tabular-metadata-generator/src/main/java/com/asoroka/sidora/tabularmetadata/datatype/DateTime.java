
package com.asoroka.sidora.tabularmetadata.datatype;

import static javax.xml.XMLConstants.W3C_XML_SCHEMA_NS_URI;
import static org.joda.time.format.ISODateTimeFormat.dateTimeParser;

public class DateTime extends XSDAssociatedType<DateTime, Lex> {

    private DateTime(final ValueTypeSystem system) {
        super(new Lex(system), W3C_XML_SCHEMA_NS_URI + "#dateTime", system);
    }

    @Override
    public DateTime self() {
        return this;
    }

    @SuppressWarnings("unchecked")
    @Override
    public org.joda.time.DateTime parse(final java.lang.String s) throws ParsingException {
        try {
            return dateTimeParser().parseDateTime(s);
        } catch (final IllegalArgumentException e) {
            throw new ParsingException("Could not parse as DataTime!", e);
        }
    }
}
