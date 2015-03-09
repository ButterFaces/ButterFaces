package de.larmic.butterfaces.component.html.table;

import javax.el.ValueExpression;
import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;
import javax.faces.component.FacesComponent;
import javax.faces.component.UIComponentBase;
import javax.faces.component.behavior.ClientBehaviorHolder;
import java.util.Arrays;
import java.util.Collection;

/**
 * Created by larmic on 10.09.14.
 */
@ResourceDependencies({
        @ResourceDependency(library = "butterfaces-configurable", name = "jquery.min.js", target = "head"),
        @ResourceDependency(library = "butterfaces-configurable", name = "bootstrap.min.css", target = "head"),
        @ResourceDependency(library = "butterfaces-configurable", name = "bootstrap.min.js", target = "head"),
        @ResourceDependency(library = "butterfaces-css", name = "butterfaces-table.css", target = "head"),
        @ResourceDependency(library = "butterfaces-js", name = "butterfaces-bootstrap-fixes.jquery.js", target = "head"),
        @ResourceDependency(library = "butterfaces-js", name = "butterfaces-disableElements.jquery.js", target = "head")
})
@FacesComponent(HtmlTableToolbar.COMPONENT_TYPE)
public class HtmlTableToolbar extends UIComponentBase implements ClientBehaviorHolder {

    public static final String COMPONENT_TYPE = "de.larmic.butterfaces.component.table.toolbar";
    public static final String COMPONENT_FAMILY = "de.larmic.butterfaces.component.family";
    public static final String RENDERER_TYPE = "de.larmic.butterfaces.renderkit.html_basic.TableHeaderRenderer";

    protected static final String PROPERTY_TABLE_ID = "tableId";
    protected static final String PROPERTY_TABLE_SHOW_REFRESH_BUTTON = "showRefreshButton";
    protected static final String PROPERTY_TABLE_SHOW_TOGGLE_COLUMN_BUTTON = "showToggleColumnButton";
    protected static final String PROPERTY_AJAX_DISABLE_RENDER_REGION_ON_REQUEST = "ajaxDisableRenderRegionsOnRequest";

    public HtmlTableToolbar() {
        super();
        this.setRendererType(RENDERER_TYPE);
    }

    @Override
    public Collection<String> getEventNames() {
        return Arrays.asList("click");
    }

    @Override
    public String getDefaultEventName() {
        return "click";
    }

    @Override
    public String getFamily() {
        return COMPONENT_FAMILY;
    }

    public String getTableId() {
        return (String) this.getStateHelper().eval(PROPERTY_TABLE_ID);
    }

    public void setTableId(String tableId) {
        this.updateStateHelper(PROPERTY_TABLE_ID, tableId);
    }

    public boolean isShowRefreshButton() {
        final Object eval = this.getStateHelper().eval(PROPERTY_TABLE_SHOW_REFRESH_BUTTON);
        return eval == null ? true : (Boolean) eval;
    }

    public void setShowRefreshButton(boolean showRefreshButton) {
        this.updateStateHelper(PROPERTY_TABLE_SHOW_REFRESH_BUTTON, showRefreshButton);
    }

    public boolean isShowToggleColumnButton() {
        final Object eval = this.getStateHelper().eval(PROPERTY_TABLE_SHOW_TOGGLE_COLUMN_BUTTON);
        return eval == null ? true : (Boolean) eval;
    }

    public void setShowToggleColumnButton(boolean showToggleColumnButton) {
        this.updateStateHelper(PROPERTY_TABLE_SHOW_TOGGLE_COLUMN_BUTTON, showToggleColumnButton);
    }

    public boolean isAjaxDisableRenderRegionsOnRequest() {
        final Object eval = this.getStateHelper().eval(PROPERTY_AJAX_DISABLE_RENDER_REGION_ON_REQUEST);
        return eval == null ? true : (Boolean) eval;
    }

    public void setAjaxDisableRenderRegionsOnRequest(boolean ajaxDisableRenderRegionsOnRequest) {
        this.updateStateHelper(PROPERTY_AJAX_DISABLE_RENDER_REGION_ON_REQUEST, ajaxDisableRenderRegionsOnRequest);
    }

    private void updateStateHelper(final String propertyName, final Object value) {
        this.getStateHelper().put(propertyName, value);

        final ValueExpression ve = this.getValueExpression(propertyName);

        if (ve != null) {
            ve.setValue(this.getFacesContext().getELContext(), value);
        }
    }
}
