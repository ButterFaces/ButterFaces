package de.larmic.jsf2.component.taglib.html_basic;

import javax.faces.component.UIComponent;

import com.sun.faces.taglib.html_basic.PanelGroupTag;

public class TextTag extends PanelGroupTag {

	public static final String COMPONENT_TYPE = "de.larmic.component.text";
	public static final String RENDERER_TYPE = "de.larmic.jsf2.renderkit.html_basic.TextRenderer";

	private javax.el.ValueExpression label;

	public void setLabel(final javax.el.ValueExpression label) {
		this.label = label;
	}

	@Override
	protected void setProperties(final UIComponent component) {
		/* you have to call the super class */
		super.setProperties(component);

		if (this.label != null) {
			component.setValueExpression("label", this.label);
		}
	}

	@Override
	public void release() {
		super.release();

		this.label = null;
	}

	@Override
	public String getRendererType() {
		return RENDERER_TYPE;
	}

	@Override
	public String getComponentType() {
		return COMPONENT_TYPE;
	}

}
