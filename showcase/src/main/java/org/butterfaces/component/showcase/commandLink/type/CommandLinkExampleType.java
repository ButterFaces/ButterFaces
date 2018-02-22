package org.butterfaces.component.showcase.commandLink.type;

public enum CommandLinkExampleType {
    AJAX("Ajax submit"),
    NO_AJAX("Default submit"),
    RESET_VALUES("Reset values");

    public final String label;

    CommandLinkExampleType(final String label) {
		this.label = label;
	}
}
