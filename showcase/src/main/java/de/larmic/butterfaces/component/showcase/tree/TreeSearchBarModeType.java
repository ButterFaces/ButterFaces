package de.larmic.butterfaces.component.showcase.tree;

public enum TreeSearchBarModeType {
    NONE("none"), ALWAYS_VISIBLE("always-visible"), SHOW_IF_FILLED("show-if-filled");
    public final String label;

    TreeSearchBarModeType(final String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
