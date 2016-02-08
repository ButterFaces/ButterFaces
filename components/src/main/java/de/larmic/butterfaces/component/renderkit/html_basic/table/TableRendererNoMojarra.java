/*
 * Copyright Lars Michaelis and Stephan Zerhusen 2016.
 * Distributed under the MIT License.
 * (See accompanying file README.md file or copy at http://opensource.org/licenses/MIT)
 */
package de.larmic.butterfaces.component.renderkit.html_basic.table;

import de.larmic.butterfaces.component.behavior.JsfAjaxRequest;
import de.larmic.butterfaces.component.html.repeat.visitor.DataVisitResult;
import de.larmic.butterfaces.component.html.repeat.visitor.DataVisitor;
import de.larmic.butterfaces.component.html.table.HtmlColumnNoMojarra;
import de.larmic.butterfaces.component.html.table.HtmlTableNoMojarra;
import de.larmic.butterfaces.component.partrenderer.RenderUtils;
import de.larmic.butterfaces.event.TableSingleSelectionListener;
import de.larmic.butterfaces.resolver.ClientBehaviorResolver;
import de.larmic.butterfaces.util.StringJoiner;
import de.larmic.butterfaces.util.StringUtils;

import javax.faces.component.UIComponent;
import javax.faces.component.behavior.AjaxBehavior;
import javax.faces.component.behavior.ClientBehavior;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.render.FacesRenderer;
import javax.faces.render.Renderer;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * This class is experimental and still in progress
 *
 * @author Lars Michaelis
 */
@FacesRenderer(componentFamily = HtmlTableNoMojarra.COMPONENT_FAMILY, rendererType = HtmlTableNoMojarra.RENDERER_TYPE)
public class TableRendererNoMojarra extends Renderer {

    @Override
    public boolean getRendersChildren() {
        return true;
    }

    @Override
    public void encodeBegin(FacesContext context, UIComponent component) throws IOException {
        if (!component.isRendered()) {
            return;
        }

        super.encodeBegin(context, component);

        final HtmlTableNoMojarra table = (HtmlTableNoMojarra) component;
        final ResponseWriter writer = context.getResponseWriter();
        final List<HtmlColumnNoMojarra> columns = getColumns(table);

        if (!columns.isEmpty()) {
            writer.startElement("div", table);
            writer.writeAttribute("id", component.getClientId(context), "id");
            writer.writeAttribute("class", "butter-table", "styleclass");

            writer.startElement("table", table);
            writer.writeAttribute("class", createBootstrapTableStyleClasses(table), "styleclass");

            writer.startElement("thead", table);
            writer.startElement("tr", table);
            for (HtmlColumnNoMojarra column : columns) {
                writer.startElement("th", table);
                writer.writeAttribute("class", "butter-component-table-column-header", "styleclass");
                writer.startElement("div", table);
                writer.startElement("span", table);
                writer.writeAttribute("class", "butter-component-table-column-label", "styleclass");
                writer.writeText(column.getLabel(), null);
                writer.endElement("span");
                writer.endElement("div");
                writer.endElement("th");
            }
            writer.endElement("tr");
            writer.endElement("thead");
            writer.startElement("tbody", table);
        }
    }

    private String createBootstrapTableStyleClasses(final HtmlTableNoMojarra table) {
        StringJoiner stringJoiner = StringJoiner.on(' ')
                .join("table")
                .join("table-hover");

        if (table.isTableCondensed()) {
            stringJoiner = stringJoiner.join("table-condensed");
        }
        if (table.isTableBordered()) {
            stringJoiner = stringJoiner.join("table-bordered");
        }
        if (table.isTableStriped()) {
            stringJoiner = stringJoiner.join("table-striped");
        }

        return stringJoiner.toString();
    }

    @Override
    public void encodeEnd(FacesContext context, UIComponent component) throws IOException {
        if (!component.isRendered()) {
            return;
        }

        super.encodeEnd(context, component);

        final HtmlTableNoMojarra table = (HtmlTableNoMojarra) component;
        final ResponseWriter writer = context.getResponseWriter();
        final List<HtmlColumnNoMojarra> columns = getColumns(table);

        if (!columns.isEmpty()) {
            writer.endElement("tbody");
            writer.endElement("table");
            writer.endElement("div");
        }
    }

