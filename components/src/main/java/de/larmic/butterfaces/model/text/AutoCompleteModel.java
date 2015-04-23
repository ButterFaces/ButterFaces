package de.larmic.butterfaces.model.text;

import java.util.List;

/**
 * Created by larmic on 20.04.15.
 */
public interface AutoCompleteModel {

    List<String> autoComplete(final Object value);

}
