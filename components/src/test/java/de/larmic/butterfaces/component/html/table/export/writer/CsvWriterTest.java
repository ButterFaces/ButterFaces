/*
 * Copyright Lars Michaelis and Stephan Zerhusen 2016.
 * Distributed under the MIT License.
 * (See accompanying file README.md file or copy at http://opensource.org/licenses/MIT)
 */
package de.larmic.butterfaces.component.html.table.export.writer;

import de.larmic.butterfaces.component.html.table.export.iterator.TableExportWriterIterator;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Lars Michaelis
 */
public class CsvWriterTest {

    @Test
    public void testWrite() throws Exception {
        final CsvWriter csvWriter = new CsvWriter();
        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        csvWriter.write(outputStream, new TestWriterIterator());

        final byte[] writtenBytes = outputStream.toByteArray();
        assertThat(extractBOM(writtenBytes)).isEqualTo(CsvWriter.UTF8_BOM);

        assertThat(new String(writtenBytes))
                .isEqualTo("\uFEFF\"col1\";\"col2\";\r\n\"r1c1\";\"r1c2\";\r\n\"r2c1\";\"r2c2\";");
    }

    private byte[] extractBOM(byte[] writtenBytes) {
        byte[] actualBOM = new byte[3];
        System.arraycopy(writtenBytes, 0, actualBOM, 0, 3);
        return actualBOM;
    }

    private class TestWriterIterator implements TableExportWriterIterator {

        private final Iterator<List<String>> iterator;

        public TestWriterIterator() {
            final List<String> row1 = Arrays.asList("r1c1", "r1c2");
            final List<String> row2 = Arrays.asList("r2c1", "r2c2");
            final List<List<String>> rows = Arrays.asList(row1, row2);
            iterator = rows.iterator();
        }

        @Override
        public int getRowCount() {
            return 2;
        }

        @Override
        public List<String> nextRow() {
            return iterator.next();
        }

        @Override
        public List<String> getHeader() {
            return Arrays.asList("col1", "col2");
        }

        @Override
        public boolean hasNext() {
            return iterator.hasNext();
        }

        @Override
        public Object next() {
            return iterator.next();
        }

        @Override
        public void remove() {

        }
    }
}