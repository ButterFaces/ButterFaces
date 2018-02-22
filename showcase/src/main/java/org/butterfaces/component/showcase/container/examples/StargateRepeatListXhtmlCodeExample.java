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
public class StargateRepeatListXhtmlCodeExample extends XhtmlCodeExample {

    public StargateRepeatListXhtmlCodeExample(boolean rendered) {
        super(true);

        this.appendInnerContent("\n        <div class=\"row repeat-episode-list\">");
        this.appendInnerContent("           <b:repeat value=\"#{myBean.values}\"");
        this.appendInnerContent("                     var=\"episode\"");
        this.appendInnerContent("                     rendered=\"" + rendered + "\">");

        this.appendInnerContent("              <div class=\"col-md-4 repeat-episode-item\">");
        this.appendInnerContent("                 <img src=\"#{episode.image}\" alt=\"#{episode.title}\"/>");
        this.appendInnerContent("                 <div class=\"repeat-episode-content\">");
        this.appendInnerContent("                 <div><strong>#{episode.title}</strong></div>");
        this.appendInnerContent("                 <div><small>written by: #{episode.writtenBy}</small></div>");
        this.appendInnerContent("                 <b:commandLink value=\"Play\"");
        this.appendInnerContent("                                action=\"#{repeatShowcase.play}\"");
        this.appendInnerContent("                                glyphicon=\"fa fa-play-circle-o\"");
        this.appendInnerContent("                                styleClass=\"btn btn-outline-secondary btn-sm\">");
        this.appendInnerContent("                    <f:ajax execute=\"@this\"/>");
        this.appendInnerContent("                 </b:commandLink>");
        this.appendInnerContent("              </div>");

        this.appendInnerContent("           </b:repeat>");
        this.appendInnerContent("        </div>");
    }
}
