package de.larmic.butterfaces.component.showcase.waitingpanel;

public enum WaitingPanelChildrenType {
	NONE("Default (no children)"), EXAMPLE_1("Big example"), EXAMPLE_2("Small example");
	public final String label;

	private WaitingPanelChildrenType(final String label) {
		this.label = label;
	}
}
