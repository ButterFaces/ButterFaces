/*
 * Copyright Lars Michaelis and Stephan Zerhusen 2016.
 * Distributed under the MIT License.
 * (See accompanying file README.md file or copy at http://opensource.org/licenses/MIT)
 */
package org.butterfaces.component.showcase.tree.examples;

import org.butterfaces.component.showcase.example.WebXmlCodeExample;

import static org.butterfaces.resolver.WebXmlParameters.*;

/**
 * @author Lars Michaelis
 */
public class TreeBoxWebXmlExample extends WebXmlCodeExample {
    public TreeBoxWebXmlExample() {
        super("web.xml", "webxml");

        appendInnerContent("");
        appendInnerContent("  <!-- Text showing if no entry is found -->");
        appendInnerContent("  <!-- Could be overridden by noEnttriesText component attribute -->");
        appendInnerContent("  <!-- default is 'No matching entries...' -->");
        appendInnerContent("  <context-param>");
        appendInnerContent("     <param-name>" + CTX_PARAM_NO_ENTRIES_TEXT + "</param-name>");
        appendInnerContent("     <param-value>No matching entries...</param-value>");
        appendInnerContent("  </context-param>");

        appendInnerContent("");
        appendInnerContent("  <!-- Text showing if entries are loading -->");
        appendInnerContent("  <!-- Could be overridden by spinnerText component attribute -->");
        appendInnerContent("  <!-- default is 'Fetching data...' -->");
        appendInnerContent("  <context-param>");
        appendInnerContent("     <param-name>" + CTX_PARAM_SPINNER_TEXT + "</param-name>");
        appendInnerContent("     <param-value>Fetching data...</param-value>");
        appendInnerContent("  </context-param>");

        appendInnerContent("");
        appendInnerContent("  <!-- If true, it shows 'x'-button to delete the selected entry. -->");
        appendInnerContent("  <!-- Could be overridden by showClearButton component attribute -->");
        appendInnerContent("  <!-- default is true -->");
        appendInnerContent("  <context-param>");
        appendInnerContent("     <param-name>" + CTX_PARAM_TREEBOX_SHOW_CLEAR_BUTTON + "</param-name>");
        appendInnerContent("     <param-value>true</param-value>");
        appendInnerContent("  </context-param>");

        appendInnerContent("");
    }
}
