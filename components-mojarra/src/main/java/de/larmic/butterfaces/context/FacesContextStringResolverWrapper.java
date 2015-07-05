package de.larmic.butterfaces.context;

import com.sun.faces.renderkit.html_basic.HtmlResponseWriter;

import javax.faces.context.FacesContext;
import javax.faces.context.FacesContextWrapper;
import javax.faces.context.ResponseWriter;
import java.io.StringWriter;

/**
 * Created by larmic on 03.07.15.
 */
public class FacesContextStringResolverWrapper extends FacesContextWrapper {

    private final FacesContext context;
    private final HtmlResponseWriter responseWriter;


    public FacesContextStringResolverWrapper(final FacesContext context, final StringWriter writer) {
        this.context = context;
        this.responseWriter = new HtmlResponseWriter(writer, context.getResponseWriter().getContentType(), context.getResponseWriter().getCharacterEncoding());
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
