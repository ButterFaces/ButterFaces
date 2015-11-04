package de.larmic.butterfaces.resolver;

import java.util.Iterator;
import java.util.List;

import javax.faces.component.UIComponentBase;

import de.larmic.butterfaces.component.html.ajax.JsfAjaxRequest;
import de.larmic.butterfaces.component.partrenderer.StringUtils;

/**
 * First try to create a jsf.ajax.request factory. I think there is a nicer way to implement this but up to now it works.
 *
 * @deprecated Use {@link JsfAjaxRequest} instead.
 */
@Deprecated
public class AjaxRequest {

    private final List<String> renderIds;
    private final UIComponentBase component;
    private final String onevent;
    private final String eventName;
    private final String params;

    public AjaxRequest(final UIComponentBase component, final String event) {
        this(component, event, null);
    }

    public AjaxRequest(final UIComponentBase component, final String event, final String onEvent) {
        this(component, event, onEvent, null);
    }

    public AjaxRequest(final UIComponentBase component, final String event, final String onEvent, final String params) {
        this.renderIds = JsfAjaxRequest.createRerenderIds(component, event);
        this.component = component;
        this.onevent = onEvent;
        this.eventName = event;
        this.params = params;
    }

    public String createJavaScriptCall() {
        return this.createJavaScriptCall(eventName);
    }

    public String createJavaScriptCall(final String customEventName) {
        return this.createJavaScriptCall(customEventName, onevent);
    }

    public String createJavaScriptCall(final String customEventName, final String customOnEvent) {
        final String render = this.createRender(this.renderIds);

        if (StringUtils.isEmpty(customOnEvent)) {
            return "jsf.ajax.request('" + component.getClientId() + "','" + customEventName + "',{" + createRenderPart(render) + createParamsPart(params) + "'javax.faces.behavior.event':'" + customEventName + "'});";
        }

        return "jsf.ajax.request('" + component.getClientId() + "','" + customEventName + "',{" + createRenderPart(render) + createParamsPart(params) + "onevent: " + customOnEvent + ", 'javax.faces.behavior.event':'" + customEventName + "'});";
    }

    public String createJavaScriptCall(final String customEventName, final boolean disableElements) {
        if (disableElements && !renderIds.isEmpty()) {
            final StringBuilder onEvent = new StringBuilder("(function(data) { butter.ajax.disableElementsOnRequest(data, [");
            onEvent.append(StringUtils.joinWithCommaSeparator(renderIds, true));
            onEvent.append("]) })");

            return this.createJavaScriptCall(customEventName, onEvent.toString());
        }

        return this.createJavaScriptCall(customEventName, null);
    }

    private String createRenderPart(final String render) {
        if (StringUtils.isNotEmpty(render)) {
            return "render: '" + render + "', ";
        }

        return "";
    }

    private String createParamsPart(final String params) {
        if (StringUtils.isNotEmpty(params)) {
            return "params: " + params + ", ";
        }

        return "";
    }

    public List<String> getRenderIds() {
        return renderIds;
    }

    private String createRender(final List<String> additionalRenderIds) {
        final StringBuilder render = new StringBuilder();

        if (!additionalRenderIds.isEmpty()) {
            final Iterator<String> iterator = additionalRenderIds.iterator();

            while (iterator.hasNext()) {
                render.append(iterator.next());

                if (iterator.hasNext()) {
                    render.append(" ");
                }
            }
        }

        return render.toString();
    }
}
