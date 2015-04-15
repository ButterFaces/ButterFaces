package de.larmic.butterfaces.component.showcase.tags;

public enum ConfirmKeyType {
	DEFAULT("default (enter and comma)"), ENTER("Enter"), COMMA("Comma"), SPACE("Space");

    public final String label;

	ConfirmKeyType(final String label) {
		this.label = label;
	}
}
