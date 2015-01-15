package de.larmic.butterfaces.component.showcase.text;

public enum FacetType {
	NONE("No facets"), INPUT_GROUP_ADDON("input-group-addon");
	public final String label;

	private FacetType(final String label) {
		this.label = label;
	}
}
