package de.larmic.butterfaces.resolver;

import de.larmic.butterfaces.component.partrenderer.StringUtils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MustacheResolver {

    /**
     * Scans given text for mustache keys (syntax {{value}}).
     * @return empty list if no mustache key is found.
     */
    public static List<String> getMustacheKeys(final String text) {
        final Set<String> keys = new HashSet<>();

        if (StringUtils.isNotEmpty(text)) {
            // TODO [larmic] switch to regex
            final String[] possibleKeys = text.split("\\{\\{");
            for (String possibleKey : possibleKeys) {
                if (possibleKey.contains("}}")) {
                    final String[] split = possibleKey.split("\\}\\}");
                    keys.add(split[0]);
                }
            }
        }

        return new ArrayList<>(keys);
    }

}
