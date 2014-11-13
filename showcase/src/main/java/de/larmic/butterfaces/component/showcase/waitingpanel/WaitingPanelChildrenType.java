package de.larmic.butterfaces.component.showcase.waitingpanel;

public enum WaitingPanelChildrenType {
	NONE("Default (no children)"), EXAMPLE_1("Custom example 1"), EXAMPLE_2("Custom example 2");
	public final String label;

	private WaitingPanelChildrenType(final String label) {
		this.label = label;
	}
}
