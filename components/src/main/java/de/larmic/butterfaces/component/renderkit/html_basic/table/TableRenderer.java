package de.larmic.butterfaces.component.renderkit.html_basic.table;

import com.sun.faces.renderkit.Attribute;
import com.sun.faces.renderkit.RenderKitUtils;
import de.larmic.butterfaces.component.html.table.HtmlColumn;
import de.larmic.butterfaces.component.html.table.HtmlTable;
import de.larmic.butterfaces.component.partrenderer.RenderUtils;
import de.larmic.butterfaces.component.partrenderer.StringUtils;
import de.larmic.butterfaces.event.TableSingleSelectionListener;

import javax.faces.component.UIComponent;
import javax.faces.component.UINamingContainer;
import javax.faces.component.behavior.ClientBehavior;
import javax.faces.component.behavior.ClientBehaviorContext;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.render.FacesRenderer;
import java.io.IOException;
import java.util.*;

/**
 * Created by larmic on 10.09.14.
 */
@FacesRenderer(componentFamily = HtmlTable.COMPONENT_FAMILY, rendererType = HtmlTable.RENDERER_TYPE)
public class TableRenderer extends de.larmic.butterfaces.component.renderkit.html_basic.mojarra.TableRenderer {

    private List<HtmlColumn> cachedColumns;
    private boolean hasColumnWidthSet;
    // dirty: method renderRowStart does not have a rowIndex parameter but it should.
    // alternative: copy encode children but this means coping a lot of private methods... :(
    // we will try this way... maybe migrating later...
    private int rowIndex;

    @Override
    public void encodeBegin(final FacesContext context,
                            final UIComponent component) throws IOException {
        rendererParamsNotNull(context, component);

        if (!shouldEncode(component)) {
            return;
        }

        final HtmlTable table = (HtmlTable) component;
        this.cachedColumns = getColumns(table);
        this.hasColumnWidthSet = hasColumnWidthSet(this.cachedColumns);
        this.rowIndex = 0;

        super.encodeBegin(context, component);
    }

    @Override
    public void encodeEnd(FacesContext context, UIComponent component) throws IOException {
        super.encodeEnd(context, component);

        final ResponseWriter responseWriter = context.getResponseWriter();

        RenderUtils.renderJQueryPluginCall(component.getClientId(), "fixBootstrapDropDown()", responseWriter, component);
    }

    @Override
    protected void renderHeader(final FacesContext context,
                                final UIComponent table,
                                final ResponseWriter writer) throws IOException {
        final HtmlTable htmlTable = (HtmlTable) table;

        if (hasColumnWidthSet) {

            writer.startElement("colgroup", table);

            int columnNumber = 0;

            for (HtmlColumn column : this.cachedColumns) {
                writer.startElement("col", table);
                writer.writeAttribute("class", "butter-table-colgroup", null);
                writer.writeAttribute("columnNumber", "" + columnNumber, null);

                final StringBuilder style = new StringBuilder();

                if (StringUtils.isNotEmpty(column.getColWidth())) {
                    style.append("width: ");
                    style.append(column.getColWidth());
                }
                if (this.isHideColumn(htmlTable, column)) {
                    if (style.length() > 0) {
                        style.append("; ");
                    }
                    style.append("display: none");
                }

                if (style.length() > 0) {
                    writer.writeAttribute("style", style.toString(), null);
                }
                writer.endElement("col");

                columnNumber++;
            }
            writer.endElement("colgroup");
        }

        writer.startElement("thead", table);
        writer.startElement("tr", table);

        int columnNumber = 0;

        for (HtmlColumn column : this.cachedColumns) {
            writer.startElement("th", table);
            writer.writeAttribute("id", column.getClientId(), null);
            writer.writeAttribute("class", "butter-component-table-column-header", null);
            writer.writeAttribute("columnNumber", "" + columnNumber, null);

            if (this.isHideColumn(htmlTable, column)) {
                writer.writeAttribute("style", "display:none", null);
            }

            // render header label
            writer.startElement("span", table);
            writer.writeAttribute("class", "butter-component-table-column-label", null);
            writer.writeText(column.getLabel(), null);
            writer.endElement("span");

            writer.endElement("th");

            columnNumber++;
        }
        writer.endElement("tr");
        writer.endElement("thead");
    }

    private boolean isHideColumn(final HtmlTable table, final HtmlColumn column) {
        if (table.getModel() != null) {
            final Boolean hideColumn = table.getModel().isHideColumn(column.getId());
            if (hideColumn != null) {
                return hideColumn;
            }
        }
        return column.isHideColumn();
    }

