package de.larmic.butterfaces.component.showcase.commandLink;

public enum CommandLinkExampleType {
    AJAX("Ajax submit"), NO_AJAX("Default submit"), RESET_VALUES("Reset values");

    public final String label;

	private CommandLinkExampleType(final String label) {
		this.label = label;
	}
}
