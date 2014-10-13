
package com.asoroka.sidora.tabularmetadata.datatype;

import static com.google.common.collect.FluentIterable.from;
import static java.util.Collections.singleton;
import static javax.xml.XMLConstants.W3C_XML_SCHEMA_NS_URI;

import java.util.Set;
import java.util.SortedSet;

/**
 * An unparsed lex.
 * 
 * @author ajs6f
 */
@SuppressWarnings("rawtypes")
public class Lex extends XSDAssociatedType<Lex, Lex> {

    /**
     * Note that Lex cannot have a supertype. See {@link #supertype()}.
     * 
     * @param system
     */
    public Lex(final ValueTypeSystem system) {
        super(null, W3C_XML_SCHEMA_NS_URI + "#string", system);
    }

    @SuppressWarnings("unchecked")
    @Override
    public String parse(final String s) {
        return s;
    }

    @Override
    public Lex self() {
        return this;
    }

    @Override
    public SortedSet<AbstractValueType> supertypes() {
        final Set<AbstractValueType> selfSet = singleton((AbstractValueType) self());
        return from(selfSet).toSortedSet(orderingByHierarchy);
    }

    /*
     * Lex is inevitably the top type. Any lex can be parsed as Lex by Herbrand interpretation. (non-Javadoc)
     * @see com.asoroka.sidora.tabularmetadata.datatype.AbstractValueType#supertype()
     */
    @Override
    public Lex supertype() {
        return null;
    }
}
