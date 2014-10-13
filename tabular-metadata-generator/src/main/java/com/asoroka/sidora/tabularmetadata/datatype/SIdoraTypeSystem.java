
package com.asoroka.sidora.tabularmetadata.datatype;

import static com.asoroka.sidora.tabularmetadata.datatype.Boolean.BOOLEAN;
import static com.asoroka.sidora.tabularmetadata.datatype.DateTime.DATETIME;
import static com.asoroka.sidora.tabularmetadata.datatype.Decimal.DECIMAL;
import static com.asoroka.sidora.tabularmetadata.datatype.Geographic.GEOGRAPHIC;
import static com.asoroka.sidora.tabularmetadata.datatype.Integer.INTEGER;
import static com.asoroka.sidora.tabularmetadata.datatype.Lex.LEX;
import static com.asoroka.sidora.tabularmetadata.datatype.NonNegativeInteger.NONNEGATIVEINTEGER;
import static com.asoroka.sidora.tabularmetadata.datatype.PositiveInteger.POSITIVEINTEGER;
import static com.asoroka.sidora.tabularmetadata.datatype.URI.URI;
import static com.google.common.collect.ImmutableSet.of;

import java.util.Set;

public class SIdoraTypeSystem extends AbstractValueTypeSystem {

    public static final Set<AbstractValueType<?, ?>> basicTypes = of(LEX, BOOLEAN, DATETIME, DECIMAL, GEOGRAPHIC,
            INTEGER, NONNEGATIVEINTEGER, POSITIVEINTEGER, URI);

    public SIDoraTypeSystem() {
        for (final ValueType<?, ?> t : basicTypes) {
            register(t);
        }
    }

    @Override
    public ValueType topType() {
        return LEX;
    }

    public static Set<ValueType> parseableAsTypes(final String s) {
        return parseableAsTypes(s);
    }
}
