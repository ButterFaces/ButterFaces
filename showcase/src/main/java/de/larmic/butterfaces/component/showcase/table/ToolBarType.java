package de.larmic.butterfaces.component.showcase.table;

public enum ToolBarType {
	NONE("No custom toolbar"), TEXT("Simple text toolbar"), INPUT("Filter toolbar");
	public final String label;

	private ToolBarType(final String label) {
		this.label = label;
	}
}
