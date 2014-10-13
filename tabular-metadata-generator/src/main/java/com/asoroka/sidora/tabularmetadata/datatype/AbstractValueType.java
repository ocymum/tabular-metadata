
package com.asoroka.sidora.tabularmetadata.datatype;

import static com.google.common.base.Suppliers.memoize;
import static java.util.Objects.hash;

import java.util.Comparator;
import java.util.SortedSet;
import java.util.TreeSet;

import com.google.common.base.Supplier;

@SuppressWarnings("rawtypes")
public abstract class AbstractValueType<SelfType extends AbstractValueType<SelfType, SuperType>, SuperType extends AbstractValueType<SuperType, ?>>
        implements ValueType<SelfType, SuperType> {

    /**
     * The immediate supertype of this type.
     */
    final SuperType supertype;

    /**
     * @param supertype
     */
    public AbstractValueType(final SuperType supertype, final ValueTypeSystem system) {
        super();
        this.supertype = supertype;
        system.register(this);
    }

    @Override
    public SuperType supertype() {
        return supertype;
    }

    private final Supplier<SortedSet<AbstractValueType>> supertypesMemo =
            memoize(new Supplier<SortedSet<AbstractValueType>>() {

                @Override
                public SortedSet<AbstractValueType> get() {
                    final SortedSet<AbstractValueType> supertypes;

                    supertypes = supertype == null ? new TreeSet<>(orderingByHierarchy) : supertype.supertypes();
                    supertypes.add(self());
                    return supertypes;
                }
            });

    public SortedSet<AbstractValueType> supertypes() {
        return supertypesMemo.get();
    }

    /**
     * A simple ordering by hierarchy. Those types with more supertypes are considered "smaller" than those with
     * fewer.
     */
    public final Comparator<AbstractValueType> orderingByHierarchy =
            new Comparator<AbstractValueType>() {

                @Override
                public int compare(final AbstractValueType left, final AbstractValueType right) {
                    return right.supertypes().size() - left.supertypes().size();
                }

            };

    @Override
    public int hashCode() {
        return hash(self(), supertype());
    }

    @Override
    public boolean equals(final Object obj) {
        // types in our system are all singletons
        return this == obj;
    }
}
