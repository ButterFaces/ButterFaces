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

	public abstract String getCss();

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
