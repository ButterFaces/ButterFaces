package de.larmic.butterfaces.component.renderkit.html_basic;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.convert.Converter;
import javax.faces.model.SelectItem;
import javax.faces.render.FacesRenderer;

import de.larmic.butterfaces.component.base.renderer.HtmlBasicRenderer;
import de.larmic.butterfaces.component.html.HtmlComboBox;
import de.larmic.butterfaces.component.html.HtmlInputComponent;
import de.larmic.butterfaces.component.html.InputComponentFacet;
import de.larmic.butterfaces.component.html.text.HtmlText;
import de.larmic.butterfaces.component.partrenderer.HtmlAttributePartRenderer;
import de.larmic.butterfaces.component.partrenderer.InnerComponentWrapperPartRenderer;
import de.larmic.butterfaces.component.partrenderer.LabelPartRenderer;
import de.larmic.butterfaces.component.partrenderer.OuterComponentWrapperPartRenderer;
import de.larmic.butterfaces.component.partrenderer.ReadonlyPartRenderer;
import de.larmic.butterfaces.component.partrenderer.RenderUtils;
import de.larmic.butterfaces.util.StringUtils;
import de.larmic.butterfaces.component.partrenderer.TooltipPartRenderer;
import de.larmic.butterfaces.component.renderkit.html_basic.mojarra.MenuRenderer;
import de.larmic.butterfaces.util.ReflectionUtil;
import de.larmic.butterfaces.context.StringHtmlEncoder;
import de.larmic.butterfaces.resolver.MustacheResolver;

@FacesRenderer(componentFamily = HtmlText.COMPONENT_FAMILY, rendererType = HtmlComboBox.RENDERER_TYPE)
public class ComboBoxRenderer extends MenuRenderer {

    private List<String> ruleListItemTemplateKeys;

    @Override
    public void encodeBegin(final FacesContext context, final UIComponent component) throws IOException {
        rendererParamsNotNull(context, component);

        if (!shouldEncode(component)) {
            return;
        }

        super.encodeBegin(context, component);

        final HtmlComboBox comboBox = (HtmlComboBox) component;
        final ResponseWriter writer = context.getResponseWriter();

        ruleListItemTemplateKeys = extractRuleListItemTemplateKeys(context, comboBox);

        // Open outer component wrapper div
        new OuterComponentWrapperPartRenderer().renderComponentBegin(component, writer, "butter-component-combobox");

        // Render label if components label attribute is set
        new LabelPartRenderer().renderLabel(component, writer);

        // Open inner component wrapper div
        new InnerComponentWrapperPartRenderer().renderInnerWrapperBegin(component, writer);

        // Render readonly span if components readonly attribute is set
        new ReadonlyPartRenderer().renderReadonly(comboBox, writer);

        if (!comboBox.isReadonly()) {
            writer.startElement(HtmlBasicRenderer.ELEMENT_DIV, component);
            writer.writeAttribute("class", "input-group", "styleClass");

            final UIComponent inputGroupAddonLeftFacet = component.getFacet(InnerComponentWrapperPartRenderer.INPUT_GROUP_ADDON_LEFT);
            final UIComponent inputGroupBtnLeftFacet = component.getFacet(InnerComponentWrapperPartRenderer.INPUT_GROUP_BTN_LEFT);
            if (comboBox.getSupportedFacets().contains(InputComponentFacet.BOOTSTRAP_INPUT_GROUP_LEFT_ADDON) && inputGroupAddonLeftFacet != null) {
                writer.startElement("span", component);
                writer.writeAttribute("class", "input-group-addon", null);
                inputGroupAddonLeftFacet.encodeAll(context);
                writer.endElement("span");
            }
            if (comboBox.getSupportedFacets().contains(InputComponentFacet.BOOTSTRAP_INPUT_GROUP_LEFT_BTN) && inputGroupBtnLeftFacet != null) {
                writer.startElement("span", component);
                writer.writeAttribute("class", "input-group-btn", null);
                inputGroupBtnLeftFacet.encodeAll(context);
                writer.endElement("span");
            }

            writer.startElement("input", component);
            writer.writeAttribute("type", "text", null);
            writer.writeAttribute("autocomplete", "off", null);
            writer.writeAttribute("class", "butter-component-combobox-ghost form-control", "styleClass");
            writer.endElement("input");

            writer.startElement("span", component);
            writer.writeAttribute("class", "input-group-addon cursor-pointer", "styleClass");
            writer.startElement("span", component);
            writer.writeAttribute("class", "glyphicon glyphicon-chevron-down", "styleClass");
            writer.endElement("span");
            writer.endElement("span");

            writer.endElement(HtmlBasicRenderer.ELEMENT_DIV);
        }
    }

    protected List<String> extractRuleListItemTemplateKeys(final FacesContext context,
                                                           final HtmlComboBox comboBox) throws IOException {
        if (!comboBox.isReadonly()) {
            final UIComponent templateFacet = comboBox.getFacet("template");
            if (templateFacet != null) {
                final String encodedFacet = StringHtmlEncoder.encodeComponent(context, templateFacet);
                return MustacheResolver.getMustacheKeys(encodedFacet);
            }
        }

        return new ArrayList<>();
    }

