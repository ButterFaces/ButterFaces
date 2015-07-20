package de.larmic.butterfaces.component.html.table;

import de.larmic.butterfaces.component.partrenderer.StringUtils;
import de.larmic.butterfaces.event.TableSingleSelectionListener;
import de.larmic.butterfaces.model.json.Ordering;
import de.larmic.butterfaces.model.table.*;

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
import java.util.*;

/**
 * Created by larmic on 10.09.14.
 */
@ResourceDependencies({
        @ResourceDependency(library = "butterfaces-configurable", name = "jquery.min.js", target = "head"),
        @ResourceDependency(library = "butterfaces-configurable", name = "bootstrap.min.css", target = "head"),
        @ResourceDependency(library = "butterfaces-configurable", name = "bootstrap.min.js", target = "head"),
        @ResourceDependency(library = "butterfaces-css", name = "butterfaces-table.css", target = "head"),
        @ResourceDependency(library = "butterfaces-js", name = "butterfaces-bootstrap-fixes.jquery.js", target = "head"),
        @ResourceDependency(library = "butterfaces-js", name = "butterfaces-disableElements.jquery.js", target = "head"),
        @ResourceDependency(library = "butterfaces-js", name = "butterfaces-ajax.js", target = "head"),
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
    protected static final String PROPERTY_TABLE_ROW_CLASS = "rowClass";
    protected static final String PROPERTY_AJAX_DISABLE_RENDER_REGION_ON_REQUEST = "ajaxDisableRenderRegionsOnRequest";
    protected static final String PROPERTY_UNIQUE_IDENTIFIER = "uniqueIdentifier";

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
            // all children that are {@link HtmlColumn} or should be rendered
            for (UIComponent uiComponent : getChildren()) {
                if ((uiComponent instanceof HtmlColumn) && uiComponent.isRendered()) {
                    final HtmlColumn column = (HtmlColumn) uiComponent;
                    this.cachedColumns.add(column);
                }
            }
        }

        // clear "maybe" unsorted columns
        this.getChildren().clear();

        // sort columns by model if necessary
        if (getTableOrderModel() != null) {
            final List<HtmlColumn> notOrderedByModelColumnIdentifiers = new ArrayList<>();
            final List<Ordering> existingOrderings = new ArrayList<>();

            for (HtmlColumn cachedColumn : cachedColumns) {
                final Integer position = getTableOrderModel().getOrderPosition(getModelUniqueIdentifier(), cachedColumn.getModelUniqueIdentifier());
                if (position == null) {
                    notOrderedByModelColumnIdentifiers.add(cachedColumn);
                } else {
                    existingOrderings.add(new Ordering(cachedColumn.getModelUniqueIdentifier(), position));
                }
            }

            // in case of not ordered columns update table model
            if (!notOrderedByModelColumnIdentifiers.isEmpty()) {
                // order already existing column orderings
                Ordering.sort(existingOrderings);

                final List<String> orderings = new ArrayList<>();
                for (Ordering existingOrdering : existingOrderings) {
                    orderings.add(existingOrdering.getIdentifier());
                }
                for (HtmlColumn notOrderedByModelColumnIdentifier : notOrderedByModelColumnIdentifiers) {
                    orderings.add(notOrderedByModelColumnIdentifier.getModelUniqueIdentifier());
                }

                // update table model to sync model and
                final TableColumnOrdering ordering = new TableColumnOrdering(getModelUniqueIdentifier(), orderings);
                getTableOrderModel().update(ordering);
            }

            // sort columns by table model. Every column should be found.
            Collections.sort(cachedColumns, new Comparator<HtmlColumn>() {
                @Override
                public int compare(HtmlColumn o1, HtmlColumn o2) {
                    if (getTableOrderModel() != null) {
                        final Integer orderPosition = getTableOrderModel().getOrderPosition(getModelUniqueIdentifier(), o1.getModelUniqueIdentifier());
                        final Integer o2OrderPosition = getTableOrderModel().getOrderPosition(getModelUniqueIdentifier(), o2.getModelUniqueIdentifier());

                        if (orderPosition != null && o2OrderPosition != null) {
                            return orderPosition.compareTo(o2OrderPosition);
                        }
                    }
                    return 0;
                }
            });
        }

        // insert (sorted) {@link HtmlColumn}s.
        for (HtmlColumn cachedColumn : cachedColumns) {
            this.getChildren().add(cachedColumn);
        }

        return this.cachedColumns;
    }

    public boolean invokeOnComponent(FacesContext context, String clientId, ContextCallback callback) throws FacesException {
        int savedRowIndex = this.getRowIndex();

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
     */
    public boolean invokeOnComponentFromUIComponent(FacesContext context, String clientId, ContextCallback callback) throws FacesException {
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
            Iterator<UIComponent> itr = this.getFacetsAndChildren();

            while (itr.hasNext() && !found) {
                found = itr.next().invokeOnComponent(context, clientId,
                        callback);
            }
        }
        return found;
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

    public TableSortModel getTableSortModel() {
        final TableModel tableModel = this.getModel();
        return tableModel != null ? tableModel.getTableSortModel() : null;
    }

    public TableOrderModel getTableOrderModel() {
        final TableModel tableModel = this.getModel();
        return tableModel != null ? tableModel.getTableOrderModel() : null;
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
