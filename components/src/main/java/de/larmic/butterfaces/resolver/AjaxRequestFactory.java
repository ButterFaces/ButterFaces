package de.larmic.butterfaces.resolver;

import javax.faces.component.UIComponentBase;

/**
 * Factory of {@link AjaxRequest} to wrap some code.
 */
public class AjaxRequestFactory {

    public AjaxRequestFactory() {
    }

    public AjaxRequest createRequest(final UIComponentBase component, final String event) {
        return this.createRequest(component, event, null, null);
    }

    public AjaxRequest createRequest(final UIComponentBase component, final String event, final String onevent, final String params) {
        try {
            return new AjaxRequest(component, event, onevent, params);
        } catch (IllegalStateException e) {
            // thrown if event is found but disabled
            return null;
        } catch (IllegalArgumentException e) {
            // thrown if event does not exists on component
            return null;
        }
    }

}
