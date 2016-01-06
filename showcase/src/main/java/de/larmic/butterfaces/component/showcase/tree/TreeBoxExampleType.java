package de.larmic.butterfaces.component.showcase.tree;

public enum TreeBoxExampleType {
	ROOT_NODE("simple tree"),
	NODES("list of nodes"),
	STRINGS("list of strings"),
	OBJECTS("list of objects"),
	TEMPLATE("custom template");
	public final String label;

	TreeBoxExampleType(final String label) {
		this.label = label;
	}
}
