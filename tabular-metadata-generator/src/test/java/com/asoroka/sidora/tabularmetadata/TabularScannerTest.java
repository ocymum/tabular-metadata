
package com.asoroka.sidora.tabularmetadata;

import static com.google.common.base.Charsets.UTF_8;
import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Lists.transform;
import static com.google.common.collect.Sets.newTreeSet;
import static java.util.Arrays.asList;
import static org.apache.commons.csv.CSVFormat.DEFAULT;
import static org.apache.commons.csv.CSVParser.parse;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;
import static org.slf4j.LoggerFactory.getLogger;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.csv.CSVParser;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.slf4j.Logger;

import com.asoroka.sidora.tabularmetadata.datatype.DataType;
import com.asoroka.sidora.tabularmetadata.heuristics.enumerations.EnumeratedValuesHeuristic;
import com.asoroka.sidora.tabularmetadata.heuristics.ranges.RangeDeterminingHeuristic;
import com.asoroka.sidora.tabularmetadata.heuristics.types.TypeDeterminingHeuristic;
import com.asoroka.sidora.tabularmetadata.testframework.TestUtilities;

@RunWith(MockitoJUnitRunner.class)
public class TabularScannerTest extends TestUtilities {

    @Mock
    private DataType mockDataType;

    @SuppressWarnings("rawtypes")
    @Mock
    private RangeDeterminingHeuristic mockRangeStrategy;

    @SuppressWarnings("rawtypes")
    @Mock
    private TypeDeterminingHeuristic mockTypeStrategy;

    @SuppressWarnings("rawtypes")
    @Mock
    private EnumeratedValuesHeuristic mockEnumStrategy;

    private static final File smalltestfile = new File("src/test/resources/test-data/small-test.csv");

    private static ArrayList<DataType> expectedResults;

    private static final Logger log = getLogger(TabularScannerTest.class);

    @Before
    public void setUp() {
        expectedResults = newArrayList(mockDataType, mockDataType, mockDataType, mockDataType);
        when(mockTypeStrategy.results()).thenReturn(newTreeSet(asList(mockDataType)));
        when(mockTypeStrategy.newInstance()).thenReturn(mockTypeStrategy);
        when(mockEnumStrategy.newInstance()).thenReturn(mockEnumStrategy);
        when(mockRangeStrategy.newInstance()).thenReturn(mockRangeStrategy);
    }

    @Test
    public void testOperation() throws IOException {
        final TabularScanner testScanner;
        try (final CSVParser parser = parse(smalltestfile, UTF_8, DEFAULT.withHeader())) {
            log.debug("Found header map: {}", parser.getHeaderMap());
            testScanner =
                    new TabularScanner(parser.iterator(), mockTypeStrategy, mockRangeStrategy, mockEnumStrategy);
            testScanner.scan(0);
        }
        final List<DataType> guesses =
                transform(testScanner.getTypeStrategies(), getMostLikelyType);
        assertEquals("Failed to find the correct column types!", expectedResults, guesses);
    }

    @Test
    public void testOperationWithLimitedScan() throws IOException {
        final TabularScanner testScanner;
        try (final CSVParser parser = parse(smalltestfile, UTF_8, DEFAULT.withHeader())) {
            log.debug("Found header map: {}", parser.getHeaderMap());
            testScanner =
                    new TabularScanner(parser.iterator(), mockTypeStrategy, mockRangeStrategy, mockEnumStrategy);
            testScanner.scan(2);
        }
        final List<DataType> guesses =
                transform(testScanner.getTypeStrategies(), getMostLikelyType);
        assertEquals("Failed to find the correct column types!", expectedResults, guesses);
    }
}
