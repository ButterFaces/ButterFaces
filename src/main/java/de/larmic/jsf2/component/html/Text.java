package de.larmic.jsf2.component.html;

import javax.faces.component.html.HtmlInputText;
import javax.faces.component.html.HtmlOutputLabel;
import javax.faces.component.html.HtmlPanelGroup;
import javax.faces.context.FacesContext;

import de.larmic.jsf2.component.taglib.html_basic.TextTag;

public class Text extends HtmlPanelGroup {

	private static final String LAYOUT = "block";

	private final HtmlOutputLabel labelComponent = new HtmlOutputLabel();
	private final HtmlOutputLabel readonlyComponent = new HtmlOutputLabel();
	private final HtmlInputText editableComponent = new HtmlInputText();

	private Boolean readonly;

	public Text() {
		this.setLayout(LAYOUT);

		this.getChildren().add(this.labelComponent);

		this.setReadonly(false);
	}

	@Override
	public String getFamily() {
		return TextTag.COMPONENT_TYPE;
	}

	@Override
	public Object saveState(final FacesContext context) {
		final Object values[] = new Object[5];
		values[0] = super.saveState(context);
		values[1] = this.readonly;
		values[2] = this.labelComponent.getValue();
		values[3] = this.readonlyComponent.getValue();
		values[4] = this.editableComponent.getValue();
		return ((values));
	}

	@Override
	public void restoreState(final FacesContext context, final Object state) {
		final Object values[] = (Object[]) state;
		super.restoreState(context, values[0]);
		this.readonly = (Boolean) values[1];
		this.labelComponent.setValue(values[2]);
		this.readonlyComponent.setValue(values[3]);
		this.editableComponent.setValue(values[4]);
	}

	public String getLabel() {
		return (String) this.labelComponent.getValue();
	}

	public void setLabel(final String label) {
		this.labelComponent.setValue(label);
	}

	public boolean isReadonly() {
		return this.readonly;
	}

	public void setReadonly(final boolean readonly) {
		this.readonly = readonly;

		this.getChildren().remove(this.readonlyComponent);
		this.getChildren().remove(this.editableComponent);

		if (readonly) {
			this.getChildren().add(this.readonlyComponent);
		} else {
			this.getChildren().add(this.editableComponent);
		}
	}

	public String getValue() {
		if (this.readonly) {
			return (String) this.readonlyComponent.getValue();
		}

		return (String) this.editableComponent.getValue();
	}

	public void setValue(final String value) {
		this.readonlyComponent.setValue(value);
		this.editableComponent.setValue(value);
	}

}
