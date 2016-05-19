/*
 * Copyright Lars Michaelis and Stephan Zerhusen 2016.
 * Distributed under the MIT License.
 * (See accompanying file README.md file or copy at http://opensource.org/licenses/MIT)
 */
package de.larmic.butterfaces.component.showcase.table;

import de.larmic.butterfaces.component.showcase.AbstractCodeShowcase;
import de.larmic.butterfaces.component.showcase.example.*;
import de.larmic.butterfaces.component.showcase.table.example.DemoPojoCodeExample;
import de.larmic.butterfaces.component.showcase.table.example.TableWebXmlExample;
import de.larmic.butterfaces.component.showcase.tree.SelectionAjaxType;
import de.larmic.butterfaces.event.TableSingleSelectionListener;
import de.larmic.butterfaces.model.table.DefaultTableModel;
import de.larmic.butterfaces.model.table.SortType;
import de.larmic.butterfaces.model.table.TableToolbarRefreshListener;
import org.apache.commons.lang3.StringUtils;

import javax.faces.model.SelectItem;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Lars Michaelis
 */
@Named
@ViewScoped
public class TableShowcase extends AbstractCodeShowcase implements Serializable {

    private final List<DemoPojo> demoPojos = new ArrayList<>();
    private DemoPojo selectedValue = null;
    private String doSomethingWithRow = null;
    private SelectionAjaxType selectionAjaxType = SelectionAjaxType.AJAX;
    private FourthColumnWidthType fourthColumnWidthType = FourthColumnWidthType.NONE;
    private ToolBarType toolBarType = ToolBarType.SERVER_FILTER;
    private ToolbarFacetType toolbarFacetType = ToolbarFacetType.NONE;
    private DefaultTableModel tableModel = new DefaultTableModel();

    private String refreshTooltip;
    private String columnOptionsTooltip;
    private boolean tableCondensed;
    private boolean tableBordered;
    private boolean tableStriped = true;
    private boolean showRefreshButton = true;
    private boolean showToggleColumnButton = true;
    private boolean showOrderColumnButton = true;
    private boolean ajaxDisableRenderRegionsOnRequest = true;
    private boolean useTableModel = true;
    private boolean useSelectionListener = true;
    private String filterValue;
    private String colWidthColumn1;
    private String colWidthColumn2;
    private String colWidthColumn3;
    private String colWidthColumn4;
    private int numberOfRefreshes;

    public TableShowcase() {
        tableModel.getTableRowSortingModel().sortColumn("filterTable", "column1", null, SortType.ASCENDING);
    }

    @Override
    public void buildCodeExamples(final List<AbstractCodeExample> codeExamples) {
        codeExamples.add(this.createXhtmlCodeExample());
        codeExamples.add(this.createMyBeanCodeExample());
        codeExamples.add(new DemoPojoCodeExample("table.demo"));
        codeExamples.add(new TableWebXmlExample());

        if (this.toolBarType == ToolBarType.TEXT) {
            final CssCodeExample cssCodeExample = new CssCodeExample();
            cssCodeExample.addCss(".butter-table-toolbar-custom", "margin-top: 10px;");
            codeExamples.add(cssCodeExample);
        }

    }

    public List<DemoPojo> getStringRows() {
        if (demoPojos.isEmpty()) {
            for (int row = 0; row < 10; row++) {
                final int rowNumber = row + 1;
                demoPojos.add(new DemoPojo(rowNumber, String.format("r%sc1", rowNumber), String.format("r%sc2", rowNumber)));
            }
        }

        if (toolBarType == ToolBarType.SERVER_FILTER && StringUtils.isNotEmpty(filterValue)) {
            final List<DemoPojo> filteredDemoPojos = new ArrayList<>();

            for (DemoPojo demoPojo : demoPojos) {
                if (StringUtils.containsIgnoreCase(demoPojo.getA(), filterValue)
                        || StringUtils.containsIgnoreCase(demoPojo.getB(), filterValue)) {
                    filteredDemoPojos.add(demoPojo);
                }
            }

            return filteredDemoPojos;
        }

        if (this.shouldReverseRows()) {
            Collections.reverse(demoPojos);
        }

        return demoPojos;
    }

