package de.larmic.butterfaces.component.showcase.comboBox;

public enum FacetType {
	NONE("No facets"), INPUT_GROUP_ADDON("left input-group-addon"), INPUT_GROUP_BTN("left input-group-btn");
	public final String label;

	private FacetType(final String label) {
		this.label = label;
	}
}
