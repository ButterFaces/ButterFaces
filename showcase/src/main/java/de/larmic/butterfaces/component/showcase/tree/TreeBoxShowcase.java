package de.larmic.butterfaces.component.showcase.tree;

import de.larmic.butterfaces.component.partrenderer.StringUtils;
import de.larmic.butterfaces.component.showcase.AbstractInputShowcase;
import de.larmic.butterfaces.component.showcase.example.AbstractCodeExample;
import de.larmic.butterfaces.component.showcase.example.JavaCodeExample;
import de.larmic.butterfaces.component.showcase.example.XhtmlCodeExample;
import de.larmic.butterfaces.model.tree.Node;

import javax.faces.view.ViewScoped;
import javax.inject.Named;
import java.io.Serializable;
import java.util.List;

@Named
@ViewScoped
@SuppressWarnings("serial")
public class TreeBoxShowcase extends AbstractInputShowcase implements Serializable {

    private final ShowcaseTreeNode showcaseTreeNode = new ShowcaseTreeNode();
    private String placeholder = "Enter text...";
    private boolean autoFocus;
    private boolean hideRootNode;

    @Override
    protected Object initValue() {
        return null;
    }

    @Override
    public String getReadableValue() {
        return this.getValue() != null ? ((Node) this.getValue()).getTitle() : null;
    }

    @Override
    public void buildCodeExamples(final List<AbstractCodeExample> codeExamples) {
        codeExamples.add(buildXhtmlCodeExample());

        if (isValidation()) {
            codeExamples.add(buildValidatorCodeExample());
        }

        generateDemoCSS(codeExamples);
    }

    private XhtmlCodeExample buildXhtmlCodeExample() {
        final XhtmlCodeExample xhtmlCodeExample = new XhtmlCodeExample(false);

        xhtmlCodeExample.appendInnerContent("        <b:treeBox id=\"input\"");
        xhtmlCodeExample.appendInnerContent("                   label=\"" + this.getLabel() + "\"");
        xhtmlCodeExample.appendInnerContent("                   hideLabel=\"" + isHideLabel() + "\"");
        xhtmlCodeExample.appendInnerContent("                   value=\"" + this.getValue() + "\"");
        xhtmlCodeExample.appendInnerContent("                   placeholder=\"" + this.getPlaceholder() + "\"");
        xhtmlCodeExample.appendInnerContent("                   styleClass=\"" + StringUtils.getNotNullValue(this.getStyleClass(), "") + "\"");
        xhtmlCodeExample.appendInnerContent("                   readonly=\"" + this.isReadonly() + "\"");
        xhtmlCodeExample.appendInnerContent("                   disabled=\"" + this.isDisabled() + "\"");
        xhtmlCodeExample.appendInnerContent("                   required=\"" + this.isRequired() + "\"");
        xhtmlCodeExample.appendInnerContent("                   autoFocus=\"" + this.isAutoFocus() + "\"");
        xhtmlCodeExample.appendInnerContent("                   rendered=\"" + this.isRendered() + "\">");

        this.addAjaxTag(xhtmlCodeExample, "change");

        if (this.isValidation()) {
            xhtmlCodeExample.appendInnerContent("            <f:validator validatorId=\"treeBoxValidator\" />");
        }

        if (StringUtils.isNotEmpty(getTooltip())) {
            xhtmlCodeExample.appendInnerContent("            <b:tooltip>");
            xhtmlCodeExample.appendInnerContent("                " + getTooltip());
            xhtmlCodeExample.appendInnerContent("            </b:tooltip>");
        }

        xhtmlCodeExample.appendInnerContent("        </b:tags>", false);

        this.addOutputExample(xhtmlCodeExample);

        return xhtmlCodeExample;
    }

    private AbstractCodeExample buildValidatorCodeExample() {
        final JavaCodeExample codeExample = new JavaCodeExample("TreeBoxValidator.java", "validator", "treeBox", "TreeBoxValidator", false, "@FacesValidator");

        codeExample.addInterfaces("Validator");

        codeExample.addImport("de.larmic.butterfaces.model.tree.Node");

        codeExample.addImport("javax.faces.application.FacesMessage");
        codeExample.addImport("javax.faces.component.UIComponent");
        codeExample.addImport("javax.faces.context.FacesContext");
        codeExample.addImport("javax.faces.validator.FacesValidator");
        codeExample.addImport("javax.faces.validator.Validator");
        codeExample.addImport("javax.faces.validator.ValidatorException");

        codeExample.appendInnerContent("   private static final String ERROR_MESSAGE = \"Selecting root node is not allowed\";\n");
        codeExample.appendInnerContent("   @Override");
        codeExample.appendInnerContent("   public void validate(FacesContext context,");
        codeExample.appendInnerContent("                        UIComponent component,");
        codeExample.appendInnerContent("                        Object value) throws ValidatorException {");
        codeExample.appendInnerContent("      if (value instanceof Node");
        codeExample.appendInnerContent("            && \"rootNode\".equals(((Node) value).getTitle())) {");
        codeExample.appendInnerContent("         final FacesMessage message = new FacesMessage(ERROR_MESSAGE);");
        codeExample.appendInnerContent("         throw new ValidatorException(message);");
        codeExample.appendInnerContent("      }");
        codeExample.appendInnerContent("   }");

        return codeExample;
    }

    public String getPlaceholder() {
        return this.placeholder;
    }

    public void setPlaceholder(final String placeholder) {
        this.placeholder = placeholder;
    }

    public boolean isAutoFocus() {
        return autoFocus;
    }

    public void setAutoFocus(boolean autoFocus) {
        this.autoFocus = autoFocus;
    }

    public ShowcaseTreeNode getShowcaseTreeNode() {
        return showcaseTreeNode;
    }

    public void setHideRootNode(boolean hideRootNode) {
        this.hideRootNode = hideRootNode;
    }

    public boolean isHideRootNode() {
        return hideRootNode;
    }
}
