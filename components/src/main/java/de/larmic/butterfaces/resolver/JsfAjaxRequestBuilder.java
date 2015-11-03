package de.larmic.butterfaces.resolver;

import java.util.ArrayList;
import java.util.List;

import static de.larmic.butterfaces.component.partrenderer.StringUtils.isNotEmpty;
import static java.util.Objects.requireNonNull;

/**
 * Builds an ajax request call that depends on the JSF Javascript API under https://docs.oracle.com/cd/E17802_01/j2ee/javaee/javaserverfaces/2.0/docs/js-api/symbols/jsf.ajax.html#.request
 */
public class JsfAjaxRequestBuilder {

   private final String source;
   private String event;
   private String execute;
   private String render;
   private List<String> onEventHandlers = new ArrayList<>();
   private List<String> onErrorHandlers = new ArrayList<>();
   private String params;

   /**
    * Constructor.
    *
    * @param source     the DOM element that triggered this Ajax request, or an id string of the element to use as the triggering
    *                   element
    * @param isIdString if set to <code>true</code> the source string will be wrapped in ''
    */
   public JsfAjaxRequestBuilder(String source, boolean isIdString) {
      requireNonNull(source, "source attribute may not be empty!");
      if (isIdString) {
         this.source = "'" + source + "'";
      } else {
         this.source = source;
      }
   }

   /**
    * @param event The DOM event that triggered this Ajax request.
    *
    * @return the actual instance of {@link JsfAjaxRequestBuilder}
    */
   public JsfAjaxRequestBuilder setEvent(String event) {
      this.event = event;
      return this;
   }

   /**
    * @param execute space seperated list of client identifiers
    *
    * @return the actual instance of {@link JsfAjaxRequestBuilder}
    */
   public JsfAjaxRequestBuilder setExecute(String execute) {
      this.execute = execute;
      return this;
   }

   /**
    * @param render space seperated list of client identifiers
    *
    * @return the actual instance of {@link JsfAjaxRequestBuilder}
    */
   public JsfAjaxRequestBuilder setRender(String render) {
      this.render = render;
      return this;
   }

   /**
    * @param params object containing parameters to include in the request
    *
    * @return the actual instance of {@link JsfAjaxRequestBuilder}
    */
   public JsfAjaxRequestBuilder setParams(String params) {
      this.params = params;
      return this;
   }

   /**
    * @param functionString function to callback for event
    *
    * @return the actual instance of {@link JsfAjaxRequestBuilder}
    */
   public JsfAjaxRequestBuilder addOnEventHandler(String functionString) {
      onEventHandlers.add(functionString);
      return this;
   }

   /**
    * @param functionString function to callback for error
    *
    * @return the actual instance of {@link JsfAjaxRequestBuilder}
    */
   public JsfAjaxRequestBuilder addOnErrorHandler(String functionString) {
      onErrorHandlers.add(functionString);
      return this;
   }

   @Override
   public String toString() {
      final StringBuilder sb = new StringBuilder("jsf.ajax.request(");
      sb.append(source);
      if (isNotEmpty(event)) {
         sb.append(", '").append(event).append("'");
      }
      if (hasOptions()) {
         sb.append(", {");

         boolean isAtLeastOneOptionSet = false;
         if (isNotEmpty(execute)) {
            sb.append("execute: '").append(execute).append("'");
            isAtLeastOneOptionSet = true;
         }

         if (isNotEmpty(render)) {
            writeSeparatorIfNecessary(sb, isAtLeastOneOptionSet);
            sb.append("render: '").append(render).append("'");
            isAtLeastOneOptionSet = true;
         }

         if (!onEventHandlers.isEmpty()) {
            writeSeparatorIfNecessary(sb, isAtLeastOneOptionSet);
            sb.append("onevent: ").append(renderFunctionCalls(onEventHandlers));
            isAtLeastOneOptionSet = true;
         }

         if (!onErrorHandlers.isEmpty()) {
            writeSeparatorIfNecessary(sb, isAtLeastOneOptionSet);
            sb.append("onerror: ").append(renderFunctionCalls(onErrorHandlers));
            isAtLeastOneOptionSet = true;
         }

         if (isNotEmpty(params)) {
            writeSeparatorIfNecessary(sb, isAtLeastOneOptionSet);
            sb.append("params: '").append(params).append("'");
         }

         sb.append("}");
      }
      sb.append(");");
      return sb.toString();
   }

   private void writeSeparatorIfNecessary(StringBuilder sb, boolean isAtLeastOneOptionSet) {
      if (isAtLeastOneOptionSet) {
         sb.append(", ");
      }
   }

   private String renderFunctionCalls(List<String> functions) {
      final StringBuilder sb = new StringBuilder("function(data){");
      for (String function : functions) {
         sb.append(function);
         if (!function.contains(")")) {
            // this is not a function call, so call it manually
            sb.append("(data)");
         }
         sb.append(";");
      }
      sb.append("}");
      return sb.toString();
   }

   private boolean hasOptions() {
      return isNotEmpty(execute)
            || isNotEmpty(render)
            || !onEventHandlers.isEmpty()
            || !onErrorHandlers.isEmpty()
            || isNotEmpty(params);
   }
}