    private boolean shouldReverseRows() {
        if ((this.tableModel.getTableRowSortingModel().getSortType(null, "column1") == SortType.ASCENDING
                || this.tableModel.getTableRowSortingModel().getSortType(null, "column3") == SortType.ASCENDING
                || this.tableModel.getTableRowSortingModel().getSortType(null, "column4") == SortType.ASCENDING)
                && !demoPojos.get(0).getA().equals("r1c1")) {
            return true;
        } else if ((this.tableModel.getTableRowSortingModel().getSortType(null, "column1") == SortType.DESCENDING
                || this.tableModel.getTableRowSortingModel().getSortType(null, "column3") == SortType.DESCENDING
                || this.tableModel.getTableRowSortingModel().getSortType(null, "column4") == SortType.DESCENDING)
                && demoPojos.get(0).getA().equals("r1c1")) {
            return true;
        }


        return false;
    }

    public TableSingleSelectionListener<DemoPojo> getTableSelectionListener() {
        if (useSelectionListener) {
            return new TableSingleSelectionListener<DemoPojo>() {
                @Override
                public void processTableSelection(DemoPojo data) {
                    selectedValue = data;
                }

                @Override
                public boolean isValueSelected(DemoPojo data) {
                    return selectedValue != null ? data.getIdentifier() == selectedValue.getIdentifier() : false;
                }
            };
        }

        return null;
    }

