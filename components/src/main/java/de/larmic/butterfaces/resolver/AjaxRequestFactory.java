package de.larmic.butterfaces.resolver;

import javax.faces.component.UIComponentBase;

/**
 * Created by larmic on 20.03.15.
 */
public class AjaxRequestFactory {

    public AjaxRequestFactory() {
    }

    public final AjaxRequest createRequest(final UIComponentBase component, final String event, final String onevent) {
        try {
            return new AjaxRequest(component, event, onevent);
        } catch (IllegalStateException e) {
            // thrown if event is found but disabled
            return null;
        } catch (IllegalArgumentException e) {
            // thrown if event does not exists on component
            return null;
        }
    }

}
