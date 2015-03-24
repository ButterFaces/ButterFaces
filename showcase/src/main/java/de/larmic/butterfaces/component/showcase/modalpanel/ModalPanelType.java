package de.larmic.butterfaces.component.showcase.modalpanel;

public enum ModalPanelType {
    SIMPLE("Simple example"), COMPLEX("Validation example");

    public final String label;

	private ModalPanelType(final String label) {
		this.label = label;
	}
}
