package de.larmic.butterfaces.component.showcase.type;

public enum RadioBoxLayoutType {
	LINE_DIRECTION("lineDirection"), PAGE_DIRECTION("pageDirection");
	public final String label;

	private RadioBoxLayoutType(final String label) {
		this.label = label;
	}

    public String getLabel() {
        return label;
    }
}