    @Override
    public void decode(FacesContext context, UIComponent component) {
        final HtmlTableNoMojarra table = (HtmlTableNoMojarra) component;
        final Map<String, List<ClientBehavior>> behaviors = table.getClientBehaviors();

        if (behaviors.isEmpty()) {
            return;
        }

        final Object untypedTableValue = table.getValue();


        if (!(untypedTableValue instanceof Iterable)) {
            return;
        }

        final ExternalContext external = context.getExternalContext();
        final Map<String, String> params = external.getRequestParameterMap();
        final String behaviorEvent = params.get("javax.faces.behavior.event");

        if (StringUtils.isNotEmpty(behaviorEvent)) {
            if (params.get("params").startsWith("select_")) {
                final Integer rowIndex = convertStringToInteger(params.get("params").replaceFirst("select_", ""));
                final Object value = findRowValue(table, rowIndex);

                if (value != null) {
                    final TableSingleSelectionListener listener = table.getSingleSelectionListener();

                    if (listener != null) {
                        listener.processTableSelection(value);
                    }
                }
            }
        }
    }

    @Override
    public void encodeChildren(FacesContext context, UIComponent component) throws IOException {
        final HtmlTableNoMojarra table = (HtmlTableNoMojarra) component;
        final ResponseWriter writer = context.getResponseWriter();

        final AjaxBehavior clickAjaxBehavior = ClientBehaviorResolver.resolveActiveAjaxBehavior(table, "click");

        try {
            DataVisitor visitor = new DataVisitor() {
                public DataVisitResult process(FacesContext context, Integer rowKey) throws IOException {
                    table.setRowKey(context, rowKey);

                    if (table.isRowAvailable() && table.getChildCount() > 0) {
                        writer.startElement("tr", table);
                        writer.writeAttribute("rowindex", rowKey, null);
                        writer.writeAttribute("class", "butter-table-row", "styleclass");

                        if (clickAjaxBehavior != null && table.getSingleSelectionListener() != null) {
                            final JsfAjaxRequest ajaxRequest = new JsfAjaxRequest(table.getClientId(), true)
                                    .setEvent("click")
                                    .setRender(table, "click")
                                    .setParams("'select_" + rowKey + "'")
                                    .setBehaviorEvent("click");
                            final String jQueryPluginCall = RenderUtils.createJQueryPluginCall(table.getClientId(), "selectTableRow({rowIndex:'" + rowKey + "'})");
                            //writer.writeAttribute("onclick", ajaxRequest.toString() + ";" + jQueryPluginCall.replaceFirst(clientId, baseClientId), null);
                            //writer.writeAttribute("onclick", ajaxRequest.toString() + ";" + jQueryPluginCall, null);
                            // TODO call javascript to mark selected row
                            writer.writeAttribute("onclick", ajaxRequest.toString() + ";" + jQueryPluginCall, null);
                        }

                        for (UIComponent child : table.getChildren()) {
                            child.encodeAll(context);
                        }
                        writer.endElement("tr");
                    }

                    return DataVisitResult.CONTINUE;
                }
            };

            table.walk(context, visitor);
        } finally {
            table.setRowKey(context, null);
        }
    }


    private Object findRowValue(final HtmlTableNoMojarra table, final int row) {
        final Object value = table.getValue();

        if (value instanceof Iterable) {
            final Iterator iterator = ((Iterable) value).iterator();
            int actualRow = 0;

            while (iterator.hasNext()) {
                final Object actualRowValue = iterator.next();

                if (actualRow == row) {
                    return actualRowValue;
                }

                actualRow++;
            }

            return null;
        }

        return null;
    }

    private List<HtmlColumnNoMojarra> getColumns(final HtmlTableNoMojarra table) {
        final List<HtmlColumnNoMojarra> columns = new ArrayList<>();

        for (UIComponent uiComponent : table.getChildren()) {
            if (uiComponent instanceof HtmlColumnNoMojarra) {
                columns.add((HtmlColumnNoMojarra) uiComponent);
            }
        }

        return columns;
    }

    private Integer convertStringToInteger(final String value) {
        try {
            return Integer.valueOf(value);
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
