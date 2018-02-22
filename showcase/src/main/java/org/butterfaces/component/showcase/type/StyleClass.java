package org.butterfaces.component.showcase.type;

public enum StyleClass {
	DEFAULT("no custom class"),
    BIG_LABEL("demo-big-label");
	public final String label;

	private StyleClass(final String label) {
		this.label = label;
	}
}
