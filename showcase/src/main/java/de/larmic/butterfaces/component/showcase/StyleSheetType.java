package de.larmic.butterfaces.component.showcase;

public enum StyleSheetType {
	DEFAULT("default"),
    DISABLE_DEFAULT("disable default style"),
    BOOT_STRAP("bootstrap"),
    BOOT_STRAP_ONLY("only bootstrap");
	public final String label;

	private StyleSheetType(final String label) {
		this.label = label;
	}
}
