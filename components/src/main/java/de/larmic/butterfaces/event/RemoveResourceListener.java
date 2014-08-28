package de.larmic.butterfaces.event;

import javax.faces.component.UIComponent;
import javax.faces.component.UIViewRoot;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.SystemEvent;
import javax.faces.event.SystemEventListener;

/**
 * Created by larmic on 28.08.14.
 */
public class RemoveResourceListener implements SystemEventListener {

    private static final String HEAD = "head";
    private static final String CONFIGURABLE_LIBRARY_NAME = "butterfaces-configurable";
    public static final String CTX_PARAM_JQUERY = "de.larmic.butterfaces.provideJQuery";

    @Override
    public void processEvent(SystemEvent event) throws AbortProcessingException {
        final FacesContext context = FacesContext.getCurrentInstance();
        final ExternalContext externalContext = context.getExternalContext();
        final String provideJQueryParam = externalContext.getInitParameter(CTX_PARAM_JQUERY);
        final boolean provideJQuery = Boolean.parseBoolean(provideJQueryParam == null ? Boolean.TRUE.toString() : provideJQueryParam);

        if (!provideJQuery) {
            // Fetch included resources list size
            int i = context.getViewRoot().getComponentResources(context, HEAD).size() - 1;

            while (i >= 0) {
                // Fetch current resource from included resources list
                final UIComponent resource = context.getViewRoot().getComponentResources(context, HEAD).get(i);
                // Fetch resource library and resource name
                final String resourceLibrary = (String) resource.getAttributes().get("library");
                final String resourceName = (String) resource.getAttributes().get("name");

                if (CONFIGURABLE_LIBRARY_NAME.equals(resourceLibrary) && resourceName.startsWith("jquery")) {
                    // Remove resource from view
                    context.getViewRoot().removeComponentResource(context, resource, HEAD);
                }

                i--;
            }
        }
    }

    @Override
    public boolean isListenerForSource(Object source) {
        return (source instanceof UIViewRoot);
    }
}
