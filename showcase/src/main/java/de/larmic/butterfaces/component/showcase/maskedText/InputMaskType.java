package de.larmic.butterfaces.component.showcase.maskedText;

public enum InputMaskType {
	NONE("No input mask"), PHONE("Phone"), PHONE_DATA("Phone (data)"), CURRENCY_BY_DATA("Currency (data)");
	public final String label;

	private InputMaskType(final String label) {
		this.label = label;
	}
}
