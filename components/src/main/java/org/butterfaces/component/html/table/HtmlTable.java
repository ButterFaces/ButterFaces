/*
 * Copyright Lars Michaelis and Stephan Zerhusen 2016.
 * Distributed under the MIT License.
 * (See accompanying file README.md file or copy at http://opensource.org/licenses/MIT)
 */
package org.butterfaces.component.html.table;

import org.butterfaces.component.renderkit.html_basic.table.cache.TableColumnCache;
import org.butterfaces.event.TableSingleSelectionListener;
import org.butterfaces.model.table.TableColumnOrderingModel;
import org.butterfaces.model.table.TableColumnVisibilityModel;
import org.butterfaces.model.table.TableModel;
import org.butterfaces.model.table.TableRowSortingModel;
import org.butterfaces.util.StringUtils;
import org.butterfaces.component.renderkit.html_basic.table.cache.TableColumnCache;
import org.butterfaces.event.TableSingleSelectionListener;
import org.butterfaces.util.StringUtils;

import javax.el.ValueExpression;
import javax.faces.FacesException;
import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;
import javax.faces.component.ContextCallback;
import javax.faces.component.FacesComponent;
import javax.faces.component.UIComponent;
import javax.faces.component.UIData;
import javax.faces.component.behavior.ClientBehaviorHolder;
import javax.faces.context.FacesContext;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

/**
 * @author Lars Michaelis
 */
@ResourceDependencies({
        @ResourceDependency(library = "butterfaces-dist-css", name = "butterfaces-table.css", target = "head"),
        @ResourceDependency(library = "butterfaces-dist-css", name = "butterfaces-overlay.css", target = "head"),
        @ResourceDependency(library = "butterfaces-dist-css", name = "dist-butterfaces-bootstrap.css", target = "head"),
        @ResourceDependency(library = "butterfaces-dist-bundle-dev-js", name = "butterfaces-third-party-jquery.js", target = "head"),
        @ResourceDependency(library = "butterfaces-dist-bundle-dev-js", name = "butterfaces-third-party-bootstrap.js", target = "head"),
        @ResourceDependency(library = "butterfaces-dist-js", name = "butterfaces-overlay.js", target = "head"),
        @ResourceDependency(library = "butterfaces-dist-js", name = "butterfaces-ajax.js", target = "head"),
        @ResourceDependency(library = "butterfaces-dist-js", name = "butterfaces-table.jquery.js", target = "head")
})
@FacesComponent(HtmlTable.COMPONENT_TYPE)
public class HtmlTable extends UIData implements ClientBehaviorHolder {

    public static final String COMPONENT_TYPE = "org.butterfaces.component.table.new";
    public static final String COMPONENT_FAMILY = "org.butterfaces.component.family";
    public static final String RENDERER_TYPE = "org.butterfaces.renderkit.html_basic.TableRenderer.new";

    protected static final String PROPERTY_UNIQUE_IDENTIFIER = "uniqueIdentifier";
    protected static final String PROPERTY_TABLE_CONDENSED = "tableCondensed";
    protected static final String PROPERTY_TABLE_BORDERED = "tableBordered";
    protected static final String PROPERTY_TABLE_STRIPED = "tableStriped";

    protected static final String PROPERTY_AJAX_DISABLE_RENDER_REGION_ON_REQUEST = "ajaxDisableRenderRegionsOnRequest";
    protected static final String PROPERTY_MODEL = "model";
    protected static final String PROPERTY_TABLE_ROW_CLASS = "rowClass";
    protected static final String PROPERTY_STYLE_CLASS = "styleClass";
    protected static final String PROPERTY_STYLE = "style";

    protected static final String PROPERTY_SINGLE_SELECTION_LISTENER = "singleSelectionListener";

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

    /**
     * Returns a <code>TableColumnCache</code> object containing details such
     * as row and column classes, cachedColumns, and a mechanism for scrolling through
     * the row/column classes.
     *
     * @param context the <code>FacesContext</code> for the current request
     * @return the <code>TableColumnCache</code> for provided table
     */
    public TableColumnCache getTableColumnCache(FacesContext context) {
        final String key = createKey(this);
        final Map<Object, Object> attributes = context.getAttributes();
        TableColumnCache info = (TableColumnCache) attributes.get(key);
        if (info == null) {
            info = new TableColumnCache(this);
            attributes.put(key, info);
        }
        return info;
    }


