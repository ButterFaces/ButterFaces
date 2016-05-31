package de.larmic.butterfaces.component.showcase.maskedText;

import de.larmic.butterfaces.util.StringUtils;
import de.larmic.butterfaces.component.showcase.AbstractInputShowcase;
import de.larmic.butterfaces.component.showcase.example.AbstractCodeExample;
import de.larmic.butterfaces.component.showcase.example.XhtmlCodeExample;
import de.larmic.butterfaces.component.showcase.text.FacetType;

import javax.annotation.PostConstruct;
import javax.faces.model.SelectItem;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Named
@ViewScoped
@SuppressWarnings("serial")
public class MaskedTextShowcase extends AbstractInputShowcase implements Serializable {

    private FacetType selectedFacetType = FacetType.NONE;
    private InputMaskType selectedInputMaskType = null;
    private String placeholder = DEFAULT_TEXT_PLACEHOLDER;
    private String type;
    private String pattern;
    private String min;
    private String max;
    private boolean autoFocus;
    private String inputMask;
    private String dataInputMask;

    @PostConstruct
    public void init() {
        this.setSelectedInputMaskType(InputMaskType.PHONE);
    }

    @Override
    protected Object initValue() {
        return null;
    }

    @Override
    public String getReadableValue() {
        return (String) this.getValue();
    }

    @Override
    public void buildCodeExamples(final List<AbstractCodeExample> codeExamples) {
        final XhtmlCodeExample xhtmlCodeExample = selectedInputMaskType == InputMaskType.PHONE ? new XhtmlCodeExample(false) : new XhtmlCodeExample(false, true);

        xhtmlCodeExample.appendInnerContent("        <b:maskedText id=\"input\"");
        xhtmlCodeExample.appendInnerContent("                      label=\"" + this.getLabel() + "\"");
        xhtmlCodeExample.appendInnerContent("                      hideLabel=\"" + isHideLabel() + "\"");
        xhtmlCodeExample.appendInnerContent("                      value=\"" + this.getValue() + "\"");
        xhtmlCodeExample.appendInnerContent("                      placeholder=\"" + this.getPlaceholder() + "\"");
        xhtmlCodeExample.appendInnerContent("                      type=\"" + this.getType() + "\"");
        xhtmlCodeExample.appendInnerContent("                      pattern=\"" + this.getPattern() + "\"");
        xhtmlCodeExample.appendInnerContent("                      min=\"" + this.getMin() + "\"");
        xhtmlCodeExample.appendInnerContent("                      max=\"" + this.getMax() + "\"");
        xhtmlCodeExample.appendInnerContent("                      styleClass=\"" + this.getStyleClass() + "\"");
        xhtmlCodeExample.appendInnerContent("                      readonly=\"" + this.isReadonly() + "\"");
        xhtmlCodeExample.appendInnerContent("                      required=\"" + this.isRequired() + "\"");
        xhtmlCodeExample.appendInnerContent("                      disabled=\"" + this.isDisabled() + "\"");
        xhtmlCodeExample.appendInnerContent("                      autoFocus=\"" + this.isAutoFocus() + "\"");
        if (selectedInputMaskType == InputMaskType.PHONE || selectedInputMaskType == InputMaskType.ALPHA_NUMERIC || selectedInputMaskType == InputMaskType.DATE) {
            xhtmlCodeExample.appendInnerContent("                      maskedInput=\"" + inputMask + "\"");
        } else if (selectedInputMaskType == InputMaskType.CURRENCY_BY_DATA) {
            xhtmlCodeExample.appendInnerContent("                      p:data-inputmask=\"" + dataInputMask + "\"");
        }
        xhtmlCodeExample.appendInnerContent("                      rendered=\"" + this.isRendered() + "\">");

        this.addAjaxTag(xhtmlCodeExample, "keyup");

        if (this.isValidation()) {
            xhtmlCodeExample.appendInnerContent("            <f:validateLength minimum=\"2\" maximum=\"10\"/>");
        }

        if (StringUtils.isNotEmpty(getTooltip())) {
            xhtmlCodeExample.appendInnerContent("            <b:tooltip>");
            xhtmlCodeExample.appendInnerContent("                " + getTooltip());
            xhtmlCodeExample.appendInnerContent("            </b:tooltip>");
        }

        if (selectedFacetType == FacetType.INPUT_GROUP_ADDON) {
            xhtmlCodeExample.appendInnerContent("            <f:facet name=\"input-group-addon-left\">");
            xhtmlCodeExample.appendInnerContent("                Left input-group-addon");
            xhtmlCodeExample.appendInnerContent("            </f:facet>");
            xhtmlCodeExample.appendInnerContent("            <f:facet name=\"input-group-addon-right\">");
            xhtmlCodeExample.appendInnerContent("                Right input-group-addon");
            xhtmlCodeExample.appendInnerContent("            </f:facet>");
        } else if (selectedFacetType == FacetType.INPUT_GROUP_BTN) {
            xhtmlCodeExample.appendInnerContent("            <f:facet name=\"input-group-btn-left\">");
            xhtmlCodeExample.appendInnerContent("                <button type=\"button\" class=\"btn btn-default\">");
            xhtmlCodeExample.appendInnerContent("                     Go!");
            xhtmlCodeExample.appendInnerContent("                 </button>");
            xhtmlCodeExample.appendInnerContent("            </f:facet>");
            xhtmlCodeExample.appendInnerContent("            <f:facet name=\"input-group-btn-right\">");
            xhtmlCodeExample.appendInnerContent("                <button type=\"button\" class=\"btn btn-default\"");
            xhtmlCodeExample.appendInnerContent("                        dropdown-toggle\"");
            xhtmlCodeExample.appendInnerContent("                        data-toggle=\"dropdown\"");
            xhtmlCodeExample.appendInnerContent("                        aria-expanded=\"false\">");
            xhtmlCodeExample.appendInnerContent("                     Action <span class=\"caret\"></span>");
            xhtmlCodeExample.appendInnerContent("                 </button>");
            xhtmlCodeExample.appendInnerContent("                 <ul class=\"dropdown-menu dropdown-menu-right\" role=\"menu\">");
            xhtmlCodeExample.appendInnerContent("                     <li><a href=\"#\">Action</a></li>");
            xhtmlCodeExample.appendInnerContent("                     <li><a href=\"#\">Another action</a></li>");
            xhtmlCodeExample.appendInnerContent("                     <li><a href=\"#\">Something else here</a></li>");
            xhtmlCodeExample.appendInnerContent("                     <li class=\"divider\"></li>");
            xhtmlCodeExample.appendInnerContent("                     <li><a href=\"#\">Separated link</a></li>");
            xhtmlCodeExample.appendInnerContent("                 </ul>");
            xhtmlCodeExample.appendInnerContent("            </f:facet>");
        }

        xhtmlCodeExample.appendInnerContent("        </b:maskedText>", false);

        this.addOutputExample(xhtmlCodeExample);

        codeExamples.add(xhtmlCodeExample);

        generateDemoCSS(codeExamples);
    }