    private XhtmlCodeExample createXhtmlCodeExample() {
        final XhtmlCodeExample xhtmlCodeExample = new XhtmlCodeExample(true);

        if (showRefreshButton) {
            xhtmlCodeExample.appendInnerContent("        <h:panelGroup id=\"numberOfRefreshes\"");
            xhtmlCodeExample.appendInnerContent("                      layout=\"block\"");
            xhtmlCodeExample.appendInnerContent("                      styleClass=\"alert alert-success\">");
            xhtmlCodeExample.appendInnerContent("            Number of refresh clicks: #{myBean.numberOfRefreshes}");
            xhtmlCodeExample.appendInnerContent("        </h:panelGroup>\n");
        }

        xhtmlCodeExample.appendInnerContent("        <b:tableToolbar tableId=\"table\"");
        xhtmlCodeExample.appendInnerContent("                        ajaxDisableRenderRegionsOnRequest=\"" + this.ajaxDisableRenderRegionsOnRequest + "\"");
        if (showRefreshButton) {
            xhtmlCodeExample.appendInnerContent("                        refreshListener=\"#{myBean.toolbarRefreshListener}\"");
        }
        if (refreshTooltip != null) {
            xhtmlCodeExample.appendInnerContent("                        refreshTooltip=\"" + refreshTooltip + "\"");
        }
        if (columnOptionsTooltip != null) {
            xhtmlCodeExample.appendInnerContent("                        columnOptionsTooltip=\"" + columnOptionsTooltip + "\"");
        }
        xhtmlCodeExample.appendInnerContent("                        rendered=\"" + this.isRendered() + "\">");
        if (showRefreshButton) {
            xhtmlCodeExample.appendInnerContent("            <!-- add refresh ajax event to enable refresh button -->");
            xhtmlCodeExample.appendInnerContent("            <f:ajax event=\"refresh\" render=\"formId:numberOfRefreshes\" />");
        }
        if (showToggleColumnButton) {
            xhtmlCodeExample.appendInnerContent("            <!-- add toggle ajax event to enable toggle column buttons -->");
            xhtmlCodeExample.appendInnerContent("            <f:ajax event=\"toggle\" />");
        }
        if (showOrderColumnButton) {
            xhtmlCodeExample.appendInnerContent("            <!-- add order ajax event to enable order column buttons -->");
            xhtmlCodeExample.appendInnerContent("            <f:ajax event=\"order\" />");
        }
        if (this.toolbarFacetType == ToolbarFacetType.LEFT_FACET) {
            xhtmlCodeExample.appendInnerContent("            <f:facet name=\"default-options-left\">");
            xhtmlCodeExample.appendInnerContent("                <a class=\"btn btn-default\">Left facet</a>");
            xhtmlCodeExample.appendInnerContent("            </f:facet>");
        } else if (this.toolbarFacetType == ToolbarFacetType.CENTER_FACET) {
            xhtmlCodeExample.appendInnerContent("            <f:facet name=\"default-options-center\">");
            xhtmlCodeExample.appendInnerContent("                <a class=\"btn btn-default\">Center facet</a>");
            xhtmlCodeExample.appendInnerContent("            </f:facet>");
        } else if (this.toolbarFacetType == ToolbarFacetType.RIGHT_FACET) {
            xhtmlCodeExample.appendInnerContent("            <f:facet name=\"default-options-right\">");
            xhtmlCodeExample.appendInnerContent("                <a class=\"btn btn-default\">Right facet</a>");
            xhtmlCodeExample.appendInnerContent("            </f:facet>");
        }
        if (this.toolBarType == ToolBarType.TEXT) {
            xhtmlCodeExample.appendInnerContent("            Custom toolbar text...");
        } else if (this.toolBarType == ToolBarType.SERVER_FILTER) {
            xhtmlCodeExample.appendInnerContent("            <b:text value=\"#{myBean.filterValue}\"");
            xhtmlCodeExample.appendInnerContent("                    placeholder=\"Enter text...\"");
            xhtmlCodeExample.appendInnerContent("                    autoFocus=\"true\"");
            xhtmlCodeExample.appendInnerContent("                    hideLabel=\"true\">");
            xhtmlCodeExample.appendInnerContent("                <f:ajax event=\"keyup\" render=\"table\"/>");
            xhtmlCodeExample.appendInnerContent("            </b:text>");
        } else if (toolBarType == ToolBarType.CLIENT_FILTER) {
            xhtmlCodeExample.appendInnerContent("            <div class=\"form-inline pull-left\" role=\"form\">");
            xhtmlCodeExample.appendInnerContent("                <div class=\"form-group\">");
            xhtmlCodeExample.appendInnerContent("                    <input type=\"text\"");
            xhtmlCodeExample.appendInnerContent("                           class=\"form-control jQueryPluginSelector\"");
            xhtmlCodeExample.appendInnerContent("                           placeholder=\"Enter text...\"");
            xhtmlCodeExample.appendInnerContent("                           data-filterable-item-container=\".butter-table\"/>");
            xhtmlCodeExample.appendInnerContent("                </div>");
            xhtmlCodeExample.appendInnerContent("            </div>");
        }
        xhtmlCodeExample.appendInnerContent("        </b:tableToolbar>\n");

        xhtmlCodeExample.appendInnerContent("        <b:table id=\"table\"");
        xhtmlCodeExample.appendInnerContent("                 var=\"rowItem\"");
        xhtmlCodeExample.appendInnerContent("                 value=\"#{myBean.value}\"");
        if (useTableModel) {
            xhtmlCodeExample.appendInnerContent("                 model=\"#{myBean.tableModel}\"");
        }
        if (selectionAjaxType == SelectionAjaxType.AJAX && useSelectionListener) {
            xhtmlCodeExample.appendInnerContent("                 singleSelectionListener=\"#{myBean}\"");
        }
        if (toolBarType == ToolBarType.CLIENT_FILTER) {
            xhtmlCodeExample.appendInnerContent("                 rowClass=\"filterable-item\"");
        }
        xhtmlCodeExample.appendInnerContent("                 tableBordered=\"" + this.tableBordered + "\"");
        xhtmlCodeExample.appendInnerContent("                 tableCondensed=\"" + this.tableCondensed + "\"");
        xhtmlCodeExample.appendInnerContent("                 tableStriped=\"" + this.tableStriped + "\"");
        xhtmlCodeExample.appendInnerContent("                 ajaxDisableRenderRegionsOnRequest=\"" + this.ajaxDisableRenderRegionsOnRequest + "\"");
        xhtmlCodeExample.appendInnerContent("                 rendered=\"" + this.isRendered() + "\">");

        xhtmlCodeExample.appendInnerContent("            <!-- at this time you have to put an ajax tag to activate some features-->");
        if (selectionAjaxType == SelectionAjaxType.AJAX) {
            xhtmlCodeExample.appendInnerContent("            <f:ajax render=\"formId:selectedRow\"/>");
        } else if (selectionAjaxType == SelectionAjaxType.AJAX_DISABLED) {
            xhtmlCodeExample.appendInnerContent("            <f:ajax render=\"formId:selectedRow\" disabled=\"true\"/>");
        }

        xhtmlCodeExample.appendInnerContent("            <b:column id=\"column1\"");
        if (fourthColumnWidthType == FourthColumnWidthType.PERCENT) {
            xhtmlCodeExample.appendInnerContent("                    colWidth=\"10%\"");
        } else if (fourthColumnWidthType == FourthColumnWidthType.PX) {
            xhtmlCodeExample.appendInnerContent("                    colWidth=\"50px\"");
        } else if (fourthColumnWidthType == FourthColumnWidthType.RELATIVE) {
            xhtmlCodeExample.appendInnerContent("                    colWidth=\"5*\"");
        }
        xhtmlCodeExample.appendInnerContent("                      label=\"Readonly text\">");
        xhtmlCodeExample.appendInnerContent("                /* text */");
        xhtmlCodeExample.appendInnerContent("                <b:tooltip placement=\"top\">");
        xhtmlCodeExample.appendInnerContent("                   /* tooltip text */");
        xhtmlCodeExample.appendInnerContent("                </b:tooltip>");
        xhtmlCodeExample.appendInnerContent("            </b:column>");

        xhtmlCodeExample.appendInnerContent("            <b:column id=\"column2\"");
        if (fourthColumnWidthType == FourthColumnWidthType.PERCENT) {
            xhtmlCodeExample.appendInnerContent("                      colWidth=\"65%\"");
        } else if (fourthColumnWidthType == FourthColumnWidthType.PX) {
            xhtmlCodeExample.appendInnerContent("                      colWidth=\"30px\"");
        } else if (fourthColumnWidthType == FourthColumnWidthType.RELATIVE) {
            xhtmlCodeExample.appendInnerContent("                      colWidth=\"1*\"");
        }
        xhtmlCodeExample.appendInnerContent("                      sortColumnEnabled=\"false\"");
        xhtmlCodeExample.appendInnerContent("                      label=\"Action\">");
        xhtmlCodeExample.appendInnerContent("                /* action */");
        xhtmlCodeExample.appendInnerContent("            </b:column>");

        xhtmlCodeExample.appendInnerContent("            <b:column id=\"column3\"");
        if (fourthColumnWidthType == FourthColumnWidthType.PERCENT) {
            xhtmlCodeExample.appendInnerContent("                      colWidth=\"15%\"");
        } else if (fourthColumnWidthType == FourthColumnWidthType.PX) {
            xhtmlCodeExample.appendInnerContent("                      colWidth=\"10px\"");
        } else if (fourthColumnWidthType == FourthColumnWidthType.RELATIVE) {
            xhtmlCodeExample.appendInnerContent("                      colWidth=\"1*\"");
        }
        xhtmlCodeExample.appendInnerContent("                      label=\"Creation date\">");
        xhtmlCodeExample.appendInnerContent("                /* date */");
        xhtmlCodeExample.appendInnerContent("            </b:column>");

        xhtmlCodeExample.appendInnerContent("            <b:column id=\"column4\"");
        if (fourthColumnWidthType == FourthColumnWidthType.PERCENT) {
            xhtmlCodeExample.appendInnerContent("                      colWidth=\"10%\"");
        } else if (fourthColumnWidthType == FourthColumnWidthType.PX) {
            xhtmlCodeExample.appendInnerContent("                      colWidth=\"10px\"");
        } else if (fourthColumnWidthType == FourthColumnWidthType.RELATIVE) {
            xhtmlCodeExample.appendInnerContent("                      colWidth=\"1*\"");
        }
        xhtmlCodeExample.appendInnerContent("                      sortColumnEnabled=\"false\"");
        xhtmlCodeExample.appendInnerContent("                      hideColumn=\"true\"");
        xhtmlCodeExample.appendInnerContent("                      label=\"Readonly text (default hide)\">");
        xhtmlCodeExample.appendInnerContent("                /* disabled input with text */");
        xhtmlCodeExample.appendInnerContent("            </b:column>");

        xhtmlCodeExample.appendInnerContent("        </b:table>");

        if (selectionAjaxType != SelectionAjaxType.NONE) {
            xhtmlCodeExample.appendInnerContent("\n        <h:panelGroup id=\"selectedRow\">");
            xhtmlCodeExample.appendInnerContent("            <h:output value=\"#{myBean.selectedRow.a}\"");
            xhtmlCodeExample.appendInnerContent("                      rendered=\"#{not empty myBean.selectedRow}\"/>");
            xhtmlCodeExample.appendInnerContent("        <h:panelGroup/>");
        }

        if (toolBarType == ToolBarType.CLIENT_FILTER) {
            xhtmlCodeExample.appendInnerContent("\n        /* activate client side filter jquery plugin */");
            xhtmlCodeExample.appendInnerContent("        <b:activateLibraries>");
            xhtmlCodeExample.appendInnerContent("        <script type=\"text/javascript\">");
            xhtmlCodeExample.appendInnerContent("            jQuery('.jQueryPluginSelector').butterItemFilterField();");
            xhtmlCodeExample.appendInnerContent("        </script>");
        }

        return xhtmlCodeExample;
    }

