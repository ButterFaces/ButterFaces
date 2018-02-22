/*
 * Copyright Lars Michaelis and Stephan Zerhusen 2016.
 * Distributed under the MIT License.
 * (See accompanying file README.md file or copy at http://opensource.org/licenses/MIT)
 */
package org.butterfaces.component.html.table.export.el;

import org.butterfaces.component.html.action.HtmlCommandLink;
import org.butterfaces.component.html.table.HtmlColumn;
import org.butterfaces.component.html.table.export.writer.StringListResponseWriter;
import org.butterfaces.util.StringUtils;
import org.butterfaces.component.html.action.HtmlCommandLink;
import org.butterfaces.component.html.table.export.writer.StringListResponseWriter;
import org.butterfaces.util.StringUtils;

import javax.el.ELContext;
import javax.el.ELResolver;
import javax.faces.component.UIComponent;
import javax.faces.component.UIComponentBase;
import javax.faces.component.html.HtmlGraphicImage;
import javax.faces.component.html.HtmlOutputLabel;
import javax.faces.component.html.HtmlOutputLink;
import javax.faces.component.html.HtmlPanelGroup;
import javax.faces.context.FacesContext;
import javax.faces.context.FacesContextWrapper;
import javax.faces.context.ResponseWriter;
import java.beans.FeatureDescriptor;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

/**
 * @author Lars Michaelis
 */
public class TableExportELResolver extends ELResolver {
    public static final String EXPORT_ERROR_TEXT = "<<Error while export>>";

    private static final Logger LOG = Logger.getLogger(TableExportELResolver.class.getName());
    private static final ThreadLocal<CurrentInstance> KEY_VALUE = new ThreadLocal<>();

    /**
     * Resolves given row by using a wrapped faces context with a string list response writer. EL will be solved.
     *
     * @param tableVar  the table var attribute (needed by el resolving )
     * @param columns   the table columns that should be solved
     * @param rowObject the actual row object (entity, query result or something)
     * @return the solved row as list
     */
    public static List<String> resolveRow(final String tableVar, final List<HtmlColumn> columns, final Object rowObject) {
        try {
            final List<String> row = new ArrayList<>(columns.size());
            final StringListResponseWriter writer = new StringListResponseWriter(row);

            final FacesContext facesContext = createWrappedFacesContext();
            facesContext.setResponseWriter(writer);

            TableExportELResolver.activate(tableVar, rowObject);

            int columnCount = 0;
            for (HtmlColumn column : columns) {
                columnCount++;
                for (UIComponent child : column.getChildren()) {
                    try {
                        writeContent(row, writer, facesContext, columnCount, child);
                    } catch (Exception e) {
                        LOG.warning(String.format("Could not encode child %s with clientId %s", child.getClass(), child.getId()));

                        try {
                            // child class maybe not supported.
                            writer.writeText(EXPORT_ERROR_TEXT, "");
                        } catch (IOException e1) {
                            // normally this could not happen
                            LOG.warning(String.format("Could not encode child %s with clientId %s", child.getClass(), child.getId()));
                        }
                    }
                }

                // Ensure that the correct amount of rows will be returned.
                // This may happen, if the column has no children.
                if (columnCount > row.size()) {
                    row.add("");
                }
            }

            return row;
        } finally {
            deactivate();
        }
    }

    //TODO plain HTML-Elements will not be rendered / exported
    private static void writeContent(List<String> row, StringListResponseWriter writer, FacesContext facesContext,
                                     int columnCount, UIComponent child) throws IOException {
        if (child instanceof HtmlCommandLink) {
            final HtmlOutputLabel label = new HtmlOutputLabel();
            label.setValue(((HtmlCommandLink) child).getValue());
            label.setRendered(child.isRendered());
            label.encodeBegin(facesContext);
            label.encodeEnd(facesContext);
        } else if (child instanceof HtmlGraphicImage) {
            writer.writeText("", "");
        //} else if (child instanceof UIInstructions) {
        //    child.encodeBegin(facesContext);
        //    child.encodeEnd(facesContext);
        //    postCleanUp(row, columnCount);
        } else if (child instanceof HtmlPanelGroup || child instanceof HtmlOutputLink) {
            final UIComponentBase uiComponent = (UIComponentBase) child;
            if (uiComponent.isRendered()) {
                for (UIComponent uiChildComponent : uiComponent.getChildren()) {
                    writeContent(row, writer, facesContext, columnCount, uiChildComponent);
                }
            }
            postCleanUp(row, columnCount);
        } else {
            child.encodeBegin(facesContext);
            child.encodeEnd(facesContext);
        }
    }

    private static void postCleanUp(List<String> row, int columnCount) {
        List<Integer> indexToDelete = new ArrayList<>();
        for (int i = columnCount - 1; i < row.size(); i++) {
            final String s = row.get(i);
            if (s == null || StringUtils.isEmpty(s.trim())) {
                indexToDelete.add(i);
            }
        }
        for (int i = indexToDelete.size() - 1; i >= 0; i--) {
            row.remove((int) indexToDelete.get(i));
        }
    }

    public static void activate(String key, Object value) {
        KEY_VALUE.set(new CurrentInstance(key, value));
    }

    public static void deactivate() {
        KEY_VALUE.remove();
    }

    @Override
    public Object getValue(ELContext context, Object base, Object property) {
        final CurrentInstance currentInstance = KEY_VALUE.get();
        if (currentInstance == null) {
            return null;
        } else {
            return internalGetValue(context, base, property, currentInstance);
        }
    }

    private Object internalGetValue(ELContext context, Object base, Object property, CurrentInstance currentInstance) {
        if (base == null && property != null && property.equals(currentInstance.key)) {
            context.setPropertyResolved(true);
            return currentInstance.value;
        }

        return null;
    }

    private static FacesContext createWrappedFacesContext() {
        return new FacesContextWrapper() {
            private ResponseWriter responseWriter;

            @Override
            public FacesContext getWrapped() {
                return FacesContext.getCurrentInstance();
            }

            @Override
            public ResponseWriter getResponseWriter() {
                return responseWriter;
            }

            @Override
            public void setResponseWriter(ResponseWriter responseWriter) {
                this.responseWriter = responseWriter;
            }
        };
    }

    @Override
    public Class<?> getType(ELContext context, Object base, Object property) {
        final CurrentInstance currentInstance = KEY_VALUE.get();
        if (currentInstance == null) {
            return null;
        } else {
            final Object value = internalGetValue(context, base, property, currentInstance);

            // NPE fix. Sometimes TableExportELResolver is called without triggering csv export.
            // Don't know why but null check should fix the problem.
            return value != null ? value.getClass() : null;
        }
    }

    @Override
    public void setValue(ELContext context, Object base, Object property, Object value) {
        // do not implement
    }

    @Override
    public boolean isReadOnly(ELContext context, Object base, Object property) {
        return true;
    }

    @Override
    public Iterator<FeatureDescriptor> getFeatureDescriptors(ELContext context, Object base) {
        return null;
    }

    @Override
    public Class<?> getCommonPropertyType(ELContext context, Object base) {
        return null;
    }

    private static class CurrentInstance {
        private final String key;
        private final Object value;

        protected CurrentInstance(String key, Object value) {
            this.key = key;
            this.value = value;
        }
    }
}
