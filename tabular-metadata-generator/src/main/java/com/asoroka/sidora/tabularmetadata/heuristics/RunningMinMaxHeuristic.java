/**
 * TEMPORARY LICENSE HEADER STANDIN
 * REPLACE WITH APPROPRIATE SIDORA LICENSE
 */

package com.asoroka.sidora.tabularmetadata.heuristics;

import static com.asoroka.sidora.tabularmetadata.datatype.ValueType.parseableAs;
import static com.google.common.collect.Maps.asMap;
import static com.google.common.collect.Ordering.natural;
import static java.util.Objects.hash;

import java.util.EnumMap;
import java.util.Map;

import com.asoroka.sidora.tabularmetadata.datatype.ValueType;
import com.asoroka.sidora.tabularmetadata.datatype.ParsingException;
import com.google.common.base.Function;
import com.google.common.collect.Range;

/**
 * Calculates the ranges of values supplied for each possible parseable type, without caching the values supplied.
 * 
 * @author ajs6f
 * @param <T>
 */
public abstract class RunningMinMaxHeuristic<T extends RunningMinMaxHeuristic<T>> extends PerTypeHeuristic<T> {

    /**
     * A {@link Map} from data types to the minimum value from all presented values that were parseable in that type.
     */
    protected Map<ValueType, Comparable<?>> minimums = new EnumMap<>(ValueType.class);

    /**
     * A {@link Map} from data types to the maximum value from all presented values that were parseable in that type.
     */
    protected Map<ValueType, Comparable<?>> maximums = new EnumMap<>(ValueType.class);

    @Override
    public void addValue(final String value) {
        super.addValue(value);
        for (final ValueType type : parseableAs(value)) {
            final Comparable<?> currentMin = minimums.get(type);
            final Comparable<?> currentMax = maximums.get(type);
            try {
                final Comparable<?> v = type.parse(value);
                // TODO avoid this repeated conditional
                minimums.put(type, (currentMin == null) ? v : natural().min(currentMin, v));
                maximums.put(type, (currentMax == null) ? v : natural().max(currentMax, v));
            } catch (final ParsingException e) {
                // we are only parsing for types that have already been checked
                throw new AssertionError("Could not parse to a type that was passed as parsing!", e);
            }
        }
    }

    @Override
    public Map<ValueType, Range<?>> getRanges() {
        return asMap(typesAsLikely(), getRangeForType());
    }

    private Function<ValueType, Range<?>> getRangeForType() {
        return new Function<ValueType, Range<?>>() {

            @Override
            public Range<?> apply(final ValueType type) {
                // the following could be shortened to three comparisons, but at a cost in clarity
                if (minimums.containsKey(type) && maximums.containsKey(type)) {
                    return Range.closed(minimums.get(type), maximums.get(type));
                }
                if (minimums.containsKey(type)) {
                    return Range.atLeast(minimums.get(type));
                }
                if (maximums.containsKey(type)) {
                    return Range.atMost(maximums.get(type));
                }
                return Range.all();
            }
        };
    }

    @Override
    public <MinMax extends Comparable<MinMax>> Range<MinMax> getRange() {
        final ValueType mostLikelyType = mostLikelyType();
        return (Range<MinMax>) getRanges().get(mostLikelyType);
    }

    @Override
    public int hashCode() {
        return super.hashCode() + 2 * hash(minimums, maximums);
    }
}
