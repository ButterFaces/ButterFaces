package org.butterfaces.component.showcase.table;

public enum ToolBarType {
    NONE("No custom toolbar"), TEXT("Simple text toolbar"), SERVER_FILTER("Filter toolbar (server side)"), CLIENT_FILTER("Filter toolbar (client side)");

    public final String label;

	private ToolBarType(final String label) {
		this.label = label;
	}
}
