package de.larmic.butterfaces.model.table;

/**
 * Created by larmic on 03.12.14.
 */
public class DefaultTableModel implements TableModel {

    private final TableSortModel tableSortModel = new DefaultTableSortModel();
    private final TableColumnVisibilityModel tableColumnVisibilityModel = new DefaultColumnVisibilityTableModel();
    private final TableOrderModel tableOrderModel = new DefaultTableOrderModel();

    @Override
    public TableSortModel getTableSortModel() {
        return tableSortModel;
    }

    @Override
    public TableColumnVisibilityModel getTableColumnVisibilityModel() {
        return tableColumnVisibilityModel;
    }

    @Override
    public TableOrderModel getTableOrderModel() {
        return tableOrderModel;
    }
}