    @Override
    protected void renderTableStart(final FacesContext context,
                                    final UIComponent component,
                                    final ResponseWriter writer,
                                    final Attribute[] attributes) throws IOException {
        final HtmlTable table = (HtmlTable) component;

        writer.startElement("div", table);
        writeIdAttributeIfNecessary(context, writer, table);
        writer.writeAttribute("class", "butter-table", null);

        this.renderTableToolbar(context, writer, table);

        writer.startElement("div", table);

        final String styleClass = (String) table.getAttributes().get("styleClass");
        if (styleClass != null) {
            writer.writeAttribute("class", "table-responsive " + styleClass, "styleClass");
        } else {
            writer.writeAttribute("class", "table-responsive", "styleClass");
        }

        writer.startElement("table", table);
        final StringBuilder tableStyleClass = new StringBuilder("table table-hover");

        if (this.hasColumnWidthSet) {
            tableStyleClass.append(" table-fixed");
        }
        if (table.isTableCondensed()) {
            tableStyleClass.append(" table-condensed");
        }
        if (table.isTableBordered()) {
            tableStyleClass.append(" table-bordered");
        }
        if (table.isTableStriped()) {
            tableStyleClass.append(" table-striped");
        }

        writer.writeAttribute("class", tableStyleClass.toString(), "styleClass");

        RenderKitUtils.renderPassThruAttributes(context, writer, table, attributes);
        writer.writeText("\n", table, null);
    }

    private void renderTableToolbar(final FacesContext context,
                                    final ResponseWriter writer,
                                    final HtmlTable table) throws IOException {
        writer.startElement("div", table);
        writer.writeAttribute("class", "butter-table-toolbar clearfix", null);

        writer.startElement("div", table); // start button group
        writer.writeAttribute("class", "btn-group pull-right", null);

        this.renderTableToolbarRefreshButton(writer, table);
        this.renderTableToolbarToggleColumnButton(context, writer, table);

        writer.endElement("div"); // end button group

        writer.endElement("div");
    }

    private void renderTableToolbarToggleColumnButton(final FacesContext context,
                                                      final ResponseWriter writer,
                                                      final HtmlTable table) throws IOException {
        if (table.isShowToggleColumnButton()) {
            // show and hide option toggle
            writer.startElement("a", table);
            writer.writeAttribute("class", "btn btn-default dropdown-toggle", null);
            writer.writeAttribute("data-toggle", "dropdown", null);
            writer.writeAttribute("title", "Column options", null);
            writer.writeAttribute("role", "button", null);
            writer.startElement("i", table);
            writer.writeAttribute("class", "glyphicon glyphicon-th", null);
            writer.endElement("i");
            writer.startElement("span", table);
            writer.writeAttribute("class", "caret", null);
            writer.endElement("span");
            writer.endElement("a");

            // show and hide option content
            writer.startElement("ul", table);
            writer.writeAttribute("class", "dropdown-menu dropdown-menu-form butter-table-toolbar-columns", null);
            writer.writeAttribute("role", "menu", null);

            final ClientBehaviorContext behaviorContext =
                    ClientBehaviorContext.createClientBehaviorContext(context,
                            table, "click", table.getClientId(context), null);

            int columnNumber = 0;
            for (HtmlColumn cachedColumn : this.cachedColumns) {
                writer.startElement("li", table);
                writer.startElement("label", table);
                writer.writeAttribute("class", "checkbox", null);
                writer.startElement("input", table);
                writer.writeAttribute("type", "checkbox", null);
                writer.writeAttribute("columnNumber", "" + columnNumber, null);

                final String jQueryPluginCall = RenderUtils.createJQueryPluginCall(table.getClientId(), "toggleColumnVisibilty({columnIndex:'" + columnNumber + "'})");

                final Map<String, List<ClientBehavior>> behaviors = table.getClientBehaviors();
                if (behaviors.containsKey("click")) {
                    final String click = behaviors.get("click").get(0).getScript(behaviorContext);

                    if (StringUtils.isNotEmpty(click)) {
                        final String correctedEventName = click.replace(",'click',", ",'toggle_" + columnNumber + "',");
                        writer.writeAttribute("onclick", correctedEventName + ";" + jQueryPluginCall, null);
                    }
                } else {
                    writer.writeAttribute("onclick", jQueryPluginCall, null);
                }
                if (!this.isHideColumn(table, cachedColumn)) {
                    writer.writeAttribute("checked", "checked", null);
                }
                writer.endElement("input");
                writer.writeText(cachedColumn.getLabel(), null);
                writer.endElement("label");
                writer.endElement("li");
                columnNumber++;
            }
            writer.endElement("ul");
        }
    }

    private void renderTableToolbarRefreshButton(ResponseWriter writer, HtmlTable table) throws IOException {
        if (table.isShowRefreshButton()) {
            writer.startElement("a", table);
            writer.writeAttribute("class", "btn btn-default", null);
            writer.writeAttribute("role", "button", null);
            writer.writeAttribute("title", "Refresh table", null);
            writer.writeAttribute("onclick", "jsf.ajax.request(this,null,{event:'action',render: '" + table.getClientId() + "', onevent:" + this.getOnEventListenerName(table) + "});", null);
            writer.startElement("i", table);
            writer.writeAttribute("class", "glyphicon glyphicon-refresh", null);
            writer.endElement("i");
            writer.endElement("a");
        }
    }

