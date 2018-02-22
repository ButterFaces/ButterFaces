/*
 * Copyright Lars Michaelis and Stephan Zerhusen 2016.
 * Distributed under the MIT License.
 * (See accompanying file README.md file or copy at http://opensource.org/licenses/MIT)
 */
package org.butterfaces.component.html.table.export.writer;

import javax.faces.component.UIComponent;
import javax.faces.context.ResponseWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.List;

/**
 * @author Lars Michaelis
 */
public class StringListResponseWriter extends ResponseWriter {

    private final List<String> list;

    public StringListResponseWriter(final List<String> list) {
        this.list = list;
    }

    @Override
    public String getContentType() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public String getCharacterEncoding() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void write(char[] cbuf, int off, int len) throws IOException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void flush() throws IOException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void close() throws IOException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void startDocument() throws IOException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void endDocument() throws IOException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void startElement(String name, UIComponent component) throws IOException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void endElement(String name) throws IOException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void writeAttribute(String name, Object value, String property) throws IOException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void writeURIAttribute(String name, Object value, String property) throws IOException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void writeComment(Object comment) throws IOException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void writeText(Object text, String property) throws IOException {
        final String value = (String) text;

        if (!"\n".equals(value)) {
            list.add(value);
        }
    }

    @Override
    public void writeText(char[] text, int off, int len) throws IOException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public ResponseWriter cloneWithWriter(Writer writer) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
}

