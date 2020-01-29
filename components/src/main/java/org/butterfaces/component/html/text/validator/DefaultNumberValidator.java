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

        if (lowerLimitIsSet(htmlNumber)) {
            validateValueIsBiggerThan(value, Integer.parseInt(htmlNumber.getMin()));
        }

        if (upperLimitIsSet(htmlNumber)) {
            validateValueIsLowerThan(value, Integer.parseInt(htmlNumber.getMax()));
        }
    }

    private boolean lowerLimitIsSet(HtmlNumber htmlNumber) {
        return htmlNumber.getMin() != null && !"".equals(htmlNumber.getMin());
    }

    private boolean upperLimitIsSet(HtmlNumber htmlNumber) {
        return htmlNumber.getMax() != null && !"".equals(htmlNumber.getMax());
    }

    private void validateValueIsBiggerThan(final Object value, final int upperLimit) {
        final int intValue = parseValue(value);

        if (intValue < upperLimit) {
            throw new ValidatorException(new FacesMessage("Number is to small", String.format("%s is to small", value)));
        }
    }

    private void validateValueIsLowerThan(final Object value, final int lowerLimit) {
        final int intValue = parseValue(value);

        if (intValue > lowerLimit) {
            throw new ValidatorException(new FacesMessage("Number is to big", String.format("%s is to big", value)));
        }
    }

    private int parseValue(Object value) {
        if (value == null || "".equals(value.toString().trim())) {
            throw new ValidatorException(new FacesMessage("No number", String.format("%s is no number", value)));
        }

        try {
            return Integer.parseInt(value.toString());
        } catch (NumberFormatException e) {
            throw new ValidatorException(new FacesMessage("No number", String.format("%s is no number", value)));
        }
    }
}
