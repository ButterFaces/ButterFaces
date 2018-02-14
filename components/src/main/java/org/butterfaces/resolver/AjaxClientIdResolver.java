package org.butterfaces.resolver;

import org.butterfaces.util.StringUtils;
import org.butterfaces.util.StringUtils;

import javax.faces.component.UIComponent;
import javax.faces.component.UINamingContainer;
import javax.faces.component.behavior.AjaxBehavior;
import javax.faces.component.behavior.ClientBehavior;
import javax.faces.component.behavior.ClientBehaviorHolder;
import javax.faces.component.html.HtmlForm;
import javax.faces.context.FacesContext;
import java.util.*;

/**
 * Resolves client id for all ajax render ids in an  {@link UIComponent}. This client ids should be readably by jquery.
 */
public class AjaxClientIdResolver {

    private final Set<String> ajaxRenderClientIds = new HashSet<>();
    private final Set<String> resolvedAjaxRenderClientIds = new HashSet<>();
    private final Set<String> resolvedAjaxRenderJQueryClientIds = new HashSet<>();
    private final String jQueryRenderIDSelector;

    public AjaxClientIdResolver(final UIComponent component) {
        if (!(component instanceof ClientBehaviorHolder)) {
            throw new IllegalArgumentException("Component " + component.getClass() + " is not implementing ClientBehaviorHolder");
        }

        ajaxRenderClientIds.addAll(this.findClientIdsToDisableOnRequest((ClientBehaviorHolder) component));
        resolvedAjaxRenderClientIds.addAll(this.resolveClientIds(component, ajaxRenderClientIds));
        resolvedAjaxRenderJQueryClientIds.addAll(this.convertClientIdsToJQuerySelectorIds(resolvedAjaxRenderClientIds));
        jQueryRenderIDSelector = this.createJQueryIDSelector(resolvedAjaxRenderJQueryClientIds, component);
    }

    private String createJQueryIDSelector(final Collection<String> jQueryReadableClientIds, final UIComponent component) {
        if ((null == jQueryReadableClientIds) || jQueryReadableClientIds.isEmpty()) {
            return "undefined";
        }

        final StringBuilder builder = new StringBuilder();

        final Iterator<String> iterator = jQueryReadableClientIds.iterator();

        while (iterator.hasNext()) {
            final String jQueryReadableClientId = iterator.next();

            if (jQueryReadableClientId.equals("@all")) {
                builder.append("html");
            } else if (jQueryReadableClientId.equals("@form")) {
                final String clientIdOfSurroundingFormClientId = this.findClientIdOfSurroundingFormClientId(component);
                if (StringUtils.isNotEmpty(clientIdOfSurroundingFormClientId)) {
                    builder.append("#" + clientIdOfSurroundingFormClientId);
                }
            } else if (jQueryReadableClientId.equals("@this") || jQueryReadableClientId.equals("@none")) {
            } else {
                builder.append(jQueryReadableClientId);
            }

            if (iterator.hasNext()) {
                builder.append(", ");
            }
        }

        return builder.toString();
    }

    private Set<String> findClientIdsToDisableOnRequest(final ClientBehaviorHolder clientBehaviorHolder) {
        final Set<String> renderClientIds = new HashSet<>();
        if (!clientBehaviorHolder.getClientBehaviors().isEmpty()) {
            final Collection<List<ClientBehavior>> clientBehaviors = clientBehaviorHolder.getClientBehaviors().values();

            for (List<ClientBehavior> clientBehavior : clientBehaviors) {
                for (ClientBehavior behavior : clientBehavior) {
                    if (behavior instanceof AjaxBehavior) {
                        final AjaxBehavior ajaxBehavior = (AjaxBehavior) behavior;
                        for (String render : ajaxBehavior.getRender()) {
                            if (StringUtils.isNotEmpty(render)) {
                                renderClientIds.add(render.trim());
                            }
                        }
                    }
                }
            }
        }
        return renderClientIds;
    }

    private Set<String> convertClientIdsToJQuerySelectorIds(final Collection<String> clientIds) {
        final Set<String> jQueryClientIds = new HashSet<>();

        for (String clientIdToConvert : clientIds) {
            if (clientIdToConvert.startsWith("@")) {
               jQueryClientIds.add(clientIdToConvert);
            } else {
                jQueryClientIds.add("#" + clientIdToConvert.replaceAll(":", "\\\\\\\\:"));
            }
        }

        return jQueryClientIds;
    }

    private Set<String> resolveClientIds(final UIComponent component,
                                         final Collection<String> clientIdsToResolve) {
        final Set<String> resolvedClientIds = new HashSet<>();

        if ((null == clientIdsToResolve) || clientIdsToResolve.isEmpty()) {
            return resolvedClientIds;
        }

        for (String clientIdToResolve : clientIdsToResolve) {
            if (clientIdToResolve.trim().length() == 0) {
                continue;
            }

            if (clientIdToResolve.equals("@all") || clientIdToResolve.equals("@none") ||
                    clientIdToResolve.equals("@form") || clientIdToResolve.equals("@this")) {
                resolvedClientIds.add(clientIdToResolve);
            } else {
                resolvedClientIds.add(getResolvedId(component, clientIdToResolve));
            }
        }

        return resolvedClientIds;
    }

    private String getResolvedId(final UIComponent component, final String clientIdToResolve) {

        // some component does not return correct client id (i.e. table) so try it by using parent component
        final UIComponent resolvedComponent = this.resolveComponent(component, clientIdToResolve);

        if (resolvedComponent == null) {
            if (clientIdToResolve.charAt(0) == UINamingContainer.getSeparatorChar(FacesContext.getCurrentInstance())) {
                return clientIdToResolve.substring(1);
            }
            return clientIdToResolve;
        }

        return resolvedComponent.getClientId();
    }

    private UIComponent resolveComponent(final UIComponent component, final String clientIdToResolve) {
        if (component == null) {
            return null;
        }

        final UIComponent resolvedComponent = component.findComponent(clientIdToResolve);

        // recursive call
        return resolvedComponent != null ? resolvedComponent : this.resolveComponent(component.getParent(), clientIdToResolve);
    }

    private String findClientIdOfSurroundingFormClientId(final UIComponent component) {
        if (component instanceof HtmlForm) {
            return component.getClientId();
        }

        if (component.getParent() == null) {
            return null;
        }

        return findClientIdOfSurroundingFormClientId(component.getParent());
    }

    public Set<String> getResolvedAjaxRenderClientIds() {
        return resolvedAjaxRenderClientIds;
    }

    public Set<String> getAjaxRenderClientIds() {
        return ajaxRenderClientIds;
    }

    public Set<String> getResolvedAjaxRenderJQueryClientIds() {
        return resolvedAjaxRenderJQueryClientIds;
    }

    public String getjQueryRenderIDSelector() {
        return jQueryRenderIDSelector;
    }
}
