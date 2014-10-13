/**
 * TEMPORARY LICENSE HEADER STANDIN
 * REPLACE WITH APPROPRIATE SIDORA LICENSE
 */

package com.asoroka.sidora.tabularmetadata.datatype;

import static com.asoroka.sidora.tabularmetadata.datatype.Boolean.BOOLEAN;
import static com.asoroka.sidora.tabularmetadata.datatype.DateTime.DATETIME;
import static com.asoroka.sidora.tabularmetadata.datatype.Decimal.DECIMAL;
import static com.asoroka.sidora.tabularmetadata.datatype.Geographic.GEOGRAPHIC;
import static com.asoroka.sidora.tabularmetadata.datatype.Integer.INTEGER;
import static com.asoroka.sidora.tabularmetadata.datatype.NonNegativeInteger.NONNEGATIVEINTEGER;;
import static com.asoroka.sidora.tabularmetadata.datatype.PositiveInteger.POSITIVEINTEGER;
import static com.asoroka.sidora.tabularmetadata.datatype.Lex.LEX;
import static com.asoroka.sidora.tabularmetadata.datatype.URI.URI;
import static com.asoroka.sidora.tabularmetadata.datatype.SIdoraTypeSystem.parseableAsTypes;

import static com.google.common.collect.ImmutableMap.builder;
import static com.google.common.collect.Sets.newHashSet;
import static java.util.Arrays.asList;
import static com.google.common.collect.ImmutableSet.of;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Test;

import com.google.common.collect.ImmutableMap;

public class SIDoraTypeSystemTest {

    // private static final Logger log = getLogger(DataTypeTest.class);

    private static Map<ValueType, Set<ValueType>> expectedParseableTypes;

    private static Map<ValueType, Set<ValueType>> expectedSuperTypes;

    private static Map<ValueType, Set<String>> sampleValues;

    private static Map<String, ValueType> dataTypeNames;

    static {
        final ImmutableMap.Builder<ValueType, Set<ValueType>> b = builder();

        b.put(POSITIVEINTEGER, (Set<ValueType>)of(LEX, DECIMAL, INTEGER, NONNEGATIVEINTEGER, POSITIVEINTEGER, DATETIME));
        b.put(NONNEGATIVEINTEGER, of(LEX, DECIMAL, INTEGER, NONNEGATIVEINTEGER, DATETIME));
        b.put(INTEGER, of(LEX, DECIMAL, INTEGER, DATETIME));
        b.put(DECIMAL, of(LEX, DECIMAL));
        b.put(GEOGRAPHIC, of(GEOGRAPHIC, LEX));
        b.put(BOOLEAN, of(BOOLEAN, LEX));
        b.put(DATETIME, of(DATETIME, LEX));
        b.put(LEX, of(LEX));
        b.put(URI, of(URI, LEX));

        expectedParseableTypes = b.build();

        final ImmutableMap.Builder<ValueType, Set<ValueType>> b2 = builder();

        b2.put(PositiveInteger, of(String, Decimal, Integer, NonNegativeInteger, PositiveInteger));
        b2.put(NonNegativeInteger, of(String, Decimal, Integer, NonNegativeInteger));
        b2.put(Integer, of(String, Decimal, Integer));
        b2.put(Decimal, of(String, Decimal));
        b2.put(Geographic, of(String, Geographic));
        b2.put(Boolean, of(String, Boolean));
        b2.put(String, of(String));
        b2.put(DateTime, of(String, DateTime));
        b2.put(URI, of(String, URI));

        expectedSuperTypes = b2.build();

        final ImmutableMap.Builder<ValueType, Set<String>> b3 = builder();

        b3.put(PositiveInteger, newHashSet("123", "9000"));
        b3.put(NonNegativeInteger, newHashSet("0"));
        b3.put(Integer, newHashSet("-1", "-9999"));
        b3.put(Decimal, newHashSet("-5344543.4563453", "6734.999"));
        b3.put(Geographic, newHashSet("38.03,-78.478889", "38.03,-78.478889, 0"));
        b3.put(Boolean, newHashSet("truE", "falsE", "t", "F"));
        b3.put(String, newHashSet(":::oobleck"));
        b3.put(URI, newHashSet("http://example.com"));
        b3.put(DateTime, newHashSet("1990-3-4"));

        sampleValues = b3.build();

        final ImmutableMap.Builder<String, ValueType> b4 = builder();

        b4.putAll(ImmutableMap.of("PositiveInteger", PositiveInteger, "NonNegativeInteger", NonNegativeInteger,
                "Integer", Integer));
        b4.putAll(ImmutableMap.of("Decimal", Decimal, "Geographic", Geographic, "Boolean", Boolean));
        b4.putAll(ImmutableMap.of("DateTime", DateTime, "String", String, "URI", URI));

        dataTypeNames = b4.build();
    }

    @Test
    public void testCorrectParsingOfValues() {
        for (final ValueType testType : ValueType.values()) {
            for (final String testValue : sampleValues.get(testType)) {
                final Set<ValueType> result = parseableAs(testValue);
                assertEquals("Did not find the appropriate datatypes suggested as parseable for a " + testType + "!",
                        expectedParseableTypes
                                .get(testType), result);
            }
        }
    }

    @Test
    public void testSupertypes() {
        for (final ValueType testDatatype : ValueType.values()) {
            final Set<ValueType> result = testDatatype.supertypes();
            assertEquals("Did not find the appropriate supertypes for" + testDatatype + "!", expectedSuperTypes
                    .get(testDatatype), result);
        }
    }

    @Test
    public void testOrderingByHierarchy() {
        List<ValueType> listToBeSorted = asList(Decimal, PositiveInteger, Integer, NonNegativeInteger, String);
        List<ValueType> listInCorrectSorting = asList(PositiveInteger, NonNegativeInteger, Integer, Decimal, String);
        List<ValueType> sorted = new ArrayList<>(sortByHierarchy(listToBeSorted));
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

    @Test
    public void testNames() {
        assertEquals(
                "Our test datatype names are fewer or greater in number than the number of actual DataTypes!",
                ValueType.values().length, dataTypeNames.size());
        for (final String name : dataTypeNames.keySet()) {
            assertEquals(dataTypeNames.get(name), ValueType.valueOf(name));
        }
    }

}
