/**
 * TEMPORARY LICENSE HEADER STANDIN
 * REPLACE WITH APPROPRIATE SIDORA LICENSE
 */

package com.asoroka.sidora.tabularmetadata.datatype;

import static com.asoroka.sidora.tabularmetadata.datatype.DataType.Boolean;
import static com.asoroka.sidora.tabularmetadata.datatype.DataType.DateTime;
import static com.asoroka.sidora.tabularmetadata.datatype.DataType.Decimal;
import static com.asoroka.sidora.tabularmetadata.datatype.DataType.Geographic;
import static com.asoroka.sidora.tabularmetadata.datatype.DataType.Integer;
import static com.asoroka.sidora.tabularmetadata.datatype.DataType.NonNegativeInteger;
import static com.asoroka.sidora.tabularmetadata.datatype.DataType.PositiveInteger;
import static com.asoroka.sidora.tabularmetadata.datatype.DataType.String;
import static com.asoroka.sidora.tabularmetadata.datatype.DataType.URI;
import static com.asoroka.sidora.tabularmetadata.datatype.DataType.parseableAs;
import static com.asoroka.sidora.tabularmetadata.datatype.DataType.sortByHierarchy;
import static com.asoroka.sidora.tabularmetadata.test.TestUtilities.retrieveTestData;
import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Test;

public class DataTypeTest {

    // private static final Logger log = getLogger(DataTypeTest.class);

    private static Map<DataType, Set<DataType>> expectedParseableTypes = retrieveTestData(
            DataTypeTest.class, "expectedParseableTypes");

    private static Map<DataType, Set<DataType>> expectedSuperTypes = retrieveTestData(
            DataTypeTest.class, "expectedSuperTypes");

    private static Map<DataType, List<String>> sampleValues = retrieveTestData(
            DataTypeTest.class, "sampleValues");

    @Test
    public void testCorrectParsingOfValues() {
        for (final DataType testType : DataType.values()) {
            for (final String testValue : sampleValues.get(testType)) {
                final Set<DataType> result = parseableAs(testValue);
                assertEquals("Did not find the appropriate datatypes suggested as parseable for a " + testType + "!",
                        expectedParseableTypes
                                .get(testType), result);
            }
        }
    }

    @Test
    public void testSupertypes() {
        for (final DataType testDatatype : DataType.values()) {
            final Set<DataType> result = testDatatype.supertypes();
            assertEquals("Did not find the appropriate supertypes for" + testDatatype + "!", expectedSuperTypes
                    .get(testDatatype), result);
        }
    }

    @Test
    public void testOrderingByHierarchy() {
        List<DataType> listToBeSorted = asList(Decimal, PositiveInteger, Integer, NonNegativeInteger, String);
        List<DataType> listInCorrectSorting = asList(PositiveInteger, NonNegativeInteger, Integer, Decimal, String);
        List<DataType> sorted = new ArrayList<>(sortByHierarchy(listToBeSorted));
        assertEquals("Got wrong ordering by hierarchy of datatypes!", listInCorrectSorting, sorted);

        listToBeSorted = asList(String, Geographic);
        listInCorrectSorting = asList(Geographic, String);
        sorted = new ArrayList<>(sortByHierarchy(listToBeSorted));
        assertEquals("Got wrong ordering by hierarchy of datatypes!", listInCorrectSorting, sorted);

        listToBeSorted = asList(String, DateTime);
        listInCorrectSorting = asList(DateTime, String);
        sorted = new ArrayList<>(sortByHierarchy(listToBeSorted));
        assertEquals("Got wrong ordering by hierarchy of datatypes!", listInCorrectSorting, sorted);

        listToBeSorted = asList(String, Boolean);
        listInCorrectSorting = asList(Boolean, String);
        sorted = new ArrayList<>(sortByHierarchy(listToBeSorted));
        assertEquals("Got wrong ordering by hierarchy of datatypes!", listInCorrectSorting, sorted);

        listToBeSorted = asList(String, URI);
        listInCorrectSorting = asList(URI, String);
        sorted = new ArrayList<>(sortByHierarchy(listToBeSorted));
        assertEquals("Got wrong ordering by hierarchy of datatypes!", listInCorrectSorting, sorted);
    }

    @Test
    public void testBadGeographies() {
        String testValue = "23, 23, 23, 23";
        assertFalse("Accepted a four-valued tuple as geographic coordinates!", parseableAs(testValue)
                .contains(Geographic));
        testValue = "23";
        assertFalse("Accepted a one-valued tuple as geographic coordinates!", parseableAs(testValue)
                .contains(Geographic));
    }

    @Test
    public void testNoDecimalPointDecimal() {
        final String testValue = "7087";
        assertTrue("Failed to accept a no-decimal-point number as a legitimate Decimal!", parseableAs(testValue)
                .contains(Decimal));
    }

    @Test
    public void testBadIntegerPartDecimal() {
        final String testValue = "fhglf.7087";
        assertFalse("Accepted a \"number\" with non-integral integer part as a legitimate Decimal!", parseableAs(
                testValue)
                .contains(Decimal));
    }

    @Test
    public void testBadDecimalPartDecimal() {
        final String testValue = "34235.dfgsdfg";
        assertFalse("Accepted a \"number\" with non-integral decimal part as a legitimate Decimal!", parseableAs(
                testValue)
                .contains(Decimal));
    }

    @Test
    public void testBadBothPartsDecimal() {
        final String testValue = "sgsg.dfgsdfg";
        assertFalse("Accepted a \"number\" with non-integral decimal part as a legitimate Decimal!", parseableAs(
                testValue)
                .contains(Decimal));
    }

    @Test
    public void testCompletelyBadDecimal() {
        final String testValue = "s24fgsdfg";
        assertFalse("Accepted a \"number\" with non-integral decimal part as a legitimate Decimal!", parseableAs(
                testValue)
                .contains(Decimal));
    }

    @Test
    public void testBadURI() {
        final String testValue = "38.03,-78.478889";
        assertFalse("Accepted a string that cannot be parsed as an URI as a legitimate URI!", parseableAs(testValue)
                .contains(URI));
    }

}
