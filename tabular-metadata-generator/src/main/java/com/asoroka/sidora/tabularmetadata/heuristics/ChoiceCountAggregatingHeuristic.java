
package com.asoroka.sidora.tabularmetadata.heuristics;

import static com.google.common.base.Functions.constant;
import static com.google.common.collect.Collections2.transform;
import static com.google.common.collect.Maps.toMap;
import static com.google.common.collect.Sets.filter;
import static com.google.common.collect.Sets.powerSet;
import static java.util.Objects.hash;

import java.util.Collection;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.asoroka.sidora.tabularmetadata.datatype.ValueType;
import com.google.common.base.Function;
import com.google.common.base.Predicate;

/**
 * A {@link DataTypeHeuristic} that records the number of each possible choice of parseables made for values
 * submitted.
 * 
 * @author ajs6f
 */
public abstract class ChoiceCountAggregatingHeuristic<T extends ChoiceCountAggregatingHeuristic<T>> extends
        RunningMinMaxHeuristic<T> {

    /**
     * A {@link Map} from choices of {@link ValueType}s to the number of times that choice occurred.
     */
    private Map<EnumSet<ValueType>, Integer> choiceOccurrences;

    protected ChoiceCountAggregatingHeuristic() {
        // This sequence simply fills the choice map with all possible choices, each mapped to zero, to
        // record that we haven't yet seen any choices made. Java is verbose.
        final Set<Set<ValueType>> powerSet = powerSet(ValueType.valuesSet());
        choiceOccurrences = new HashMap<>(powerSet.size());
        final Collection<EnumSet<ValueType>> filteredPowerSet = transform(filter(powerSet, noEmptySet), set2enumset);
        choiceOccurrences.putAll(toMap(filteredPowerSet, constant(0)));
    }

    @Override
    public void addValue(final String value) {
        super.addValue(value);
        final EnumSet<ValueType> choices = ValueType.parseableAs(value);
        choiceOccurrences.put(choices, choiceOccurrences.get(choices) + 1);
    }

    @Override
    public int hashCode() {
        return super.hashCode() + 2 * hash(choiceOccurrences);
    }

    private static final Function<Set<ValueType>, EnumSet<ValueType>> set2enumset =
            new Function<Set<ValueType>, EnumSet<ValueType>>() {

                @Override
                public EnumSet<ValueType> apply(final Set<ValueType> s) {
                    return EnumSet.copyOf(s);
                }
            };

    private static final Predicate<Set<ValueType>> noEmptySet = new Predicate<Set<ValueType>>() {

        @Override
        public boolean apply(final Set<ValueType> s) {
            return !s.isEmpty();
        }
    };

}
