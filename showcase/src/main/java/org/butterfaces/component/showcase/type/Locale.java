package org.butterfaces.component.showcase.type;

public enum Locale {
    EN("english (en)", "en"),
    DE("german (de)", "de"),
    ES("spain (es)", "es");
    public final String label;
    public final String value;

    Locale(final String label, final String value) {
        this.label = label;
        this.value = value;
    }
}
