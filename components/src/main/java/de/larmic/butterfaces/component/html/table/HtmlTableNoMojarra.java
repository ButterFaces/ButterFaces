/*
 * Copyright Lars Michaelis and Stephan Zerhusen 2016.
 * Distributed under the MIT License.
 * (See accompanying file README.md file or copy at http://opensource.org/licenses/MIT)
 */
package de.larmic.butterfaces.component.html.table;

import de.larmic.butterfaces.component.html.repeat.HtmlRepeat;
import de.larmic.butterfaces.event.TableSingleSelectionListener;
import de.larmic.butterfaces.model.table.TableColumnOrderingModel;
import de.larmic.butterfaces.model.table.TableColumnVisibilityModel;
import de.larmic.butterfaces.model.table.TableModel;
import de.larmic.butterfaces.model.table.TableRowSortingModel;
import de.larmic.butterfaces.util.StringUtils;

import javax.el.ValueExpression;
import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;
import javax.faces.component.FacesComponent;
import javax.faces.component.behavior.ClientBehaviorHolder;
import java.util.Arrays;
import java.util.Collection;

/**
 * This class is experimental and still in progress
 *
 * @author Lars Michaelis
 */
@ResourceDependencies({
        @ResourceDependency(library = "butterfaces-dist-bower", name = "jquery.js", target = "head"),
        @ResourceDependency(library = "butterfaces-dist-bower", name = "bootstrap.css", target = "head"),
        @ResourceDependency(library = "butterfaces-dist-bower", name = "bootstrap.js", target = "head"),
        @ResourceDependency(library = "butterfaces-dist-css", name = "butterfaces-table.css", target = "head"),
        @ResourceDependency(library = "butterfaces-dist-js", name = "butterfaces-table.jquery.js", target = "head")
})
@FacesComponent(HtmlTableNoMojarra.COMPONENT_TYPE)
public class HtmlTableNoMojarra extends HtmlRepeat implements ClientBehaviorHolder {

    public static final String COMPONENT_TYPE = "de.larmic.butterfaces.component.tableNoMojarra";
    public static final String COMPONENT_FAMILY = "de.larmic.butterfaces.component.family";
    public static final String RENDERER_TYPE = "de.larmic.butterfaces.renderkit.html_basic.TableRendererNoMojarra";

    protected static final String PROPERTY_UNIQUE_IDENTIFIER = "uniqueIdentifier";
    protected static final String PROPERTY_TABLE_CONDENSED = "tableCondensed";
    protected static final String PROPERTY_TABLE_BORDERED = "tableBordered";
    protected static final String PROPERTY_TABLE_STRIPED = "tableStriped";

    protected static final String PROPERTY_MODEL = "model";

    protected static final String PROPERTY_SINGLE_SELECTION_LISTENER = "singleSelectionListener";

    public HtmlTableNoMojarra() {
        setRendererType(RENDERER_TYPE);
    }

    @Override
    public String getFamily() {
        return COMPONENT_FAMILY;
    }

    @Override
    public Collection<String> getEventNames() {
        return Arrays.asList("click");
    }

    @Override
    public String getDefaultEventName() {
        return "click";
    }

    public boolean isHideColumn(final HtmlColumnNoMojarra column) {
        if (getTableColumnVisibilityModel() != null) {
            final String tableUniqueIdentifier = getModelUniqueIdentifier();
            final String columnUniqueIdentifier = column.getModelUniqueIdentifier();
            final Boolean hideColumn = getTableColumnVisibilityModel().isColumnHidden(tableUniqueIdentifier, columnUniqueIdentifier);
            if (hideColumn != null) {
                return hideColumn;
            }
        }
        return column.isHideColumn();
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

    public boolean isTableCondensed() {
        final Object eval = this.getStateHelper().eval(PROPERTY_TABLE_CONDENSED);
        return eval == null ? false : (Boolean) eval;
    }

    public void setTableCondensed(boolean tableCondensed) {
        this.updateStateHelper(PROPERTY_TABLE_CONDENSED, tableCondensed);
    }

    public boolean isTableBordered() {
        final Object eval = this.getStateHelper().eval(PROPERTY_TABLE_BORDERED);
        return eval == null ? false : (Boolean) eval;
    }

    public void setTableBordered(boolean tableBordered) {
        this.updateStateHelper(PROPERTY_TABLE_BORDERED, tableBordered);
    }

    public boolean isTableStriped() {
        final Object eval = this.getStateHelper().eval(PROPERTY_TABLE_STRIPED);
        return eval == null ? true : (Boolean) eval;
    }

    public void setTableStriped(boolean tableStriped) {
        this.updateStateHelper(PROPERTY_TABLE_STRIPED, tableStriped);
    }

    public TableSingleSelectionListener getSingleSelectionListener() {
        return (TableSingleSelectionListener) this.getStateHelper().eval(PROPERTY_SINGLE_SELECTION_LISTENER);
    }

    public void setSingleSelectionListener(TableSingleSelectionListener singleSelectionListener) {
        this.updateStateHelper(PROPERTY_SINGLE_SELECTION_LISTENER, singleSelectionListener);
    }

    public TableModel getModel() {
        return (TableModel) this.getStateHelper().eval(PROPERTY_MODEL);
    }

    public TableRowSortingModel getTableSortModel() {
        final TableModel tableModel = this.getModel();
        return tableModel != null ? tableModel.getTableRowSortingModel() : null;
    }

    public TableColumnOrderingModel getTableOrderingModel() {
        final TableModel tableModel = this.getModel();
        return tableModel != null ? tableModel.getTableColumnOrderingModel() : null;
    }

    public TableColumnVisibilityModel getTableColumnVisibilityModel() {
        final TableModel tableModel = this.getModel();
        return tableModel != null ? tableModel.getTableColumnVisibilityModel() : null;
    }

    private void updateStateHelper(final String propertyName, final Object value) {
        this.getStateHelper().put(propertyName, value);

        final ValueExpression ve = this.getValueExpression(propertyName);

        if (ve != null) {
            ve.setValue(this.getFacesContext().getELContext(), value);
        }
    }
}
