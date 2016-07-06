/*
 * Copyright Lars Michaelis and Stephan Zerhusen 2016.
 * Distributed under the MIT License.
 * (See accompanying file README.md file or copy at http://opensource.org/licenses/MIT)
 */
package de.larmic.butterfaces.component.html.table.export.iterator;

import de.larmic.butterfaces.component.html.table.HtmlColumn;
import de.larmic.butterfaces.component.html.table.export.el.TableExportELResolver;

import java.util.Iterator;
import java.util.List;

/**
 * @author Lars Michaelis
 */
public class TableExportWriterTableListIterator <T> implements TableExportWriterIterator<T> {

    private final List<String> header;
    private final List<T> dataModel;
    private final Iterator<T> iterator;
    private final List<HtmlColumn> columns;
    private final String tableVar;

    public TableExportWriterTableListIterator(final List<String> header,
                                              final List<T> dataModel,
                                              final List<HtmlColumn> columns,
                                              final String tableVar) {
        this.header = header;
        this.dataModel = dataModel;
        this.iterator = dataModel.iterator();
        this.tableVar = tableVar;
        this.columns = columns;
    }

    @Override
    public int getRowCount() {
        return dataModel.size();
    }

    @Override
    public List<String> nextRow() {
        return TableExportELResolver.resolveRow(tableVar, columns, next());
    }

    @Override
    public List<String> getHeader() {
        return header;
    }

    @Override
    public boolean hasNext() {
        return iterator.hasNext();
    }

    @Override
    public T next() {
        return iterator.next();
    }

    @Override
    public void remove() {
        // do nothing
    }
}
