
package com.asoroka.sidora.tabularmetadata.heuristics;

import java.util.SortedSet;

import com.asoroka.sidora.tabularmetadata.datatype.DataType;

/**
 * Determines into which {@link DataType}s a series of values most likely falls.
 * 
 * @author ajs6f
 */
public interface TypeDeterminingHeuristic extends Heuristic {

    /**
     * @return Types for the proffered values in order of their likelihood according to this heuristic.
     */
    public SortedSet<DataType> typesAsLikely();

    /**
     * @return The single most likely type for the proffered values according to this heuristic. Under any normal
     *         regime, this should be equal to {@code typesAsLikely().first()}.
     */
    public DataType mostLikelyType();

}
