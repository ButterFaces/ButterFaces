package de.larmic.butterfaces.handler;

import javax.faces.application.Resource;
import javax.faces.application.ResourceHandler;
import javax.faces.application.ResourceHandlerWrapper;

public class ButterFacesResourceHandler extends ResourceHandlerWrapper {

    private static final String CONFIGURABLE_LIBRARY_NAME = "butterfaces-configurable";

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
        if (CONFIGURABLE_LIBRARY_NAME.equals(libraryName)) {
            return new ButterFacesResource(resource);
        }

        return resource;
    }
}
