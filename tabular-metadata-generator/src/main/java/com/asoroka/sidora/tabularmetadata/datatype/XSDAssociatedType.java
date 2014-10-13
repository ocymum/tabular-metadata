
package com.asoroka.sidora.tabularmetadata.datatype;

import java.net.URI;

/**
 * Maps from our types to types in the XSD type system.
 * 
 * @author ajs6f
 * @param <T>
 * @param <JavaValueSpace>
 */
public abstract class XSDAssociatedType<SelfType extends XSDAssociatedType<SelfType, SuperType>, SuperType extends XSDAssociatedType<SuperType, ?>>
        extends
        AbstractValueType<SelfType, SuperType> {

    /**
     * The XSD type, if any, with which this ValueType is associated
     */
    public final URI xsdType;

    protected XSDAssociatedType(final SuperType supertype, final String uri, final ValueTypeSystem system) {
        super(supertype, system);
        this.xsdType = uri == null ? null : URI.create(uri);
    }

}
