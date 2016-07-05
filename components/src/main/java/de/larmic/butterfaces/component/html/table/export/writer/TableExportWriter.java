/*
 * Copyright Lars Michaelis and Stephan Zerhusen 2016.
 * Distributed under the MIT License.
 * (See accompanying file README.md file or copy at http://opensource.org/licenses/MIT)
 */
package de.larmic.butterfaces.component.html.table.export.writer;

import de.larmic.butterfaces.component.html.table.export.iterator.TableExportWriterIterator;

import java.io.IOException;
import java.io.OutputStream;

/**
 * @author Lars Michaelis
 */
public interface TableExportWriter {

    <T> void write(OutputStream outputStream, final TableExportWriterIterator<T> iterator) throws IOException;

}