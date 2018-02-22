package org.butterfaces.component.renderkit.html_basic.text.util;

import org.butterfaces.context.StringHtmlEncoder;
import org.butterfaces.resolver.MustacheResolver;

import javax.faces.component.UIComponent;
import javax.faces.component.UIComponentBase;
import javax.faces.context.FacesContext;
import java.io.IOException;
import java.util.*;

public class TrivialComponentsUtil {

    /**
     * TODO: trivial components does not support foo.bar so this methods replaces foo.bar by foo_bar.
     * TODO this could removed if https://github.com/trivial-components/trivial-components/issues/36 is fixed
     */
    public static List<String> replaceDotInMustacheKeys(final List<String> mustacheKeys) {
        final List<String> fixedMustacheKeys = new ArrayList<>();

        for (String mustacheKey : mustacheKeys) {
            fixedMustacheKeys.add(mustacheKey.replace('.', '#'));
        }

        return fixedMustacheKeys;
    }

    /**
     * TODO: trivial components does not support foo.bar so this methods replaces foo.bar by foo_bar.
     * TODO this could removed if https://github.com/trivial-components/trivial-components/issues/36 is fixed
     */
    public static String replaceDotInMustacheKeys(final List<String> mustacheKeys, final String pluginCall) {
        String fixedPluginCall = pluginCall;
        for (String mustacheKey : mustacheKeys) {
            if (mustacheKey.contains(".")) {
                fixedPluginCall = fixedPluginCall.replace("{{" + mustacheKey + "}}", "{{" + mustacheKey.replace('.', '#') + "}}");
            }
        }

        return fixedPluginCall;
    }

    public static List<String> createMustacheKeys(FacesContext context, UIComponentBase component) throws IOException {
        final Set<String> mustacheKeys = new HashSet<>();
        mustacheKeys.addAll(createMustacheKeysFromTemplate(context, component, "template"));
        mustacheKeys.addAll(createMustacheKeysFromTemplate(context, component, "emptyEntryTemplate"));
        mustacheKeys.addAll(createMustacheKeysFromTemplate(context, component, "selectedEntryTemplate"));
        return new ArrayList<>(mustacheKeys);
    }

    private static List<String> createMustacheKeysFromTemplate(FacesContext context, UIComponentBase component, String facetKey) throws IOException {
        final UIComponent templateFacet = component.getFacet(facetKey);
        if (templateFacet != null) {
            final String encodedTemplate = StringHtmlEncoder.encodeComponentWithSurroundingDiv(context, templateFacet);
            return MustacheResolver.getMustacheKeysForTreeNode(encodedTemplate);
        } else {
            return Collections.emptyList();
        }
    }

}
