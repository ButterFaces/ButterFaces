package de.larmic.butterfaces.component.showcase;

import java.util.ArrayList;
import java.util.List;

import javax.faces.model.SelectItem;

import de.larmic.butterfaces.component.showcase.text.FacetType;
import de.larmic.butterfaces.component.showcase.type.AjaxType;

/**
 * @deprecated Use {@link de.larmic.butterfaces.component.showcase.AbstractInputShowcase} instead.
 */
@Deprecated
public abstract class AbstractInputShowcaseSingleCodeComponent extends AbstractShowcaseSingleCodeComponent {

    protected static final String DEFAULT_NUMBER_PLACEHOLDER = "Enter number...";
    protected static final String DEFAULT_TEXT_PLACEHOLDER = "Enter text...";

    private Object value;
    private String label = "label";
    private String tooltip = "tooltip";
    private String badgeText = null;
    private boolean readonly;
    private boolean required;
    private boolean validation;
    private boolean hideLabel;
    private boolean autoFocus;
    private AjaxType ajaxType = AjaxType.NONE;
    private String styleClass = null;

    public AbstractInputShowcaseSingleCodeComponent() {
        this.value = this.initValue();
    }

    /**
     * @return specific value object (i.e. a String, Date or Enum) that is
     * showing after loading showcase.
     */
    protected abstract Object initValue();

    /**
     * @return a readable value of field value (maybe translated enum or
     * something).
     */
    public abstract String getReadableValue();

    public boolean isAjax() {
        return AjaxType.NONE != this.getAjaxType();
    }

    public List<SelectItem> getAjaxTypes() {
        final List<SelectItem> items = new ArrayList<>();

        for (final AjaxType type : AjaxType.values()) {
            items.add(new SelectItem(type, type.label));
        }
        return items;
    }

    public List<SelectItem> getAvailableFacetTypes() {
        final List<SelectItem> items = new ArrayList<>();

        for (final FacetType type : FacetType.values()) {
            items.add(new SelectItem(type, type.label));
        }
        return items;
    }

    public List<SelectItem> getStyleClasses() {
        final List<SelectItem> items = new ArrayList<>();

        items.add(new SelectItem(null, "default (null)"));
        items.add(new SelectItem("demo-big-label", "demo-big-label"));

        return items;
    }



    public void createAjaxXhtml(final StringBuilder sb, final String event) {
        if (this.isAjax()) {
            final String execute = AjaxType.THIS == this.ajaxType ? "@this" : "input";
            sb.append("            <f:ajax event=\"" + event + "\"  execute=\"" + execute + "\" render=\"output\"/>\n");
        }
    }

    public void createOutputXhtml(final StringBuilder sb) {
        if (this.isAjax()) {
            sb.append("\n");
            sb.append("\n");
            sb.append("        <h:outputText id=\"output\" value=\"" + this.getValue() + "\"/>");
        }
    }

    @Override
    public String getCss() {
        return super.getCss();
    }

    public void submit() {

    }

    public Object getValue() {
        return this.value;
    }

    public void setValue(final Object value) {
        this.value = value;
    }

    public String getLabel() {
        return this.label;
    }

    public void setLabel(final String label) {
        this.label = label;
    }

    public String getBadgeText() {
        return badgeText;
    }

    public void setBadgeText(String badgeText) {
        this.badgeText = badgeText;
    }

    public String getTooltip() {
        return this.tooltip;
    }

    public void setTooltip(final String tooltip) {
        this.tooltip = tooltip;
    }

    public boolean isReadonly() {
        return this.readonly;
    }

    public void setReadonly(final boolean readonly) {
        this.readonly = readonly;
    }

    public boolean isRequired() {
        return this.required;
    }

    public void setRequired(final boolean required) {
        this.required = required;
    }

    public boolean isValidation() {
        return this.validation;
    }

    public void setValidation(final boolean validation) {
        this.validation = validation;
    }

    public AjaxType getAjaxType() {
        return this.ajaxType;
    }

    public void setAjaxType(final AjaxType ajax) {
        this.ajaxType = ajax;
    }

    public boolean isHideLabel() {
        return hideLabel;
    }

    public void setHideLabel(boolean hideLabel) {
        this.hideLabel = hideLabel;
    }

    public String getStyleClass() {
        return styleClass;
    }

    public void setStyleClass(String styleClass) {
        this.styleClass = styleClass;
    }

    public boolean isAutoFocus() {
        return autoFocus;
    }

    public void setAutoFocus(boolean autoFocus) {
        this.autoFocus = autoFocus;
    }
}
