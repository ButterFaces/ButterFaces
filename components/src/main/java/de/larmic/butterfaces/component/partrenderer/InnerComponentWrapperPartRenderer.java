package de.larmic.butterfaces.component.partrenderer;

import de.larmic.butterfaces.component.base.renderer.HtmlDeprecatedBasicRenderer;
import de.larmic.butterfaces.component.html.InputComponentFacet;
import de.larmic.butterfaces.component.html.feature.HideLabel;
import de.larmic.butterfaces.component.html.feature.Readonly;
import de.larmic.butterfaces.component.html.feature.SupportedFacets;

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

    public void renderInnerWrapperBegin(final UIComponent component,
                                        final ResponseWriter writer) throws IOException {
        final boolean readonly = component instanceof Readonly && ((Readonly) component).isReadonly();
        this.renderInnerWrapperBegin(component, writer, readonly);
    }

    public void renderInnerWrapperBegin(final UIComponent component,
                                        final ResponseWriter writer,
                                        final boolean readonly) throws IOException {
        if (!readonly) {
            writer.startElement(HtmlDeprecatedBasicRenderer.ELEMENT_DIV, component);
            writer.writeAttribute("class", this.createComponentStyleClass(component), null);
        }
    }

    private String createComponentStyleClass(final UIComponent component) {
        final StringBuilder componentStyleClass = new StringBuilder();
        componentStyleClass.append(this.createDefaultStyleClass(component));

        if (component instanceof SupportedFacets) {
            final SupportedFacets supportedFacets = (SupportedFacets) component;
            if (hasLeftInputGroup(component, supportedFacets)
                    || hasRightInputGroup(component, supportedFacets)
                    || supportedFacets.getSupportedFacets().contains(InputComponentFacet.CALENDAR)) {
                componentStyleClass.append(" input-group");
            }
        }

        return componentStyleClass.toString();
    }

    private boolean hasLeftInputGroup(UIComponent component, SupportedFacets supportedFacets) {
        return (supportedFacets.getSupportedFacets().contains(InputComponentFacet.BOOTSTRAP_INPUT_GROUP_LEFT_ADDON))
                && component.getFacet(INPUT_GROUP_ADDON_LEFT) != null
                || supportedFacets.getSupportedFacets().contains(InputComponentFacet.BOOTSTRAP_INPUT_GROUP_LEFT_BTN)
                && component.getFacet(INPUT_GROUP_BTN_LEFT) != null;
    }

    private boolean hasRightInputGroup(UIComponent component, SupportedFacets supportedFacets) {
        return (supportedFacets.getSupportedFacets().contains(InputComponentFacet.BOOTSTRAP_INPUT_GROUP_RIGHT_ADDON))
                && component.getFacet(INPUT_GROUP_ADDON_RIGHT) != null
                || supportedFacets.getSupportedFacets().contains(InputComponentFacet.BOOTSTRAP_INPUT_GROUP_RIGHT_BTN)
                && component.getFacet(INPUT_GROUP_BTN_RIGHT) != null;
    }

    private String createDefaultStyleClass(UIComponent component) {
        final boolean hideLabel = component instanceof HideLabel && ((HideLabel) component).isHideLabel();
        final StringBuilder defaultStyleClass = new StringBuilder();
        if (hideLabel) {
            defaultStyleClass.append("butter-component-value-hiddenLabel");
        } else {
            defaultStyleClass.append("butter-component-value");
        }
        return defaultStyleClass.toString();
    }

    public void renderInnerWrapperEnd(final UIComponent component,
                                      final ResponseWriter writer) throws IOException {
        final boolean readonly = component instanceof Readonly && ((Readonly) component).isReadonly();
        this.renderInnerWrapperEnd(component, writer, readonly);
    }

    public void renderInnerWrapperEnd(final UIComponent component,
                                      final ResponseWriter writer,
                                      final boolean readonly) throws IOException {
        if (!readonly) {
            writer.endElement(HtmlDeprecatedBasicRenderer.ELEMENT_DIV);

            if (component instanceof UIInput) {
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
                    RenderUtils.renderJavaScriptCall(function, writer, component);
                }
            }
        }
    }
}