    private JavaCodeExample createMyBeanCodeExample() {
        final JavaCodeExample myBean = new JavaCodeExample("MyBean.java", "mybean", "table.demo", "MyBean", true);

        if (this.selectionAjaxType == SelectionAjaxType.AJAX) {
            myBean.addImport("de.larmic.butterfaces.event.TableSingleSelectionListener");
        }
        if (useTableModel) {
            myBean.addImport("de.larmic.butterfaces.model.table.TableModel");
            myBean.addImport("de.larmic.butterfaces.model.table.DefaultTableModel");
        }
        if (showRefreshButton) {
            myBean.addImport("de.larmic.butterfaces.model.table.TableToolbarRefreshListener");
        }

        myBean.addImport("javax.faces.view.ViewScoped");
        myBean.addImport("javax.inject.Named");

        if (this.selectionAjaxType == SelectionAjaxType.AJAX) {
            myBean.addInterfaces("TableSingleSelectionListener");
        }

        if (showRefreshButton) {
            myBean.addInterfaces("TableToolbarRefreshListener");
        }

        if (useTableModel) {
            myBean.appendInnerContent("    private TableModel tableModel = new DefaultTableModel();\n");
        }
        if (this.toolBarType == ToolBarType.SERVER_FILTER) {
            myBean.appendInnerContent("    private String filterValue;\n");
        }
        if (showRefreshButton) {
            myBean.appendInnerContent("    private int numberOfRefreshes;\n");
        }

        if (useTableModel) {
            myBean.appendInnerContent("    @PostConstruct");
            myBean.appendInnerContent("    public init() {");
            myBean.appendInnerContent("       // initial table ordering by first column");
            myBean.appendInnerContent("       tableModel.getTableRowSortingModel()");
            myBean.appendInnerContent("              .sortColumn(\"table\", \"column1\", null, SortType.ASCENDING);");
            myBean.appendInnerContent("    }");
        }

        myBean.appendInnerContent("    public List<DemoPojo> getValue() {");
        myBean.appendInnerContent("        final List<DemoPojo> pairs = new ArrayList<>();");
        myBean.appendInnerContent("        pairs.add(new DemoPojo(1L, \"r1c1\", \"r1c2\"));");
        myBean.appendInnerContent("        pairs.add(new DemoPojo(2L, \"r2c1\", \"r2c2\"));");
        myBean.appendInnerContent("        pairs.add(new DemoPojo(3L, \"r3c1\", \"r3c2\"));");
        myBean.appendInnerContent("        pairs.add(new DemoPojo(4L, \"r4c1\", \"r4c2\"));");
        myBean.appendInnerContent("        pairs.add(new DemoPojo(5L, \"r5c1\", \"r5c2\"));");
        myBean.appendInnerContent("        pairs.add(new DemoPojo(6L, \"r6c1\", \"r6c2\"));");
        myBean.appendInnerContent("        pairs.add(new DemoPojo(7L, \"r7c1\", \"r7c2\"));");
        if (this.selectionAjaxType == SelectionAjaxType.AJAX && useTableModel) {
            myBean.appendInnerContent("        // TODO sort by table model");
        }
        if (this.toolBarType == ToolBarType.SERVER_FILTER) {
            myBean.appendInnerContent("        return this.filterByValue(pairs, this.filterValue);");
        } else {
            myBean.appendInnerContent("        return pairs;");
        }
        myBean.appendInnerContent("    }\n");

        if (selectionAjaxType == SelectionAjaxType.AJAX) {
            myBean.appendInnerContent("    private DemoPojo selectedRow;\n");
            myBean.appendInnerContent("    @Override");
            myBean.appendInnerContent("    public void processTableSelection(final DemoPojo data) {");
            myBean.appendInnerContent("        this.selectedRow = data;");
            myBean.appendInnerContent("    }\n");
            myBean.appendInnerContent("    @Override");
            myBean.appendInnerContent("    public boolean isValueSelected(DemoPojo data) {");
            myBean.appendInnerContent("        return selectedRow != null ? data.getId() == selectedRow.getId() : false;");
            myBean.appendInnerContent("    }\n");
        }

        if (showRefreshButton) {
            myBean.appendInnerContent("    @Override");
            myBean.appendInnerContent("    public void onPreRefresh() {");
            myBean.appendInnerContent("        numberOfRefreshes++;");
            myBean.appendInnerContent("    }\n");
            myBean.appendInnerContent("    public int getNumberOfRefreshes() {");
            myBean.appendInnerContent("        return numberOfRefreshes;");
            myBean.appendInnerContent("    }\n");
        }

        if (this.toolBarType == ToolBarType.SERVER_FILTER) {
            myBean.appendInnerContent("    public List<DemoPojo> filterByValue(final List<DemoPojo> pairs,");
            myBean.appendInnerContent("                                          final String filterValue) {");
            myBean.appendInnerContent("        // TODO implement me");
            myBean.appendInnerContent("        return pairs;");
            myBean.appendInnerContent("    }\n");
            myBean.appendInnerContent("    public String getFilterValue() {");
            myBean.appendInnerContent("        return this.filterValue;");
            myBean.appendInnerContent("    }\n");
            myBean.appendInnerContent("    public void setFilterValue(final String filterValue) {");
            myBean.appendInnerContent("        this.filterValue = filterValue;");
            myBean.appendInnerContent("    }\n");
        }

        if (selectionAjaxType == SelectionAjaxType.AJAX) {
            myBean.appendInnerContent("    public DemoPojo getSelectedRow() {");
            myBean.appendInnerContent("        return selectedRow;");
            myBean.appendInnerContent("    }\n");
        }

        if (useTableModel) {
            myBean.appendInnerContent("    public TableModel getTableModel() {");
            myBean.appendInnerContent("        return this.tableModel;");
            myBean.appendInnerContent("    }");
        }

        return myBean;
    }

