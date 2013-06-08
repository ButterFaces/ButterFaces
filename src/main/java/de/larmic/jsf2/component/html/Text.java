package de.larmic.jsf2.component.html;

import javax.faces.component.FacesComponent;
import javax.faces.component.UIInput;
import javax.faces.component.html.HtmlInputText;
import javax.faces.component.html.HtmlOutputLabel;

@FacesComponent(Text.COMPONENT_TYPE)
public class Text extends AbstractHtmlContainer {

	public static final String COMPONENT_TYPE = "de.larmic.component.text";
	public static final String RENDERER_TYPE = "de.larmic.jsf2.renderkit.html_basic.TextRenderer";

	private final HtmlOutputLabel readonlyComponent = new HtmlOutputLabel();

	public Text() {
		super(RENDERER_TYPE);

		// TODO [larmic] use renderer instead
		this.getChildren().add(this.readonlyComponent);
	}

	@Override
	protected UIInput initInputComponent() {
		return new HtmlInputText();
	}

	@Override
	public String getFamily() {
		return COMPONENT_TYPE;
	}

	public HtmlOutputLabel getReadonlyComponent() {
		return this.readonlyComponent;
	}

	// TODO [larmic] Use Object instead and move to abstract class
	public String getValue() {
		if (this.isReadonly()) {
			return (String) this.readonlyComponent.getValue();
		}

		return (String) this.inputComponent.getValue();
	}

	public void setValue(final String value) {
		this.readonlyComponent.setValue(value);
		this.inputComponent.setValue(value);
	}

}
