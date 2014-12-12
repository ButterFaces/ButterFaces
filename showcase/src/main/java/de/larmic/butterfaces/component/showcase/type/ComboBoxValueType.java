package de.larmic.butterfaces.component.showcase.type;

public enum ComboBoxValueType {
	STRING("string"), ENUM("enum"), OBJECT("object");
	public final String label;

	private ComboBoxValueType(final String label) {
		this.label = label;
	}
}
