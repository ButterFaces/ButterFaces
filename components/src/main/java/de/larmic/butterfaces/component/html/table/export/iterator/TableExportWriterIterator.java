/*
 * Copyright Lars Michaelis and Stephan Zerhusen 2016.
 * Distributed under the MIT License.
 * (See accompanying file README.md file or copy at http://opensource.org/licenses/MIT)
 */
package de.larmic.butterfaces.component.html.table.export.iterator;

import java.util.Iterator;
import java.util.List;

/**
 * Extends an iterator and prepares header and rows for csv export.
 *
 * @author Lars Michaelis
 */
public interface TableExportWriterIterator<T> extends Iterator<T> {

    /**
     * @return the number of rows (except header).
     */
    int getRowCount();

    /**
     * Prepares next row information. Sort order have to match header sort order.
     *
     * @return a list of strings of actual selected row.
     */
    List<String> nextRow();

    /**
     * @return a sorted header list.
     */
    List<String> getHeader();
}
