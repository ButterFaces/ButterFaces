/*
 * Copyright Lars Michaelis and Stephan Zerhusen 2016.
 * Distributed under the MIT License.
 * (See accompanying file README.md file or copy at http://opensource.org/licenses/MIT)
 */
package org.butterfaces.component.renderkit.html_basic.action;

import org.butterfaces.util.StringUtils;
import org.butterfaces.util.StringUtils;

/**
 * @author Lars Michaelis
 */
public class ParameterAppender {

    public static void appendProperty(StringBuilder builder, String name, Object value) {
        if (StringUtils.isEmpty(name)) {
            throw new IllegalArgumentException();
        }

        if (builder.length() > 0) {
            final char lastChar = builder.charAt(builder.length() - 1);
            if (lastChar != ',' && lastChar != '{') {
                builder.append(',');
            }
        }

        appendQuotedValue(builder, name);

        builder.append(":");

        if (value != null) {
            appendQuotedValue(builder, value.toString());
        } else {
            builder.append("''");
        }
    }

    private static void appendQuotedValue(StringBuilder builder, String value) {
        builder.append("'");

        final int length = value.length();

        for (int i = 0; i < length; i++) {
            final char c = value.charAt(i);

            if (c == '\'' || c == '\\') {
                builder.append('\\');
            }

            builder.append(c);
        }

        builder.append("'");
    }

}
