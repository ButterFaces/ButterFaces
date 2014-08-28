package de.larmic.butterfaces.handler;

import javax.faces.application.Resource;
import javax.faces.application.ResourceHandler;
import javax.faces.application.ResourceHandlerWrapper;

/**
 * Adds a timestamp parameter to each (javascript and css) resource if resource handler is enabled. Resource browser
 * cache will be ignored on this resources until server will be redeployed. In case of this manual browser cache delete
 * is no longer needed.
 * <p/>
 * Usage:
 * Add following code to your faces-config.xml
 * <code>
 * <resource-handler>de.larmic.butterfaces.handler.ButterFacesResourceHandler</resource-handler>
 * </code>
 */
public class ButterFacesResourceHandler extends ResourceHandlerWrapper {


    private static final String LIBRARY_CSS_NAME = "css";
    private static final String LIBRARY_JS_NAME = "js";

    private ResourceHandler wrapped;

    public ButterFacesResourceHandler(ResourceHandler wrapped) {
        this.wrapped = wrapped;
    }

    @Override
    public ResourceHandler getWrapped() {
        return this.wrapped;
    }

    @Override
    public Resource createResource(String resourceName, String libraryName) {
        final Resource resource = super.createResource(resourceName, libraryName);

        // here a check of library name could be necessary, etc.
        if (LIBRARY_CSS_NAME.equals(libraryName) || LIBRARY_JS_NAME.equals(libraryName)) {
            return new ButterFacesResource(resource);
        }

        return resource;
    }
}