    @Override
    protected void renderTableEnd(final FacesContext context,
                                  final UIComponent component,
                                  final ResponseWriter writer) throws IOException {
        super.renderTableEnd(context, component, writer);
        writer.endElement("div");
        writer.endElement("div");

        writer.startElement("script", component);
        writer.writeText("function " + this.getOnEventListenerName(component) + "(data) {", null);
        writer.writeText("    refreshTable(data, '" + component.getClientId() + "');", null);
        writer.writeText("}", null);
        writer.endElement("script");
    }

    @Override
    protected void renderRowStart(final FacesContext context,
                                  final UIComponent component,
                                  final ResponseWriter writer) throws IOException {
        final HtmlTable htmlTable = (HtmlTable) component;
        final ClientBehaviorContext behaviorContext =
                ClientBehaviorContext.createClientBehaviorContext(context,
                        htmlTable, "click", component.getClientId(context), null);

        final String clientId = htmlTable.getClientId();
        final String baseClientId = clientId.substring(0, clientId.length() - (rowIndex + "").length() - 1);

        writer.startElement("tr", htmlTable);
        writer.writeAttribute("rowIndex", rowIndex, null);
        writer.writeAttribute("class", "butter-table-row", null);

        final Map<String, List<ClientBehavior>> behaviors = htmlTable.getClientBehaviors();
        if (behaviors.containsKey("click")) {
            final String click = behaviors.get("click").get(0).getScript(behaviorContext);

            if (StringUtils.isNotEmpty(click)) {
                final String correctedEventName = click.replace(",'click',", ",'click_" + rowIndex + "',");
                final String correctedClientId = correctedEventName.replaceFirst(clientId, baseClientId);
                final String jQueryPluginCall = RenderUtils.createJQueryPluginCall(htmlTable.getClientId(), "selectRow({rowIndex:'" + rowIndex + "'})");
                writer.writeAttribute("onclick", correctedClientId + ";" + jQueryPluginCall.replaceFirst(clientId, baseClientId), null);
            }
        }

        writer.writeText("\n", htmlTable, null);

        rowIndex++;
    }

    @Override
    public void decode(final FacesContext context, final UIComponent component) {
        final HtmlTable htmlTable = (HtmlTable) component;
        final Map<String, List<ClientBehavior>> behaviors = htmlTable.getClientBehaviors();

        if (behaviors.isEmpty()) {
            return;
        }

        final Object untypedTableValue = htmlTable.getValue();
        final TableSingleSelectionListener listener = htmlTable.getSingleSelectionListener();

        if (listener == null) {
            return;
        }

        if (!(untypedTableValue instanceof Iterable)) {
            return;
        }

        final Iterable tableValues = (Iterable) untypedTableValue;

        final ExternalContext external = context.getExternalContext();
        final Map<String, String> params = external.getRequestParameterMap();
        final String behaviorEvent = params.get("javax.faces.behavior.event");

        if (behaviorEvent != null) {
            final String[] split = behaviorEvent.split("_");
            final String event = split[0];
            final int eventNumber = Integer.valueOf(split[1]);
            if ("click".equals(event)) {
                final Object rowObject = findRowObject(tableValues, eventNumber);

                if (rowObject != null) {
                    listener.processValueChange(rowObject);
                }
            } else if ("toggle".equals(event) && htmlTable.getModel() != null) {
                final HtmlColumn toggledColumn = cachedColumns.get(eventNumber);
                if (this.isHideColumn(htmlTable, toggledColumn)) {
                    htmlTable.getModel().showColumn(toggledColumn.getId());
                } else {
                    htmlTable.getModel().hideColumn(toggledColumn.getId());
                }
            }
        }
    }

    private String getOnEventListenerName(final UIComponent component) {
        final char separatorChar = UINamingContainer.getSeparatorChar(FacesContext.getCurrentInstance());
        return "refreshTable" + "_" + component.getClientId().replace(separatorChar + "", "_");
    }

    private Object findRowObject(final Iterable tableValues, final int row) {
        final Iterator iterator = tableValues.iterator();
        int actualRow = 0;

        while (iterator.hasNext()) {
            final Object value = iterator.next();

            if (actualRow == row) {
                return value;
            }

            actualRow++;
        }

        return null;
    }

    private boolean hasColumnWidthSet(final List<HtmlColumn> columns) {
        for (HtmlColumn column : columns) {
            if (StringUtils.isNotEmpty(column.getColWidth())) {
                return true;
            }
        }

        return false;
    }

    /**
     * <p>Return an Iterator over the <code>HtmlColumn</code> children of the
     * specified <code>UIData</code> that have a <code>rendered</code> property
     * of <code>true</code>.</p>
     *
     * @param table the table from which to extract children
     * @return the List of all HtmlColumn children
     */
    private List<HtmlColumn> getColumns(final UIComponent table) {
        final int childCount = table.getChildCount();
        if (childCount > 0) {
            final List<HtmlColumn> results = new ArrayList<>(childCount);
            for (UIComponent kid : table.getChildren()) {
                if ((kid instanceof HtmlColumn) && kid.isRendered()) {
                    results.add((HtmlColumn) kid);
                }
            }
            return results;
        } else {
            return Collections.emptyList();
        }
    }
}
