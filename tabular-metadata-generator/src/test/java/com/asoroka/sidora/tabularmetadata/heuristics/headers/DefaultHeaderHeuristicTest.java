
package com.asoroka.sidora.tabularmetadata.heuristics.headers;

import static com.asoroka.sidora.tabularmetadata.test.TestUtilities.retrieveUnitTestData;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;

public class DefaultHeaderHeuristicTest extends HeaderHeuristicTestFrame<DefaultHeaderHeuristic> {

    private static final List<String> goodData = retrieveUnitTestData(DefaultHeaderHeuristicTest.class, "goodData");

    private static final List<String> badData = retrieveUnitTestData(DefaultHeaderHeuristicTest.class, "badData");

    @Override
    protected DefaultHeaderHeuristic newTestHeuristic() {
        return new DefaultHeaderHeuristic();
    }

    @Test
    public void testDefault() {
        final DefaultHeaderHeuristic testHeuristic = newTestHeuristic();
        for (final String field : goodData) {
            testHeuristic.addValue(field);
        }
        assertTrue(testHeuristic.isHeader());
        for (final String field : badData) {
            testHeuristic.addValue(field);
        }
        assertFalse(testHeuristic.isHeader());
    }

}
