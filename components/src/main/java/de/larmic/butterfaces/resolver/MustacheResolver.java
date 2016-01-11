/*
 * Copyright Lars Michaelis and Stephan Zerhusen 2016.
 * Distributed under the MIT License.
 * (See accompanying file README.md file or copy at http://opensource.org/licenses/MIT)
 */
package de.larmic.butterfaces.resolver;

import de.larmic.butterfaces.util.StringUtils;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Lars Michaelis
 */
public class MustacheResolver {

    public static final Pattern MUSTACHE_PATTERN = Pattern.compile("\\{\\{(.*?)\\}\\}");

    /**
     * Scans given template for mustache keys (syntax {{value}}).
     *
     * @return empty list if no mustache key is found.
     */
    public static List<String> getMustacheKeys(final String template) {
        final Set<String> keys = new HashSet<>();

        if (StringUtils.isNotEmpty(template)) {
            final Matcher matcher = MUSTACHE_PATTERN.matcher(template);

            while(matcher.find()) {
                keys.add(matcher.group(1));
            }

        }

        return new ArrayList<>(keys);
    }

    public static List<String> getMustacheKeysForTreeNode(final String template) {
        final List<String> mustacheKeys = getMustacheKeys(template);
        // TODO test and remove ignore case
        mustacheKeys.removeAll(Arrays.asList("id", "title", "expanded", "description", "imageStyle", "imageClass", "styleClass"));
        return mustacheKeys;
    }

}
