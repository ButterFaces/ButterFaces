package de.larmic.butterfaces.component.showcase.type;

public enum RadioBoxExampleType {
	STRING("string"),
	ENUM("enum"),
	OBJECT("object");

	public final String label;

	RadioBoxExampleType(final String label) {
		this.label = label;
	}
}
