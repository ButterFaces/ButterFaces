package de.larmic.butterfaces.component.showcase.type;

public enum Locale {
	EN("english (default)"),
	DE("german"),
    ES("spain");
	public final String label;

	Locale(final String label) {
		this.label = label;
	}
}
