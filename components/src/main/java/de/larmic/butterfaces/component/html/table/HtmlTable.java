package de.larmic.butterfaces.component.html.table;

import de.larmic.butterfaces.event.TableSingleSelectionListener;
import de.larmic.butterfaces.model.table.TableModel;

import javax.el.ValueExpression;
import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;
import javax.faces.component.FacesComponent;
import javax.faces.component.UIData;
import javax.faces.component.behavior.ClientBehaviorHolder;
import java.util.Arrays;
import java.util.Collection;

/**
 * Created by larmic on 10.09.14.
 */
@ResourceDependencies({
        @ResourceDependency(library = "butterfaces-configurable", name = "jquery-2.1.1.min.js", target = "head"),
        @ResourceDependency(library = "butterfaces-configurable", name = "bootstrap.min.css", target = "head"),
        @ResourceDependency(library = "butterfaces-configurable", name = "bootstrap.min.js", target = "head"),
        @ResourceDependency(library = "css", name = "butterfaces-table.css", target = "head"),
        @ResourceDependency(library = "js", name = "butterfaces-bootstrap-fixes.jquery.js", target = "head"),
        @ResourceDependency(library = "js", name = "butterfaces-table.js", target = "head"),
        @ResourceDependency(library = "js", name = "butterfaces-table.jquery.js", target = "head")
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
    protected static final String PROPERTY_TABLE_SHOW_REFRESH_BUTTON = "showRefreshButton";
    protected static final String PROPERTY_TABLE_SHOW_TOGGLE_COLUMN_BUTTON = "showToggleColumnButton";

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

    public TableSingleSelectionListener getSingleSelectionListener() {
        return (TableSingleSelectionListener) this.getStateHelper().eval(PROPERTY_SINGLE_SELECTION_LISTENER);
    }

    public void setSingleSelectionListener(TableSingleSelectionListener singleSelectionListener) {
        this.updateStateHelper(PROPERTY_SINGLE_SELECTION_LISTENER, singleSelectionListener);
    }

    public TableModel getModel() {
        return (TableModel) this.getStateHelper().eval(PROPERTY_MODEL);
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

    public boolean isShowRefreshButton() {
        final Object eval = this.getStateHelper().eval(PROPERTY_TABLE_SHOW_REFRESH_BUTTON);
        return eval == null ? true : (Boolean) eval;
    }

    public void setShowRefreshButton(boolean showRefreshButton) {
        this.updateStateHelper(PROPERTY_TABLE_SHOW_REFRESH_BUTTON, showRefreshButton);
    }

    public boolean isShowToggleColumnButton() {
        final Object eval = this.getStateHelper().eval(PROPERTY_TABLE_SHOW_TOGGLE_COLUMN_BUTTON);
        return eval == null ? true : (Boolean) eval;
    }

    public void setShowToggleColumnButton(boolean showToggleColumnButton) {
        this.updateStateHelper(PROPERTY_TABLE_SHOW_TOGGLE_COLUMN_BUTTON, showToggleColumnButton);
    }

    private void updateStateHelper(final String propertyName, final Object value) {
        this.getStateHelper().put(propertyName, value);

        final ValueExpression ve = this.getValueExpression(propertyName);

        if (ve != null) {
            ve.setValue(this.getFacesContext().getELContext(), value);
        }
    }
}
