/*
 * Copyright Lars Michaelis and Stephan Zerhusen 2016.
 * Distributed under the MIT License.
 * (See accompanying file README.md file or copy at http://opensource.org/licenses/MIT)
 */
package org.butterfaces.component.html.repeat.model;

import javax.faces.model.*;
import java.sql.ResultSet;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * @author Lars Michaelis
 */
public class DataModelWrapperFactory {

    public static DataModelWrapper createDataModelWrapper(Object value) {
        return new DataModelWrapper(convertToJsfDataModel(value));
    }

    private static DataModel<?> convertToJsfDataModel(Object value) {
        DataModel<?> model = null;

        if (value == null) {
            model = new ListDataModel(Collections.EMPTY_LIST);
        } else if (value instanceof DataModel) {
            model = (DataModel) value;
        } else if (value instanceof Collection) {
            model = new ListDataModel((List) value);
        } else if (Object[].class.isAssignableFrom(value.getClass())) {
            model = new ArrayDataModel((Object[]) value);
        } else if (value instanceof ResultSet) {
            model = new ResultSetDataModel((ResultSet) value);
        } else {
            model = new ScalarDataModel(value);
        }

        return model;
    }

}