    public List<SelectItem> getAjaxSelectionTypes() {
        final List<SelectItem> items = new ArrayList<>();

        for (final SelectionAjaxType type : SelectionAjaxType.values()) {
            items.add(new SelectItem(type, type.label));
        }
        return items;
    }

    public List<SelectItem> getToolBarFacetTypes() {
        final List<SelectItem> items = new ArrayList<>();

        for (final ToolbarFacetType type : ToolbarFacetType.values()) {
            items.add(new SelectItem(type, type.label));
        }
        return items;
    }

    public List<SelectItem> getToolBarTypes() {
        final List<SelectItem> items = new ArrayList<>();

        for (final ToolBarType type : ToolBarType.values()) {
            items.add(new SelectItem(type, type.label));
        }
        return items;
    }

    public List<SelectItem> getTwoColumnWidthTypes() {
        final List<SelectItem> items = new ArrayList<>();

        for (final FourthColumnWidthType type : FourthColumnWidthType.values()) {
            items.add(new SelectItem(type, type.label));
        }
        return items;
    }

    public void doSomethingWith(final DemoPojo selectedValue) {
        this.doSomethingWithRow = "I have done something with " + (selectedValue == null ? "null" : selectedValue.getA());
    }

    public SelectionAjaxType getSelectionAjaxType() {
        return selectionAjaxType;
    }

