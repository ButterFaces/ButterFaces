package de.larmic.butterfaces.component.partrenderer;

import de.larmic.butterfaces.component.html.HtmlInputComponent;
import de.larmic.butterfaces.component.html.InputComponentFacet;

import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.ResponseWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by larmic on 27.08.14.
 */
public class InnerComponentWrapperPartRenderer {

    public static final String INPUT_GROUP_ADDON_LEFT = "input-group-addon-left";
    public static final String INPUT_GROUP_ADDON_RIGHT = "input-group-addon-right";
    public static final String INPUT_GROUP_BTN_LEFT = "input-group-btn-left";
    public static final String INPUT_GROUP_BTN_RIGHT = "input-group-btn-right";

    public void renderInnerWrapperBegin(final HtmlInputComponent component,
                                        final ResponseWriter writer) throws IOException {
        this.renderInnerWrapperBegin(component, writer, component.isReadonly());
    }

    public void renderInnerWrapperBegin(final HtmlInputComponent component,
                                        final ResponseWriter writer,
                                        final boolean readonly) throws IOException {
        final UIInput uiComponent = (UIInput) component;

        if (!readonly) {
            writer.startElement("div", uiComponent);
            writer.writeAttribute("class", this.createComponentStyleClass(component), null);
        }
    }

    private String createComponentStyleClass(final HtmlInputComponent component) {
        final StringBuilder componentStyleClass = new StringBuilder();
        componentStyleClass.append(this.createDefaultStyleClass(component));

        if (component.getSupportedFacets().contains(InputComponentFacet.BOOTSTRAP_INPUT_GROUP_ADDON)
                && (component.getFacet(INPUT_GROUP_ADDON_LEFT) != null || component.getFacet(INPUT_GROUP_ADDON_RIGHT) != null)
                || component.getSupportedFacets().contains(InputComponentFacet.BOOTSTRAP_INPUT_GROUP_BTN)
                && (component.getFacet(INPUT_GROUP_BTN_LEFT) != null || component.getFacet(INPUT_GROUP_BTN_RIGHT) != null)
                || component.getSupportedFacets().contains(InputComponentFacet.CALENDAR)) {
            componentStyleClass.append(" input-group");
        }

        return componentStyleClass.toString();
    }

    private String createDefaultStyleClass(HtmlInputComponent component) {
        final StringBuilder defaultStyleClass = new StringBuilder();
        if (component.isHideLabel()) {
            defaultStyleClass.append("butter-component-value-hiddenLabel");
        }else {
           defaultStyleClass.append("butter-component-value");
        }
        return defaultStyleClass.toString();
    }

    public void renderInnerWrapperEnd(final HtmlInputComponent component,
                                      final ResponseWriter writer) throws IOException {
        this.renderInnerWrapperEnd((UIComponent) component, writer, component.isReadonly());
    }

    public void renderInnerWrapperEnd(final UIComponent component,
                                      final ResponseWriter writer,
                                      final boolean readonly) throws IOException {
        if (!readonly) {
            final UIInput uiComponent = (UIInput) component;

            writer.endElement("div");

            final Set<String> eventNames = ((UIInput) component).getClientBehaviors().keySet();
            final Iterator<String> eventNamesIterator = eventNames.iterator();

            if (eventNamesIterator.hasNext()) {
                final StringBuilder sb = new StringBuilder("[");

                while (eventNamesIterator.hasNext()) {
                    sb.append("'");
                    sb.append(eventNamesIterator.next());
                    sb.append("'");

                    if (eventNamesIterator.hasNext()) {
                        sb.append(", ");
                    }
                }

                sb.append("]");

                final String function = "butter.fix.updateMojarraScriptSourceId('" + component.getClientId() + "', " + sb.toString() + ");";
                RenderUtils.renderJavaScriptCall(function, writer, uiComponent);
            }
        }
    }
}
