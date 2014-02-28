package de.larmic.jsf2.component.showcase;

import javax.faces.model.SelectItem;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractShowcaseComponent {
	protected static final String NEW_LINE = "\n";
	protected static final String START_BRACE = "{";
	protected static final String END_BRACE = "}";

	private Object value;
	private String label = "label";
	private String tooltip = "tooltip";
	private boolean readonly;
	private boolean required;
	private boolean rendered = true;
	private boolean floating;
	private boolean validation;
	private AjaxType ajaxType = AjaxType.NONE;

	public AbstractShowcaseComponent() {
		this.value = this.initValue();
	}

	/**
	 * @return specific value object (i.e. a String, Date or Enum) that is
	 *         showing after loading showcase.
	 */
	protected abstract Object initValue();

	/**
	 * @return a readable value of field value (maybe translated enum or
	 *         something).
	 */
	public abstract String getReadableValue();

	public abstract String getXHtml();

	/**
	 * Is called by getCss() and can be used to add custom css output.
	 */
	public void addCss(final StringBuilder sb) {

	}

	public boolean isAjax() {
		return AjaxType.NONE != this.getAjaxType();
	}

	public List<SelectItem> getAjaxTypes() {
		final List<SelectItem> items = new ArrayList<SelectItem>();

		for (final AjaxType type : AjaxType.values()) {
			items.add(new SelectItem(type, type.label));
		}
		return items;
	}

    protected void addXhtmlStart(final StringBuilder sb) {
        sb.append("<!DOCTYPE html>");
        sb.append("\n");
        sb.append("<html xmlns=\"http://www.w3.org/1999/xhtml\"");
        sb.append("\n");
        sb.append("      xmlns:h=\"http://java.sun.com/jsf/html\"");
        sb.append("\n");
        sb.append("      xmlns:l=\"http://larmic.de/jsf2\">");
        sb.append("\n");
        sb.append("<h:head />");
        sb.append("\n");
        sb.append("<body>");
        sb.append("\n");
        sb.append("    <form>");
        sb.append("\n");

    }

    protected void addXhtmlEnd(final StringBuilder sb) {
        sb.append("\n");
        sb.append("    </form>");
        sb.append("\n");
        sb.append("</body>");
        sb.append("\n");
        sb.append("</html>");
    }

	public void createAjaxXhtml(final StringBuilder sb, final String event) {
		if (this.isAjax()) {
			final String execute = AjaxType.THIS == this.ajaxType ? "@this" : "input";
			sb.append("            <f:ajax event=\"" + event + "\"  execute=\"" + execute + "\" render=\"output\"/>\n");
		}
	}

	public void createOutputXhtml(final StringBuilder sb) {
		if (this.isAjax()) {
			sb.append("\n");
			sb.append("\n");
			sb.append("        <h:outputText id=\"output\" value=\"" + this.getValue() + "\"/>");
		}
	}

	public String getCss() {
		final StringBuilder sb = new StringBuilder();

		sb.append(".larmic-component-label {\n");
		sb.append("    width: 50px;\n");
		sb.append("}\n");

		this.addCss(sb);

		return sb.toString();
	}

	public void submit() {

	}

	public Object getValue() {
		return this.value;
	}

	public void setValue(final Object value) {
		this.value = value;
	}

	public String getLabel() {
		return this.label;
	}

	public void setLabel(final String label) {
		this.label = label;
	}

	public String getTooltip() {
		return this.tooltip;
	}

	public void setTooltip(final String tooltip) {
		this.tooltip = tooltip;
	}

	public boolean isReadonly() {
		return this.readonly;
	}

	public void setReadonly(final boolean readonly) {
		this.readonly = readonly;
	}

	public boolean isRequired() {
		return this.required;
	}

	public void setRequired(final boolean required) {
		this.required = required;
	}

	public boolean isRendered() {
		return this.rendered;
	}

	public void setRendered(final boolean rendered) {
		this.rendered = rendered;
	}

	public boolean isFloating() {
		return this.floating;
	}

	public void setFloating(final boolean floating) {
		this.floating = floating;
	}

	public boolean isValidation() {
		return this.validation;
	}

	public void setValidation(final boolean validation) {
		this.validation = validation;
	}

	public AjaxType getAjaxType() {
		return this.ajaxType;
	}

	public void setAjaxType(final AjaxType ajax) {
		this.ajaxType = ajax;
	}
}
