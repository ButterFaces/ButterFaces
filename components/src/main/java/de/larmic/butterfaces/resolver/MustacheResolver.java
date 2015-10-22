package de.larmic.butterfaces.resolver;

import de.larmic.butterfaces.component.partrenderer.StringUtils;

import java.util.*;

public class MustacheResolver {

    /**
     * Scans given template for mustache keys (syntax {{value}}).
     * @return empty list if no mustache key is found.
     */
    public static List<String> getMustacheKeys(final String template) {
        final Set<String> keys = new HashSet<>();

        if (StringUtils.isNotEmpty(template)) {
            // TODO [larmic] switch to regex
            final String[] possibleKeys = template.split("\\{\\{");
            for (String possibleKey : possibleKeys) {
                if (possibleKey.contains("}}")) {
                    final String[] split = possibleKey.split("\\}\\}");
                    keys.add(split[0]);
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
