package de.larmic.jsf2.component.showcase;

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
	private boolean ajax;

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

	public String getCss() {
		final StringBuilder sb = new StringBuilder();

		sb.append(".larmic-component-label {\n");
		sb.append("    width: 50px;\n");
		sb.append("}\n");

		sb.append("\n");
		sb.append(".larmic-component-input {\n");
		sb.append("    font-size: 14px !important;\n");
		sb.append("    height: 18px;\n");
		sb.append("    width: 150px;\n");
		sb.append("}\n");

		sb.append("\n");
		sb.append(".input-invalid {\n");
		sb.append("    background-color: #FFEBDA !important;\n");
		sb.append("    border-color: #FF0044;\n");
		sb.append("    border-style: solid;\n");
		sb.append("}\n");

		sb.append("\n");
		sb.append(".larmic-component-tooltip {\n");
		sb.append("    border-radius: 3px;\n");
		sb.append("    box-shadow: 2px 2px 3px #AAAAAA;\n");
		sb.append("}\n");

		sb.append("\n");
		sb.append(".larmic-component-error-message {\n");
		sb.append("    background-color: #FFEBDA;\n");
		sb.append("    border: 1px solid #FF0044;\n");
		sb.append("    border-radius: 3px;\n");
		sb.append("    margin: 5px;\n");
		sb.append("}\n");

		sb.append("\n");
		sb.append(".larmic-component-error-message li {\n");
		sb.append("    list-style: disc outside none;\n");
		sb.append("    margin-left: 20px;\n");
		sb.append("    color: #555555;\n");
		sb.append("    font-style: italic;\n");
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

	public boolean isAjax() {
		return this.ajax;
	}

	public void setAjax(final boolean ajax) {
		this.ajax = ajax;
	}
}
