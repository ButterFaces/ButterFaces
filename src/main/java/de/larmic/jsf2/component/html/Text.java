package de.larmic.jsf2.component.html;

import javax.faces.component.html.HtmlPanelGroup;
import javax.faces.context.FacesContext;

import de.larmic.jsf2.component.taglib.html_basic.TextTag;

public class Text extends HtmlPanelGroup {

	private static final String LAYOUT = "block";

	private String label;

	public Text() {
		this.setLayout(LAYOUT);
	}

	@Override
	public String getFamily() {
		return TextTag.COMPONENT_TYPE;
	}

	@Override
	public Object saveState(final FacesContext context) {
		final Object values[] = new Object[2];
		values[0] = super.saveState(context);
		values[1] = this.label;
		return ((values));
	}

	@Override
	public void restoreState(final FacesContext context, final Object state) {
		final Object values[] = (Object[]) state;
		super.restoreState(context, values[0]);
		this.label = (String) values[1];
	}

	public String getLabel() {
		return this.label;
	}

	public void setLabel(final String label) {
		this.label = label;
	}

}
