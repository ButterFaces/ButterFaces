/*
 * Copyright Lars Michaelis and Stephan Zerhusen 2016.
 * Distributed under the MIT License.
 * (See accompanying file README.md file or copy at http://opensource.org/licenses/MIT)
 */
package org.butterfaces.component.showcase.container.examples;

import org.butterfaces.component.showcase.example.CssCodeExample;

/**
 * @author Lars Michaelis
 */
public class StargateRepeatListCssCodeExample extends CssCodeExample {

    public StargateRepeatListCssCodeExample() {
        addCss(".repeat-episode-list", "padding: 0 10px", "max-height: 400px", "overflow: auto");
        addCss(".repeat-episode-item", "border: #ddd 1px solid", "border-radius: 4px");
        addCss(".repeat-episode-item.col-md-4", "padding: 5px", "width: calc(33.33333333% - 12px)");
        addCss(".repeat-episode-item img", "width: 100%");
        addCss(".repeat-episode-item repeat-episode-content", "opacity: 0.6", "position: absolute", "top: 0", "color: white", "margin-top: 10px", "margin-left: 10px", "height: 100%", "width: 100%");
        addCss(".repeat-episode-item repeat-episode-content a", "position: inherit", "right: 25px", "bottom: 20px");
    }
}
