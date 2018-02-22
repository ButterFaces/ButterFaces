/*
 * Copyright Lars Michaelis and Stephan Zerhusen 2016.
 * Distributed under the MIT License.
 * (See accompanying file README.md file or copy at http://opensource.org/licenses/MIT)
 */
package org.butterfaces.component.html.repeat.event;

import org.butterfaces.component.html.repeat.UIDataAdaptor;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.event.AbortProcessingException;

/**
 * @author Lars Michaelis
 */
public class RowKeyEventBroadcaster {

    public static void broadcast(FacesContext context, RowKeyFacesEvent rowKeyFacesEvent) throws AbortProcessingException {
        final UIDataAdaptor dataAdaptor = rowKeyFacesEvent.getComponent();
        final UIComponent targetComponent = rowKeyFacesEvent.getFacesEvent().getComponent();
        final UIComponent compositeParent = getCompositeParent(targetComponent);

        try {
            if (compositeParent != null) {
                compositeParent.pushComponentToEL(context, null);
            }
            targetComponent.pushComponentToEL(context, null);
            dataAdaptor.setRowKey(context, rowKeyFacesEvent.getEventRowKey());

            targetComponent.broadcast(rowKeyFacesEvent.getFacesEvent());
        } finally {
            dataAdaptor.setRowKey(context, dataAdaptor.getRowKey());
            targetComponent.popComponentFromEL(context);
            if (compositeParent != null) {
                compositeParent.popComponentFromEL(context);
            }
        }
    }

    private static UIComponent getCompositeParent(UIComponent targetComponent) {
        if (!UIComponent.isCompositeComponent(targetComponent)) {
            return UIComponent.getCompositeComponentParent(targetComponent);
        }

        return null;
    }
}