    /**
     * Removes the cached TableColumnCache from the specified component.
     *
     * @param context the <code>FacesContext</code> for the current request
     * @param table   the table from which the TableColumnCache will be removed
     */
    public void clearMetaInfo(FacesContext context, UIComponent table) {
        context.getAttributes().remove(createKey(table));
    }

    /**
     * Creates a unique key based on the provided <code>UIComponent</code> with
     * which the TableColumnCache can be looked up.
     *
     * @param table the table that's being rendered
     * @return a unique key to store the metadata in the request and still have
     * it associated with a specific component.
     */
    private String createKey(UIComponent table) {
        return TableColumnCache.KEY + '_' + table.hashCode();
    }

    @Override
    public boolean invokeOnComponent(final FacesContext context,
                                     final String clientId,
                                     final ContextCallback callback) throws FacesException {
        final int savedRowIndex = this.getRowIndex();

        try {
            return super.invokeOnComponent(context, clientId, callback);
        } catch (Exception e) {
            // This error will occur if a composite component is used as column child.
            this.setRowIndex(savedRowIndex);
            return invokeOnComponentFromUIComponent(context, clientId, callback);
        }

    }

    /**
     * Copy from {@link UIComponent#invokeOnComponent} because super call will trigger {@link UIData#invokeOnComponent(FacesContext, String, ContextCallback)}.
     *
     * @param context  context
     * @param clientId table client id
     * @param callback callback
     * @return true if component is found
     */
    private boolean invokeOnComponentFromUIComponent(final FacesContext context,
                                                     final String clientId,
                                                     final ContextCallback callback) throws FacesException {
        if (null == context || null == clientId || null == callback) {
            throw new NullPointerException();
        }

        boolean found = false;
        if (clientId.equals(this.getClientId(context))) {
            try {
                this.pushComponentToEL(context, this);
                callback.invokeContextCallback(context, this);
                return true;
            } catch (Exception e) {
                throw new FacesException(e);
            } finally {
                this.popComponentFromEL(context);
            }
        } else {
            final Iterator<UIComponent> itr = this.getFacetsAndChildren();

            while (itr.hasNext() && !found) {
                found = itr.next().invokeOnComponent(context, clientId, callback);
            }
        }
        return found;
    }

    public boolean isHideColumn(final HtmlColumn column) {
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

    public String getStyleClass() {
        return (String) this.getStateHelper().eval(PROPERTY_STYLE_CLASS);
    }

    public void setStyleClass(String styleClass) {
        this.updateStateHelper(PROPERTY_STYLE_CLASS, styleClass);
    }

    public String getStyle() {
        return (String) this.getStateHelper().eval(PROPERTY_STYLE);
    }

    public void setStyle(String style) {
        this.updateStateHelper(PROPERTY_STYLE, style);
    }

    public String getModelUniqueIdentifier() {
        return StringUtils.getNotNullValue(getUniqueIdentifier(), getId());
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

    public void setModel(TableModel tableModel) {
        this.updateStateHelper(PROPERTY_MODEL, tableModel);
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

    public String getRowClass() {
        return (String) this.getStateHelper().eval(PROPERTY_TABLE_ROW_CLASS);
    }

    public void setRowClass(String rowClass) {
        this.updateStateHelper(PROPERTY_TABLE_ROW_CLASS, rowClass);
    }

    public boolean isAjaxDisableRenderRegionsOnRequest() {
        final Object eval = this.getStateHelper().eval(PROPERTY_AJAX_DISABLE_RENDER_REGION_ON_REQUEST);
        return eval == null ? true : (Boolean) eval;
    }

    public void setAjaxDisableRenderRegionsOnRequest(boolean ajaxDisableRenderRegionsOnRequest) {
        this.updateStateHelper(PROPERTY_AJAX_DISABLE_RENDER_REGION_ON_REQUEST, ajaxDisableRenderRegionsOnRequest);
    }

    private void updateStateHelper(final String propertyName, final Object value) {
        this.getStateHelper().put(propertyName, value);

        final ValueExpression ve = this.getValueExpression(propertyName);

        if (ve != null) {
            ve.setValue(this.getFacesContext().getELContext(), value);
        }
    }
}
