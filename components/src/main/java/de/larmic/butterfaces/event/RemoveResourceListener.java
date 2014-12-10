package de.larmic.butterfaces.event;

import javax.faces.component.UIComponent;
import javax.faces.component.UIViewRoot;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.SystemEvent;
import javax.faces.event.SystemEventListener;
import java.util.ArrayList;
import java.util.Iterator;
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
    public static final String JQUERY_PREFIX_RESOURCE_IDENTIFIER = "jquery";
    public static final String PRETTYPRINT_PREFIX_RESOURCE_IDENTIFIER = "prettify";

    @Override
    public void processEvent(final SystemEvent event) throws AbortProcessingException {
        final FacesContext context = FacesContext.getCurrentInstance();
        final ExternalContext externalContext = context.getExternalContext();
        final boolean provideJQuery = this.readContextParameter(externalContext, CTX_PARAM_JQUERY);
        final boolean provideBootstrap = this.readContextParameter(externalContext, CTX_PARAM_BOOTSTRAP);
        final boolean providePrettyPrint = this.readContextParameter(externalContext, CTX_PARAM_PRETTYPRINT);

        final List<UIComponent> resources = new ArrayList<>(context.getViewRoot().getComponentResources(context, HEAD));

        // configurable resources
        UIComponent jqueryResource = null;
        UIComponent bootstrapJSResource = null;
        UIComponent bootstrapCSSResource = null;
        UIComponent prettyPrintResource = null;

        final Iterator<UIComponent> iterator = resources.iterator();

        // find all configurable resources
        while (iterator.hasNext()) {
            final UIComponent resource = iterator.next();

            final String resourceLibrary = (String) resource.getAttributes().get("library");
            final String resourceName = (String) resource.getAttributes().get("name");

            if (CONFIGURABLE_LIBRARY_NAME.equals(resourceLibrary)) {
                if (resourceName.startsWith(JQUERY_PREFIX_RESOURCE_IDENTIFIER)) {
                    jqueryResource = resource;
                    iterator.remove();
                } else if (resourceName.startsWith("bootstrap.min.css")) {
                    bootstrapCSSResource = resource;
                    iterator.remove();
                } else if (resourceName.startsWith("bootstrap.min.js")) {
                    bootstrapJSResource = resource;
                    iterator.remove();
                } else if (resourceName.startsWith(PRETTYPRINT_PREFIX_RESOURCE_IDENTIFIER)) {
                    prettyPrintResource = resource;
                    iterator.remove();
                }
            }

            // to accelerate listener break if all configurable resources are found
            if (jqueryResource != null && bootstrapCSSResource != null && bootstrapJSResource != null && prettyPrintResource != null) {
                break;
            }
        }

        // remove all resources from head
        for (UIComponent resource : context.getViewRoot().getComponentResources(context, HEAD)) {
            context.getViewRoot().removeComponentResource(context, resource, HEAD);
        }

        // add jquery
        if (jqueryResource != null && provideJQuery) {
            context.getViewRoot().addComponentResource(context, jqueryResource, HEAD);
        }

        // add bootstrap
        if (provideBootstrap) {
            if (bootstrapJSResource != null) {
                context.getViewRoot().addComponentResource(context, bootstrapJSResource, HEAD);
            }
            if (bootstrapCSSResource != null) {
                context.getViewRoot().addComponentResource(context, bootstrapCSSResource, HEAD);
            }
        }

        // add pretty print
        if (prettyPrintResource != null && providePrettyPrint) {
            context.getViewRoot().addComponentResource(context, prettyPrintResource, HEAD);
        }

        // add remaining resources
        for (UIComponent resource : resources) {
            context.getViewRoot().addComponentResource(context, resource, HEAD);
        }
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
