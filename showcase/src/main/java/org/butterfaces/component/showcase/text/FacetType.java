package org.butterfaces.component.showcase.text;

public enum FacetType {
	NONE("No facets"), INPUT_GROUP_ADDON("input-group-addon"), INPUT_GROUP_BTN("input-group-btn");
	public final String label;

	private FacetType(final String label) {
		this.label = label;
	}
}
