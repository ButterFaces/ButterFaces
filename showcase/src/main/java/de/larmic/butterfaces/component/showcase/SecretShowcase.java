package de.larmic.butterfaces.component.showcase;

import de.larmic.butterfaces.component.partrenderer.StringUtils;
import de.larmic.butterfaces.component.showcase.example.AbstractCodeExample;
import de.larmic.butterfaces.component.showcase.example.CssCodeExample;
import de.larmic.butterfaces.component.showcase.example.XhtmlCodeExample;

import javax.faces.view.ViewScoped;
import javax.inject.Named;
import java.io.Serializable;
import java.util.List;

@Named
@ViewScoped
@SuppressWarnings("serial")
public class SecretShowcase extends AbstractInputShowcase implements Serializable {

	private String placeholder = DEFAULT_TEXT_PLACEHOLDER;

    private boolean autoFocus;

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
        final XhtmlCodeExample xhtmlCodeExample = new XhtmlCodeExample(false);

        xhtmlCodeExample.appendInnerContent("        <b:secret id=\"input\"");
        xhtmlCodeExample.appendInnerContent("                  label=\"" + this.getLabel() + "\"");
        xhtmlCodeExample.appendInnerContent("                  value=\"" + this.getValue() + "\"");
        xhtmlCodeExample.appendInnerContent("                  tooltip=\"" + this.getTooltip() + "\"");
        xhtmlCodeExample.appendInnerContent("                  placeholder=\"" + this.getPlaceholder() + "\"");
        xhtmlCodeExample.appendInnerContent("                  styleClass=\"" + this.getStyleClass() + "\"");
        xhtmlCodeExample.appendInnerContent("                  inputStyleClass=\"" + this.getInputStyleClass() + "\"");
        xhtmlCodeExample.appendInnerContent("                  labelStyleClass=\"" + this.getLabelStyleClass() + "\"");
        xhtmlCodeExample.appendInnerContent("                  readonly=\"" + this.isReadonly() + "\"");
        xhtmlCodeExample.appendInnerContent("                  required=\"" + this.isRequired() + "\"");
        xhtmlCodeExample.appendInnerContent("                  autoFocus=\"" + this.isAutoFocus() + "\"");
        xhtmlCodeExample.appendInnerContent("                  rendered=\"" + this.isRendered() + "\">");

        this.addAjaxTag(xhtmlCodeExample, "keyup");

        if (this.isValidation()) {
            xhtmlCodeExample.appendInnerContent("            <f:validateLength minimum=\"2\" maximum=\"10\"/>");
        }

        /*
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
        }*/

        xhtmlCodeExample.appendInnerContent("        </b:secret>", false);

        this.addOutputExample(xhtmlCodeExample);

        codeExamples.add(xhtmlCodeExample);

        if (StringUtils.isNotEmpty(this.getStyleClass())) {
            final CssCodeExample cssCodeExample = new CssCodeExample();
            cssCodeExample.addCss(".some-demo-class", "background-color: red;");
            codeExamples.add(cssCodeExample);
        }
    }

    public boolean isAutoFocus() {
        return autoFocus;
    }

    public void setAutoFocus(boolean autoFocus) {
        this.autoFocus = autoFocus;
    }

    protected String getEmptyDistanceString() {
        return "                  ";
    }

	public String getPlaceholder() {
		return this.placeholder;
	}

	public void setPlaceholder(final String placeholder) {
		this.placeholder = placeholder;
	}

}
