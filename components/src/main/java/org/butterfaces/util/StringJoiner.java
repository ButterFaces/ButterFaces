/*
 * Copyright Lars Michaelis and Stephan Zerhusen 2016.
 * Distributed under the MIT License.
 * (See accompanying file README.md file or copy at http://opensource.org/licenses/MIT)
 */
package org.butterfaces.util;


import java.util.ArrayList;
import java.util.List;

/**
 * @author Lars Michaelis
 */
public final class StringJoiner {

    private final List<String> joins = new ArrayList<>();
    private String separator;
    private String wrapped;

    private StringJoiner(final String separator) {
        this.separator = separator;
        this.wrapped = null;
    }

    private StringJoiner(final String separator, final String wrapped, final Iterable<String> values) {
        this(separator);

        this.wrapped = wrapped;

        for (String value : values) {
            this.joins.add(StringUtils.getNullSafeValue(value));
        }
    }

    public static StringJoiner on(final String sep) {
        return new StringJoiner(sep);
    }

    public static StringJoiner on(final char sep) {
        return new StringJoiner(String.valueOf(sep));
    }

    public StringJoiner join(final String value) {
        final List<String> newJoins = new ArrayList<>(joins);
        newJoins.add(value);
        return new StringJoiner(separator, wrapped, newJoins);
    }

    public StringJoiner join(final Iterable<String> values) {
        final List<String> newJoins = new ArrayList<>(joins);
        for (String value : values) {
            newJoins.add(value);
        }
        return new StringJoiner(separator, wrapped, newJoins);
    }

    public StringJoiner wrappedBy(final String wrapped) {
        return new StringJoiner(separator, wrapped, joins);
    }

    public StringJoiner wrappedBy(final char wrapped) {
        return new StringJoiner(separator, String.valueOf(wrapped), joins);
    }

    @Override
    public String toString() {
        if (joins.isEmpty()) {
            return "";
        }

        final StringBuilder sb = new StringBuilder();

        for (String s : joins) {
            if (s == null) {
                continue;
            }

            if (sb.length() > 0) {
                sb.append(separator);
            }

            if (StringUtils.isNotEmpty(wrapped)) {
                sb.append(wrapped);
                sb.append(s);
                sb.append(wrapped);
            } else {
                sb.append(s);
            }
        }

        return sb.toString();
    }
}
