/*
 * Copyright Lars Michaelis and Stephan Zerhusen 2016.
 * Distributed under the MIT License.
 * (See accompanying file README.md file or copy at http://opensource.org/licenses/MIT)
 */
package de.larmic.butterfaces.component.showcase.container.examples;

import de.larmic.butterfaces.component.showcase.example.XhtmlCodeExample;

/**
 * @author Lars Michaelis
 */
public class NestedRepeatListXhtmlCodeExample extends XhtmlCodeExample {

    public NestedRepeatListXhtmlCodeExample() {
        super(false);

        this.appendInnerContent("\n        <b:repeat value=\"#{myBean.outerValues}\"");
        this.appendInnerContent("                  var=\"outerValue\">");

        this.appendInnerContent("           <li>#{outerValue}</li>");
        this.appendInnerContent("           <ul>");
        this.appendInnerContent("              <b:repeat value=\"#{myBean.innerValueForOuterValue}\"");
        this.appendInnerContent("                        var=\"innerValue\">");
        this.appendInnerContent("                 <li>#{innerValue}</li>");
        this.appendInnerContent("              </b:repeat>");
        this.appendInnerContent("           </ul>");

        this.appendInnerContent("        </b:repeat>");
    }
}
