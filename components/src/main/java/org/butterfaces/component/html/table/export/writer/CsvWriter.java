/*
 * Copyright Lars Michaelis and Stephan Zerhusen 2016.
 * Distributed under the MIT License.
 * (See accompanying file README.md file or copy at http://opensource.org/licenses/MIT)
 */
package org.butterfaces.component.html.table.export.writer;

import org.butterfaces.component.html.table.export.iterator.TableExportWriterIterator;
import org.butterfaces.component.html.table.export.iterator.TableExportWriterIterator;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.logging.Logger;

/**
 * @author Lars Michaelis
 */
public class CsvWriter implements TableExportWriter {

    public static final String NEWLINE = "\r\n";
    public static final String SEPARATOR = ";";
    public static final byte[] UTF8_BOM = new byte[]{(byte) 0xEF, (byte) 0xBB, (byte) 0xBF};

    private static final Logger LOG = Logger.getLogger(CsvWriter.class.getName());
    private final String encoding;
    private boolean quotedCsvLine;

    public CsvWriter() {
        this("UTF-8", true);
    }

    public CsvWriter(String encoding) {
        this.encoding = encoding;
        this.quotedCsvLine = true;
    }

    public CsvWriter(String encoding, boolean quotedCsvLine) {
        this.encoding = encoding;
        this.quotedCsvLine = quotedCsvLine;
    }

    @Override
    public <T> void write(OutputStream outputStream, final TableExportWriterIterator<T> iterator)
            throws IOException {
        if (!iterator.hasNext()) {
            throw new IllegalArgumentException("CSV export list is null or empty");
        }

        final long startTime = GregorianCalendar.getInstance().getTimeInMillis();

        LOG.finer("Begin CSV export");

        writeBOM(outputStream);
        OutputStreamWriter writer = new OutputStreamWriter(outputStream, encoding);
        writeCsvLine(writer, iterator.getHeader());
        writeContent(writer, iterator);
        writer.flush();
        writer.close();

        final long endTime = GregorianCalendar.getInstance().getTimeInMillis();
        final Date time = new Date(endTime - startTime);
        final SimpleDateFormat formatter = new SimpleDateFormat("mm:ss:SSS", Locale.GERMAN);

        LOG.info(String.format("CSV export was used %s to export data", iterator.getClass()));
        LOG.info(String.format("CSV export of %s data items takes %s milliseconds.", iterator.getRowCount(), formatter.format(time)));

        LOG.finer("CSV export completed");
    }

    private void writeBOM(OutputStream outputStream) throws IOException {
        if ("UTF-8".equals(encoding) || "UTF8".equals(encoding)) {
            outputStream.write(UTF8_BOM); // http://stackoverflow.com/a/1251372/524913
        }
    }

    private <T> void writeContent(final Writer writer, final TableExportWriterIterator<T> data) throws IOException {
        while (data.hasNext()) {
            writer.write(NEWLINE);
            final List<String> row = data.nextRow();
            writeCsvLine(writer, row);
        }
    }

    private void writeCsvLine(Writer writer, List<String> cellContents) throws IOException {
        for (String cellContent : cellContents) {
            if (quotedCsvLine) {
                writer.write('"');
            }
            writer.write(cellContent.replace("\"", "\"\""));
            if (quotedCsvLine) {
                writer.write('"');
            }
            writer.write(SEPARATOR);
        }
    }
}
