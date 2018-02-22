/*
 * Copyright Lars Michaelis and Stephan Zerhusen 2016.
 * Distributed under the MIT License.
 * (See accompanying file README.md file or copy at http://opensource.org/licenses/MIT)
 */
package org.butterfaces.component.renderkit.html_basic.table;

import org.butterfaces.component.base.renderer.HtmlBasicRenderer;
import org.butterfaces.component.behavior.JsfAjaxRequest;
import org.butterfaces.component.html.table.HtmlColumn;
import org.butterfaces.component.html.table.HtmlTable;
import org.butterfaces.component.html.table.HtmlTableToolbar;
import org.butterfaces.component.partrenderer.RenderUtils;
import org.butterfaces.component.renderkit.html_basic.table.cache.TableColumnCache;
import org.butterfaces.model.table.json.JsonToModelConverter;
import org.butterfaces.model.table.TableColumnOrdering;
import org.butterfaces.model.table.TableColumnVisibility;
import org.butterfaces.resolver.ClientBehaviorResolver;
import org.butterfaces.resolver.UIComponentResolver;
import org.butterfaces.resolver.WebXmlParameters;
import org.butterfaces.util.StringUtils;
import org.butterfaces.component.partrenderer.RenderUtils;
import org.butterfaces.component.renderkit.html_basic.table.cache.TableColumnCache;
import org.butterfaces.util.StringUtils;

import javax.faces.component.UIComponent;
import javax.faces.component.behavior.AjaxBehavior;
import javax.faces.component.behavior.ClientBehavior;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.render.FacesRenderer;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Lars Michaelis
 */
@FacesRenderer(componentFamily = HtmlTableToolbar.COMPONENT_FAMILY, rendererType = HtmlTableToolbar.RENDERER_TYPE)
public class TableToolbarRenderer extends HtmlBasicRenderer {

    @Override
    public void encodeBegin(final FacesContext context,
                            final UIComponent component) throws IOException {
        if (!component.isRendered()) {
            return;
        }

        super.encodeBegin(context, component);

        final HtmlTableToolbar tableToolbar = (HtmlTableToolbar) component;
        final ResponseWriter responseWriter = context.getResponseWriter();
        final HtmlTable table = getTableComponent(tableToolbar);

        if (table == null) {
            throw new IllegalStateException("Could not find table component with id '" + tableToolbar.getTableId() + "'.");
        }

        responseWriter.startElement(ELEMENT_DIV, tableToolbar);
        this.writeIdAttribute(context, responseWriter, tableToolbar);
        responseWriter.writeAttribute("class", "butter-table-toolbar", null);
        responseWriter.writeAttribute("data-table-html-id", table.getClientId(), null);
    }

    @Override
    public void encodeChildren(final FacesContext context,
                               final UIComponent component) throws IOException {
        if (component.getChildCount() > 0) {
            final ResponseWriter responseWriter = context.getResponseWriter();

            responseWriter.startElement(ELEMENT_DIV, component);
            responseWriter.writeAttribute("class", "butter-table-toolbar-custom pull-left", null);
            super.encodeChildren(context, component);
            responseWriter.endElement(ELEMENT_DIV);
        }
    }

    @Override
    public void encodeEnd(final FacesContext context,
                          final UIComponent component) throws IOException {
        super.encodeEnd(context, component);

        final HtmlTableToolbar tableHeader = (HtmlTableToolbar) component;
        final HtmlTable table = getTableComponent(tableHeader);
        final ResponseWriter responseWriter = context.getResponseWriter();
        final WebXmlParameters webXmlParameters = new WebXmlParameters(context.getExternalContext());

        responseWriter.startElement(ELEMENT_DIV, tableHeader); // start right toolbar
        responseWriter.startElement(ELEMENT_DIV, tableHeader); // start button group
        responseWriter.writeAttribute("class", "btn-group pull-right table-toolbar-default", null);

        this.renderFacet(context, component, "default-options-left");
        this.renderTableToolbarRefreshButton(responseWriter, tableHeader, table, webXmlParameters);
        this.renderFacet(context, component, "default-options-center");
        this.renderTableToolbarToggleColumnButton(responseWriter, tableHeader, table, webXmlParameters);
        this.renderFacet(context, component, "default-options-right");

        responseWriter.endElement(ELEMENT_DIV); // end button group
        responseWriter.endElement(ELEMENT_DIV); // end right toolbar

        responseWriter.endElement(ELEMENT_DIV);

        RenderUtils.renderJQueryPluginCall(component.getClientId(), "fixBootstrapDropDown()", responseWriter, component);
    }

