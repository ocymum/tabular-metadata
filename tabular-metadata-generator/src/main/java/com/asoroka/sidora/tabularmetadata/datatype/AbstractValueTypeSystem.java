
package com.asoroka.sidora.tabularmetadata.datatype;

import static com.google.common.collect.Collections2.filter;
import static com.google.common.collect.ImmutableSet.copyOf;
import static com.google.common.collect.MutableClassToInstanceMap.create;

import java.util.Set;

import com.google.common.base.Predicate;
import com.google.common.collect.ClassToInstanceMap;

public abstract class AbstractValueTypeSystem implements ValueTypeSystem {

    private static ClassToInstanceMap<ValueType> parsingTypeRegistry = create();

    @Override
    public void register(final ValueType type) {
        parsingTypeRegistry.put(type.getClass(), type);
    }

    @Override
    public Set<ValueType> parseableAs(final String s) {
        final Predicate<ValueType> parseablePredicate = new Predicate<ValueType>() {

            @Override
            public boolean apply(final ValueType t) {
                try {
                    t.parse(s);
                    return true;
                } catch (final ParsingException e) {
                    return false;
                }
            }
        };
        return copyOf(filter(parsingTypeRegistry.values(), parseablePredicate));
    }

}
