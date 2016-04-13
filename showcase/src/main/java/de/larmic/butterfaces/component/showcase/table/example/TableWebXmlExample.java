/*
 * Copyright Lars Michaelis and Stephan Zerhusen 2016.
 * Distributed under the MIT License.
 * (See accompanying file README.md file or copy at http://opensource.org/licenses/MIT)
 */
package de.larmic.butterfaces.component.showcase.table.example;

import de.larmic.butterfaces.component.showcase.example.WebXmlCodeExample;

/**
 * @author Lars Michaelis
 */
public class TableWebXmlExample extends WebXmlCodeExample {
    public TableWebXmlExample() {
        super("web.xml", "webxml");

        appendInnerContent("  <!-- override table and toolbar glyphicons by context param -->");
        appendInnerContent("  <!-- custom glyphicons (i.e. font-awesome) -->");
        appendInnerContent("  <!-- showcase shows default glyphicons -->");
        appendInnerContent("  <context-param>");
        appendInnerContent("     <param-name>org.butterfaces.glyhicon.refresh</param-name>");
        appendInnerContent("     <param-value>fa fa-refresh</param-value>");
        appendInnerContent("  </context-param>");
        appendInnerContent("  <context-param>");
        appendInnerContent("     <param-name>org.butterfaces.glyhicon.options</param-name>");
        appendInnerContent("     <param-value>fa fa-th</param-value>");
        appendInnerContent("  </context-param>");
        appendInnerContent("  <context-param>");
        appendInnerContent("     <param-name>org.butterfaces.glyhicon.sort.none</param-name>");
        appendInnerContent("     <param-value>fa fa-sort</param-value>");
        appendInnerContent("  </context-param>");
        appendInnerContent("  <context-param>");
        appendInnerContent("     <param-name>org.butterfaces.glyhicon.sort.ascending</param-name>");
        appendInnerContent("     <param-value>fa fa-sort-down</param-value>");
        appendInnerContent("  </context-param>");
        appendInnerContent("  <context-param>");
        appendInnerContent("     <param-name>org.butterfaces.glyhicon.sort.descending</param-name>");
        appendInnerContent("     <param-value>fa fa-sort-up</param-value>");
        appendInnerContent("  </context-param>");

        appendInnerContent("\n  <!-- Shows waiting panel over regions that will be renderer by ajax request -->");
        appendInnerContent("  <!-- Could be overridden by ajaxDisableRenderRegionsOnRequest component attribute -->");
        appendInnerContent("  <!-- default is true -->");
        appendInnerContent("  <context-param>");
        appendInnerContent("     <param-name>org.butterfaces.ajaxDisableRenderRegionsOnRequest</param-name>");
        appendInnerContent("     <param-value>true</param-value>");
        appendInnerContent("  </context-param>");
    }
}
