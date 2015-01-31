package de.larmic.butterfaces.component.html.table;

import de.larmic.butterfaces.event.TableSingleSelectionListener;
import de.larmic.butterfaces.model.table.TableColumnDisplayModel;
import de.larmic.butterfaces.model.table.TableModel;
import de.larmic.butterfaces.model.table.TableSortModel;

import javax.el.ValueExpression;
import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;
import javax.faces.component.FacesComponent;
import javax.faces.component.UIComponent;
import javax.faces.component.UIData;
import javax.faces.component.behavior.ClientBehaviorHolder;
import java.util.*;

/**
 * Created by larmic on 10.09.14.
 */
@ResourceDependencies({
        @ResourceDependency(library = "butterfaces-configurable", name = "jquery.min.js", target = "head"),
        @ResourceDependency(library = "butterfaces-configurable", name = "bootstrap.min.css", target = "head"),
        @ResourceDependency(library = "butterfaces-configurable", name = "bootstrap.min.js", target = "head"),
        @ResourceDependency(library = "butterfaces-css", name = "butterfaces-table.css", target = "head"),
        @ResourceDependency(library = "butterfaces-js", name = "butterfaces-disableElements.jquery.js", target = "head"),
        @ResourceDependency(library = "butterfaces-js", name = "butterfaces-table.js", target = "head"),
        @ResourceDependency(library = "butterfaces-js", name = "butterfaces-table.jquery.js", target = "head")
})
@FacesComponent(HtmlTable.COMPONENT_TYPE)
public class HtmlTable extends UIData implements ClientBehaviorHolder {

    public static final String COMPONENT_TYPE = "de.larmic.butterfaces.component.table";
    public static final String COMPONENT_FAMILY = "de.larmic.butterfaces.component.family";
    public static final String RENDERER_TYPE = "de.larmic.butterfaces.renderkit.html_basic.TableRenderer";

    protected static final String PROPERTY_SINGLE_SELECTION_LISTENER = "singleSelectionListener";
    protected static final String PROPERTY_MODEL = "model";
    protected static final String PROPERTY_TABLE_CONDENSED = "tableCondensed";
    protected static final String PROPERTY_TABLE_BORDERED = "tableBordered";
    protected static final String PROPERTY_TABLE_STRIPED = "tableStriped";
    protected static final String PROPERTY_TABLE_SORT_UNDEFINED_CLASS = "sortUndefinedClass";
    protected static final String PROPERTY_TABLE_SORT_ASCENDING_CLASS = "sortAscendingClass";
    protected static final String PROPERTY_TABLE_SORT_DESCENDING_CLASS  = "sortDescendingClass";
    protected static final String PROPERTY_TABLE_ROW_CLASS  = "rowClass";

    private final List<HtmlColumn> cachedColumns = new ArrayList<>();

    public HtmlTable() {
        super();
        this.setRendererType(RENDERER_TYPE);
    }

    @Override
    public Collection<String> getEventNames() {
        return Arrays.asList("click");
    }

    @Override
    public String getDefaultEventName() {
        return "click";
    }

    @Override
    public String getFamily() {
        return COMPONENT_FAMILY;
    }

    public List<HtmlColumn> getCachedColumns() {
        final int childCount = this.getChildCount();
        if (childCount > 0 && this.cachedColumns.isEmpty()) {
            for (UIComponent kid : this.getChildren()) {
                if ((kid instanceof HtmlColumn) && kid.isRendered()) {
                    this.cachedColumns.add((HtmlColumn) kid);
                }
            }
            return this.cachedColumns;
        }

        return this.cachedColumns;
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

    public TableSortModel getTableSortModel() {
        final TableModel tableModel = this.getModel();
        return tableModel != null ? tableModel.getTableSortModel() : null;
    }

    public TableColumnDisplayModel getTableColumnDisplayModel() {
        final TableModel tableModel = this.getModel();
        return tableModel != null ? tableModel.getTableColumnDisplaxModel() : null;
    }

    public void setModel(TableModel tableModel) {
        this.updateStateHelper(PROPERTY_MODEL, tableModel);
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

    public String getSortUndefinedClass() {
        return (String) this.getStateHelper().eval(PROPERTY_TABLE_SORT_UNDEFINED_CLASS);
    }

    public void setSortUndefinedClass(String sortUndefinedClass) {
        this.updateStateHelper(PROPERTY_TABLE_SORT_UNDEFINED_CLASS, sortUndefinedClass);
    }

    public String getSortAscendingClass() {
        return (String) this.getStateHelper().eval(PROPERTY_TABLE_SORT_ASCENDING_CLASS);
    }

    public void setSortAscendingClass(String sortAscendingClass) {
        this.updateStateHelper(PROPERTY_TABLE_SORT_ASCENDING_CLASS, sortAscendingClass);
    }

    public String getSortDescendingClass() {
        return (String) this.getStateHelper().eval(PROPERTY_TABLE_SORT_DESCENDING_CLASS);
    }

    public void setSortDescendingClass(String sortDescendingClass) {
        this.updateStateHelper(PROPERTY_TABLE_SORT_DESCENDING_CLASS, sortDescendingClass);
    }

    public String getRowClass() {
        return (String) this.getStateHelper().eval(PROPERTY_TABLE_ROW_CLASS);
    }

    public void setRowClass(String rowClass) {
        this.updateStateHelper(PROPERTY_TABLE_ROW_CLASS, rowClass);
    }

    private void updateStateHelper(final String propertyName, final Object value) {
        this.getStateHelper().put(propertyName, value);

        final ValueExpression ve = this.getValueExpression(propertyName);

        if (ve != null) {
            ve.setValue(this.getFacesContext().getELContext(), value);
        }
    }
}
