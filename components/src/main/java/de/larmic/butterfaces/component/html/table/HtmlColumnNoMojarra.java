/*
 * Copyright Lars Michaelis and Stephan Zerhusen 2016.
 * Distributed under the MIT License.
 * (See accompanying file README.md file or copy at http://opensource.org/licenses/MIT)
 */
package de.larmic.butterfaces.component.html.table;

import de.larmic.butterfaces.util.StringUtils;

import javax.el.ValueExpression;
import javax.faces.component.FacesComponent;
import javax.faces.component.UIComponentBase;

/**
 * This class is experimental and still in progress
 *
 * @author Lars Michaelis
 */
@FacesComponent(HtmlColumnNoMojarra.COMPONENT_TYPE)
public class HtmlColumnNoMojarra extends UIComponentBase {

    public static final String COMPONENT_TYPE = "de.larmic.butterfaces.component.columnNoMojarra";
    public static final String COMPONENT_FAMILY = "de.larmic.butterfaces.component.family";
    public static final String RENDERER_TYPE = "de.larmic.butterfaces.renderkit.html_basic.ColumnRendererNoMojarra";

    protected static final String PROPERTY_LABEL = "label";
    protected static final String PROPERTY_COL_WIDTH = "colWidth";
    protected static final String PROPERTY_UNIQUE_IDENTIFIER = "uniqueIdentifier";
    protected static final String PROPERTY_HIDE_COLUMN = "hideColumn";
    protected static final String PROPERTY_SORT_COLUMN_ENABLED = "sortColumnEnabled";
    protected static final String PROPERTY_SORT_BY = "sortBy";

    public HtmlColumnNoMojarra() {
        setRendererType(RENDERER_TYPE);
    }

    @Override
    public String getFamily() {
        return COMPONENT_FAMILY;
    }

    public String getLabel() {
        return (String) this.getStateHelper().eval(PROPERTY_LABEL);
    }

    public void setLabel(final String label) {
        this.updateStateHelper(PROPERTY_LABEL, label);
    }

    public String getColWidth() {
        return (String) this.getStateHelper().eval(PROPERTY_COL_WIDTH);
    }

    public void setColWidth(String colWidth) {
        this.updateStateHelper(PROPERTY_COL_WIDTH, colWidth);
    }

    public boolean isHideColumn() {
        final Object eval = this.getStateHelper().eval(PROPERTY_HIDE_COLUMN);
        return eval == null ? false : (Boolean) eval;
    }

    public void setHideColumn(boolean hideColumn) {
        this.updateStateHelper(PROPERTY_HIDE_COLUMN, hideColumn);
    }

    public String getModelUniqueIdentifier() {
        return StringUtils.getNotNullValue(getUniqueIdentifier(), getId());
    }

    public String getUniqueIdentifier() {
        return (String) this.getStateHelper().eval(PROPERTY_UNIQUE_IDENTIFIER);
    }

    public void setUniqueIdentifier(String uniqueIdentifier) {
        this.updateStateHelper(PROPERTY_UNIQUE_IDENTIFIER, uniqueIdentifier);
    }

    public boolean isSortColumnEnabled() {
        final Object eval = this.getStateHelper().eval(PROPERTY_SORT_COLUMN_ENABLED);
        return eval == null ? true : (Boolean) eval;
    }

    public void setSortColumnEnabled(boolean sortColumnEnabled) {
        this.updateStateHelper(PROPERTY_SORT_COLUMN_ENABLED, sortColumnEnabled);
    }

    public String getSortBy() {
        return (String) this.getStateHelper().eval(PROPERTY_SORT_BY);
    }

    public void setSortBy(String sortBy) {
        this.updateStateHelper(PROPERTY_SORT_BY, sortBy);
    }

    protected void updateStateHelper(final String propertyName, final Object value) {
        this.getStateHelper().put(propertyName, value);

        final ValueExpression ve = this.getValueExpression(propertyName);

        if (ve != null) {
            ve.setValue(this.getFacesContext().getELContext(), value);
        }
    }
}
