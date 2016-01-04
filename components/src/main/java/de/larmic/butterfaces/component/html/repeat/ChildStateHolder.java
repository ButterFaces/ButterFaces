/*
 * Copyright Lars Michaelis and Stephan Zerhusen 2016.
 * Distributed under the MIT License.
 * (See accompanying file README.md file or copy at http://opensource.org/licenses/MIT)
 */
package de.larmic.butterfaces.component.html.repeat;

import javax.faces.component.EditableValueHolder;
import javax.faces.component.StateHolder;
import javax.faces.component.UIComponentBase;
import javax.faces.component.UIForm;
import javax.faces.context.FacesContext;

/**
 * @author Lars Michaelis
 */
public final class ChildStateHolder implements StateHolder {
    public static final ChildStateHolder EMPTY = new ChildStateHolder();
    private boolean valid = true;
    private boolean localValueSet;
    private boolean submitted;
    private Object submittedValue;
    private Object value;
    private Object iterationState;

    public ChildStateHolder() {
        super();
    }

    public ChildStateHolder(EditableValueHolder evh) {
        this.value = evh.getLocalValue();
        this.valid = evh.isValid();
        this.submittedValue = evh.getSubmittedValue();
        this.localValueSet = evh.isLocalValueSet();
    }

    public ChildStateHolder(UIForm form) {
        this.submitted = form.isSubmitted();
    }

    Object getSubmittedValue() {
        return this.submittedValue;
    }

    void setSubmittedValue(Object submittedValue) {
        this.submittedValue = submittedValue;
    }

    boolean isValid() {
        return this.valid;
    }

    void setValid(boolean valid) {
        this.valid = valid;
    }

    Object getValue() {
        return this.value;
    }

    void setValue(Object value) {
        this.value = value;
    }

    boolean isLocalValueSet() {
        return this.localValueSet;
    }

    void setLocalValueSet(boolean localValueSet) {
        this.localValueSet = localValueSet;
    }

    Object getIterationState() {
        return iterationState;
    }

    void setIterationState(Object iterationState) {
        this.iterationState = iterationState;
    }

    boolean isSubmitted() {
        return submitted;
    }

    void setSubmitted(boolean submitted) {
        this.submitted = submitted;
    }

    public void apply(EditableValueHolder evh) {
        evh.setValue(this.value);
        evh.setValid(this.valid);
        evh.setSubmittedValue(this.submittedValue);
        evh.setLocalValueSet(this.localValueSet);
    }

    public void apply(UIForm form) {
        form.setSubmitted(this.submitted);
    }

    private boolean isObjectTransient(Object o) {
        return o == null || o instanceof StateHolder && ((StateHolder) o).isTransient();
    }

    public void setTransient(boolean newTransientValue) {
        throw new UnsupportedOperationException();
    }

    public boolean isTransient() {
        if (iterationState != null) {
            return isObjectTransient(iterationState);
        }

        return !(!valid || localValueSet || submitted) && isObjectTransient(submittedValue) && isObjectTransient(value);

    }

    public Object saveState(FacesContext context) {
        if (isTransient()) {
            return null;
        }

        if (iterationState != null) {
            return new Object[]{UIComponentBase.saveAttachedState(context, iterationState)};
        } else {
            return new Object[]{
                    valid ? Boolean.TRUE : Boolean.FALSE,
                    localValueSet ? Boolean.TRUE : Boolean.FALSE,
                    submitted ? Boolean.TRUE : Boolean.FALSE,
                    UIComponentBase.saveAttachedState(context, submittedValue),
                    UIComponentBase.saveAttachedState(context, value)};
        }
    }

    public void restoreState(FacesContext context, Object stateObject) {
        if (stateObject == null) {
            return;
        }

        final Object[] state = (Object[]) stateObject;

        if (state.length == 1) {
            iterationState = UIComponentBase.restoreAttachedState(context, state[0]);
        } else {
            valid = Boolean.TRUE.equals(state[0]);
            localValueSet = Boolean.TRUE.equals(state[1]);
            submitted = Boolean.TRUE.equals(state[2]);
            submittedValue = UIComponentBase.restoreAttachedState(context, state[3]);
            value = UIComponentBase.restoreAttachedState(context, state[4]);
        }
    }

    @Override
    public String toString() {
        if (iterationState != null) {
            return "iterationState: " + iterationState;
        } else {
            return "submittedValue: " + submittedValue
                    + " value: " + value
                    + " localValueSet: " + localValueSet
                    + " submitted: " + submitted;
        }
    }
}
