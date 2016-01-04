/*
 * Copyright Lars Michaelis and Stephan Zerhusen 2016.
 * Distributed under the MIT License.
 * (See accompanying file README.md file or copy at http://opensource.org/licenses/MIT)
 */
package de.larmic.butterfaces.component.html.repeat.visitor;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import java.util.Iterator;

/**
 * @author Lars Michaelis
 */
public interface ChildrenTreeDataVisitorCallback {

    void setRowKey(FacesContext facesContext, Integer rowKey);
    boolean isRowAvailable();
    Iterator<UIComponent> dataChildren();

}
