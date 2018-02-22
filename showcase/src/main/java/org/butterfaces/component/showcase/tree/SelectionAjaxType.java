package org.butterfaces.component.showcase.tree;

public enum SelectionAjaxType {
	NONE("No ajax selection"),
    AJAX("ajax enabled"),
    AJAX_DISABLED("ajax disabled");
	public final String label;

	SelectionAjaxType(final String label) {
		this.label = label;
	}
}
