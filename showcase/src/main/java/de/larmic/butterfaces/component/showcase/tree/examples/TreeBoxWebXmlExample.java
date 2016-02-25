/*
 * Copyright Lars Michaelis and Stephan Zerhusen 2016.
 * Distributed under the MIT License.
 * (See accompanying file README.md file or copy at http://opensource.org/licenses/MIT)
 */
package de.larmic.butterfaces.component.showcase.tree.examples;

import de.larmic.butterfaces.component.showcase.example.WebXmlCodeExample;

/**
 * @author Lars Michaelis
 */
public class TreeBoxWebXmlExample extends WebXmlCodeExample {
    public TreeBoxWebXmlExample() {
        super("web.xml", "webxml");

        appendInnerContent("  <!-- Text showing if no entry is found -->");
        appendInnerContent("  <!-- Could be overridden by noEnttriesText component attribute -->");
        appendInnerContent("  <!-- default is 'No matching entries...' -->");
        appendInnerContent("  <context-param>");
        appendInnerContent("     <param-name>de.larmic.butterfaces.noEntriesText</param-name>");
        appendInnerContent("     <param-value>No matching entries...</param-value>");
        appendInnerContent("  </context-param>");

        appendInnerContent("  <!-- Text showing if entries are loading -->");
        appendInnerContent("  <!-- Could be overridden by spinnerText component attribute -->");
        appendInnerContent("  <!-- default is 'Fetching data...' -->");
        appendInnerContent("  <context-param>");
        appendInnerContent("     <param-name>de.larmic.butterfaces.spinnerText</param-name>");
        appendInnerContent("     <param-value>Fetching data...</param-value>");
        appendInnerContent("  </context-param>");
    }
}
