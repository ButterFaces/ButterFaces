package de.larmic.butterfaces.event;

import javax.faces.component.UIComponent;
import javax.faces.component.UIViewRoot;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.SystemEvent;
import javax.faces.event.SystemEventListener;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by larmic on 28.08.14.
 */
public class RemoveResourceListener implements SystemEventListener {

    private static final String HEAD = "head";
    private static final String CONFIGURABLE_LIBRARY_NAME = "butterfaces-configurable";
    public static final String CTX_PARAM_JQUERY = "de.larmic.butterfaces.provideJQuery";
    public static final String CTX_PARAM_BOOTSTRAP = "de.larmic.butterfaces.provideBootstrap";
    public static final String CTX_PARAM_PRETTYPRINT = "de.larmic.butterfaces.providePrettify";
    public static final String CTX_PARAM_SORT_JQUERY = "de.larmic.butterfaces.sortJQueryResourceToFirstPosition";
    public static final String JQUERY_PREFIX_RESOURCE_IDENTIFIER = "jquery";
    public static final String BOOTSTRAP_PREFIX_RESOURCE_IDENTIFIER = "bootstrap";
    public static final String PRETTYPRINT_PREFIX_RESOURCE_IDENTIFIER = "prettify";

    @Override
    public void processEvent(final SystemEvent event) throws AbortProcessingException {
        final FacesContext context = FacesContext.getCurrentInstance();
        final ExternalContext externalContext = context.getExternalContext();
        final boolean provideJQuery = this.readContextParameter(externalContext, CTX_PARAM_JQUERY);
        final boolean provideBootstrap = this.readContextParameter(externalContext, CTX_PARAM_BOOTSTRAP);
        final boolean providePrettyprint = this.readContextParameter(externalContext, CTX_PARAM_PRETTYPRINT);
        final boolean sortJQuery = this.readContextParameter(externalContext, CTX_PARAM_SORT_JQUERY);

        final List<UIComponent> resources = new ArrayList<>(context.getViewRoot().getComponentResources(context, HEAD));

        // put jquery on first position
        if (provideJQuery && sortJQuery) {
            final UIComponent jqueryResource = this.findAndRemoveJQueryResource(context, resources);

            if (jqueryResource != null) {
                context.getViewRoot().addComponentResource(context, jqueryResource, HEAD);

                for (UIComponent resource : resources) {
                    // remove it on some position
                    context.getViewRoot().removeComponentResource(context, resource, HEAD);
                    // add it on last position
                    context.getViewRoot().addComponentResource(context, resource, HEAD);
                }
            }
        }

        if (!provideJQuery || !provideBootstrap) {
            // Fetch included resources list size
            int i = resources.size() - 1;

            while (i >= 0) {
                // Fetch current resource from included resources list
                final UIComponent resource = resources.get(i);
                // Fetch resource library and resource name
                final String resourceLibrary = (String) resource.getAttributes().get("library");
                final String resourceName = (String) resource.getAttributes().get("name");

                if (CONFIGURABLE_LIBRARY_NAME.equals(resourceLibrary)) {
                    if (!provideJQuery && resourceName.startsWith(JQUERY_PREFIX_RESOURCE_IDENTIFIER)) {
                        // Remove resource from view
                        context.getViewRoot().removeComponentResource(context, resource, HEAD);
                    }
                    if (!provideBootstrap && resourceName.startsWith(BOOTSTRAP_PREFIX_RESOURCE_IDENTIFIER)) {
                        // Remove resource from view
                        context.getViewRoot().removeComponentResource(context, resource, HEAD);
                    }
                    if (!providePrettyprint && resourceName.startsWith(PRETTYPRINT_PREFIX_RESOURCE_IDENTIFIER)) {
                        // Remove resource from view
                        context.getViewRoot().removeComponentResource(context, resource, HEAD);
                    }
                }

                i--;
            }
        }
    }

    private UIComponent findAndRemoveJQueryResource(final FacesContext context,
                                                    final List<UIComponent> resources) {
        for (UIComponent resource : resources) {
            final String resourceLibrary = (String) resource.getAttributes().get("library");
            final String resourceName = (String) resource.getAttributes().get("name");
            if (CONFIGURABLE_LIBRARY_NAME.equals(resourceLibrary)
                    && resourceName.startsWith(JQUERY_PREFIX_RESOURCE_IDENTIFIER)) {
                context.getViewRoot().removeComponentResource(context, resource, HEAD);
                resources.remove(resource);
                return resource;
            }
        }

        return null;
    }

    private boolean readContextParameter(final ExternalContext externalContext, final String contextParameterName) {
        final String provideJQueryParam = externalContext.getInitParameter(contextParameterName);
        return Boolean.parseBoolean(provideJQueryParam == null ? Boolean.TRUE.toString() : provideJQueryParam);
    }

    @Override
    public boolean isListenerForSource(Object source) {
        return (source instanceof UIViewRoot);
    }
}
