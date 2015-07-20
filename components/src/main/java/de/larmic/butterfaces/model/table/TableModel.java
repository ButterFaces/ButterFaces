package de.larmic.butterfaces.model.table;

/**
 * Created by larmic on 03.12.14.
 */
public interface TableModel {

    /**
     * @return null if no sort model is used. This means sort columns is not available.
     */
    TableRowSortingModel getTableRowSortingModel();

    /**
     * @return null if no column display (show and hide) is used. In this case column information will be lost if table is rerendered.
     */
    TableColumnVisibilityModel getTableColumnVisibilityModel();

    TableColumnOrderingModel getTableColumnOrderingModel();

}
