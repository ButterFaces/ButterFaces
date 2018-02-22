/*
 * Copyright Lars Michaelis and Stephan Zerhusen 2016.
 * Distributed under the MIT License.
 * (See accompanying file README.md file or copy at http://opensource.org/licenses/MIT)
 */
package org.butterfaces.resolver;

import org.butterfaces.util.StringUtils;
import org.butterfaces.util.StringUtils;

import javax.faces.component.UIComponent;
import javax.faces.component.UIForm;
import javax.faces.component.UINamingContainer;
import javax.faces.component.UIViewRoot;
import javax.faces.component.visit.VisitCallback;
import javax.faces.component.visit.VisitContext;
import javax.faces.component.visit.VisitResult;
import javax.faces.context.FacesContext;

/**
 * @author Lars Michaelis
 */
public class UIComponentResolver {

    public static String getFormClientId(UIComponent component, FacesContext context) {
        final UIForm form = getForm(component);
        return form != null ? form.getClientId(context) : null;
    }

    public static UIForm getForm(UIComponent component) {
        UIComponent parent = component.getParent();
        while (parent != null) {
            if (parent instanceof UIForm) {
                break;
            }
            parent = parent.getParent();
        }

        UIForm form = (UIForm) parent;
        if (form != null) {
            return form;
        }

        return null;
    }

    public String findComponentsClientId(final String id) {
        if (id.contains(UINamingContainer.getSeparatorChar(FacesContext.getCurrentInstance())+"")) {
            // we assume that client id is correct
            return id;
        }

        final UIComponent component = findComponent(id);
        return component != null ? component.getClientId() : id;
    }

    public UIComponent findComponent(final String id) {
        return findComponent(id, null);
    }

    public <T extends UIComponent> T findComponent(final String id, final Class<T> componentClass) {
        final FacesContext context = FacesContext.getCurrentInstance();
        final UIViewRoot root = context.getViewRoot();
        final UIComponent[] found = new UIComponent[1];

        final String clientId = checkClientId(context, id);

        root.visitTree(new VisitContextImpl(context), new VisitCallback() {
            @Override
            public VisitResult visit(VisitContext context, UIComponent component) {
                if(clientId.equals(component.getId()) && (componentClass == null || component.getClass().equals(componentClass))){
                    found[0] = component;
                    return VisitResult.COMPLETE;
                }
                return VisitResult.ACCEPT;
            }
        });

        return (T) found[0];
    }

    private String checkClientId(final FacesContext context, final String clientId) {
        return StringUtils.isNotEmpty(clientId) && clientId.charAt(0) == UINamingContainer.getSeparatorChar(context)
                ? clientId.substring(1)
                : clientId;
    }

}
