package de.larmic.butterfaces.event;

import de.larmic.butterfaces.resolver.WebXmlParameters;

import javax.faces.component.UIComponent;
import javax.faces.component.UIOutput;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.SystemEvent;
import javax.faces.event.SystemEventListener;
import java.util.ArrayList;
import java.util.List;

/**
 * Removes web.xml configurable resources (jquery, bootstrap and prettyprint). Provides a compresses and minified
 * resource if it is defined in web.xml.
 */
public class HandleResourceListener implements SystemEventListener {

    private static final String HEAD = "head";
    private static final String TARGET = "target";
    private static final String CONFIGURABLE_LIBRARY_NAME = "butterfaces-dist-bower";
    public static final String JQUERY_PREFIX_RESOURCE_IDENTIFIER = "jquery";
    public static final String BOOTSTRAP_PREFIX_RESOURCE_IDENTIFIER = "bootstrap";

    @Override
    public boolean isListenerForSource(Object source) {
        return (source instanceof UIViewRoot);
    }

    @Override
    public void processEvent(final SystemEvent event) throws AbortProcessingException {
        final FacesContext context = FacesContext.getCurrentInstance();
        final WebXmlParameters webXmlParameters = new WebXmlParameters(context.getExternalContext());
        final boolean provideJQuery = webXmlParameters.isProvideJQuery();
        final boolean provideBootstrap = webXmlParameters.isProvideBoostrap();
        final boolean useCompressedResources = webXmlParameters.isUseCompressedResources();

        final boolean localhost = "localhost".equals(context.getExternalContext().getRequestServerName());

        final List<UIComponent> resources =
                new ArrayList<>(context.getViewRoot().getComponentResources(context, HEAD));

        if (useCompressedResources && !localhost) {
            handleCompressedResources(context, provideJQuery, provideBootstrap, resources);
        } else {
            handleConfigurableResources(context, provideJQuery, provideBootstrap, resources);
        }
    }

    private void handleCompressedResources(FacesContext context,
                                           boolean provideJQuery,
                                           boolean provideBootstrap,
                                           List<UIComponent> resources) {
        removeAllResourcesFromViewRoot(context, resources);

        if (provideBootstrap && provideJQuery) {
            this.addGeneratedCSSResource(context, "dist-butterfaces-bootstrap.min.css");
            this.addGeneratedJSResource(context, "butterfaces-all-with-jquery-and-bootstrap-bundle.min.js", "butterfaces-dist-bundle-js");
        } else if (provideBootstrap) {
            this.addGeneratedCSSResource(context, "dist-butterfaces-bootstrap.min.css");
            this.addGeneratedJSResource(context, "butterfaces-all-with-bootstrap-bundle.min.js", "butterfaces-dist-bundle-js");
        } else if (provideJQuery) {
            this.addGeneratedCSSResource(context, "dist-butterfaces-only.min.css");
            this.addGeneratedJSResource(context, "butterfaces-all-with-jquery-bundle.min.js", "butterfaces-dist-bundle-js");
        } else {
            this.addGeneratedCSSResource(context, "dist-butterfaces-only.min.css");
            this.addGeneratedJSResource(context, "butterfaces-all-bundle.min.js", "butterfaces-dist-bundle-js");
        }
    }

    private void removeAllResourcesFromViewRoot(FacesContext context, List<UIComponent> resources) {
        for (UIComponent resource : resources) {
            final String resourceLibrary = (String) resource.getAttributes().get("library");

            if (resourceLibrary.startsWith("butterfaces")) {
                removeHeadResource(context, resource);
            }
        }
    }

    private void handleConfigurableResources(FacesContext context,
                                             boolean provideJQuery,
                                             boolean provideBootstrap,
                                             List<UIComponent> resources) {
        for (UIComponent resource : resources) {
            final String resourceLibrary = (String) resource.getAttributes().get("library");
            final String resourceName = (String) resource.getAttributes().get("name");
            boolean isResourceAccepted = true;

            if (CONFIGURABLE_LIBRARY_NAME.equals(resourceLibrary)) {
                if (!provideJQuery && resourceName.startsWith(JQUERY_PREFIX_RESOURCE_IDENTIFIER)) {
                    isResourceAccepted = false;
                } else if (!provideBootstrap && resourceName.startsWith(BOOTSTRAP_PREFIX_RESOURCE_IDENTIFIER)) {
                    isResourceAccepted = false;
                }
            }

            if (!isResourceAccepted) {
                context.getViewRoot().removeComponentResource(context, resource, HEAD);
                context.getViewRoot().removeComponentResource(context, resource, TARGET);
            }
        }
    }

    private void addGeneratedJSResource(final FacesContext context, final String resourceName, final String library) {
        addGeneratedResource(context, resourceName, "javax.faces.resource.Script", library);
    }

    private void addGeneratedCSSResource(final FacesContext context, final String resourceName) {
        addGeneratedResource(context, resourceName, "javax.faces.resource.Stylesheet", "butterfaces-dist-css");
    }

    private void addGeneratedResource(FacesContext context, String resourceName, String rendererType, String value) {
        final UIOutput resource = new UIOutput();
        resource.getAttributes().put("name", resourceName);
        resource.setRendererType(rendererType);
        resource.getAttributes().put("library", value);
        context.getViewRoot().addComponentResource(context, resource, HEAD);
    }

    private void removeHeadResource(FacesContext context, UIComponent resource) {
        context.getViewRoot().removeComponentResource(context, resource, HEAD);
        context.getViewRoot().removeComponentResource(context, resource, TARGET);
    }
}