    private void renderFacet(final FacesContext context, final UIComponent component, final String facetName) throws IOException {
        final UIComponent leftFacet = this.getFacet(component, facetName);

        if (leftFacet != null) {
            leftFacet.encodeAll(context);
        }
    }

    @Override
    public void decode(FacesContext context, UIComponent component) {
        final HtmlTableToolbar tableToolbar = (HtmlTableToolbar) component;
        final Map<String, List<ClientBehavior>> behaviors = tableToolbar.getClientBehaviors();

        if (behaviors.isEmpty()) {
            return;
        }

        final ExternalContext external = context.getExternalContext();
        final Map<String, String> params = external.getRequestParameterMap();
        final String behaviorEvent = params.get("javax.faces.behavior.event");
        final HtmlTable table = getTableComponent(tableToolbar);
        final String tableUniqueIdentifier = table.getModelUniqueIdentifier();

        if (behaviorEvent != null) {
            if (HtmlTableToolbar.EVENT_TOGGLE_COLUMN.equals(behaviorEvent) && table.getTableColumnVisibilityModel() != null) {
                final TableColumnVisibility visibility = new JsonToModelConverter().convertTableColumnVisibility(tableUniqueIdentifier, params.get("params"));
                table.getTableColumnVisibilityModel().update(visibility);
            } else if (behaviorEvent.equals(HtmlTableToolbar.EVENT_REFRESH_TABLE)) {
                if (tableToolbar.getTableToolbarRefreshListener() != null) {
                    tableToolbar.getTableToolbarRefreshListener().onPreRefresh();
                }
            } else if (HtmlTableToolbar.EVENT_ORDER_COLUMN.equals(behaviorEvent) && table.getTableOrderingModel() != null) {
                final TableColumnOrdering ordering = new JsonToModelConverter().convertTableColumnOrdering(tableUniqueIdentifier, params.get("params"));
                table.getTableOrderingModel().update(ordering);
            }
        }
    }

    @Override
    public boolean getRendersChildren() {
        return true;
    }

