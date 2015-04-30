package de.larmic.butterfaces.component.showcase.container;

public enum AdditionalHeaderType {
	NONE("No additional header"), TEXT("additional text"), BUTTON("additional button");
	public final String label;

	private AdditionalHeaderType(final String label) {
		this.label = label;
	}
}
