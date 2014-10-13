
package com.asoroka.sidora.tabularmetadata.datatype;

import static com.google.common.collect.Lists.transform;
import static java.lang.Float.parseFloat;
import static java.util.Arrays.asList;

import java.util.List;

import com.google.common.base.Function;

public class Geographic extends AbstractValueType<Geographic, Lex> {

    public Geographic(final ValueTypeSystem system) {
        super(new Lex(system), system);
    }

    @Override
    public Geographic self() {
        return this;
    }

    @SuppressWarnings("unchecked")
    @Override
    public GeographicValue parse(final String s) throws ParsingException {
        try {
            final List<Float> parts = transform(asList(s.split(",")), string2float);
            return new GeographicValue(parts);
        } catch (final NumberFormatException e) {
            throw new ParsingException("Could not parse as Geographic!", e);
        }
    }

    static final Function<java.lang.String, Float> string2float = new Function<String, Float>() {

        @Override
        public Float apply(final java.lang.String seg) {
            return parseFloat(seg);
        }
    };
}
