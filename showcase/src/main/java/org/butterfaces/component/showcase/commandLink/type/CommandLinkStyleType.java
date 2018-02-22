package org.butterfaces.component.showcase.commandLink.type;

public enum CommandLinkStyleType {
    LINK("default link", null),
    BOOTSTRAP_BUTTON("Bootstrap button", "btn btn-primary");

    public final String label;
    public final String value;

    CommandLinkStyleType(final String label, final String value) {
		this.label = label;
		this.value = value;
	}
}
