/**
 *
 */
package de.larmic.butterfaces.event;

import de.larmic.butterfaces.resolver.WebXmlParameters;

import javax.faces.application.ProjectStage;
import javax.faces.component.UIComponent;
import javax.faces.component.UIOutput;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.SystemEvent;
import javax.faces.event.SystemEventListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Removes web.xml configurable resources (jquery, bootstrap and prettyprint).
 * Provides a compresses and minified resource if it is defined in web.xml.
 */
public class HandleResourceListener implements SystemEventListener {
    private static final String HEAD = "head";
    private static final String TARGET = "target";
    private static final String CONFIGURABLE_LIBRARY_NAME = "butterfaces-dist-bower";
    public static final String JQUERY_PREFIX_RESOURCE_IDENTIFIER = "jquery";
    public static final String BOOTSTRAP_PREFIX_RESOURCE_IDENTIFIER = "bootstrap";

    /**
     * Just for the view root
     */
    @Override
    public boolean isListenerForSource(Object source) {
        return source instanceof UIViewRoot;
    }

    /**
     * Process event. Just the first time.
     */
    @Override
    public void processEvent(SystemEvent event) throws AbortProcessingException {
        final UIViewRoot source = (UIViewRoot) event.getSource();

        final FacesContext context = FacesContext.getCurrentInstance();
        final WebXmlParameters webXmlParameters = new WebXmlParameters(context.getExternalContext());
        final boolean provideJQuery = webXmlParameters.isProvideJQuery();
        final boolean provideBootstrap = webXmlParameters.isProvideBoostrap();
        final boolean useCompressedResources = webXmlParameters.isUseCompressedResources();
        final boolean disablePrimeFacesJQuery = webXmlParameters.isIntegrationPrimeFacesDisableJQuery();

        final List<UIComponent> resources = new ArrayList<>(source.getComponentResources(context, HEAD));
        // Production mode and compressed
        if (useCompressedResources && context.getApplication().getProjectStage() == ProjectStage.Production) {
            handleCompressedResources(context, provideJQuery, provideBootstrap, resources, source);
        } else {
            handleConfigurableResources(context, provideJQuery, provideBootstrap, resources, source);
        }

        if (disablePrimeFacesJQuery) {
            for (UIComponent resource : resources) {
                final String resourceLibrary = (String) resource.getAttributes().get("library");
                final String resourceName = (String) resource.getAttributes().get("name");
                if ("primefaces".equals(resourceLibrary) && "jquery/jquery.js".equals(resourceName)) {
                    source.removeComponentResource(context, resource);
                }
            }
        }
    }

    /**
     * Use compressed resources.
     */
    private void handleCompressedResources(final FacesContext context,
                                           final boolean provideJQuery,
                                           final boolean provideBootstrap,
                                           final List<UIComponent> resources,
                                           final UIViewRoot view) {
        removeAllResourcesFromViewRoot(context, resources, view);

        if (provideBootstrap && provideJQuery) {
            this.addGeneratedCSSResource(context, "dist-butterfaces-bootstrap.min.css", view);
            this.addGeneratedJSResource(context, "butterfaces-all-with-jquery-and-bootstrap-bundle.min.js",
                    "butterfaces-dist-bundle-js", view);
        } else if (provideBootstrap) {
            this.addGeneratedCSSResource(context, "dist-butterfaces-bootstrap.min.css", view);
            this.addGeneratedJSResource(context, "butterfaces-all-with-bootstrap-bundle.min.js",
                    "butterfaces-dist-bundle-js", view);
        } else if (provideJQuery) {
            this.addGeneratedCSSResource(context, "dist-butterfaces-only.min.css", view);
            this.addGeneratedJSResource(context, "butterfaces-all-with-jquery-bundle.min.js",
                    "butterfaces-dist-bundle-js", view);
        } else {
            this.addGeneratedCSSResource(context, "dist-butterfaces-only.min.css", view);
            this.addGeneratedJSResource(context, "butterfaces-all-bundle.min.js", "butterfaces-dist-bundle-js", view);
        }

        // butterfaces first, and then libraries from the project to override css and js
        for (UIComponent resource : resources) {
            context.getViewRoot().addComponentResource(context, resource, HEAD);
        }
    }

    /**
     * Remove resources from the view.
     */
    private void removeAllResourcesFromViewRoot(final FacesContext context,
                                                final List<UIComponent> resources,
                                                final UIViewRoot view) {
        final Iterator<UIComponent> it = resources.iterator();

        while (it.hasNext()) {
            final UIComponent resource = it.next();
            final String resourceLibrary = (String) resource.getAttributes().get("library");

            removeResource(context, resource, view);
            if (resourceLibrary != null && resourceLibrary.startsWith("butterfaces"))
                it.remove();
        }
    }

    /**
     * Use normal resources
     */
    private void handleConfigurableResources(FacesContext context,
                                             boolean provideJQuery,
                                             boolean provideBootstrap,
                                             List<UIComponent> resources,
                                             UIViewRoot view) {
        boolean isResourceAccepted;

        for (UIComponent resource : resources) {
            final String resourceLibrary = (String) resource.getAttributes().get("library");
            final String resourceName = (String) resource.getAttributes().get("name");
            isResourceAccepted = true;

            if (resourceName != null && CONFIGURABLE_LIBRARY_NAME.equals(resourceLibrary)) {
                if (!provideJQuery && resourceName.startsWith(JQUERY_PREFIX_RESOURCE_IDENTIFIER)) {
                    isResourceAccepted = false;
                } else if (!provideBootstrap && resourceName.startsWith(BOOTSTRAP_PREFIX_RESOURCE_IDENTIFIER)) {
                    isResourceAccepted = false;
                }
            }

            if (!isResourceAccepted)
                removeResource(context, resource, view);
        }
    }

    /**
     * Add a new JS resource.
     */
    private void addGeneratedJSResource(FacesContext context, String resourceName, String library, UIViewRoot view) {
        addGeneratedResource(context, resourceName, "javax.faces.resource.Script", library, view);
    }

    /**
     * Add a new css resource.
     */
    private void addGeneratedCSSResource(FacesContext context, String resourceName, UIViewRoot view) {
        addGeneratedResource(context, resourceName, "javax.faces.resource.Stylesheet", "butterfaces-dist-css", view);
    }

    /**
     * Add a new resource.
     */
    private void addGeneratedResource(FacesContext context, String resourceName, String rendererType, String value,
                                      UIViewRoot view) {
        final UIOutput resource = new UIOutput();
        resource.getAttributes().put("name", resourceName);
        resource.setRendererType(rendererType);
        resource.getAttributes().put("library", value);
        view.addComponentResource(context, resource, HEAD);
    }

    /**
     * Remove resource from head and target.
     */
    private void removeResource(FacesContext context, UIComponent resource, UIViewRoot view) {
        view.removeComponentResource(context, resource, HEAD);
        view.removeComponentResource(context, resource, TARGET);
    }
}
