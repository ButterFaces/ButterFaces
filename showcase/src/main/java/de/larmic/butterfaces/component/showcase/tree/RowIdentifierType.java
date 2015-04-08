package de.larmic.butterfaces.component.showcase.tree;

public enum RowIdentifierType {
	NONE("No identifier"), FIELD("element field identifier"), GETTER("element getter identifier");
	public final String label;

	private RowIdentifierType(final String label) {
		this.label = label;
	}
}
