package de.larmic.butterfaces.component.showcase.tree;

public enum TreeBoxExampleType {
	NODES("list of nodes"),
	ROOT_NODE("one root"),
	STRINGS("list of strings"),
	TEMPLATE("custom template");
	public final String label;

	TreeBoxExampleType(final String label) {
		this.label = label;
	}
}
