
package com.asoroka.sidora.tabularmetadata.heuristics;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Map;
import java.util.SortedSet;

import org.junit.Test;

import com.asoroka.sidora.tabularmetadata.datatype.DataType;
import com.google.common.collect.Range;

public abstract class DataTypeHeuristicTestFrame<T extends ValueHeuristic<T>> {

    protected abstract T newTestHeuristic();

    @Test
    public void testClone() {
        final T testHeuristic = newTestHeuristic();
        assertTrue(testHeuristic.clone().equals(testHeuristic));
    }

    @Test
    public void testGet() {
        final T testHeuristic = newTestHeuristic();
        assertTrue(testHeuristic.get() == (testHeuristic));
    }

    @Test
    public void testEquals() {
        final T testHeuristic = newTestHeuristic();
        assertTrue(testHeuristic.equals(newTestHeuristic()));
        assertFalse(testHeuristic.equals(new Object()));

        final T testHeuristic2 = newTestHeuristic();
        testHeuristic2.addValue("anything");
        assertFalse(testHeuristic.equals(testHeuristic2));

        final TestHeuristic otherTypeOfHeuristic = new TestHeuristic();
        assertFalse(testHeuristic.equals(otherTypeOfHeuristic));
    }

    protected static class TestHeuristic implements ValueHeuristic<TestHeuristic> {

        @Override
        public TestHeuristic clone() {
            return new TestHeuristic();
        }

        @Override
        public TestHeuristic get() {
            return this;
        }

        @Override
        public SortedSet<DataType> typesAsLikely() {
            // NO OP
            return null;
        }

        @Override
        public void addValue(final String value) {
            // NO OP
        }

        @Override
        public <MinMax extends Comparable<MinMax>> Range<MinMax> getRange() {
            // NO OP
            return null;
        }

        @Override
        public DataType mostLikelyType() {
            // NO OP
            return null;
        }

        @Override
        public Map<DataType, Range<?>> getRanges() {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public void reset() {
            // NO OP
        }
    }
}