/*
 * Copyright Lars Michaelis and Stephan Zerhusen 2016.
 * Distributed under the MIT License.
 * (See accompanying file README.md file or copy at http://opensource.org/licenses/MIT)
 */
package org.butterfaces.component.html.repeat.visitor;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import java.io.IOException;
import java.util.Iterator;

/**
 * @author Lars Michaelis
 */
public abstract class ChildrenComponentVisitor implements DataVisitor {

    private final ChildrenTreeDataVisitorCallback childrenTreeDataVisitorCallback;

    protected ChildrenComponentVisitor(ChildrenTreeDataVisitorCallback childrenTreeDataVisitorCallback) {
        this.childrenTreeDataVisitorCallback = childrenTreeDataVisitorCallback;
    }

    @Override
    public DataVisitResult process(FacesContext context, Integer rowKey) throws IOException {
        childrenTreeDataVisitorCallback.setRowKey(context, rowKey);

        if (childrenTreeDataVisitorCallback.isRowAvailable()) {
            final Iterator<UIComponent> childIterator = childrenTreeDataVisitorCallback.dataChildren();

            while (childIterator.hasNext()) {
                final UIComponent component = childIterator.next();
                final UIComponent parent = component.getParent();

                if (!parent.isRendered()) {
                    continue;
                }

                processComponent(context, component);
            }
        }

        return DataVisitResult.CONTINUE;
    }

    public abstract void processComponent(FacesContext context, UIComponent c);

}
