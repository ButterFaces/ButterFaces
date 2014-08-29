package de.larmic.butterfaces.component.showcase;

import javax.faces.model.SelectItem;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractInputShowcaseComponent extends AbstractShowcaseComponent {

    protected static final String DEFAULT_NUMBER_PLACEHOLDER = "Enter number...";
    protected static final String DEFAULT_TEXT_PLACEHOLDER = "Enter text...";

    private Object value;
    private String label = "label";
    private String tooltip = "tooltip";
    private boolean readonly;
    private boolean required;
    private boolean floating;
    private boolean validation;
    private boolean hideLabel;
    private AjaxType ajaxType = AjaxType.NONE;

    public AbstractInputShowcaseComponent() {
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

    protected void addXhtmlStart(final StringBuilder sb) {
        sb.append("<!DOCTYPE html>");
        sb.append("\n");
        sb.append("<html xmlns=\"http://www.w3.org/1999/xhtml\"");
        sb.append("\n");
        sb.append("      xmlns:h=\"http://java.sun.com/jsf/html\"");
        sb.append("\n");
        sb.append("      xmlns:l=\"http://butterfaces.larmic.de/components\">");
        sb.append("\n");
        sb.append("<h:head />");
        sb.append("\n");
        sb.append("<body>");
        sb.append("\n");
        sb.append("    <form>");
        sb.append("\n");

    }

    protected void addXhtmlEnd(final StringBuilder sb) {
        sb.append("\n");
        sb.append("    </form>");
        sb.append("\n");
        sb.append("</body>");
        sb.append("\n");
        sb.append("</html>");
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

    protected void appendString(final String attribute, final String value, final StringBuilder sb, final boolean isLastValue) {
        if (value != null && !"".equals(value)) {
            sb.append(getEmptyDistanceString() + attribute + "=\"" + value + "\"" + (isLastValue ? ">" : "") + " \n");
        }
    }

    protected void appendString(final String attribute, final String value, final StringBuilder sb) {
        this.appendString(attribute, value, sb, false);
    }

    protected void appendBoolean(final String attribute, final boolean value, final StringBuilder sb, final boolean isLastValue) {
        if (value) {
            sb.append(getEmptyDistanceString() + attribute + "=\"" + value + "\"" + (isLastValue ? ">" : "") + " \n");
        }
    }

    protected String getEmptyDistanceString() {
        return "                ";
    }

    protected void appendBoolean(final String attribute, final boolean value, final StringBuilder sb) {
        this.appendBoolean(attribute, value, sb, false);
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

    public boolean isFloating() {
        return this.floating;
    }

    public void setFloating(final boolean floating) {
        this.floating = floating;
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
}
