
package com.asoroka.sidora.tabularmetadata.datatype;

import java.util.Set;

public interface ValueTypeSystem {

    ValueType topType();

    void register(final ValueType type);

    Set<ValueType> parseableAs(final String s);

}
