/*
 * Copyright Lars Michaelis and Stephan Zerhusen 2016.
 * Distributed under the MIT License.
 * (See accompanying file README.md file or copy at http://opensource.org/licenses/MIT)
 */
package org.butterfaces.component.showcase.commandLink.example;

import org.butterfaces.component.showcase.example.WebXmlCodeExample;

/**
 * @author Lars Michaelis
 */
public class CommandLinkWebXmlExample extends WebXmlCodeExample {
    public CommandLinkWebXmlExample() {
        super("web.xml", "webxml");

        appendInnerContent("  <!-- Button text when ajax request is running -->");
        appendInnerContent("  <!-- default is Processing -->");
        appendInnerContent("  <context-param>");
        appendInnerContent("     <param-name>org.butterfaces.ajaxProcessingTextOnRequest</param-name>");
        appendInnerContent("     <param-value>Processing</param-value>");
        appendInnerContent("  </context-param>");

        appendInnerContent("\n  <!-- Glyphicon when ajax request is running -->");
        appendInnerContent("  <!-- default is empty -->");
        appendInnerContent("  <context-param>");
        appendInnerContent("     <param-name>org.butterfaces.ajaxProcessingTextOnRequest</param-name>");
        appendInnerContent("     <param-value></param-value>");
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
