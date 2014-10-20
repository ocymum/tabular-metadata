
package com.asoroka.sidora.tabularmetadata.test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.slf4j.LoggerFactory.getLogger;

import java.net.URL;
import java.util.Map;

import org.junit.Test;
import org.mockito.internal.stubbing.answers.Returns;
import org.slf4j.Logger;

import com.asoroka.sidora.tabularmetadata.heuristics.enumerations.EnumeratedValuesHeuristic;
import com.asoroka.sidora.tabularmetadata.heuristics.ranges.RangeDeterminingHeuristic;
import com.asoroka.sidora.tabularmetadata.heuristics.types.TypeDeterminingHeuristic;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.StaxDriver;

/**
 * Utilities for testing.
 * 
 * @author ajs6f
 */
public class TestUtilities {

    /**
     * For deserializing test data.
     */
    private static final XStream X_STREAM = new XStream(new StaxDriver());

    private static final Logger log = getLogger(TestUtilities.class);

    /**
     * The following peculiar locution arises from the need to provide "cloneability" while avoiding a recursive mock
     * 
     * @return a cloneable mock strategy
     */
    public static MockedHeuristic cloneableMockStrategy(final MockedHeuristic strategy) {
        final MockedHeuristic mocked = mock(MockedHeuristic.class);
        final Returns type = new Returns(strategy.typesAsLikely());
        when(mocked.typesAsLikely()).thenAnswer(type);
        final Returns range = new Returns(strategy.getRanges());
        when(mocked.getRanges()).thenAnswer(range);
        final Returns enums = new Returns(strategy.getEnumeratedValues());
        when(mocked.getEnumeratedValues()).thenAnswer(enums);
        final Returns cloner = new Returns(strategy);
        when(mocked.clone()).thenAnswer(cloner);
        return mocked;
    }

    /**
     * Exists purely to help simplify mocking for tests.
     * 
     * @author ajs6f
     */
    public static interface MockedHeuristic extends TypeDeterminingHeuristic<MockedHeuristic>,
            RangeDeterminingHeuristic<MockedHeuristic>,
            EnumeratedValuesHeuristic<MockedHeuristic> {
        // NO CONTENT
    }

    /**
     * Retrieves some test data from the classpath. The data file is assumed to be an XStream-serialized map, with
     * different entries for different pieces of test data needed for testing a single class. The name of the test
     * data file is assumed to be the name of the class under test, with "test-data.xml" appended.
     * 
     * @param testClass the class relative to which to retrieve the test data, normally the class under test
     * @param key the key from which to retrieve the test data
     * @return
     */
    public static <T> T retrieveTestData(final Class<?> testClass, final Object key) {
        final String testDataFileName = testClass.getSimpleName() + "-test-data.xml";
        log.trace("Attempting to load test data from: {}", testDataFileName);
        final URL testDataFile = testClass.getResource(testDataFileName);
        log.trace("Loading test data from: {}", testDataFile);
        final Map<?, ?> testData = (Map<?, ?>) X_STREAM.fromXML(testDataFile);
        return (T) testData.get(key);
    }

    @Test
    public void testTestDataRetrieval() {
        final String testData = retrieveTestData(this.getClass(), "test");
        assertEquals("successful", testData);
    }
}
