package de.larmic.butterfaces.resolver;

import de.larmic.butterfaces.component.partrenderer.StringUtils;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MustacheResolver {

    public static final Pattern MUSTACHE_PATTERN = Pattern.compile(".*?\\{\\{(.*?)\\}\\}.*?");

    /**
     * Scans given template for mustache keys (syntax {{value}}).
     *
     * @return empty list if no mustache key is found.
     */
    public static List<String> getMustacheKeys(final String template) {
        final Set<String> keys = new HashSet<>();

        if (StringUtils.isNotEmpty(template)) {
            for (String value : template.split("\\{\\{")) {
                final String textValue = value.equals(template) ? template : "{{" + value;

                final Matcher matcher = MUSTACHE_PATTERN.matcher(textValue);
                if (matcher.matches()) {
                    keys.add(matcher.group(1));
                }
            }
        }

        return new ArrayList<>(keys);
    }

    public static List<String> getMustacheKeysForTree(final String template) {
        final List<String> mustacheKeys = getMustacheKeys(template);
        // TODO test and remove ignore case
        mustacheKeys.removeAll(Arrays.asList("id", "title", "expanded", "description", "imageStyle", "imageClass", "styleClass"));
        return mustacheKeys;
    }

}
