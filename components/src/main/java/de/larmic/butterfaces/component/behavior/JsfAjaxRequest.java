package de.larmic.butterfaces.component.behavior;

import de.larmic.butterfaces.component.partrenderer.StringUtils;

import javax.faces.component.UIComponent;
import javax.faces.component.UIComponentBase;
import javax.faces.component.UINamingContainer;
import javax.faces.component.behavior.AjaxBehavior;
import javax.faces.component.behavior.ClientBehavior;
import javax.faces.context.FacesContext;
import java.util.*;

import static de.larmic.butterfaces.component.partrenderer.StringUtils.isNotEmpty;
import static java.util.Objects.requireNonNull;

/**
 * Builds an ajax request call that depends on the JSF Javascript API under https://docs.oracle.com/cd/E17802_01/j2ee/javaee/javaserverfaces/2.0/docs/js-api/symbols/jsf.ajax.html#.request
 */
public class JsfAjaxRequest {

    private final String source;
    private String event;
    private String execute;
    private String render;
    private List<String> onEventHandlers = new ArrayList<>();
    private List<String> onErrorHandlers = new ArrayList<>();
    private String params;
    private String behaviorEvent;

    /**
     * Constructor.
     *
     * @param source     the DOM element that triggered this Ajax request, or an id string of the element to use as the triggering
     *                   element
     * @param isIdString if set to <code>true</code> the source string will be wrapped in ''
     */
    public JsfAjaxRequest(String source, boolean isIdString) {
        requireNonNull(source, "source attribute may not be empty!");
        if (isIdString) {
            this.source = "'" + source + "'";
        } else {
            this.source = source;
        }
    }

    public JsfAjaxRequest(String source, boolean isIdString, AjaxBehavior ajaxBehavior, String event) {
        this(source, isIdString);

        if (!ajaxBehavior.isDisabled()) {
            if (ajaxBehavior.getExecute() != null) {
                this.setExecute(StringUtils.joinWithSpaceSeparator(ajaxBehavior.getExecute()));
            }
            if (ajaxBehavior.getRender() != null) {
                this.setRenderAsList(ajaxBehavior.getRender());
            }
            if (ajaxBehavior.getOnevent() != null) {
                this.addOnEventHandler(ajaxBehavior.getOnevent());
            }
            if (ajaxBehavior.getOnerror() != null) {
                this.addOnErrorHandler(ajaxBehavior.getOnerror());
            }
            this.setEvent(event);
            this.setBehaviorEvent(event);
        }
    }

    /**
     * @param event The DOM event that triggered this Ajax request.
     * @return the actual instance of {@link JsfAjaxRequest}
     */
    public JsfAjaxRequest setEvent(String event) {
        this.event = event;
        return this;
    }

    /**
     * @param execute space seperated list of client identifiers
     * @return the actual instance of {@link JsfAjaxRequest}
     */
    public JsfAjaxRequest setExecute(String execute) {
        this.execute = execute;
        return this;
    }

    /**
     * @param render space seperated list of client identifiers
     * @return the actual instance of {@link JsfAjaxRequest}
     */
    public JsfAjaxRequest setRender(String render) {
        this.render = render;
        return this;
    }

    public JsfAjaxRequest setRender(final UIComponentBase component, final String eventName) {
        this.render = StringUtils.joinWithSpaceSeparator(createRerenderIds(component, eventName));
        return this;
    }

    // Returns the resolved (client id) for a particular id.
    public static String getResolvedId(final UIComponent component, final String id) {
        if (id.equals("@all") || id.equals("@none") || id.equals("@form") || id.equals("@this")) {
            return id;
        }

        UIComponent resolvedComponent = component.findComponent(id);
        if (resolvedComponent == null) {
            final FacesContext context = FacesContext.getCurrentInstance();
            if (context != null && id.charAt(0) == UINamingContainer.getSeparatorChar(context)) {
                return id.substring(1);
            }
            return id;
        }

        return resolvedComponent.getClientId();
    }