    private void renderTableToolbarToggleColumnButton(final ResponseWriter writer,
                                                      final HtmlTableToolbar tableToolbar,
                                                      final HtmlTable table,
                                                      final WebXmlParameters webXmlParameters) throws IOException {
        final AjaxBehavior toggleAjaxBehavior = ClientBehaviorResolver.resolveActiveAjaxBehavior(tableToolbar, HtmlTableToolbar.EVENT_TOGGLE_COLUMN);
        final AjaxBehavior orderAjaxBehavior = ClientBehaviorResolver.resolveActiveAjaxBehavior(tableToolbar, HtmlTableToolbar.EVENT_ORDER_COLUMN);

        if (toggleAjaxBehavior != null && table.getTableColumnVisibilityModel() != null
                || orderAjaxBehavior != null && table.getTableOrderingModel() != null) {
            writer.startElement(ELEMENT_DIV, tableToolbar);
            writer.writeAttribute("class", "btn-group", null);

            // show and hide option toggle
            writer.startElement("a", tableToolbar);
            writer.writeAttribute("class", "btn btn-outline-secondary dropdown-toggle", null);
            writer.writeAttribute("data-toggle", "dropdown", null);
            writer.writeAttribute("title", tableToolbar.getColumnOptionsTooltip(), null);
            writer.writeAttribute("role", "button", null);
            writer.startElement("i", tableToolbar);
            writer.writeAttribute("class", webXmlParameters.getOptionsGlyphicon(), null);
            writer.endElement("i");
            writer.startElement("span", tableToolbar);
            writer.writeAttribute("class", "caret", null);
            writer.endElement("span");
            writer.endElement("a");

            // show and hide option content
            writer.startElement("ul", tableToolbar);
            writer.writeAttribute("class", "dropdown-menu dropdown-menu-form butter-table-toolbar-columns", null);
            writer.writeAttribute("role", "menu", null);

            final TableColumnCache tableColumnCache = table.getTableColumnCache(FacesContext.getCurrentInstance());

            int columnNumber = 0;
            for (HtmlColumn cachedColumn : tableColumnCache.getCachedColumns()) {
                writer.startElement("li", tableToolbar);
                writer.writeAttribute("class", "butter-table-toolbar-column-option", "styleClass");
                writer.writeAttribute("data-original-column", columnNumber, null);
                writer.writeAttribute("data-column-model-identifier", cachedColumn.getModelUniqueIdentifier(), null);

                if (toggleAjaxBehavior != null && table.getTableColumnVisibilityModel() != null) {
                    final List<String> rerenderIds = JsfAjaxRequest.createRerenderIds(tableToolbar, HtmlTableToolbar.EVENT_TOGGLE_COLUMN);
                    rerenderIds.add(table.getClientId());
                    this.renderToggleColumnInput(writer, tableToolbar, rerenderIds, cachedColumn, table, webXmlParameters);
                }

                writer.startElement("label", tableToolbar);
                writer.writeAttribute("class", "checkbox", "styleClass");
                writer.writeAttribute("title", cachedColumn.getLabel(), "title");
                writer.writeText(cachedColumn.getLabel(), null);
                writer.endElement("label");

                if (orderAjaxBehavior != null && table.getTableOrderingModel() != null) {
                    final List<String> rerenderIds = JsfAjaxRequest.createRerenderIds(tableToolbar, HtmlTableToolbar.EVENT_ORDER_COLUMN);
                    rerenderIds.add(table.getClientId());
                    this.renderOrderColumnSpan(writer, tableToolbar, rerenderIds, columnNumber, webXmlParameters);
                }

                writer.endElement("li");
                columnNumber++;
            }
            writer.endElement("ul");

            writer.endElement(ELEMENT_DIV);
        }
    }

    private void renderOrderColumnSpan(final ResponseWriter writer,
                                       final HtmlTableToolbar tableToolbar,
                                       final List<String> renderIds,
                                       final int columnNumber,
                                       final WebXmlParameters webXmlParameters) throws IOException {
        final String ajaxColumnOrderLeft = createModelJavaScriptCall(tableToolbar.getClientId(), renderIds, "orderColumn", isAjaxDisableRenderReqionOnRequest(tableToolbar, webXmlParameters), "true, " + columnNumber);
        final String ajaxColumnOrderRight = createModelJavaScriptCall(tableToolbar.getClientId(), renderIds, "orderColumn", isAjaxDisableRenderReqionOnRequest(tableToolbar, webXmlParameters), "false, " + columnNumber);

        writer.startElement("span", tableToolbar);
        writer.writeAttribute("class", "butter-table-toolbar-column-order-item butter-table-toolbar-column-order-item-up " + webXmlParameters.getOrderLeftGlyphicon(), "styleClass");
        writer.writeAttribute("onclick", ajaxColumnOrderLeft, null);
        writer.endElement("span");
        writer.startElement("span", tableToolbar);
        writer.writeAttribute("class", "butter-table-toolbar-column-order-item butter-table-toolbar-column-order-item-down " + webXmlParameters.getOrderRightGlyphicon(), "styleClass");
        writer.writeAttribute("onclick", ajaxColumnOrderRight, null);
        writer.endElement("span");
    }

    private Boolean isAjaxDisableRenderReqionOnRequest(final HtmlTableToolbar toolbar, final WebXmlParameters parameters) {
        final Boolean disableRegion = toolbar.isAjaxDisableRenderRegionsOnRequest();
        return disableRegion != null ? disableRegion : parameters.isAjaxDisableRenderRegionsOnRequest();
    }

