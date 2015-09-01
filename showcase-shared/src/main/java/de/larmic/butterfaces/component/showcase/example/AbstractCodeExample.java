package de.larmic.butterfaces.component.showcase.example;

/**
 * Created by larmic on 12.12.14.
 */
public abstract class AbstractCodeExample {

    private final String tabName;
    private final String tabId;

    public AbstractCodeExample(final String tabName, final String tabId) {
        this.tabName = tabName;
        this.tabId = tabId;
    }

    public abstract String getPrettyPrintLang();

    public String getTabName() {
        return tabName;
    }

    public String getTabId() {
        return tabId;
    }
}
