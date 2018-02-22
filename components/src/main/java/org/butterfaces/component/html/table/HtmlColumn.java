/*
 * Copyright Lars Michaelis and Stephan Zerhusen 2016.
 * Distributed under the MIT License.
 * (See accompanying file README.md file or copy at http://opensource.org/licenses/MIT)
 */
package org.butterfaces.component.html.table;

import org.butterfaces.component.html.feature.Label;
import org.butterfaces.component.html.feature.Tooltip;
import org.butterfaces.resolver.WebXmlParameters;
import org.butterfaces.util.StringUtils;
import org.butterfaces.component.html.feature.Label;
import org.butterfaces.component.html.feature.Tooltip;
import org.butterfaces.util.StringUtils;

import javax.el.ValueExpression;
import javax.faces.component.FacesComponent;
import javax.faces.component.UIColumn;

/**
 * @author Lars Michaelis
 */
@FacesComponent(HtmlColumn.COMPONENT_TYPE)
public class HtmlColumn extends UIColumn implements Tooltip, Label {

    public static final String COMPONENT_TYPE = "org.butterfaces.component.column.new";
    public static final String COMPONENT_FAMILY = "org.butterfaces.component.family";
    public static final String RENDERER_TYPE = "org.butterfaces.renderkit.html_basic.ColumnRenderer.new";

    protected static final String PROPERTY_LABEL = "label";
    protected static final String PROPERTY_COL_WIDTH = "colWidth";
    protected static final String PROPERTY_UNIQUE_IDENTIFIER = "uniqueIdentifier";
    protected static final String PROPERTY_HIDE_COLUMN = "hideColumn";
    protected static final String PROPERTY_SORT_COLUMN_ENABLED = "sortColumnEnabled";
    protected static final String PROPERTY_SORT_BY = "sortBy";
    protected static final String PROPERTY_HEADER_STYLE_CLASS = "headerStyleClass";
    protected static final String PROPERTY_HEADER_STYLE = "headerStyle";
    protected static final String PROPERTY_STYLE_CLASS = "styleClass";
    protected static final String PROPERTY_STYLE = "style";

    // dirty: should find a better way to update field in table renderer
    private int columnNumberUsedByTable;
    private WebXmlParameters webXmlParameters;

    public HtmlColumn() {
        super();
        this.setRendererType(RENDERER_TYPE);
    }

    public String getModelUniqueIdentifier() {
        return StringUtils.getNotNullValue(getUniqueIdentifier(), getId());
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

    public String getUniqueIdentifier() {
        return (String) this.getStateHelper().eval(PROPERTY_UNIQUE_IDENTIFIER);
    }

    public void setUniqueIdentifier(String uniqueIdentifier) {
        this.updateStateHelper(PROPERTY_UNIQUE_IDENTIFIER, uniqueIdentifier);
    }

    public String getHeaderStyleClass() {
        return (String) this.getStateHelper().eval(PROPERTY_HEADER_STYLE_CLASS);
    }

    public void setHeaderStyleClass(String headerStyleClass) {
        this.updateStateHelper(PROPERTY_HEADER_STYLE_CLASS, headerStyleClass);
    }

    public String getStyleClass() {
        return (String) this.getStateHelper().eval(PROPERTY_STYLE_CLASS);
    }

    public void setStyleClass(String styleClass) {
        this.updateStateHelper(PROPERTY_STYLE_CLASS, styleClass);
    }

    public String getHeaderStyle() {
        return (String) this.getStateHelper().eval(PROPERTY_HEADER_STYLE);
    }

    public void setHeaderStyle(String headerStyle) {
        this.updateStateHelper(PROPERTY_HEADER_STYLE, headerStyle);
    }

    public String getStyle() {
        return (String) this.getStateHelper().eval(PROPERTY_STYLE);
    }

    public void setStyle(String style) {
        this.updateStateHelper(PROPERTY_STYLE, style);
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

    public int getColumnNumberUsedByTable() {
        return columnNumberUsedByTable;
    }

    public void setColumnNumberUsedByTable(int columnNumberUsedByTable) {
        this.columnNumberUsedByTable = columnNumberUsedByTable;
    }

    public WebXmlParameters getWebXmlParameters() {
        return webXmlParameters;
    }

    public void setWebXmlParameters(WebXmlParameters webXmlParameters) {
        this.webXmlParameters = webXmlParameters;
    }

    private void updateStateHelper(final String propertyName, final Object value) {
        this.getStateHelper().put(propertyName, value);

        final ValueExpression ve = this.getValueExpression(propertyName);

        if (ve != null) {
            ve.setValue(this.getFacesContext().getELContext(), value);
        }
    }

}
