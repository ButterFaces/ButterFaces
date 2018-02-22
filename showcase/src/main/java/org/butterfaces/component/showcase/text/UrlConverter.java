package org.butterfaces.component.showcase.text;

import org.apache.commons.validator.routines.UrlValidator;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;

@FacesConverter("urlConverter")
public class UrlConverter implements Converter {

    public static final String HTTP = "http://";

    @Override
    public Object getAsObject(final FacesContext context,
                              final UIComponent component,
                              final String value) {
        final StringBuilder url = new StringBuilder();

        this.appendHttpIfNecessary(value, url);

        //use Apache common URL validator to validate URL
        if(!new UrlValidator().isValid(url.toString())){
            final FacesMessage msg = new FacesMessage("URL Conversion error.", "Invalid URL format.");
            msg.setSeverity(FacesMessage.SEVERITY_ERROR);
            throw new ConverterException(msg);
        }

        return new URLBookmark(url.toString());
    }

    @Override
    public String getAsString(final FacesContext context,
                              final UIComponent component,
                              final Object value) {
        return value.toString();
    }

    private void appendHttpIfNecessary(final String value,
                                       final StringBuilder url) {
        if(!value.startsWith(HTTP, 0)){
            url.append(HTTP);
        }
        url.append(value);
    }
}
