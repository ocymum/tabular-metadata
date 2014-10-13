
package com.asoroka.sidora.tabularmetadata.heuristics;

import static com.asoroka.sidora.tabularmetadata.datatype.ValueType.notParseableAs;
import static com.google.common.base.Functions.constant;
import static com.google.common.collect.Maps.toMap;
import static java.lang.Float.NEGATIVE_INFINITY;
import static java.util.Objects.hash;

import java.util.EnumMap;
import java.util.Map;

import com.asoroka.sidora.tabularmetadata.datatype.ValueType;

public class MinimumDistanceBetweenNonparseablesHeuristic extends
        RunningMinMaxHeuristic<MinimumDistanceBetweenNonparseablesHeuristic> {

    private final int minimumDistance;

    private Map<ValueType, Boolean> candidateTypes = new EnumMap<>(ValueType.class);

    /**
     * We define locations as {@link Float}s in order to use the special value {@link Float#NEGATIVE_INFINITY}, which
     * does not exist for integer types in Java.
     */
    private Map<ValueType, Float> locationsOfLastNonparseables = new EnumMap<>(ValueType.class);

    public MinimumDistanceBetweenNonparseablesHeuristic(final int minimumDistance) {
        // assume that every type is a candidate
        final Map<ValueType, Boolean> allTrue = toMap(ValueType.valuesSet(), constant(true));
        candidateTypes.putAll(allTrue);

        // record that we haven't yet seen any nonparseables
        final Map<ValueType, Float> originalLocations = toMap(ValueType.valuesSet(), constant(NEGATIVE_INFINITY));
        locationsOfLastNonparseables.putAll(originalLocations);

        this.minimumDistance = minimumDistance;
    }

    @Override
    protected boolean candidacy(final ValueType type) {
        return candidateTypes.get(type);
    }

    @Override
    public void addValue(final String value) {
        super.addValue(value);
        for (final ValueType type : notParseableAs(value)) {
            final float distanceToLastNonParseableOfThisType =
                    totalNumValues() - locationsOfLastNonparseables.get(type);
            if (distanceToLastNonParseableOfThisType < minimumDistance) {
                // it's been too soon since the last nonparseable value of this type, knock it out of the running
                candidateTypes.put(type, false);
            } else {
                // mark that we saw a nonparseable value for this type
                locationsOfLastNonparseables.put(type, (float) totalNumValues());
            }
        }
    }

    @Override
    public MinimumDistanceBetweenNonparseablesHeuristic clone() {
        return new MinimumDistanceBetweenNonparseablesHeuristic(minimumDistance);
    }

    @Override
    public int hashCode() {
        return super.hashCode() + 2 * hash(candidateTypes, totalNumValues(), locationsOfLastNonparseables);
    }

}
