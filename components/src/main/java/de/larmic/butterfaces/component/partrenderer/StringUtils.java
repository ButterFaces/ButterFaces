package de.larmic.butterfaces.component.partrenderer;

/**
 * Created by larmic on 27.08.14.
 */
public class StringUtils {

    public static final String BLANK = "";
    public static final String SPACE = " ";

    public static final String concatWithSpace(final String... styles) {
        final StringBuilder sb = new StringBuilder();

        for (final String style : styles) {
            if (style != null && !BLANK.equals(style)) {
                sb.append(style);
                sb.append(" ");
            }
        }

        return sb.toString().trim();
    }

    public static final boolean isEmpty(final String value) {
        return !(value != null && !"".equals(value));
    }

    public static final boolean isNotEmpty(final String value) {
        return !isEmpty(value);
    }
}