    /**
     * @param renderIds list of client identifiers
     * @return the actual instance of {@link JsfAjaxRequest}
     */
    public JsfAjaxRequest setRenderAsList(final Collection<String> renderIds) {
        final StringBuilder renderBuilder = new StringBuilder();

        if (!renderIds.isEmpty()) {
            final Iterator<String> iterator = renderIds.iterator();
            while (iterator.hasNext()) {
                renderBuilder.append(iterator.next());

                if (iterator.hasNext()) {
                    renderBuilder.append(" ");
                }
            }
            render = renderBuilder.toString();
        }
        return this;
    }

    public JsfAjaxRequest addRender(final String render) {
        if (StringUtils.isNotEmpty(render)) {
            this.render = this.render == null ? render : this.render + " " + render;
        }
        return this;
    }

    /**
     * @param params object containing parameters to include in the request
     * @return the actual instance of {@link JsfAjaxRequest}
     */
    public JsfAjaxRequest setParams(String params) {
        this.params = params;
        return this;
    }

    /**
     * @param behaviorEvent ??
     * @return the actual instance of {@link JsfAjaxRequest}
     */
    public JsfAjaxRequest setBehaviorEvent(String behaviorEvent) {
        this.behaviorEvent = behaviorEvent;
        return this;
    }

    /**
     * @param functionString function to callback for event
     * @return the actual instance of {@link JsfAjaxRequest}
     */
    public JsfAjaxRequest addOnEventHandler(String functionString) {
        if (StringUtils.isNotEmpty(functionString)) {
            onEventHandlers.add(functionString);
        }
        return this;
    }

    /**
     * @param functionString function to callback for error
     * @return the actual instance of {@link JsfAjaxRequest}
     */
    public JsfAjaxRequest addOnErrorHandler(String functionString) {
        if (StringUtils.isNotEmpty(functionString)) {
            onErrorHandlers.add(functionString);
        }
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
                sb.append("params: ").append(params);
                isAtLeastOneOptionSet = true;
            }

            if (isNotEmpty(behaviorEvent)) {
                writeSeparatorIfNecessary(sb, isAtLeastOneOptionSet);
                sb.append("'javax.faces.behavior.event': '").append(behaviorEvent).append("'");
            }

            sb.append("}");
        }
        sb.append(");");
        return sb.toString();
    }

    public static AjaxBehavior findFirstActiveAjaxBehavior(final UIComponentBase component, final String eventName) {
        if (component != null) {
            final List<ClientBehavior> behaviors = component.getClientBehaviors().get(eventName);
            if (behaviors != null) {
                for (ClientBehavior behavior : behaviors) {
                    if (behavior instanceof AjaxBehavior && !((AjaxBehavior) behavior).isDisabled()) {
                        return (AjaxBehavior) behavior;
                    }
                }
            }
        }

        return null;
    }

    public static List<String> createRerenderIds(final UIComponentBase component, final String eventName) {
        final List<String> idsToRender = new ArrayList<>();
        final Map<String, List<ClientBehavior>> behaviors = component.getClientBehaviors();
        final List<ClientBehavior> refreshBehaviors = behaviors.get(eventName);

        if (refreshBehaviors == null) {
            return new ArrayList<>();
            //throw new IllegalArgumentException("Ajax event '" + eventName + "' not found on component '" + component.getClientId() + "'.");
        }

        boolean enabledAjaxEventFound = false;

        if (!refreshBehaviors.isEmpty()) {
            for (ClientBehavior refreshBehavior : refreshBehaviors) {
                if (refreshBehavior instanceof AjaxBehavior) {
                    final AjaxBehavior ajaxBehavior = (AjaxBehavior) refreshBehavior;

                    if (!ajaxBehavior.isDisabled()) {
                        if (ajaxBehavior.getRender() != null && !ajaxBehavior.getRender().isEmpty()) {
                            for (String singleRender : ajaxBehavior.getRender()) {
                                idsToRender.add(getResolvedId(component, singleRender));
                            }
                        }

                        enabledAjaxEventFound = true;
                    }
                }
            }
        }

        if (!enabledAjaxEventFound) {
            return new ArrayList<>();
            //throw new IllegalStateException("Ajax event '" + eventName + "' on component '" + component.getClientId() + "' is disabled.");
        }

        return idsToRender;
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
                || isNotEmpty(params)
                || isNotEmpty(behaviorEvent);
    }
}
