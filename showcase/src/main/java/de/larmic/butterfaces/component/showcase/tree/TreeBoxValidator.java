package de.larmic.butterfaces.component.showcase.tree;

import de.larmic.butterfaces.model.tree.Node;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

@FacesValidator
public class TreeBoxValidator implements Validator {

    private static final String ERROR_MESSAGE = "Selecting root node is not allowed";

    @Override
    public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {
        if (value instanceof Node
                && "rootNode".equals(((Node) value).getTitle())) {
            final FacesMessage message = new FacesMessage(ERROR_MESSAGE);
            throw new ValidatorException(message);
        }
    }
}
