/*
 * Copyright Lars Michaelis and Stephan Zerhusen 2016.
 * Distributed under the MIT License.
 * (See accompanying file README.md file or copy at http://opensource.org/licenses/MIT)
 */
package org.butterfaces.component.showcase.container.examples;

import org.butterfaces.component.showcase.example.XhtmlCodeExample;

/**
 * @author Lars Michaelis
 */
public class SimpleRepeatListXhtmlCodeExample extends XhtmlCodeExample {

    public SimpleRepeatListXhtmlCodeExample(boolean rendered) {
        super(false);

        this.appendInnerContent("\n        <b:commandLink glyphicon=\"glyphicon glyphicon-refresh\"");
        this.appendInnerContent("                           styleClass=\"btn btn-outline-secondary\">");
        this.appendInnerContent("           <f:ajax execute=\"@this\" render=\"repeat\"/>");
        this.appendInnerContent("        </b:commandLink>");

        this.appendInnerContent("\n        <h:panelGroup id=\"repeat\" layout=\"block\" styleClass=\"repeat-simple-list\">");
        this.appendInnerContent("           <b:repeat value=\"#{myBean.values}\"");
        this.appendInnerContent("                     var=\"value\"");
        this.appendInnerContent("                     status=\"status\"");
        this.appendInnerContent("                     rendered=\"" + rendered + "\">");

        this.appendInnerContent("              <div class=\"row\">");
        this.appendInnerContent("                 <div class=\"col-md-1\">#{value.id}</div>");
        this.appendInnerContent("                 <b:text value=\"#{value.a}\" hideLabel=\"true\" styleClass=\"col-md-2\">");
        this.appendInnerContent("                    <f:ajax execute=\"@this\" event=\"change\" render=\"@this\"/>");
        this.appendInnerContent("                    <f:validateRequired />");
        this.appendInnerContent("                 </b:text>");
        this.appendInnerContent("                 <div class=\"col-md-1\">#{value.b}</div>");
        this.appendInnerContent("                 <div class=\"col-md-3\">#{value.actualDate}</div>");
        this.appendInnerContent("                 <div class=\"col-md-5\">#{status}</div>");
        this.appendInnerContent("              </div>");

        this.appendInnerContent("           </b:repeat>");
        this.appendInnerContent("        </h:panelGroup>");
    }
}