    public void setSelectionAjaxType(final SelectionAjaxType selectionAjaxType) {
        this.selectionAjaxType = selectionAjaxType;
    }

    public DemoPojo getSelectedValue() {
        return this.selectedValue;
    }

    public String getDoSomethingWithRow() {
        return doSomethingWithRow;
    }

    public boolean isTableCondensed() {
        return tableCondensed;
    }

    public void setTableCondensed(boolean tableCondensed) {
        this.tableCondensed = tableCondensed;
    }

    public boolean isTableBordered() {
        return tableBordered;
    }

    public void setTableBordered(boolean tableBordered) {
        this.tableBordered = tableBordered;
    }

    public boolean isTableStriped() {
        return tableStriped;
    }

    public void setTableStriped(boolean tableStriped) {
        this.tableStriped = tableStriped;
    }

    public boolean isShowRefreshButton() {
        return showRefreshButton;
    }

    public void setShowRefreshButton(boolean showRefreshButton) {
        this.showRefreshButton = showRefreshButton;
    }

    public boolean isShowToggleColumnButton() {
        return showToggleColumnButton;
    }

    public boolean isShowOrderColumnButton() {
        return showOrderColumnButton;
    }

    public void setShowOrderColumnButton(boolean showOrderColumnButton) {
        this.showOrderColumnButton = showOrderColumnButton;
    }

