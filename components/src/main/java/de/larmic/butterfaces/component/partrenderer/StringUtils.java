package de.larmic.butterfaces.component.partrenderer;

import java.util.Collection;
import java.util.Iterator;

/**
 * Created by larmic on 27.08.14.
 */
public class StringUtils {

    public static final String BLANK = "";
    public static final String SPACE = " ";

    public static final String SEPARATOR_COMMA = ", ";

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

    public static String joinWithCommaSeparator(final Collection<String> values) {
        return join(values, SEPARATOR_COMMA, false);
    }

    public static String joinWithCommaSeparator(final Collection<String> values, boolean escape) {
        return join(values, SEPARATOR_COMMA, escape);
    }

    public static String joinWithSpaceSeparator(final Collection<String> values) {
        return join(values, SPACE, false);
    }

    public static String join(final Collection<String> values, final String separator, boolean escape) {
        final StringBuilder value = new StringBuilder();
        final Iterator<String> iterator = values.iterator();

        while (iterator.hasNext()) {
            final String valueToAdd = iterator.next();
            if (escape) {
                value.append("'");
            }
            value.append(valueToAdd);
            if (escape) {
                value.append("'");
            }

            if (iterator.hasNext()) {
                value.append(separator);
            }
        }

        return value.toString();
    }
}
