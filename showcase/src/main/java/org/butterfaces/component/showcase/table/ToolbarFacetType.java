package org.butterfaces.component.showcase.table;

public enum ToolbarFacetType {
	NONE("No table model"), LEFT_FACET("Left facet"), CENTER_FACET("Center facet"), RIGHT_FACET("Right facet");
	public final String label;

	private ToolbarFacetType(final String label) {
		this.label = label;
	}
}
