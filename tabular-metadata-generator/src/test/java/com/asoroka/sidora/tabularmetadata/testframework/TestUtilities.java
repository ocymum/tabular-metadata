
package com.asoroka.sidora.tabularmetadata.testframework;

import static com.asoroka.sidora.tabularmetadata.datatype.DataType.Boolean;
import static com.asoroka.sidora.tabularmetadata.datatype.DataType.Decimal;
import static com.asoroka.sidora.tabularmetadata.datatype.DataType.Integer;
import static com.asoroka.sidora.tabularmetadata.datatype.DataType.NonNegativeInteger;
import static java.lang.Integer.MAX_VALUE;
import static java.lang.Integer.MIN_VALUE;
import static java.lang.Math.abs;
import static java.lang.Math.random;
import static java.lang.Math.round;
import static java.util.Arrays.asList;
import static java.util.UUID.randomUUID;
import static org.joda.time.DateTime.now;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.junit.experimental.theories.PotentialAssignment;
import org.junit.experimental.theories.PotentialAssignment.CouldNotGenerateValueException;
import org.mockito.internal.stubbing.answers.Returns;

import com.asoroka.sidora.tabularmetadata.SelfTypeInstanceGenerator;
import com.asoroka.sidora.tabularmetadata.datatype.DataType;
import com.asoroka.sidora.tabularmetadata.datatype.GeographicValue;
import com.asoroka.sidora.tabularmetadata.heuristics.Heuristic;
import com.asoroka.sidora.tabularmetadata.heuristics.enumerations.EnumeratedValuesHeuristic;
import com.asoroka.sidora.tabularmetadata.heuristics.ranges.RangeDeterminingHeuristic;
import com.asoroka.sidora.tabularmetadata.heuristics.types.TypeDeterminingHeuristic;
import com.google.common.base.Function;
import com.googlecode.totallylazy.Callable1;

/**
 * Utilities for testing.
 * 
 * @author ajs6f
 */
public abstract class TestUtilities {

    public static void addValues(final Heuristic<?> strategy, final Iterable<?> values) {
        for (final Object value : values) {
            strategy.addValue(value.toString());
        }
    }

    /**
     * Extracts the most likely type selection from a {@link ValueHeuristic}
     */
    protected static final Function<TypeDeterminingHeuristic<?>, DataType> getMostLikelyType =
            new Function<TypeDeterminingHeuristic<?>, DataType>() {

                @Override
                public DataType apply(final TypeDeterminingHeuristic<?> heuristic) {
                    return heuristic.mostLikelyType();
                }
            };

    /**
     * The following peculiar locution arises from the need to provide the action of
     * {@link SelfTypeInstanceGenerator#newInstance()} while avoiding a recursive mock
     * 
     * @return a mock strategy
     */
    public static MockedHeuristic cloneableMockStrategy(final MockedHeuristic strategy) {
        final MockedHeuristic mocked = mock(MockedHeuristic.class);
        final Returns cloner = new Returns(strategy);
        when(mocked.newInstance()).thenAnswer(cloner);
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

    final static <From> Callable1<From, PotentialAssignment> toPotentialAssignment() {
        return new Callable1<From, PotentialAssignment>() {

            @Override
            public PotentialAssignment call(final From from) {
                return PotentialAssignment.forValue(null, from);
            }
        };
    }

    final static <To> Callable1<PotentialAssignment, To> fromPotentialAssignment() {
        return new Callable1<PotentialAssignment, To>() {

            @Override
            public To call(final PotentialAssignment from) {
                try {
                    return (To) from.getValue();
                } catch (final CouldNotGenerateValueException e) {
                    throw new AssertionError();
                }
            }
        };
    }

    /**
     * @param type a {@link DataType}
     * @return a random value appropriate to that DataType
     */
    static Comparable<?> generateRandomValue(final DataType type) {
        switch (type) {
        case Boolean:
            return random() > 0.5;
        case DateTime:
            return now().plus(round(random() * 1000000000000F));
        case Decimal:
            return (float) (random() - 0.5) * 10;
        case Geographic:
            if ((Boolean) generateRandomValue(DataType.Boolean)) {
                return new GeographicValue(asList(
                        (Float) generateRandomValue(Decimal),
                        (Float) generateRandomValue(Decimal)));
            }
            return new GeographicValue(asList(
                    (Float) generateRandomValue(Decimal),
                    (Float) generateRandomValue(Decimal),
                    (Float) generateRandomValue(Decimal)));
        case Integer:
            if (random() < 0.1) {
                if ((Boolean) generateRandomValue(Boolean)) {
                    return MAX_VALUE;
                }
                return MIN_VALUE;
            }
            return round((Float) generateRandomValue(Decimal));
        case NonNegativeInteger:
            if (random() < 0.2) {
                return 0;
            }
            final Integer randInt = (Integer) generateRandomValue(Integer);
            if (randInt.equals(MAX_VALUE) || randInt.equals(MIN_VALUE)) {
                return 0;
            }
            return abs(randInt);
        case PositiveInteger:
            return (Integer) generateRandomValue(NonNegativeInteger) + 1;
        case String:
            return randomUUID().toString();
        case URI:
            return URI.create("info:" + generateRandomValue(DataType.String));
        default:
            throw new AssertionError("A DataType of an un-enumerated kind should never exist!");
        }
    }

    static RandomValuesForAType randomValues(final DataType type, final short numValues) {
        final RandomValuesForAType values = new RandomValuesForAType(numValues).withType(type);
        for (short valueIndex = 0; valueIndex < numValues; valueIndex++) {
            values.add(generateRandomValue(type));
        }
        return values;
    }

    public static class RandomValuesForAType extends ArrayList<Comparable<?>> {

        private static final long serialVersionUID = 1L;

        public DataType type;

        public RandomValuesForAType(final short initCapacity) {
            super(initCapacity);
        }

        public RandomValuesForAType withType(final DataType t) {
            this.type = t;
            return this;
        }

        public <T> List<T> as() {
            @SuppressWarnings("unchecked")
            final List<T> list = (List<T>) this;
            return list;
        }
    }
}