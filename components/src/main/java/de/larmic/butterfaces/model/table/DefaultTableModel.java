package de.larmic.butterfaces.model.table;

/**
 * Created by larmic on 03.12.14.
 */
public class DefaultTableModel implements TableModel {

    private final TableSortModel tableSortModel = new DefaultTableSortModel();
    private final TableColumnDisplayModel columnDisplayTableModel = new DefaultColumnDisplayTableModel();
    private final TableOrderModel tableOrderModel = new DefaultTableOrderModel();

    @Override
    public TableSortModel getTableSortModel() {
        return tableSortModel;
    }

    @Override
    public TableColumnDisplayModel getTableColumnDisplayModel() {
        return columnDisplayTableModel;
    }

    @Override
    public TableOrderModel getTableOrderModel() {
        return tableOrderModel;
    }
}
