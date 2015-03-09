package de.larmic.butterfaces.component.showcase.table;

public enum ToolbarFacetType {
	NONE("No table model"), DEFAULT_MODEL("Default table model");
	public final String label;

	private ToolbarFacetType(final String label) {
		this.label = label;
	}
}
