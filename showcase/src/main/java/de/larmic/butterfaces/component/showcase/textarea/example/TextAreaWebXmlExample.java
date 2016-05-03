/*
 * Copyright Lars Michaelis and Stephan Zerhusen 2016.
 * Distributed under the MIT License.
 * (See accompanying file README.md file or copy at http://opensource.org/licenses/MIT)
 */
package de.larmic.butterfaces.component.showcase.textarea.example;

import de.larmic.butterfaces.component.showcase.example.WebXmlCodeExample;

/**
 * @author Lars Michaelis
 */
public class TextAreaWebXmlExample extends WebXmlCodeExample {
    public TextAreaWebXmlExample() {
        super("web.xml", "webxml");

        appendInnerContent("  <!-- Auto trim function for input components -->");
        appendInnerContent("  <!-- default is 'true' -->");
        appendInnerContent("  <context-param>");
        appendInnerContent("     <param-name>org.butterfaces.autoTrimInputFields</param-name>");
        appendInnerContent("     <param-value>true</param-value>");
        appendInnerContent("  </context-param>");
        appendInnerContent("  <context-param>");
        appendInnerContent("  <!-- Max length counter text for counting components -->");
        appendInnerContent("  <!-- default is '{0} of {1} characters' -->");
        appendInnerContent("     <param-name>org.butterfaces.maxLengthText</param-name>");
        appendInnerContent("     <param-value>{0} of {1} characters</param-value>");
        appendInnerContent("  </context-param>");
    }
}
