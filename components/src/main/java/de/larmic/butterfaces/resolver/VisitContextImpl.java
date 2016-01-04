/*
 * Copyright Lars Michaelis and Stephan Zerhusen 2016.
 * Distributed under the MIT License.
 * (See accompanying file README.md file or copy at http://opensource.org/licenses/MIT)
 */
package de.larmic.butterfaces.resolver;

import javax.faces.component.NamingContainer;
import javax.faces.component.UIComponent;
import javax.faces.component.visit.VisitCallback;
import javax.faces.component.visit.VisitContext;
import javax.faces.component.visit.VisitHint;
import javax.faces.component.visit.VisitResult;
import javax.faces.context.FacesContext;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumSet;
import java.util.Set;

/**
 * @author Lars Michaelis
 */
public class VisitContextImpl extends VisitContext {

    private final FacesContext facesContext;
    private final Set<VisitHint> hints;

    public VisitContextImpl(FacesContext facesContext) {
        this(facesContext, null);
    }

    public VisitContextImpl(FacesContext facesContext, Set<VisitHint> hints) {
        this.facesContext = facesContext;

        final EnumSet<VisitHint> hintsEnumSet = ((hints == null) || (hints.isEmpty()))
                ? EnumSet.noneOf(VisitHint.class)
                : EnumSet.copyOf(hints);

        this.hints = Collections.unmodifiableSet(hintsEnumSet);
    }

    @Override
    public VisitResult invokeVisitCallback(UIComponent component, VisitCallback callback) {
        return callback.visit(this, component);
    }

    @Override
    public Collection<String> getSubtreeIdsToVisit(UIComponent component) {
        if (!(component instanceof NamingContainer)) {
            throw new IllegalArgumentException("Component is not a NamingContainer: " + component);
        }

        return ALL_IDS;
    }

    @Override
    public FacesContext getFacesContext() {
        return facesContext;
    }

    @Override
    public Collection<String> getIdsToVisit() {
        return VisitContext.ALL_IDS;
    }

    @Override
    public Set<VisitHint> getHints() {
        return hints;
    }


}
