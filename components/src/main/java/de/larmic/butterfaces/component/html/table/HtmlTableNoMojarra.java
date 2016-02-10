/*
 * Copyright Lars Michaelis and Stephan Zerhusen 2016.
 * Distributed under the MIT License.
 * (See accompanying file README.md file or copy at http://opensource.org/licenses/MIT)
 */
package de.larmic.butterfaces.component.html.table;

import de.larmic.butterfaces.component.html.repeat.HtmlRepeat;
import de.larmic.butterfaces.event.TableSingleSelectionListener;
import de.larmic.butterfaces.model.json.Ordering;
import de.larmic.butterfaces.model.table.*;
import de.larmic.butterfaces.util.StringUtils;

import javax.el.ValueExpression;
import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;
import javax.faces.component.FacesComponent;
import javax.faces.component.UIComponent;
import javax.faces.component.behavior.ClientBehaviorHolder;
import java.util.*;

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
        @ResourceDependency(library = "butterfaces-dist-js", name = "butterfaces-ajax.js", target = "head"),
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

    protected static final String PROPERTY_AJAX_DISABLE_RENDER_REGION_ON_REQUEST = "ajaxDisableRenderRegionsOnRequest";
    protected static final String PROPERTY_MODEL = "model";
    protected static final String PROPERTY_TABLE_ROW_CLASS = "rowClass";
    protected static final String PROPERTY_STYLE_CLASS = "styleClass";
    protected static final String PROPERTY_STYLE = "style";

    protected static final String PROPERTY_SINGLE_SELECTION_LISTENER = "singleSelectionListener";

    private final List<HtmlColumnNoMojarra> cachedColumns = new ArrayList<>();

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

    // TODO is caching required? performance issue?
    public List<HtmlColumnNoMojarra> getCachedColumns() {
        final int childCount = this.getChildCount();
        if (childCount > 0 && this.cachedColumns.isEmpty()) {
            // all children that are {@link HtmlColumnNoMojarra} or should be rendered
            for (UIComponent uiComponent : getChildren()) {
                if ((uiComponent instanceof HtmlColumnNoMojarra) && uiComponent.isRendered()) {
                    final HtmlColumnNoMojarra column = (HtmlColumnNoMojarra) uiComponent;
                    this.cachedColumns.add(column);
                }
            }
        }

        // clear "maybe" unsorted columns
        this.getChildren().clear();

        // sort columns by model if necessary
        if (getTableOrderingModel() != null) {
            final List<HtmlColumnNoMojarra> notOrderedByModelColumnIdentifiers = new ArrayList<>();
            final List<Ordering> existingOrderings = new ArrayList<>();

            for (HtmlColumnNoMojarra cachedColumn : cachedColumns) {
                final Integer position = getTableOrderingModel().getOrderPosition(getModelUniqueIdentifier(), cachedColumn.getModelUniqueIdentifier());
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
                for (HtmlColumnNoMojarra notOrderedByModelColumnIdentifier : notOrderedByModelColumnIdentifiers) {
                    orderings.add(notOrderedByModelColumnIdentifier.getModelUniqueIdentifier());
                }

                // update table model to sync model and
                final TableColumnOrdering ordering = new TableColumnOrdering(getModelUniqueIdentifier(), orderings);
                getTableOrderingModel().update(ordering);
            }

            // sort columns by table model. Every column should be found.
            Collections.sort(cachedColumns, new Comparator<HtmlColumnNoMojarra>() {
                @Override
                public int compare(HtmlColumnNoMojarra o1, HtmlColumnNoMojarra o2) {
                    if (getTableOrderingModel() != null) {
                        final Integer orderPosition = getTableOrderingModel().getOrderPosition(getModelUniqueIdentifier(), o1.getModelUniqueIdentifier());
                        final Integer o2OrderPosition = getTableOrderingModel().getOrderPosition(getModelUniqueIdentifier(), o2.getModelUniqueIdentifier());

                        if (orderPosition != null && o2OrderPosition != null) {
                            return orderPosition.compareTo(o2OrderPosition);
                        }
                    }
                    return 0;
                }
            });
        }

        // insert (sorted) {@link HtmlColumn}s.
        for (HtmlColumnNoMojarra cachedColumn : cachedColumns) {
            this.getChildren().add(cachedColumn);
        }

        return this.cachedColumns;
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

    public String getRowClass() {
        return (String) this.getStateHelper().eval(PROPERTY_TABLE_ROW_CLASS);
    }

    public void setRowClass(String rowClass) {
        this.updateStateHelper(PROPERTY_TABLE_ROW_CLASS, rowClass);
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
