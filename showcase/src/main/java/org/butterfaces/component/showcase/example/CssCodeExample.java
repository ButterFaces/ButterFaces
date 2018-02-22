package org.butterfaces.component.showcase.example;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by larmic on 12.12.14.
 */
public class CssCodeExample extends AbstractCodeExample {

    private final Map<String, List<String>> rules = new HashMap<>();

    public CssCodeExample() {
        this("css", "css");
    }

    public CssCodeExample(final String tabName, final String tabId) {
        super(tabName, tabId);
    }

    @Override
    public String getPrettyPrintLang() {
        return "lang-css";
    }

    public void addCss(final String name, final String... cssRules) {
        final List<String> values = new ArrayList<>();

        for (String cssRule : cssRules) {
            values.add(cssRule);
        }

        rules.put(name, values);
    }

    @Override
    public String toString() {
        final StringBuilder stringBuilder = new StringBuilder();

        for (String name : rules.keySet()) {
            stringBuilder.append(name);
            stringBuilder.append(" {\n");
            for (String rule : rules.get(name)) {
                stringBuilder.append("    ");
                stringBuilder.append(rule);
                stringBuilder.append(";\n");
            }
            stringBuilder.append("}\n\n");
        }
        return stringBuilder.toString();
    }
}
