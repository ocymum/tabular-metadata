
package com.asoroka.sidora.tabularmetadata.datatype;

import java.lang.reflect.Type;

public interface HierarchicalType<SelfType extends HierarchicalType<SelfType, SuperType>, SuperType extends HierarchicalType<SuperType, ?>>
        extends Type {

    /**
     * @return The immediate supertype of this type.
     */
    public SuperType supertype();

    /**
     * @return this type
     */
    public SelfType self();
}
