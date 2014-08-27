package de.larmic.butterfaces.component.renderkit.html_basic;

import de.larmic.butterfaces.component.html.HtmlInputComponent;
import de.larmic.butterfaces.component.html.HtmlTextArea;
import de.larmic.butterfaces.component.partrenderer.*;

import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import java.io.IOException;

/**
 * Renderer support classes provides methods used by custom component
 * converters. Class is non static because of allowing overriding by other
 * custom components.
 */
public class InputRendererSupport {

    /**
     * Render outer div and label (if needed) and initializes input component.
     * <p/>
     * NOTE: encodeBegin of super implementation should be called first.
     */
    public void encodeBegin(final FacesContext context, final HtmlInputComponent component) throws IOException {
        final ResponseWriter writer = context.getResponseWriter();

        new OuterComponentWrapperPartRenderer().renderComponentBegin(component, writer);
        new LabelPartRenderer().renderLabel(component, writer);
        new InnterComponentWrapperPartRenderer().renderInnerWrapperBegin(component, writer);
        new ReadonlyPartRenderer().renderReadonly(component, writer);
    }

    /**
     * Render tooltip and closes outer div.
     * <p/>
     * NOTE: getEndTextToRender of super implementation should be called first.
     */
    public void encodeEnd(final FacesContext context, final HtmlInputComponent component) throws IOException {
        final ResponseWriter writer = context.getResponseWriter();

        new TooltipPartRenderer().renderTooltip(component, !(component instanceof HtmlTextArea), writer, context);
        new CharacterCounterPartRenderer().renderCharacterCounter(component, writer);
        new InnterComponentWrapperPartRenderer().renderInnerWrapperEnd(writer);
        new OuterComponentWrapperPartRenderer().renderComponentEnd(writer);
    }


}
