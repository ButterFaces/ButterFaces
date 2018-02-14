/*
 * Copyright Lars Michaelis and Stephan Zerhusen 2016.
 * Distributed under the MIT License.
 * (See accompanying file README.md file or copy at http://opensource.org/licenses/MIT)
 */
package org.butterfaces.util;

import java.util.Collection;

/**
 * @author Lars Michaelis
 */
public class StringUtils {

    public static final String BLANK = "";
    public static final String SPACE = " ";

    public static final String SEPARATOR_COMMA = ", ";

    /**
     * @deprecated use {@link StringJoiner} instead.
     */
    @Deprecated
    public static String concatWithSpace(final String... styles) {
        final StringBuilder sb = new StringBuilder();

        for (final String style : styles) {
            if (style != null && !BLANK.equals(style)) {
                sb.append(style);
                sb.append(SPACE);
            }
        }

        return sb.toString().trim();
    }

    public static boolean isEmpty(final String value) {
        return value == null || BLANK.equals(value);
    }

    public static boolean isNotEmpty(final String value) {
        return !isEmpty(value);
    }

    public static String getNotNullValue(final String value, final String alternative) {
        return isNotEmpty(value) ? value : alternative;
    }

    public static String getNullSafeValue(final String value) {
        return getNotNullValue(value, "");
    }

    /**
     * @deprecated use {@link StringJoiner} instead.
     */
    @Deprecated
    public static String joinWithCommaSeparator(final Collection<String> values) {
        return StringJoiner.on(SEPARATOR_COMMA).join(values).toString();
    }

    /**
     * @deprecated use {@link StringJoiner} instead.
     */
    @Deprecated
    public static String joinWithCommaSeparator(final Collection<String> values, boolean escape) {
        return StringJoiner.on(SEPARATOR_COMMA).join(values).wrappedBy("'").toString();
    }

    /**
     * @deprecated use {@link StringJoiner} instead.
     */
    @Deprecated
    public static String joinWithSpaceSeparator(final Collection<String> values) {
        return  StringJoiner.on(SPACE).join(values).toString();
    }
}