    @Override
    public void encodeEnd(final FacesContext context, final UIComponent component) throws IOException {
        rendererParamsNotNull(context, component);

        if (!shouldEncode(component)) {
            return;
        }

        final HtmlInputComponent htmlComponent = (HtmlInputComponent) component;
        final ResponseWriter writer = context.getResponseWriter();

        if (!htmlComponent.isReadonly()) {
            super.encodeEnd(context, component);
        }

        // Close inner component wrapper div
        new InnerComponentWrapperPartRenderer().renderInnerWrapperEnd(component, writer);

        renderTooltipIfNecessary(context, component);

        if (!htmlComponent.isReadonly()) {
            final UIComponent templateFacet = component.getFacet("template");
            if (templateFacet != null) {
                writer.startElement(HtmlBasicRenderer.ELEMENT_DIV, component);
                writer.writeAttribute("class", "butter-component-combobox-template", "styleClass");
                templateFacet.encodeAll(context);
                writer.endElement(HtmlBasicRenderer.ELEMENT_DIV);
            }

            // Render textarea expandable script call
            RenderUtils.renderJQueryPluginCall(component.getClientId(), "butterCombobox()", writer, component);
        }

        // Open outer component wrapper div
        new OuterComponentWrapperPartRenderer().renderComponentEnd(writer);
    }

    /**
     * Override {@link com.sun.faces.renderkit.html_basic.MenuRenderer#renderOption(FacesContext, UIComponent, UIComponent, Converter, SelectItem, Object, Object[], OptionComponentInfo)}
     */
    protected boolean renderOption(FacesContext context,
                                   UIComponent component,
                                   UIComponent selectComponent,
                                   Converter converter,
                                   SelectItem curItem,
                                   Object currentSelections,
                                   Object[] submittedValues,
                                   OptionComponentInfo optionInfo) throws IOException {

        Object valuesArray;
        Object itemValue;
        String valueString = getFormattedValue(context, component,
                curItem.getValue(), converter);
        boolean containsValue;
        if (submittedValues != null && ((UIInput) component).isValid()) {
            containsValue = containsaValue(submittedValues);
            if (containsValue) {
                valuesArray = submittedValues;
                itemValue = valueString;
            } else {
                valuesArray = currentSelections;
                itemValue = curItem.getValue();
            }
        } else {
            valuesArray = currentSelections;
            itemValue = curItem.getValue();
        }

        boolean isSelected = isSelected(context, component, itemValue, valuesArray, converter);
        if (optionInfo.isHideNoSelection()
                && curItem.isNoSelectionOption()
                && currentSelections != null
                && !isSelected) {
            return false;
        }

        ResponseWriter writer = context.getResponseWriter();
        assert (writer != null);
        writer.writeText("\t", component, null);
        writer.startElement("option", (null != selectComponent) ? selectComponent : component);
        writer.writeAttribute("value", valueString, "value");

        if (isSelected) {
            writer.writeAttribute("selected", true, "selected");
        }

        // ***** CHANGED - START - Render facet field values if needed
        renderItemTemplateOptionDataField(itemValue, writer);
        // ***** CHANGED - END

        // if the component is disabled, "disabled" attribute would be rendered
        // on "select" tag, so don't render "disabled" on every option.
        if ((!optionInfo.isDisabled()) && curItem.isDisabled()) {
            writer.writeAttribute("disabled", true, "disabled");
        }

        String labelClass;
        if (optionInfo.isDisabled() || curItem.isDisabled()) {
            labelClass = optionInfo.getDisabledClass();
        } else {
            labelClass = optionInfo.getEnabledClass();
        }
        if (labelClass != null) {
            writer.writeAttribute("class", labelClass, "labelClass");
        }

        if (curItem.isEscape()) {
            String label = curItem.getLabel();
            if (label == null) {
                label = valueString;
            }
            writer.writeText(label, component, "label");
        } else {
            writer.write(curItem.getLabel());
        }

        writer.endElement("option");
        writer.writeText("\n", component, null);
        return true;
    }

    private void renderItemTemplateOptionDataField(final Object itemValue, final ResponseWriter writer) throws IOException {
        if (itemValue != null) {
            for (String key : ruleListItemTemplateKeys) {
                final String value = new ReflectionUtil().getStringValueFromObject(itemValue, key);

                if (StringUtils.isNotEmpty(value)) {
                    writer.writeAttribute("data-" + key, value, null);
                }

                // TODO render alternative entry if value is null?
            }
        }
    }

    protected void renderTooltipIfNecessary(final FacesContext context, final UIComponent component) throws IOException {
        new TooltipPartRenderer().renderTooltipIfNecessary(context, component);
    }

    @Override
    protected void renderHtmlFeatures(final UIComponent component, final ResponseWriter writer) throws IOException {
        new HtmlAttributePartRenderer().renderHtmlFeatures(component, writer);
    }
}
