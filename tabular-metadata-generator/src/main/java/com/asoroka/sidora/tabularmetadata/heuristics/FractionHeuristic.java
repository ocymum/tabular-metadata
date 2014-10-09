
package com.asoroka.sidora.tabularmetadata.heuristics;

import com.asoroka.sidora.tabularmetadata.datatype.DataType;

/**
 * A heuristic that permits a certain specified fraction of values to fail at parsing to the most likely type.
 * 
 * @author ajs6f
 */
public class FractionHeuristic extends CountAggregatingHeuristic<FractionHeuristic> {

    /**
     * @param d The fraction of nonparsing values to permit
     */
    public FractionHeuristic(final double d) {
        super();
        this.fractionOfAllowedNonparseables = d;
    }

    private final double fractionOfAllowedNonparseables;

    @Override
    protected boolean candidacy(final DataType type) {
        final float parseableOccurence = (float) typeCounts.get(type) / (float) totalNumValues();
        return parseableOccurence >= (1 - fractionOfAllowedNonparseables);
    }

    @Override
    public FractionHeuristic clone() {
        return new FractionHeuristic(fractionOfAllowedNonparseables);
    }

}