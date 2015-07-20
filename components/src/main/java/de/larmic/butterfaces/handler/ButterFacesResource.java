package de.larmic.butterfaces.handler;

import javax.faces.application.Resource;

/**
 * Simple {@link javax.faces.application.ResourceWrapper} to add a prevent cache that resets resources after deployeing
 * a new artifact.
 */
public class ButterFacesResource extends javax.faces.application.ResourceWrapper {

	// current revision
    private static final long REVISION = System.currentTimeMillis();
	
	private Resource resource;

	public ButterFacesResource(Resource resource) {
		this.resource = resource;
	}

	@Override
	public Resource getWrapped() {
		return this.resource;
	}

	@Override
    public String getRequestPath() {
        String requestPath = resource.getRequestPath();

        if(requestPath.contains("?")) {
            requestPath = requestPath + "&preventCache=" + REVISION;
        }else{
            requestPath = requestPath + "?preventCache=" + REVISION;
        }
            
        return requestPath;
    }

	@Override
	public String getContentType() {
		return getWrapped().getContentType();
	}
}
