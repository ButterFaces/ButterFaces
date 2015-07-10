package de.larmic.butterfaces.event;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.faces.component.UIComponent;
import javax.faces.component.UIOutput;
import javax.faces.component.UIViewRoot;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.SystemEvent;
import javax.faces.event.SystemEventListener;

import de.larmic.butterfaces.resolver.WebXmlParameters;

/**
 * Removes web.xml configurable resources (jquery, bootstrap and prettyprint). Provides a compresses and minified
 * resource if it is defined in web.xml.
 */
public class HandleResourceListener implements SystemEventListener {

   private static final String HEAD = "head";
   private static final String CONFIGURABLE_LIBRARY_NAME = "butterfaces-configurable";
   public static final String JQUERY_PREFIX_RESOURCE_IDENTIFIER = "jquery";
   public static final String BOOTSTRAP_PREFIX_RESOURCE_IDENTIFIER = "bootstrap";
   public static final String PRETTYPRINT_PREFIX_RESOURCE_IDENTIFIER = "prettify";

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
      final boolean providePrettyPrint = webXmlParameters.isProvidePrettyprint();
      final boolean useCompressedResources = webXmlParameters.isUseCompressedResources();

      final boolean localhost = "localhost".equals(context.getExternalContext().getRequestServerName());

      final List<UIComponent> resources =
            new ArrayList<>(context.getViewRoot().getComponentResources(context, HEAD));

      if (useCompressedResources && !localhost) {
         handleCompressedResources(context, provideJQuery, provideBootstrap, providePrettyPrint, resources);
      } else {
         handleSingleResources(context, provideJQuery, provideBootstrap, providePrettyPrint, resources);
      }
   }

   private void handleCompressedResources(FacesContext context, boolean provideJQuery, boolean provideBootstrap,
                                          boolean providePrettyPrint, List<UIComponent> resources) {
      for (UIComponent resource : resources) {
         final String resourceLibrary = (String) resource.getAttributes().get("library");
         final String resourceName = (String) resource.getAttributes().get("name");

         if (resourceLibrary.startsWith("butterfaces-configurable")) {
            final boolean handlePrettyPrintResource = providePrettyPrint && (!provideJQuery || !provideBootstrap);
            final boolean ignorePrettyPringResource =
                  handlePrettyPrintResource && resourceName.startsWith(PRETTYPRINT_PREFIX_RESOURCE_IDENTIFIER);
            final boolean handleBootStrapResource = provideBootstrap && !provideJQuery;
            final boolean ignoreBootStrapResource =
                  handleBootStrapResource && resourceName.startsWith(BOOTSTRAP_PREFIX_RESOURCE_IDENTIFIER);

            // minified bootstrap without jquery is not supported so do not remove it
            // minified prettyprint without jquery or bootstrap is not supported so do not remove it
            final boolean ignoreResourceRemoval = ignorePrettyPringResource || ignoreBootStrapResource;

            if (!ignoreResourceRemoval) {
               removeHeadResource(context, resource);
            }
         } else if (resourceLibrary.startsWith("butterfaces")) {
            // remove all resources coming from each component except butterfaces-configurable (handled above)
            // compressed and minified resoources will be added later
            removeHeadResource(context, resource);
         }
      }

      if (provideBootstrap && provideJQuery && providePrettyPrint) {
         this.addGeneratedCSSResource(context, "butterfaces-all.min.css");
         this.addGeneratedJSResource(context, "butterfaces-all.min.js");
      } else if (provideJQuery && provideBootstrap) {
         this.addGeneratedCSSResource(context, "butterfaces-jquery-bootstrap.min.css");
         this.addGeneratedJSResource(context, "butterfaces-jquery-bootstrap.min.js");
      } else if (provideJQuery) {
         this.addGeneratedCSSResource(context, "butterfaces-jquery.min.css");
         this.addGeneratedJSResource(context, "butterfaces-jquery.min.js");
      } else {
         this.addGeneratedCSSResource(context, "butterfaces-only.min.css");
         this.addGeneratedJSResource(context, "butterfaces-only.min.js");
      }
   }

   private void handleSingleResources(FacesContext context, boolean provideJQuery, boolean provideBootstrap,
                                      boolean providePrettyPrint, List<UIComponent> resources) {
      // the ordering of the resources is not supported in JSF spec, so we have to do it manually
      removeAllResourcesFromViewRoot(context, resources);
      Collections.sort(resources, new ResourceComparator());
      addResourcesToViewRoot(context, provideJQuery, provideBootstrap, providePrettyPrint, resources);
   }

   private void removeAllResourcesFromViewRoot(FacesContext context, List<UIComponent> resources) {
      for (UIComponent resource : resources) {
         removeHeadResource(context, resource);
      }
   }

   private void addResourcesToViewRoot(FacesContext context, boolean provideJQuery, boolean provideBootstrap,
                                       boolean providePrettyPrint, List<UIComponent> resources) {
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
            } else if (!providePrettyPrint && resourceName.startsWith(PRETTYPRINT_PREFIX_RESOURCE_IDENTIFIER)) {
               isResourceAccepted = false;
            }
         }

         if (isResourceAccepted) {
            context.getViewRoot().addComponentResource(context, resource, HEAD);
         }
      }
   }

   private void addGeneratedJSResource(final FacesContext context, final String resourceName) {
      final UIOutput resource = new UIOutput();
      resource.getAttributes().put("name", resourceName);
      resource.setRendererType("javax.faces.resource.Script");
      resource.getAttributes().put("library", "butterfaces-generated");
      context.getViewRoot().addComponentResource(context, resource, HEAD);
   }

   private void addGeneratedCSSResource(final FacesContext context, final String resourceName) {
      final UIOutput resource = new UIOutput();
      resource.getAttributes().put("name", resourceName);
      resource.setRendererType("javax.faces.resource.Stylesheet");
      resource.getAttributes().put("library", "butterfaces-generated");
      context.getViewRoot().addComponentResource(context, resource, HEAD);
   }

   /**
    * Reads boolean context parameter from web.xml. If context parameter is not set Boolean.TRUE will be returned.
    *
    * @param externalContext      external context from faces context
    * @param contextParameterName web.xml context parameter name
    *
    * @return true if context is not set
    */
   private boolean readContextParameter(final ExternalContext externalContext, final String contextParameterName) {
      final String provideJQueryParam = externalContext.getInitParameter(contextParameterName);
      return Boolean.parseBoolean(provideJQueryParam == null ? Boolean.TRUE.toString() : provideJQueryParam);
   }

   private void removeHeadResource(FacesContext context, UIComponent resource) {
      context.getViewRoot().removeComponentResource(context, resource, HEAD);
   }
}
