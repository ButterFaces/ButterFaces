package de.larmic.butterfaces.component.showcase.container;

public enum LabeledContainerExampleType {
	TEXT("text"), LINK("link"), SECTION("section");
	public final String label;

	private LabeledContainerExampleType(final String label) {
		this.label = label;
	}
}
