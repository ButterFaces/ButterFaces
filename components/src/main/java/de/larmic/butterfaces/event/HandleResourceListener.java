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
import java.util.Collections;
import java.util.List;

/**
 * Removes web.xml configurable resources (jquery, bootstrap and prettyprint). Provides a compresses and minified
 * resource if it is defined in web.xml.
 */
public class HandleResourceListener implements SystemEventListener {

    private static final String HEAD = "head";
    private static final String CONFIGURABLE_LIBRARY_NAME = "butterfaces-configurable";
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
            handleSingleResources(context, provideJQuery, provideBootstrap, resources);
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

    private void handleSingleResources(FacesContext context,
                                       boolean provideJQuery,
                                       boolean provideBootstrap,
                                       List<UIComponent> resources) {
        // the ordering of the resources is not supported in JSF spec, so we have to do it manually
        removeAllResourcesFromViewRoot(context, resources);


        final List<UIComponent> cleanedResources = removeDuplicates(resources);

        try {
            Collections.sort(cleanedResources, new ResourceComparator());
        } catch (IllegalArgumentException e) {
            System.out.println(e);
        }

        addResourcesToViewRoot(context, provideJQuery, provideBootstrap, resources);
    }

    /**
     * In case of localhost resource dependencies will be duplicated. Up to now I don't know the reason but post processing
     * does the job. Maybe mark processed resource dependency...
     */
    private List<UIComponent> removeDuplicates(List<UIComponent> resources) {
        final List<UIComponent> distinctResources = new ArrayList<>();

        for (UIComponent resource : resources) {
            boolean foundResource = false;

            for (UIComponent distinctResource : distinctResources) {
                if (resource.getAttributes().get("name").equals(distinctResource.getAttributes().get("name"))
                        && resource.getAttributes().get("library").equals(distinctResource.getAttributes().get("library"))) {
                    foundResource = true;
                    break;
                }
            }

            if (!foundResource) {
                distinctResources.add(resource);
            }
        }

        return distinctResources;
    }

    private void removeAllResourcesFromViewRoot(FacesContext context, List<UIComponent> resources) {
        for (UIComponent resource : resources) {
            final String resourceLibrary = (String) resource.getAttributes().get("library");

            if (resourceLibrary.startsWith("butterfaces")) {
                removeHeadResource(context, resource);
            }
        }
    }

    private void addResourcesToViewRoot(FacesContext context,
                                        boolean provideJQuery,
                                        boolean provideBootstrap,
                                        List<UIComponent> resources) {
        // if no compression is used each component will provide its own css and js resources
        // so remove configurable resources if not provide by application
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

            if (isResourceAccepted) {
                context.getViewRoot().addComponentResource(context, resource, HEAD);
            }
        }
    }

    private void addGeneratedJSResource(final FacesContext context,
                                        final String resourceName,
                                        final String library) {
        final UIOutput resource = new UIOutput();
        resource.getAttributes().put("name", resourceName);
        resource.setRendererType("javax.faces.resource.Script");
        resource.getAttributes().put("library", library);
        context.getViewRoot().addComponentResource(context, resource, HEAD);
    }

    private void addGeneratedCSSResource(final FacesContext context, final String resourceName) {
        final UIOutput resource = new UIOutput();
        resource.getAttributes().put("name", resourceName);
        resource.setRendererType("javax.faces.resource.Stylesheet");
        resource.getAttributes().put("library", "butterfaces-dist-css");
        context.getViewRoot().addComponentResource(context, resource, HEAD);
    }

    private void removeHeadResource(FacesContext context, UIComponent resource) {
        context.getViewRoot().removeComponentResource(context, resource, HEAD);
    }
}
