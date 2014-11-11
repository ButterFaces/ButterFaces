package de.larmic.butterfaces.component.showcase.tree;

public enum TreeIconType {
	NONE("No icon"), IMAGE("Image icon"), GLYPHICON("Glyphicon icon");
	public final String label;

	private TreeIconType(final String label) {
		this.label = label;
	}
}
