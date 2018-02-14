/*
 * Copyright Lars Michaelis and Stephan Zerhusen 2016.
 * Distributed under the MIT License.
 * (See accompanying file README.md file or copy at http://opensource.org/licenses/MIT)
 */
package org.butterfaces.component.showcase.textarea.example;

import org.butterfaces.component.showcase.example.WebXmlCodeExample;

/**
 * @author Lars Michaelis
 */
public class MarkDownWebXmlExample extends WebXmlCodeExample {
    public MarkDownWebXmlExample() {
        super("web.xml", "webxml");

        appendInnerContent("  <!-- Auto trim function for input components -->");
        appendInnerContent("  <!-- default is 'true' -->");
        appendInnerContent("  <context-param>");
        appendInnerContent("     <param-name>org.butterfaces.autoTrimInputFields</param-name>");
        appendInnerContent("     <param-value>true</param-value>");
        appendInnerContent("  </context-param>");
    }
}