    public List<SelectItem> getAvailableFacetTypes() {
        final List<SelectItem> items = new ArrayList<>();

        for (final FacetType type : FacetType.values()) {
            items.add(new SelectItem(type, type.label));
        }
        return items;
    }

    public List<SelectItem> getInputMaskTypes() {
        final List<SelectItem> items = new ArrayList<>();

        for (final InputMaskType type : InputMaskType.values()) {
            items.add(new SelectItem(type, type.label));
        }
        return items;
    }


    public String getPlaceholder() {
        return this.placeholder;
    }

    public void setPlaceholder(final String placeholder) {
        this.placeholder = placeholder;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isAutoFocus() {
        return autoFocus;
    }

    public void setAutoFocus(boolean autoFocus) {
        this.autoFocus = autoFocus;
    }

    public String getPattern() {
        return pattern;
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }

    public String getMin() {
        return min;
    }

    public void setMin(String min) {
        this.min = min;
    }

    public String getMax() {
        return max;
    }

    public void setMax(String max) {
        this.max = max;
    }

    public FacetType getSelectedFacetType() {
        return selectedFacetType;
    }

    public void setSelectedFacetType(FacetType selectedFacetType) {
        this.selectedFacetType = selectedFacetType;
    }

    public InputMaskType getSelectedInputMaskType() {
        return selectedInputMaskType;
    }

    public void setSelectedInputMaskType(InputMaskType selectedInputMaskType) {
        this.selectedInputMaskType = selectedInputMaskType;

        switch (selectedInputMaskType) {
            case PHONE:
                inputMask = "(99) 9999[9]-9999";
                dataInputMask = null;
                break;
            case CURRENCY_BY_DATA:
                inputMask = null;
                dataInputMask = "'alias': 'numeric', 'groupSeparator': ',', 'autoGroup': true, 'digits': 2, 'digitsOptional': false, 'prefix': '$ ', 'placeholder': '0'";
                break;
            case CURRENCY_GERMAN:
                inputMask = null;
                dataInputMask = "'alias': 'numeric', 'groupSeparator': '.', 'radixPoint': ',', 'autoGroup': true, 'digits': 2, 'digitsOptional': false, 'suffix': ' €', 'placeholder': '0'";
                break;
            case DATE:
                inputMask = "'mm/yyyy', {yearrange: {minyear: 2000, maxyear: 2999}}";
                dataInputMask = null;
                break;
            case ALPHA_NUMERIC:
                inputMask = "*****/***/*****";
                dataInputMask = null;
                break;
        }
    }

    public String getInputMask() {
        return inputMask;
    }

    public String getDataInputMask() {
        return dataInputMask;
    }
}
