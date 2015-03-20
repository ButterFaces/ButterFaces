package de.larmic.butterfaces.resolver;

import de.larmic.butterfaces.component.partrenderer.StringUtils;

import javax.faces.component.UIComponentBase;
import javax.faces.component.behavior.AjaxBehavior;
import javax.faces.component.behavior.ClientBehavior;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by larmic on 19.03.15.
 */
public class AjaxRequest {

    private final List<String> renderIds;
    private final UIComponentBase component;
    private final String originalEventName;
    private final String onevent;
    private String eventName;

    public AjaxRequest(final UIComponentBase component, final String event, final String onevent) {
        this.renderIds = this.createRefreshIds(component, event);
        this.component = component;
        this.originalEventName = event;
        this.onevent = onevent;
        this.eventName = originalEventName;
    }

    public String createJavaScriptCall() {
        final String render = this.createRender(this.renderIds);

        if (StringUtils.isEmpty(onevent)) {
            return "jsf.ajax.request('" + component.getClientId() + "','" + eventName + "',{render: '" + render + "', 'javax.faces.behavior.event':'" + eventName + "'});";
        }

        return "jsf.ajax.request('" + component.getClientId() + "','" + eventName + "',{render: '" + render + "', onevent:" + onevent + ", 'javax.faces.behavior.event':'" + eventName + "'});";
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
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

    private List<String> createRefreshIds(final UIComponentBase component, final String eventName) {
        final List<String> idsToRender = new ArrayList<>();
        final Map<String, List<ClientBehavior>> behaviors = component.getClientBehaviors();
        final List<ClientBehavior> refreshBehaviors = behaviors.get(eventName);

        if (refreshBehaviors == null) {
            throw new IllegalArgumentException("Ajax event '" + eventName + "' not found on component '" + component.getClientId() + "'.");
        }

        boolean enabledAjaxEventFound = false;

        if (refreshBehaviors != null && !refreshBehaviors.isEmpty()) {
            for (ClientBehavior refreshBehavior : refreshBehaviors) {
                if (refreshBehavior instanceof AjaxBehavior) {
                    final AjaxBehavior ajaxBehavior = (AjaxBehavior) refreshBehavior;

                    if (!ajaxBehavior.isDisabled()) {
                        if (ajaxBehavior.getRender() != null && !ajaxBehavior.getRender().isEmpty()) {
                            for (String singleRender : ajaxBehavior.getRender()) {
                                idsToRender.add(singleRender);
                            }
                        }

                        enabledAjaxEventFound = true;
                    }
                }
            }
        }

        if (!enabledAjaxEventFound) {
            throw new IllegalStateException("Ajax event '" + eventName + "' on component '" + component.getClientId() + "' is disabled.");
        }

        return idsToRender;
    }
}
