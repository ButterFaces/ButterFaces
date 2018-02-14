/*
 * Copyright Lars Michaelis and Stephan Zerhusen 2016.
 * Distributed under the MIT License.
 * (See accompanying file README.md file or copy at http://opensource.org/licenses/MIT)
 */
package org.butterfaces.component.html.table.export.model;

import org.butterfaces.component.html.table.HtmlColumn;
import org.butterfaces.component.html.table.HtmlTable;
import org.butterfaces.component.html.table.export.iterator.TableExportWriterTableListIterator;
import org.butterfaces.component.html.table.export.writer.TableExportWriter;
import org.butterfaces.component.html.table.export.iterator.TableExportWriterTableListIterator;
import org.butterfaces.component.html.table.export.writer.TableExportWriter;

import javax.faces.component.UIComponent;
import javax.faces.model.ListDataModel;
import javax.xml.bind.JAXBException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * @author Lars Michaelis
 */
public class TableDataStream implements DataStream {

    private static final Logger LOG = Logger.getLogger(TableDataStream.class.getName());

    private enum TableModelType {
        QUERY_MODEL, LIST_MODEL
    }

    private final HtmlTable table;
    private final List<HtmlColumn> columns;
    private final List<String> header;
    private final TableModelType tableModelType;
    private final List<?> listModel;
    private final boolean exportInvisibleColumns;

    private final TableExportWriter tableExportWriter;

    public TableDataStream(final HtmlTable table,
                           final boolean exportInvisibleColumns,
                           final TableExportWriter tableExportWriter) {
        this.exportInvisibleColumns = exportInvisibleColumns;
        this.tableExportWriter = tableExportWriter;
        this.columns = getColumns(table);
        this.header = createHeader(this.columns);
        this.table = table;

        this.listModel = getListModel(table);

        if (listModel == null) {
            LOG.warning(String.format("Could not create export. Table model %s is not supported", table.getValue().getClass()));
            throw new RuntimeException("error.export.failed");
        } else {
            this.tableModelType = TableModelType.LIST_MODEL;
        }
    }

    @Override
    public void writeData(OutputStream outputStream) throws IOException, JAXBException, TransformerException, ParserConfigurationException {
        if (isResultsAvailable()) {
            if (tableModelType == TableModelType.LIST_MODEL) {
                tableExportWriter.write(outputStream,
                        new TableExportWriterTableListIterator(this.header, this.listModel, this.columns,
                                this.table.getVar()));
            } else {
                throw new IllegalArgumentException(
                        String.format("Model %s is not supported ba TableDataStream", tableModelType));
            }
        }
    }

    @Override
    public boolean isReady() {
        return true;
    }

    @Override
    public boolean isResultsAvailable() {
        if (tableModelType == TableModelType.LIST_MODEL) {
            return !listModel.isEmpty();
        } else {
            throw new IllegalArgumentException(
                    String.format("Model %s is not supported ba TableDataStream", tableModelType));
        }
    }

    /**
     * Tries to get result list from table. Generated query table model is not supported by this method.
     *
     * @return If table model is not supported by this method null will be returned
     */
    private List<?> getListModel(final HtmlTable table) {
        final Object tableValue = table.getValue();

        if (tableValue instanceof ListDataModel) {
            return (List<?>) ((ListDataModel) tableValue).getWrappedData();
        } else if (tableValue instanceof List) {
            return (List<?>) tableValue;
        }

        return null;
    }

    private List<HtmlColumn> getColumns(final HtmlTable table) {
        final List<HtmlColumn> columns = new ArrayList<>();

        for (UIComponent child : table.getChildren()) {
            if ((child instanceof HtmlColumn) &&
                    (!((HtmlColumn) child).isHideColumn() || exportInvisibleColumns) && child.isRendered()) {
                columns.add((HtmlColumn) child);
            }
        }

        return columns;
    }

    private List<String> createHeader(final List<HtmlColumn> columns) {
        final List<String> header = new ArrayList<>();

        for (final HtmlColumn column : columns) {
            String headerText = column.getLabel();
            // EWETELTKKB-26393: CSV files must not start with upper case ID. See http://support.microsoft.com/kb/215591.
            if (headerText.startsWith("ID")) {
                headerText = headerText.replaceFirst("ID", "id");
            }
            header.add(headerText);
        }

        return header;
    }
}
