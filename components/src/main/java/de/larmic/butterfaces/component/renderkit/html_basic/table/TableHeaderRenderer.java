package de.larmic.butterfaces.component.renderkit.html_basic.table;

import de.larmic.butterfaces.component.html.table.HtmlColumn;
import de.larmic.butterfaces.component.html.table.HtmlTable;
import de.larmic.butterfaces.component.html.table.HtmlTableHeader;
import de.larmic.butterfaces.component.partrenderer.RenderUtils;
import de.larmic.butterfaces.component.partrenderer.StringUtils;
import de.larmic.butterfaces.component.renderkit.html_basic.HtmlBasicRenderer;
import de.larmic.butterfaces.resolver.UIComponentResolver;

import javax.faces.component.UIComponent;
import javax.faces.component.UINamingContainer;
import javax.faces.component.behavior.AjaxBehavior;
import javax.faces.component.behavior.ClientBehavior;
import javax.faces.component.behavior.ClientBehaviorContext;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.render.FacesRenderer;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Created by larmic on 10.09.14.
 */
@FacesRenderer(componentFamily = HtmlTableHeader.COMPONENT_FAMILY, rendererType = HtmlTableHeader.RENDERER_TYPE)
public class TableHeaderRenderer extends HtmlBasicRenderer {

    private HtmlTable cachedTableComponent;

    @Override
    public void encodeBegin(final FacesContext context,
                            final UIComponent component) throws IOException {
        rendererParamsNotNull(context, component);

        if (!shouldEncode(component)) {
            return;
        }

        super.encodeBegin(context, component);

        final HtmlTableHeader tableHeader = (HtmlTableHeader) component;
        final ResponseWriter responseWriter = context.getResponseWriter();
        this.cachedTableComponent = new UIComponentResolver().findComponent(tableHeader.getTableId(), HtmlTable.class);

        responseWriter.startElement("div", tableHeader);
        this.writeIdAttribute(context, responseWriter, tableHeader);
        responseWriter.writeAttribute("class", "butter-table-toolbar row", null);
    }

    @Override
    public void encodeChildren(final FacesContext context,
                               final UIComponent component) throws IOException {
        if (component.getChildCount() > 0) {
            final ResponseWriter responseWriter = context.getResponseWriter();

            responseWriter.startElement("div", component);
            responseWriter.writeAttribute("class", "butter-table-toolbar-custom col-sm-9 pull-left", null);
            super.encodeChildren(context, component);
            responseWriter.endElement("div");
        }
    }

    @Override
    public void encodeEnd(final FacesContext context,
                          final UIComponent component) throws IOException {
        super.encodeEnd(context, component);

        final HtmlTableHeader tableHeader = (HtmlTableHeader) component;
        final ResponseWriter responseWriter = context.getResponseWriter();

        final String toolbarColumnSize = component.getChildCount() > 0 ? "col-sm-3" : "col-sm-12";

        responseWriter.startElement("div", tableHeader); // start right toolbar
        responseWriter.writeAttribute("class", toolbarColumnSize, null);
        responseWriter.startElement("div", tableHeader); // start button group
        responseWriter.writeAttribute("class", "btn-group pull-right", null);

        this.renderTableToolbarRefreshButton(responseWriter, tableHeader);
        this.renderTableToolbarToggleColumnButton(context, responseWriter, tableHeader);

        responseWriter.endElement("div"); // end button group
        responseWriter.endElement("div"); // end right toolbar

        responseWriter.endElement("div");

        RenderUtils.renderJQueryPluginCall(component.getClientId(), "fixBootstrapDropDown()", responseWriter, component);
    }

    @Override
    public void decode(FacesContext context, UIComponent component) {
        final HtmlTableHeader htmlTableHeader = (HtmlTableHeader) component;
        final Map<String, List<ClientBehavior>> behaviors = htmlTableHeader.getClientBehaviors();

        if (behaviors.isEmpty()) {
            return;
        }

        final ExternalContext external = context.getExternalContext();
        final Map<String, String> params = external.getRequestParameterMap();
        final String behaviorEvent = params.get("javax.faces.behavior.event");

        if (behaviorEvent != null) {
            final String[] split = behaviorEvent.split("_");
            final String event = split[0];
            final int eventNumber = Integer.valueOf(split[1]);
            if ("toggle".equals(event) && this.cachedTableComponent.getTableColumnDisplayModel() != null) {
                final HtmlColumn toggledColumn = this.cachedTableComponent.getCachedColumns().get(eventNumber);
                if (this.isHideColumn(this.cachedTableComponent, toggledColumn)) {
                    this.cachedTableComponent.getTableColumnDisplayModel().showColumn(toggledColumn.getId());
                } else {
                    this.cachedTableComponent.getTableColumnDisplayModel().hideColumn(toggledColumn.getId());
                }
            }
        }
    }

