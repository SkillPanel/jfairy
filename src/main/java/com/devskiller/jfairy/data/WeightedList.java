package com.devskiller.jfairy.data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.jspecify.annotations.Nullable;

import com.devskiller.jfairy.producer.BaseProducer;
import com.devskiller.jfairy.producer.util.ValidateUtils;

/**
 * One data list, split once from its comma-separated value. An element is {@code value} or {@code value*weight},
 * where the weight is a positive integer relative to the other elements of the list and a missing weight counts as 1.
 * A list without any weight is picked uniformly, exactly as before weights existed.
 */
final class WeightedList {

    private static final String LIST_SEPARATOR = ",";
    private static final char WEIGHT_SEPARATOR = '*';

    private final List<String> elements;
    private final List<String> values;
    // Running totals of the weights, or null when no element has a weight
    private final long @Nullable [] cumulativeWeights;

    private WeightedList(List<String> elements, List<String> values, long @Nullable [] cumulativeWeights) {
        this.elements = elements;
        this.values = values;
        this.cumulativeWeights = cumulativeWeights;
    }

    /**
     * @throws IllegalArgumentException if a weight is not a positive integer, an element has a weight but no value,
     *                                  or the weights add up past {@link Long#MAX_VALUE}
     */
    static WeightedList parse(String raw) {
        List<String> elements = Arrays.stream(raw.split(LIST_SEPARATOR))
            .map(String::strip)
            .filter(element -> !element.isEmpty())
            .toList();
        List<String> values = new ArrayList<>(elements.size());
        long[] cumulativeWeights = new long[elements.size()];
        boolean weighted = false;
        long total = 0;
        for (int i = 0; i < elements.size(); i++) {
            String element = elements.get(i);
            int separator = element.indexOf(WEIGHT_SEPARATOR);
            String value = element;
            long weight = 1;
            if (separator >= 0) {
                weighted = true;
                value = element.substring(0, separator).strip();
                weight = parseWeight(element, element.substring(separator + 1).strip());
                ValidateUtils.isTrue(!value.isEmpty(), "Element '%s' has a weight but no value", element);
            }
            values.add(value);
            ValidateUtils.isTrue(total <= Long.MAX_VALUE - weight, "Weights add up to more than %s", Long.MAX_VALUE);
            total += weight;
            cumulativeWeights[i] = total;
        }
        return new WeightedList(elements, List.copyOf(values), weighted ? cumulativeWeights : null);
    }

    List<String> values() {
        return values;
    }

    List<String> elements() {
        return elements;
    }

    boolean isEmpty() {
        return values.isEmpty();
    }

    String pick(BaseProducer baseProducer) {
        if (cumulativeWeights == null) {
            return baseProducer.randomElement(values);
        }
        long drawn = baseProducer.randomBetween(1L, cumulativeWeights[cumulativeWeights.length - 1]);
        int index = Arrays.binarySearch(cumulativeWeights, drawn);
        return values.get(index >= 0 ? index : -index - 1);
    }

    private static long parseWeight(String element, String weight) {
        long parsed;
        try {
            parsed = Long.parseLong(weight);
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException(
                String.format("Element '%s' must have a positive integer weight", element), ex);
        }
        ValidateUtils.isTrue(parsed > 0, "Element '%s' must have a positive integer weight", element);
        return parsed;
    }
}
