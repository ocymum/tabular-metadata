
package com.asoroka.sidora.tabularmetadata.datatype;

/**
 * This is the central contract for our "native" type system.
 * 
 * @author ajs6f
 */
public interface ValueType<SelfType extends ValueType<SelfType, SuperType>, SuperType extends ValueType<SuperType, ?>>
        extends
        HierarchicalType<SelfType, SuperType>, TypeWithMapToValueSpace {

}
