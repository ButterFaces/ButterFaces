package de.larmic.butterfaces.component.showcase.tree;

public enum RowIdentifierType {
	NONE("No identifier"), ID("element id identifier");
	public final String label;

	private RowIdentifierType(final String label) {
		this.label = label;
	}
}
