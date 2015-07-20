package de.larmic.butterfaces.model.table;

/**
 * Created by larmic on 03.12.14.
 */
public class DefaultTableModel implements TableModel {

    private final TableRowSortingModel tableRowSortingModel = new DefaultTableRowSortingModel();
    private final TableColumnVisibilityModel tableColumnVisibilityModel = new DefaultTableColumnVisibilityModel();
    private final TableColumnOrderingModel tableColumnOrderingModel = new DefaultTableOrderingModel();

    @Override
    public TableRowSortingModel getTableRowSortingModel() {
        return tableRowSortingModel;
    }

    @Override
    public TableColumnVisibilityModel getTableColumnVisibilityModel() {
        return tableColumnVisibilityModel;
    }

    @Override
    public TableColumnOrderingModel getTableColumnOrderingModel() {
        return tableColumnOrderingModel;
    }
}
