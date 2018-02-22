package org.butterfaces.context;

import javax.faces.context.FacesContext;
import javax.faces.context.FacesContextWrapper;
import javax.faces.context.ResponseWriter;
import java.io.StringWriter;

/**
 * Wrappes {@link FacesContext} and inner {@link ResponseWriter} to enable {@link StringWriter}.
 */
public class FacesContextStringResolverWrapper extends FacesContextWrapper {

    private final StringWriter stringWriter;
    private final FacesContext context;
    private final ResponseWriter responseWriter;

    public FacesContextStringResolverWrapper(final FacesContext context) {
        this.stringWriter = new StringWriter();
        this.context = context;
        this.responseWriter = context.getResponseWriter().cloneWithWriter(stringWriter);
    }

    public StringWriter getStringWriter() {
        return stringWriter;
    }

    @Override
    public FacesContext getWrapped() {
        return context;
    }

    @Override
    public ResponseWriter getResponseWriter() {
        return responseWriter;
    }
}
