package de.larmic.butterfaces.resolver;

import de.larmic.butterfaces.component.partrenderer.StringUtils;

import javax.faces.component.UIComponent;
import javax.faces.component.UINamingContainer;
import javax.faces.component.behavior.AjaxBehavior;
import javax.faces.component.behavior.ClientBehavior;
import javax.faces.component.behavior.ClientBehaviorHolder;
import javax.faces.context.FacesContext;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by larmic on 18.11.14.
 */
public class AjaxClientIdResolver {

    private final Set<String> ajaxRenderClientIds = new HashSet<>();
    private final Set<String> resolvedAjaxRenderClientIds = new HashSet<>();
    private final Set<String> resolvedAjaxRenderJQueryClientIds = new HashSet<>();

    public AjaxClientIdResolver(final UIComponent component) {
        if (!(component instanceof ClientBehaviorHolder)) {
            throw new IllegalArgumentException("Component " + component.getClass() + " is not implementing ClientBehaviorHolder");
        }

        ajaxRenderClientIds.addAll(this.findClientIdsToDisableOnRequest((ClientBehaviorHolder) component));
        resolvedAjaxRenderClientIds.addAll(this.resolveClientIds(component, ajaxRenderClientIds));
        resolvedAjaxRenderJQueryClientIds.addAll(this.convertClientIdsToJQuerySelectorIds(resolvedAjaxRenderClientIds));
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

        UIComponent resolvedComponent = component.findComponent(clientIdToResolve);

        // some component does not return correct client id (i.e. table) so try it by using parent component
        if (resolvedComponent == null && component.getParent() != null) {
            resolvedComponent = component.getParent().findComponent(clientIdToResolve);
        }

        if (resolvedComponent == null) {
            if (clientIdToResolve.charAt(0) == UINamingContainer.getSeparatorChar(FacesContext.getCurrentInstance())) {
                return clientIdToResolve.substring(1);
            }
            return clientIdToResolve;
        }

        return resolvedComponent.getClientId();
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
}
