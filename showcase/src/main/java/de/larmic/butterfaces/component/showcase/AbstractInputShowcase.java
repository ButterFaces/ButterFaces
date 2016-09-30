package de.larmic.butterfaces.component.showcase;

import de.larmic.butterfaces.component.showcase.example.AbstractCodeExample;
import de.larmic.butterfaces.component.showcase.example.CssCodeExample;
import de.larmic.butterfaces.component.showcase.example.XhtmlCodeExample;
import de.larmic.butterfaces.component.showcase.type.AjaxType;
import de.larmic.butterfaces.util.StringUtils;

import javax.faces.model.SelectItem;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractInputShowcase extends AbstractCodeShowcase {

    protected static final String DEFAULT_NUMBER_PLACEHOLDER = "Enter number...";
    protected static final String DEFAULT_TEXT_PLACEHOLDER = "Enter text...";

    private Object value;
    private String label = "label";
    private String tooltip = "tooltip";
    private boolean readonly;
    private boolean required;
    private boolean validation;
    private boolean hideLabel;
    private boolean disabled;
    private AjaxType ajaxType = AjaxType.NONE;
    private String styleClass = null;

    public AbstractInputShowcase() {
        this.value = this.initValue();
    }

    /**
     * @return specific value object (i.e. a String, Date or Enum) that is showing after loading showcase.
     */
    protected abstract Object initValue();

    /**
     * @return a readable value of field value (maybe translated enum or something).
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

    public List<SelectItem> getStyleClasses() {
        final List<SelectItem> items = new ArrayList<>();

        items.add(new SelectItem(null, "default (null)"));
        items.add(new SelectItem("demo-big-label", "demo-big-label"));

        return items;
    }

    public List<SelectItem> getLanguageExamples() {
        final List<SelectItem> items = new ArrayList<>();

        items.add(new SelectItem("en", "english (default)"));
        items.add(new SelectItem("de", "german"));
        items.add(new SelectItem("ru", "russian"));

        return items;
    }

    public void addAjaxTag(final XhtmlCodeExample codeExample, final String event) {
        if (this.isAjax()) {
            final String execute = AjaxType.THIS == this.ajaxType ? "@this" : "input";
            codeExample.appendInnerContent(
                    "            <f:ajax event=\"" + event + "\" execute=\"" + execute + "\" render=\"output\"/>");
        }
    }

    public void addOutputExample(final XhtmlCodeExample codeExample) {
        if (this.isAjax()) {
            codeExample.appendInnerContent("\n        <h:outputText id=\"output\" value=\"" + this.getValue() + "\"/>");
        }
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

    @Override
    public boolean isDisabled() {
        return disabled;
    }

    @Override
    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
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

    protected void generateDemoCSS(List<AbstractCodeExample> codeExamples) {
        if (StringUtils.isNotEmpty(this.getStyleClass())) {
            final CssCodeExample cssCodeExample = new CssCodeExample();
            cssCodeExample.addCss(".demo-big-label .butter-component-label", "width: 250px;");
            cssCodeExample.addCss(".demo-big-label .butter-component-value", "width: calc(100% - 250px);");
            codeExamples.add(cssCodeExample);
        }
    }
}
