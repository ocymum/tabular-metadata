/**
 * TEMPORARY LICENSE HEADER STANDIN
 * REPLACE WITH APPROPRIATE SIDORA LICENSE
 */

package com.asoroka.sidora.tabularmetadata.heuristics;

import static com.asoroka.sidora.tabularmetadata.datatype.ValueType.parseableAs;
import static com.google.common.base.Functions.constant;
import static com.google.common.collect.Maps.toMap;
import static java.util.Objects.hash;
import static org.slf4j.LoggerFactory.getLogger;

import java.util.Collection;
import java.util.EnumMap;
import java.util.Map;

import org.slf4j.Logger;

import com.asoroka.sidora.tabularmetadata.datatype.ValueType;

/**
 * A {@link DataTypeHeuristic} that aggregates candidate type appearance information for its field.
 * 
 * @author ajs6f
 * @param <T>
 */
public abstract class CountAggregatingHeuristic<T extends CountAggregatingHeuristic<T>> extends
        RunningMinMaxHeuristic<CountAggregatingHeuristic<T>> {

    /**
     * In this {@link Map}, we aggregate counts of parseable values for each datatype.
     */
    protected final EnumMap<ValueType, Integer> typeCounts = new EnumMap<>(ValueType.class);

    private static final Logger log = getLogger(CountAggregatingHeuristic.class);

    /**
     * Initialize counts for each datatype.
     */
    public CountAggregatingHeuristic() {
        final Map<ValueType, Integer> zeroes = toMap(ValueType.valuesSet(), constant(0));
        typeCounts.putAll(zeroes);
    }

    @Override
    public void addValue(final String value) {
        super.addValue(value);
        incrementCounts(parseableAs(value));
    }

    private void incrementCounts(final Collection<ValueType> types) {
        for (final ValueType type : types) {
            typeCounts.put(type, typeCounts.get(type) + 1);
        }
    }

    @Override
    public int hashCode() {
        return super.hashCode() + 2 * hash(typeCounts);
    }
}
