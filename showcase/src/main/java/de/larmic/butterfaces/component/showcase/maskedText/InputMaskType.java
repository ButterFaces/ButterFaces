package de.larmic.butterfaces.component.showcase.maskedText;

public enum InputMaskType {
	PHONE("Phone"), CURRENCY_BY_DATA("Currency (data)");
	public final String label;

	private InputMaskType(final String label) {
		this.label = label;
	}
}
