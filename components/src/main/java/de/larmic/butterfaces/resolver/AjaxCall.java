package de.larmic.butterfaces.resolver;

import de.larmic.butterfaces.component.partrenderer.StringUtils;

import javax.faces.component.UIComponentBase;
import javax.faces.component.behavior.AjaxBehavior;
import javax.faces.component.behavior.ClientBehavior;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by larmic on 19.03.15.
 */
public class AjaxCall {

    private final List<String> renderIds;
    private final UIComponentBase component;
    private final String event;
    private final String onevent;

    public AjaxCall(final UIComponentBase component, final String event, final String onevent) {
        this.component = component;
        this.event = event;
        this.onevent = onevent;
        this.renderIds = this.createRefreshIds(component, event);
    }

    public String createJavaScriptCall() {
        final String render = this.createRender(this.component, this.renderIds);

        if (StringUtils.isEmpty(onevent)) {
            return "jsf.ajax.request('" + component.getClientId() + "','" + event + "',{render: '" + render + "', 'javax.faces.behavior.event':'" + event + "'});";
        }

        return "jsf.ajax.request('" + component.getClientId() + "','" + event + "',{render: '" + render + "', onevent:" + onevent + ", 'javax.faces.behavior.event':'" + event + "'});";
    }

    private String createRender(final UIComponentBase component, final List<String> additionalRenderIds) {
        final StringBuilder render = new StringBuilder(component.getClientId());

        for (String renderId : additionalRenderIds) {
            render.append(", ");
            render.append(renderId);
        }

        return render.toString();
    }

    private List<String> createRefreshIds(final UIComponentBase component, final String eventName) {
        final List<String> idsToRender = new ArrayList<>();
        final Map<String, List<ClientBehavior>> behaviors = component.getClientBehaviors();
        final List<ClientBehavior> refreshBehaviors = behaviors.get(eventName);

        if (refreshBehaviors != null && !refreshBehaviors.isEmpty()) {
            for (ClientBehavior refreshBehavior : refreshBehaviors) {
                if (refreshBehavior instanceof AjaxBehavior) {
                    final AjaxBehavior ajaxBehavior = (AjaxBehavior) refreshBehavior;
                    if (ajaxBehavior.getRender() != null && !ajaxBehavior.getRender().isEmpty()) {
                        for (String singleRender : ajaxBehavior.getRender()) {
                            idsToRender.add(singleRender);
                        }
                    }
                }
            }
        }

        return idsToRender;
    }
}
