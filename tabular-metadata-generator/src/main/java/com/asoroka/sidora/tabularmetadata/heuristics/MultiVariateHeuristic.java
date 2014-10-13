
package com.asoroka.sidora.tabularmetadata.heuristics;

import static com.google.common.base.Functions.constant;
import static com.google.common.collect.Collections2.filter;
import static com.google.common.collect.Maps.toMap;
import static com.google.common.collect.Sets.powerSet;
import static org.slf4j.LoggerFactory.getLogger;

import java.util.Collection;
import java.util.EnumMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.slf4j.Logger;

import com.asoroka.sidora.tabularmetadata.datatype.ValueType;
import com.google.common.base.Predicate;

public class MultiVariateHeuristic extends ChoiceCountAggregatingHeuristic<MultiVariateHeuristic> {

    private Map<ValueType, Boolean> candidacies;

    public Map<ValueType, Float> typeLikelihoods;

    private final float minimum;

    private static final Logger log = getLogger(MultiVariateHeuristic.class);

    /**
     * @param minimum
     */
    public MultiVariateHeuristic(final float minimum) {
        super();
        this.minimum = minimum;

        final Set<Set<ValueType>> rawPowerSet = powerSet(ValueType.valuesSet());
        final int numChoices = rawPowerSet.size();
        final Float likelihoodPerAppearance = 1 / (float) numChoices;
        log.trace("likelihoodPerAppearance: {}", likelihoodPerAppearance);
        typeLikelihoods = new EnumMap<>(ValueType.class);
        final Map<ValueType, Float> zeroes = toMap(ValueType.valuesSet(), constant(0F));
        typeLikelihoods.putAll(zeroes);

        for (final ValueType type : ValueType.valuesSet()) {
            for (final Set<ValueType> choice : rawPowerSet) {
                if (choice.contains(type)) {
                    final Float currentLikelihood = typeLikelihoods.containsKey(type) ? typeLikelihoods.get(type) : 0;
                    log.trace("Current likelihood of {} is {}.", type, currentLikelihood);
                    typeLikelihoods.put(type, currentLikelihood + likelihoodPerAppearance);
                }
            }
        }
        // assume all types are out of the running until proven otherwise
        candidacies = new EnumMap<>(ValueType.class);
        final Map<ValueType, Boolean> falses = toMap(ValueType.valuesSet(), constant(false));
        candidacies.putAll(falses);

    }

    @Override
    protected boolean candidacy(final ValueType type) {

        // determine which types are acceptable
        final Set<Entry<ValueType, Boolean>> entrySet = candidacies.entrySet();
        final Collection<Entry<ValueType, Boolean>> candidates = filter(entrySet, valueTrue);
        return candidates.contains(type);
    }

    @Override
    public MultiVariateHeuristic clone() {
        return this;
    }

    private static final Predicate<Entry<ValueType, Boolean>> valueTrue =
            new Predicate<Entry<ValueType, Boolean>>() {

                @Override
                public boolean apply(final Entry<ValueType, Boolean> e) {
                    return e.getValue();
                }
            };

}
