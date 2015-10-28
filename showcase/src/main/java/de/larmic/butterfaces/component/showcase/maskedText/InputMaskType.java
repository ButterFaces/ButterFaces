package de.larmic.butterfaces.component.showcase.maskedText;

public enum InputMaskType {
	PHONE("Phone"), CURRENCY_BY_DATA("Currency (data)"), ALPHA_NUMERIC("Alpha numeric");
	public final String label;

	private InputMaskType(final String label) {
		this.label = label;
	}
}
