/*
 * Copyright Lars Michaelis and Stephan Zerhusen 2016.
 * Distributed under the MIT License.
 * (See accompanying file README.md file or copy at http://opensource.org/licenses/MIT)
 */
package de.larmic.butterfaces.model.table;

/**
 * @author Lars Michaelis
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
