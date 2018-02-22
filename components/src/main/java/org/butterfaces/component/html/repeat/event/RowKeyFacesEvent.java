/*
 * Copyright Lars Michaelis and Stephan Zerhusen 2016.
 * Distributed under the MIT License.
 * (See accompanying file README.md file or copy at http://opensource.org/licenses/MIT)
 */
package org.butterfaces.component.html.repeat.event;

import org.butterfaces.component.html.repeat.UIDataAdaptor;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import javax.faces.event.FacesEvent;
import javax.faces.event.FacesListener;
import javax.faces.event.PhaseId;

/**
 * @author Lars Michaelis
 */
public class RowKeyFacesEvent extends FacesEvent {

    private FacesEvent event;
    private Integer eventRowKey;

    public RowKeyFacesEvent(UIDataAdaptor component, FacesEvent event, Integer eventRowKey) {
        super(component);

        this.event = event;
        this.eventRowKey = eventRowKey;
    }

    public FacesEvent getFacesEvent() {
        return this.event;
    }

    public Integer getEventRowKey() {
        return eventRowKey;
    }

    public PhaseId getPhaseId() {
        return this.event.getPhaseId();
    }

    public void setPhaseId(PhaseId phaseId) {
        this.event.setPhaseId(phaseId);
    }

    public boolean isAppropriateListener(FacesListener listener) {
        return false;
    }

    public void processListener(FacesListener listener) {
        throw new NotImplementedException();
    }

    public UIDataAdaptor getComponent() {
        return (UIDataAdaptor) super.getComponent();
    }
}