    public TableToolbarRefreshListener getToolbarRefreshListener() {
        return new TableToolbarRefreshListener() {
            @Override
            public void onPreRefresh() {
                // do nothing at this time...
                // could be used for debugging
                numberOfRefreshes++;
            }
        };
    }

    public void setShowToggleColumnButton(boolean showToggleColumnButton) {
        this.showToggleColumnButton = showToggleColumnButton;
    }

    public FourthColumnWidthType getFourthColumnWidthType() {
        return fourthColumnWidthType;
    }

    public void setFourthColumnWidthType(FourthColumnWidthType fourthColumnWidthType) {
        this.fourthColumnWidthType = fourthColumnWidthType;

        switch (this.fourthColumnWidthType) {
            case NONE:
                colWidthColumn1 = null;
                colWidthColumn2 = null;
                colWidthColumn3 = null;
                colWidthColumn4 = null;
                break;
            case PERCENT:
                colWidthColumn1 = "10%";
                colWidthColumn2 = "65%";
                colWidthColumn3 = "15%";
                colWidthColumn4 = "10%";
                break;
            case PX:
                colWidthColumn1 = "50px";
                colWidthColumn2 = "30px";
                colWidthColumn3 = "10px";
                colWidthColumn4 = "10px";
                break;
            case RELATIVE:
                colWidthColumn1 = "5*";
                colWidthColumn2 = "1*";
                colWidthColumn3 = "7*";
                colWidthColumn4 = "1*";
                break;
        }
    }

    public String getColWidthColumn1() {
        return colWidthColumn1;
    }

    public String getColWidthColumn2() {
        return colWidthColumn2;
    }

    public String getColWidthColumn3() {
        return colWidthColumn3;
    }

    public String getColWidthColumn4() {
        return colWidthColumn4;
    }

    public DefaultTableModel getTableModel() {
        if (useTableModel) {
            return tableModel;
        }

        return null;
    }

    public ToolBarType getToolBarType() {
        return toolBarType;
    }

    public void setToolBarType(ToolBarType toolBarType) {
        this.toolBarType = toolBarType;
    }

    public String getFilterValue() {
        return filterValue;
    }

    public void setFilterValue(String filterValue) {
        this.filterValue = filterValue;
    }

    public String getRowClass() {
        return toolBarType == ToolBarType.CLIENT_FILTER ? "filterable-item" : null;
    }

    public boolean isAjaxDisableRenderRegionsOnRequest() {
        return ajaxDisableRenderRegionsOnRequest;
    }

    public void setAjaxDisableRenderRegionsOnRequest(boolean ajaxDisableRenderRegionsOnRequest) {
        this.ajaxDisableRenderRegionsOnRequest = ajaxDisableRenderRegionsOnRequest;
    }

    public String getRefreshTooltip() {
        return refreshTooltip;
    }

    public void setRefreshTooltip(String refreshTooltip) {
        this.refreshTooltip = refreshTooltip;
    }

    public String getColumnOptionsTooltip() {
        return columnOptionsTooltip;
    }

    public void setColumnOptionsTooltip(String columnOptionsTooltip) {
        this.columnOptionsTooltip = columnOptionsTooltip;
    }

    public ToolbarFacetType getToolbarFacetType() {
        return toolbarFacetType;
    }

    public void setToolbarFacetType(ToolbarFacetType toolbarFacetType) {
        this.toolbarFacetType = toolbarFacetType;
    }

    public int getNumberOfRefreshes() {
        return numberOfRefreshes;
    }

    public boolean isUseTableModel() {
        return useTableModel;
    }

    public void setUseTableModel(boolean useTableModel) {
        this.useTableModel = useTableModel;
    }

    public boolean isUseSelectionListener() {
        return useSelectionListener;
    }

    public void setUseSelectionListener(boolean useSelectionListener) {
        this.useSelectionListener = useSelectionListener;
    }
}