    @Override
    public boolean getRendersChildren() {
        return true;
    }

    private void renderTableToolbarToggleColumnButton(final FacesContext context,
                                                      final ResponseWriter writer,
                                                      final HtmlTableHeader tableHeader) throws IOException {
        if (tableHeader.isShowToggleColumnButton()) {
            // show and hide option toggle
            writer.startElement("a", tableHeader);
            writer.writeAttribute("class", "btn btn-default dropdown-toggle", null);
            writer.writeAttribute("data-toggle", "dropdown", null);
            writer.writeAttribute("title", "Column options", null);
            writer.writeAttribute("role", "button", null);
            writer.startElement("i", tableHeader);
            writer.writeAttribute("class", "glyphicon glyphicon-th", null);
            writer.endElement("i");
            writer.startElement("span", tableHeader);
            writer.writeAttribute("class", "caret", null);
            writer.endElement("span");
            writer.endElement("a");

            // show and hide option content
            writer.startElement("ul", tableHeader);
            writer.writeAttribute("class", "dropdown-menu dropdown-menu-form butter-table-toolbar-columns", null);
            writer.writeAttribute("role", "menu", null);

            final ClientBehaviorContext behaviorContext =
                    ClientBehaviorContext.createClientBehaviorContext(context,
                            tableHeader, "click", tableHeader.getClientId(context), null);

            int columnNumber = 0;
            for (HtmlColumn cachedColumn : this.cachedTableComponent.getCachedColumns()) {
                writer.startElement("li", tableHeader);
                writer.startElement("label", tableHeader);
                writer.writeAttribute("class", "checkbox", null);
                writer.startElement("input", tableHeader);
                writer.writeAttribute("type", "checkbox", null);
                writer.writeAttribute("columnNumber", "" + columnNumber, null);

                // TODO correct table id

                final String jQueryPluginCall = RenderUtils.createJQueryPluginCall(this.cachedTableComponent.getClientId(), "toggleColumnVisibilty({columnIndex:'" + columnNumber + "'})");

                final Map<String, List<ClientBehavior>> behaviors = tableHeader.getClientBehaviors();
                if (behaviors.containsKey("click")) {
                    final AjaxBehavior clientBehavior = (AjaxBehavior) behaviors.get("click").get(0);
                    final String click = clientBehavior.getScript(behaviorContext);

                    if (StringUtils.isNotEmpty(click)) {
                        // ajax tag is enabled
                        final AjaxBehavior ajaxBehavior = new AjaxBehavior();
                        ajaxBehavior.setRender(clientBehavior.getRender());
                        ajaxBehavior.setOnevent(this.getOnEventListenerName(this.cachedTableComponent));
                        final String ajaxBehaviorScript = ajaxBehavior.getScript(behaviorContext);

                        final String correctedEventName = ajaxBehaviorScript.replace(",'click',", ",'toggle_" + columnNumber + "',");
                        writer.writeAttribute("onclick", correctedEventName + ";" + jQueryPluginCall, null);
                    } else {
                        // ajax tag is disabled
                        writer.writeAttribute("onclick", jQueryPluginCall, null);
                    }
                } else {
                    // no ajax tag is used
                    writer.writeAttribute("onclick", jQueryPluginCall, null);
                }
                if (!this.isHideColumn(this.cachedTableComponent, cachedColumn)) {
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

    private boolean isHideColumn(final HtmlTable table, final HtmlColumn column) {
        if (table.getTableColumnDisplayModel() != null) {
            final Boolean hideColumn = table.getTableColumnDisplayModel().isColumnHidden(column.getId());
            if (hideColumn != null) {
                return hideColumn;
            }
        }
        return column.isHideColumn();
    }

    private void renderTableToolbarRefreshButton(final ResponseWriter writer,
                                                 final HtmlTableHeader tableHeader) throws IOException {
        if (tableHeader.isShowRefreshButton()) {
            writer.startElement("a", tableHeader);
            writer.writeAttribute("class", "btn btn-default", null);
            writer.writeAttribute("role", "button", null);
            writer.writeAttribute("title", "Refresh table", null);
            writer.writeAttribute("onclick", "jsf.ajax.request(this,null,{event:'action',render: '" + this.cachedTableComponent.getClientId() + "', onevent:" + this.getOnEventListenerName(this.cachedTableComponent) + "});", null);
            writer.startElement("i", tableHeader);
            writer.writeAttribute("class", "glyphicon glyphicon-refresh", null);
            writer.endElement("i");
            writer.endElement("a");
        }
    }

    private String getOnEventListenerName(final UIComponent component) {
        final char separatorChar = UINamingContainer.getSeparatorChar(FacesContext.getCurrentInstance());
        return "refreshTable" + "_" + component.getClientId().replace(separatorChar + "", "_");
    }
}
