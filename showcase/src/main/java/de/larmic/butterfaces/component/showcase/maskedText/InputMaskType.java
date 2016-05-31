package de.larmic.butterfaces.component.showcase.maskedText;

public enum InputMaskType {
	PHONE("Phone"),
    CURRENCY_BY_DATA("Currency (passthrough)"),
    CURRENCY_GERMAN("Currency (german)"),
    DATE("Month/Year"),
    ALPHA_NUMERIC("Alpha numeric");
	public final String label;

	InputMaskType(final String label) {
		this.label = label;
	}
}
