/*
 * Copyright Lars Michaelis and Stephan Zerhusen 2016.
 * Distributed under the MIT License.
 * (See accompanying file README.md file or copy at http://opensource.org/licenses/MIT)
 */
package org.butterfaces.component.showcase.tree.examples.stargate;

import org.butterfaces.component.showcase.example.CssCodeExample;
import org.butterfaces.component.showcase.example.CssCodeExample;

/**
 * @author Lars Michaelis
 */
public class TreeBoxEpisodesCssExample extends CssCodeExample {

    public TreeBoxEpisodesCssExample() {
        this.addCss(".stargateEpisodeItem", "display: flex", "align-items: stretch");
        this.addCss(".stargateEpisodeItem img", "height: 75px");
        this.addCss(".stargateEpisodeItem img.small", "height: 33px");
        this.addCss(".stargateEpisodeItem h4", "font-size: 16px", "margin-top: 5px");
        this.addCss(".stargateEpisodeItem .stargateEpisodeDetails", "font-size: 12px", "margin-left: 5px");
        this.addCss(".stargateEpisodeItem .stargateEpisodeDetails > div", "display: flex", "align-items: baseline");
        this.addCss(".stargateEpisodeItem .stargateEpisodeDetails label", "width: 80px", "font-weight: bold");
        this.addCss(".stargateEpisodeItem .stargateEpisodeDetails span", "flex: 1");
    }
}
