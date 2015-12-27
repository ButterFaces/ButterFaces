/**
 * 
 */
package de.larmic.butterfaces.event;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.faces.application.ProjectStage;
import javax.faces.component.UIComponent;
import javax.faces.component.UIOutput;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.SystemEvent;
import javax.faces.event.SystemEventListener;

import de.larmic.butterfaces.resolver.WebXmlParameters;

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
	public static final String ATTRIBUTE_RESOURCE_MODIFIED = "RESOURCE_MODIFIED";

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
		UIViewRoot source;
		FacesContext context;
		WebXmlParameters webXmlParameters;
		boolean provideJQuery;
		boolean provideBootstrap;
		boolean useCompressedResources;
		List<UIComponent> resources;

		source = (UIViewRoot) event.getSource();
		// First render view only
		if (!source.getAttributes().containsKey(ATTRIBUTE_RESOURCE_MODIFIED)) {
			context = FacesContext.getCurrentInstance();
			webXmlParameters = new WebXmlParameters(context.getExternalContext());
			provideJQuery = webXmlParameters.isProvideJQuery();
			provideBootstrap = webXmlParameters.isProvideBoostrap();
			useCompressedResources = webXmlParameters.isUseCompressedResources();

			resources = new ArrayList<>(source.getComponentResources(context, HEAD));
			// Production mode and compressed
			if (useCompressedResources && context.getApplication().getProjectStage() == ProjectStage.Production)
				handleCompressedResources(context, provideJQuery, provideBootstrap, resources, source);
			else
				handleConfigurableResources(context, provideJQuery, provideBootstrap, resources, source);
			source.getAttributes().put(ATTRIBUTE_RESOURCE_MODIFIED, true);
		}
	}

	/**
	 * Use compressed resources
	 * 
	 * @param context
	 * @param provideJQuery
	 * @param provideBootstrap
	 * @param resources
	 * @param view
	 */
	private void handleCompressedResources(FacesContext context, boolean provideJQuery, boolean provideBootstrap,
			List<UIComponent> resources, UIViewRoot view) {
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

		// butterfaces first, and then libraries from the project to override
		// css and js
		for (UIComponent resource : resources) {
			context.getViewRoot().addComponentResource(context, resource, HEAD);
		}
	}

	/**
	 * Remove resources from the view.
	 * 
	 * @param context
	 * @param resources
	 * @param view
	 */
	private void removeAllResourcesFromViewRoot(FacesContext context, List<UIComponent> resources, UIViewRoot view) {
		String resourceLibrary;
		Iterator<UIComponent> it;
		UIComponent resource;

		it = resources.iterator();
		while (it.hasNext()) {
			resource = it.next();
			resourceLibrary = (String) resource.getAttributes().get("library");

			removeHeadResource(context, resource, view);
			if (resourceLibrary != null && resourceLibrary.startsWith("butterfaces"))
				it.remove();
		}
	}

	/**
	 * Use normal resources
	 * 
	 * @param context
	 * @param provideJQuery
	 * @param provideBootstrap
	 * @param resources
	 * @param view
	 */
	private void handleConfigurableResources(FacesContext context, boolean provideJQuery, boolean provideBootstrap,
			List<UIComponent> resources, UIViewRoot view) {
		String resourceLibrary;
		String resourceName;
		boolean isResourceAccepted;

		for (UIComponent resource : resources) {
			resourceLibrary = (String) resource.getAttributes().get("library");
			resourceName = (String) resource.getAttributes().get("name");
			isResourceAccepted = true;

			if (resourceName != null && CONFIGURABLE_LIBRARY_NAME.equals(resourceLibrary)) {
				if (!provideJQuery && resourceName.startsWith(JQUERY_PREFIX_RESOURCE_IDENTIFIER)) {
					isResourceAccepted = false;
				} else if (!provideBootstrap && resourceName.startsWith(BOOTSTRAP_PREFIX_RESOURCE_IDENTIFIER)) {
					isResourceAccepted = false;
				}
			}

			if (!isResourceAccepted)
				removeHeadResource(context, resource, view);
		}
	}

	/**
	 * Add a new JS resource
	 * 
	 * @param context
	 * @param resourceName
	 * @param library
	 * @param view
	 */
	private void addGeneratedJSResource(FacesContext context, String resourceName, String library, UIViewRoot view) {
		addGeneratedResource(context, resourceName, "javax.faces.resource.Script", library, view);
	}

	/**
	 * Add a new css resource
	 * 
	 * @param context
	 * @param resourceName
	 * @param view
	 */
	private void addGeneratedCSSResource(FacesContext context, String resourceName, UIViewRoot view) {
		addGeneratedResource(context, resourceName, "javax.faces.resource.Stylesheet", "butterfaces-dist-css", view);
	}

	/**
	 * Add a new resource
	 * 
	 * @param context
	 * @param resourceName
	 * @param rendererType
	 * @param value
	 * @param view
	 */
	private void addGeneratedResource(FacesContext context, String resourceName, String rendererType, String value,
			UIViewRoot view) {
		UIOutput resource;

		resource = new UIOutput();
		resource.getAttributes().put("name", resourceName);
		resource.setRendererType(rendererType);
		resource.getAttributes().put("library", value);
		view.addComponentResource(context, resource, HEAD);
	}

	/**
	 * Remove resource from head
	 * 
	 * @param context
	 * @param resource
	 * @param view
	 */
	private void removeHeadResource(FacesContext context, UIComponent resource, UIViewRoot view) {
		view.removeComponentResource(context, resource, HEAD);
		view.removeComponentResource(context, resource, TARGET);
	}
}
