package de.larmic.butterfaces.component.partrenderer;

/**
 * Created by larmic on 27.08.14.
 */
public class StringUtils {

    public static final String concatStyles(final String... styles) {
        final StringBuilder sb = new StringBuilder();

        for (final String style : styles) {
            if (style != null && !"".equals(style)) {
                sb.append(style);
                sb.append(" ");
            }
        }

        return sb.toString();
    }

    public static final boolean isEmpty(final String value) {
        return !(value != null && !"".equals(value));
    }

}