    private void renderToggleColumnInput(final ResponseWriter writer,
                                         final HtmlTableToolbar tableToolbar,
                                         final List<String> renderIds,
                                         final HtmlColumn cachedColumn,
                                         final HtmlTable table,
                                         final WebXmlParameters webXmlParameters) throws IOException {
        writer.startElement("input", tableToolbar);
        writer.writeAttribute("type", "checkbox", null);

        final String ajax = createModelJavaScriptCall(tableToolbar.getClientId(), renderIds, "toggleColumnVisibilty", isAjaxDisableRenderReqionOnRequest(tableToolbar, webXmlParameters), null);

        writer.writeAttribute("onclick", ajax, null);

        if (!table.isHideColumn(cachedColumn)) {
            writer.writeAttribute("checked", "checked", null);
        }
        writer.endElement("input");
    }

    public static String createModelJavaScriptCall(final String clientId,
                                                   final List<String> renderIds,
                                                   final String javaScriptMethodName,
                                                   final boolean ajaxDisableRenderRegionsOnRequest,
                                                   final String optionalParameter) {
        final StringBuilder ajax = new StringBuilder("jQuery(document.getElementById('");
        ajax.append(clientId);
        ajax.append("'))." + javaScriptMethodName + "([");
        ajax.append(StringUtils.joinWithCommaSeparator(renderIds, true));
        if (StringUtils.isNotEmpty(optionalParameter)) {
            ajax.append("], " + ajaxDisableRenderRegionsOnRequest + ", " + optionalParameter + ");");
        } else {
            ajax.append("], " + ajaxDisableRenderRegionsOnRequest + ");");
        }
        return ajax.toString();
    }

    private void renderTableToolbarRefreshButton(final ResponseWriter writer,
                                                 final HtmlTableToolbar tableToolbar,
                                                 final HtmlTable table,
                                                 final WebXmlParameters webXmlParameters) throws IOException {
        final String eventName = "refresh";

        final AjaxBehavior ajaxBehavior = ClientBehaviorResolver.findFirstActiveAjaxBehavior(tableToolbar, eventName);
        if (ajaxBehavior != null) {
            final JsfAjaxRequest jsfAjaxRequest = new JsfAjaxRequest(tableToolbar.getClientId(), true)
                    .setRender(tableToolbar, eventName)
                    .addRender(table.getClientId())
                    .setEvent(eventName)
                    .addOnEventHandler(ajaxBehavior.getOnevent())
                    .addOnErrorHandler(ajaxBehavior.getOnerror())
                    .setBehaviorEvent(eventName);

            if (isAjaxDisableRenderReqionOnRequest(tableToolbar, webXmlParameters)) {
                final List<String> renderIds = new ArrayList<>(ajaxBehavior.getRender());
                renderIds.add(table.getClientId());
                final StringBuilder onEvent = new StringBuilder("ButterFaces.Ajax.disableElementsOnRequest(data, [");
                onEvent.append(StringUtils.joinWithCommaSeparator(renderIds, true));
                onEvent.append("])");
                jsfAjaxRequest.addOnEventHandler(onEvent.toString());
            }

            writer.startElement("a", tableToolbar);
            writer.writeAttribute("class", "btn btn-outline-secondary", null);
            writer.writeAttribute("role", "button", null);
            writer.writeAttribute("title", tableToolbar.getRefreshTooltip(), null);
            writer.writeAttribute("onclick", jsfAjaxRequest.toString(), null);

            writer.startElement("i", tableToolbar);
            writer.writeAttribute("class", webXmlParameters.getRefreshGlyphicon(), null);
            writer.endElement("i");

            writer.endElement("a");
        }
    }

    private HtmlTable getTableComponent(HtmlTableToolbar tableHeader) {
        return new UIComponentResolver().findComponent(tableHeader.getTableId(), HtmlTable.class);
    }
}
