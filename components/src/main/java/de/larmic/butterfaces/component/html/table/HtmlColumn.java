package de.larmic.butterfaces.component.html.table;

import javax.el.ValueExpression;
import javax.faces.component.FacesComponent;
import javax.faces.component.UIColumn;

/**
 * Created by larmic on 10.09.14.
 */
@FacesComponent(HtmlColumn.COMPONENT_TYPE)
public class HtmlColumn extends UIColumn {

    public static final String COMPONENT_TYPE = "de.larmic.butterfaces.component.column";
    public static final String COMPONENT_FAMILY = "de.larmic.butterfaces.component.family";
    public static final String RENDERER_TYPE = "de.larmic.butterfaces.renderkit.html_basic.ColumnRenderer";

    protected static final String PROPERTY_LABEL = "label";
    protected static final String PROPERTY_COL_WIDTH = "colWidth";
    protected static final String PROPERTY_HIDE_COLUMN = "hideColumn";
    protected static final String PROPERTY_SORT_COLUMN_ENABLED = "sortColumnEnabled";
    protected static final String PROPERTY_SORT_BY = "sortBy";

    public HtmlColumn() {
        super();
        this.setRendererType(RENDERER_TYPE);
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

    private void updateStateHelper(final String propertyName, final Object value) {
        this.getStateHelper().put(propertyName, value);

        final ValueExpression ve = this.getValueExpression(propertyName);

        if (ve != null) {
            ve.setValue(this.getFacesContext().getELContext(), value);
        }
    }
}
