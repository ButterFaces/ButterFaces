package org.butterfaces.component.html.text.validator;

import org.butterfaces.component.html.text.HtmlNumber;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

public class DefaultNumberValidator implements Validator {

    @Override
    public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {
        final HtmlNumber htmlNumber = (HtmlNumber) component;

        try {
            final long longValue = Long.parseLong(value.toString());

            if (htmlNumber.getMin() != null && !"".equals(htmlNumber.getMin()) && longValue < Integer.valueOf(htmlNumber.getMin())) {
                throw new ValidatorException(new FacesMessage("Number is to small", String.format("%s is to small", value)));
            }

            if (htmlNumber.getMax() != null && !"".equals(htmlNumber.getMax()) && longValue > Integer.valueOf(htmlNumber.getMax())) {
                throw new ValidatorException(new FacesMessage("Number is to big", String.format("%s is to big", value)));
            }
        } catch (NumberFormatException e) {
            throw new ValidatorException(new FacesMessage("No number", String.format("%s is no number", value)));
        }
    }
}
