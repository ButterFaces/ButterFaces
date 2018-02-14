package org.butterfaces.model.table;

/**
 * Just a table model wrapper to allow binding one attribute (model) to table component. Each specific table model
 * could be null. In this case corresponding feature is not available.
 */
public interface TableModel {

    /**
     * Only works if at least one f:ajax is used on table toolbar component. Maybe it is better to create custom
     * ajax event type for this model (like both other models have) but up to know no specific event type is needed.
     *
     * @return null if no sort model is used. This means sort cachedColumns is not available.
     */
    TableRowSortingModel getTableRowSortingModel();

    /**
     * Only works if f:ajax event="toggle" is used on table toolbar component.
     *
     * @return null if no column visibility (show and hide) is used.
     */
    TableColumnVisibilityModel getTableColumnVisibilityModel();

    /**
     * Only works if f:ajax event="order" is used on table toolbar component.
     *
     * @return null if no column ordering is used.
     */
    TableColumnOrderingModel getTableColumnOrderingModel();

}
