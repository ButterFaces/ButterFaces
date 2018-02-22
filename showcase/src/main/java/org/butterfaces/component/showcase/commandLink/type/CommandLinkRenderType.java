package org.butterfaces.component.showcase.commandLink.type;

public enum CommandLinkRenderType {
    SECTIONS("some sections", "disabledOnRequest otherDisabledOnRequest"),
    FORM("@form", "@form"),
    THIS("@this", "@this"),
    NONE("@none", "@none");

    public final String label;
    public final String value;

    CommandLinkRenderType(final String label, final String value) {
        this.label = label;
        this.value = value;
	}
}
