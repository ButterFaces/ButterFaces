/*
 * Copyright Lars Michaelis and Stephan Zerhusen 2016.
 * Distributed under the MIT License.
 * (See accompanying file README.md file or copy at http://opensource.org/licenses/MIT)
 */
package org.butterfaces.component.html.repeat.visitor;

import javax.faces.component.UIComponent;
import javax.faces.component.visit.VisitCallback;
import javax.faces.component.visit.VisitContext;
import javax.faces.context.FacesContext;
import java.util.Iterator;

/**
 * @author Lars Michaelis
 */
public class ChildrenTreeDataVisitor implements DataVisitor {

    private final VisitCallback callback;
    private final VisitContext visitContext;
    private final ChildrenTreeDataVisitorCallback childrenTreeDataVisitorCallback;
    private boolean visitResult;

    public ChildrenTreeDataVisitor(VisitCallback callback,
                                   VisitContext visitContext,
                                   ChildrenTreeDataVisitorCallback childrenTreeDataVisitorCallback1) {
        this.callback = callback;
        this.visitContext = visitContext;
        this.childrenTreeDataVisitorCallback = childrenTreeDataVisitorCallback1;
    }

    public DataVisitResult process(FacesContext context, Integer rowKey) {
        childrenTreeDataVisitorCallback.setRowKey(context, rowKey);

        if (childrenTreeDataVisitorCallback.isRowAvailable()) {
            final Iterator<UIComponent> componentIterator = childrenTreeDataVisitorCallback.dataChildren();

            while (componentIterator.hasNext()) {
                final UIComponent dataChild = componentIterator.next();

                if (dataChild.visitTree(visitContext, callback)) {
                    visitResult = true;

                    return DataVisitResult.STOP;
                }
            }
        }

        return DataVisitResult.CONTINUE;
    }

    public boolean getVisitResult() {
        return visitResult;
    }

}
