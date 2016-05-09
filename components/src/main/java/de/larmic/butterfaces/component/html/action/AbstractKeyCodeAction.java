/**
 * Copyright 2012 Lars Michaelis
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */
package de.larmic.butterfaces.component.html.action;

import javax.el.ValueExpression;
import javax.faces.component.UIComponent;
import javax.faces.component.UIComponentBase;
import javax.faces.component.UIForm;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import java.io.IOException;
import java.util.logging.Logger;

/**
 * Action is used to set a onKeyPress event on ambient form. A click-event will
 * be invoked on key code action ambient component. Note that not all jsf
 * components support click event.
 * The concrete implementation of this class has to define on which key code the
 * click event will be fired.
 */
public abstract class AbstractKeyCodeAction extends UIComponentBase {

	private static final Logger LOGGER = Logger.getLogger(AbstractKeyCodeAction.class.getName());

	private static final String SCRIPT_ELEMENT = "script";
	private static final String SCRIPT_LANGUAGE_ATTRIBUTE = "language";
	private static final String SCRIPT_LANGUAGE_JAVASCRIPT = "JavaScript";
	private static final String SCRIPT_TYPE_ATTRIBUTE = "type";
	private static final String SCRIPT_TYPE_TEXT_JAVASCRIPT = "text/javascript";

	protected static final String PROPERTY_RENDERED = "rendered";

	public abstract String getListeningKeyCode();

	public abstract String getFormActionMarker();

	@Override
	public String getFamily() {
		return "defaultAction";
	}

	public boolean isRendered() {
		final Object eval = this.getStateHelper().eval(PROPERTY_RENDERED);
		return eval == null ? true : (Boolean) eval;
	}

	public void setRendered(final boolean rendered) {
		this.updateStateHelper(PROPERTY_RENDERED, rendered);
	}

	@Override
	public void encodeEnd(final FacesContext context) throws IOException {
		if (!isRendered()) {
			return;
		}

		final UIComponent actionComponent = this.getParent();
		final String actionComponentId = actionComponent.getClientId(context);
		final UIForm form = getForm(actionComponent);

		if (form == null) {
			LOGGER.warning("AbstractKeyCodeAction does not have an ambient form. Form is required");
		} else {
			markForm(form, this.getClientId(context));

			final ResponseWriter responseWriter = context.getResponseWriter();
			writeActionScript(responseWriter, form, actionComponentId);
		}
	}

	private void writeActionScript(final ResponseWriter responseWriter, final UIForm form,
			final String actionComponentId) throws IOException {
		responseWriter.startElement(SCRIPT_ELEMENT, this);

		responseWriter.writeAttribute(SCRIPT_LANGUAGE_ATTRIBUTE, SCRIPT_LANGUAGE_JAVASCRIPT, SCRIPT_LANGUAGE_ATTRIBUTE);
		responseWriter.writeAttribute(SCRIPT_TYPE_ATTRIBUTE, SCRIPT_TYPE_TEXT_JAVASCRIPT, SCRIPT_TYPE_ATTRIBUTE);

		responseWriter.write(createFunctionCode(form.getClientId(), actionComponentId));

		responseWriter.endElement(SCRIPT_ELEMENT);
	}

	private String createFunctionCode(final String formId, final String actionComponentId) {
		final StringBuilder sb = new StringBuilder();

		// TODO [larmic] check other onkeypress functions

		sb.append("document.forms['" + formId + "'].onkeypress = ");
		sb.append("new Function(\"event\", \"" + createBody(actionComponentId) + "\");");

		return sb.toString();
	}

	private String createBody(final String actionComponentId) {
		final StringBuilder sb = new StringBuilder();

		// IE and FF have different events
		sb.append("if (!event) { event = window.event; }");
		// IE and FF have different source elements
		sb.append("var evt = event.target?event.target:event.srcElement;");
		// check if event is fired by text area
		sb.append("if (evt.tagName.toLowerCase() != 'textarea') {");
		sb.append("{var keycode;");
		sb.append("if (window.event) keycode = window.event.keyCode;");
		sb.append("else if (event) keycode = event.which;");
		sb.append("else return true;");
		sb.append("if (keycode == " + getListeningKeyCode() + ")");
		sb.append("{document.getElementById('" + actionComponentId + "').click();return false; }");
		sb.append("else return true; }");
		sb.append("}");
		sb.append("return true;");

		return sb.toString();
	}

	private void markForm(final UIForm form, final String componentId) {
		if (isFormMarked(form) && !isFormMarkedWithId(form, componentId)) {
			LOGGER.warning("AbstractKeyCodeAction only supports one key event");
		} else {
			form.getAttributes().put(getFormActionMarker(), componentId);
		}
	}

	private boolean isFormMarked(final UIForm form) {
		return form.getAttributes().containsKey(getFormActionMarker());
	}

	private boolean isFormMarkedWithId(final UIForm form, final String componentId) {
		return form.getAttributes().get(getFormActionMarker()).equals(componentId);
	}

	// TODO [larmic] find a better way
	private UIForm getForm(UIComponent component) {
		while (component != null) {
			if (component instanceof UIForm) {
				break;
			}
			component = component.getParent();
		}
		return (UIForm) component;
	}

	private void updateStateHelper(final String propertyName, final Object value) {
		this.getStateHelper().put(propertyName, value);

		final ValueExpression ve = this.getValueExpression(propertyName);

		if (ve != null) {
			ve.setValue(this.getFacesContext().getELContext(), value);
		}
	}
}
