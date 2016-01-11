package de.larmic.butterfaces.component.showcase.tree;

import de.larmic.butterfaces.component.showcase.AbstractInputShowcase;
import de.larmic.butterfaces.component.showcase.comboBox.EpisodeConverter;
import de.larmic.butterfaces.component.showcase.example.AbstractCodeExample;
import de.larmic.butterfaces.component.showcase.example.JavaCodeExample;
import de.larmic.butterfaces.component.showcase.example.XhtmlCodeExample;
import de.larmic.butterfaces.component.showcase.text.FacetType;
import de.larmic.butterfaces.component.showcase.tree.examples.TreeBoxListOfNodesJavaExample;
import de.larmic.butterfaces.component.showcase.tree.examples.TreeBoxListOfStringsJavaExample;
import de.larmic.butterfaces.component.showcase.tree.examples.TreeBoxRootNodeJavaExample;
import de.larmic.butterfaces.component.showcase.tree.examples.stargate.EpisodesCssExample;
import de.larmic.butterfaces.component.showcase.tree.examples.stargate.EpisodesJavaExample;
import de.larmic.butterfaces.model.tree.Node;
import de.larmic.butterfaces.util.StringUtils;

import javax.faces.model.SelectItem;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Named
@ViewScoped
@SuppressWarnings("serial")
public class TreeBoxShowcase extends AbstractInputShowcase implements Serializable {

    private final ShowcaseTreeNode showcaseTreeNode = new ShowcaseTreeNode();
    private FacetType selectedFacetType = FacetType.NONE;
    private TreeBoxExampleType selectedTreeBoxExampleType = TreeBoxExampleType.ROOT_NODE;
    private String placeholder = "Enter text...";
    private boolean autoFocus;
    private String noEntriesText;
    private String spinnerText;

    @Override
    protected Object initValue() {
        return null;
    }

    @Override
    public String getReadableValue() {
        if (this.getValue() instanceof Node) {
            return ((Node) this.getValue()).getTitle();
        } else if (this.getValue() instanceof String) {
            return (String) this.getValue();
        }

        return null;
    }

    @Override
    public void buildCodeExamples(final List<AbstractCodeExample> codeExamples) {
        codeExamples.add(buildXhtmlCodeExample());
        if (selectedTreeBoxExampleType == TreeBoxExampleType.NODES) {
            codeExamples.add(new TreeBoxListOfNodesJavaExample(selectedTreeBoxExampleType, showcaseTreeNode));
        } else if (selectedTreeBoxExampleType == TreeBoxExampleType.ROOT_NODE) {
            codeExamples.add(new TreeBoxRootNodeJavaExample(selectedTreeBoxExampleType, showcaseTreeNode));
        } else if (selectedTreeBoxExampleType == TreeBoxExampleType.TEMPLATE) {
            codeExamples.add(new TreeBoxRootNodeJavaExample(selectedTreeBoxExampleType, showcaseTreeNode));
            codeExamples.add(this.createNodeDataCodeExample());
        } else if (selectedTreeBoxExampleType == TreeBoxExampleType.STRINGS) {
            codeExamples.add(new TreeBoxListOfStringsJavaExample());
        } else if (selectedTreeBoxExampleType == TreeBoxExampleType.OBJECTS) {
            codeExamples.add(new EpisodesJavaExample());
            codeExamples.add(new EpisodesCssExample());
        }

        if (isValidation()) {
            codeExamples.add(buildValidatorCodeExample());
        }

        generateDemoCSS(codeExamples);
    }

    private JavaCodeExample createNodeDataCodeExample() {
        final JavaCodeExample codeExample = new JavaCodeExample("NodeData.java", "nodeData", "treeBox.demo", "NodeData", false);

        codeExample.appendInnerContent("    private final UUID uuid = UUID.randomUUID();\n");
        codeExample.appendInnerContent("    private final Date createDate = new Date();\n");
        codeExample.appendInnerContent("    // GETTER");

        return codeExample;
    }

    private XhtmlCodeExample buildXhtmlCodeExample() {
        final XhtmlCodeExample xhtmlCodeExample = new XhtmlCodeExample(false);

        xhtmlCodeExample.appendInnerContent("        <b:treeBox id=\"input\"");
        xhtmlCodeExample.appendInnerContent("                   label=\"" + this.getLabel() + "\"");
        xhtmlCodeExample.appendInnerContent("                   hideLabel=\"" + isHideLabel() + "\"");
        xhtmlCodeExample.appendInnerContent("                   value=\"" + this.getValue() + "\"");
        xhtmlCodeExample.appendInnerContent("                   values=\"#{myBean.values}\"");
        xhtmlCodeExample.appendInnerContent("                   placeholder=\"" + this.getPlaceholder() + "\"");
        xhtmlCodeExample.appendInnerContent("                   styleClass=\"" + StringUtils.getNotNullValue(this.getStyleClass(), "") + "\"");
        xhtmlCodeExample.appendInnerContent("                   readonly=\"" + this.isReadonly() + "\"");
        xhtmlCodeExample.appendInnerContent("                   disabled=\"" + this.isDisabled() + "\"");
        xhtmlCodeExample.appendInnerContent("                   inputTextProperty=\"" + this.getInputTextProperty() + "\"");
        xhtmlCodeExample.appendInnerContent("                   required=\"" + this.isRequired() + "\"");
        xhtmlCodeExample.appendInnerContent("                   autoFocus=\"" + this.isAutoFocus() + "\"");
        if (StringUtils.isNotEmpty(spinnerText)) {
            xhtmlCodeExample.appendInnerContent("                   spinnerText=\"" + spinnerText + "\"");
        }
        if (StringUtils.isNotEmpty(noEntriesText)) {
            xhtmlCodeExample.appendInnerContent("                   noEntriesText=\"" + noEntriesText + "\"");
        }
        xhtmlCodeExample.appendInnerContent("                   rendered=\"" + this.isRendered() + "\">");

        this.addAjaxTag(xhtmlCodeExample, "change");

        if (selectedTreeBoxExampleType == TreeBoxExampleType.TEMPLATE) {
            xhtmlCodeExample.appendInnerContent("            <!-- use attributes from node or node.data-->");
            xhtmlCodeExample.appendInnerContent("            <!-- javascript mustache syntax is used -->");
            xhtmlCodeExample.appendInnerContent("            <f:facet name=\"template\">");
            xhtmlCodeExample.appendInnerContent("                <strong>{{title}}</strong>");
            xhtmlCodeExample.appendInnerContent("                <div>Created: {{createDate}}</div>");
            xhtmlCodeExample.appendInnerContent("                <div>UUID: {{uuid}}</div>");
            xhtmlCodeExample.appendInnerContent("            </facet>");
        } else if (selectedTreeBoxExampleType == TreeBoxExampleType.OBJECTS) {
            xhtmlCodeExample.appendInnerContent("            <f:facet name=\"template\">");
            xhtmlCodeExample.appendInnerContent("                 <div class=\"stargateEpisodeItem\">");
            xhtmlCodeExample.appendInnerContent("                      <img class=\"stargateEpisodeImg\" src=\"{{image}}\" alt=\"{{title}}\"/>");
            xhtmlCodeExample.appendInnerContent("                      <div class=\"stargateEpisodeDetails\">");
            xhtmlCodeExample.appendInnerContent("                           <h4>{{title}} <small>({{originalAirDate}})</small></h4>");
            xhtmlCodeExample.appendInnerContent("                           <div>");
            xhtmlCodeExample.appendInnerContent("                                <label>Episode:</label>");
            xhtmlCodeExample.appendInnerContent("                                <span>");
            xhtmlCodeExample.appendInnerContent("                                     No. {{numberInSeries}} of Stargate - Kommando SG-1, ");
            xhtmlCodeExample.appendInnerContent("                                     Season 1</span>");
            xhtmlCodeExample.appendInnerContent("                                </span>");
            xhtmlCodeExample.appendInnerContent("                           </div>");
            xhtmlCodeExample.appendInnerContent("                           <div>");
            xhtmlCodeExample.appendInnerContent("                                <label>written by:</label>");
            xhtmlCodeExample.appendInnerContent("                                <span>{{writtenBy}}</span>");
            xhtmlCodeExample.appendInnerContent("                           </div>");
            xhtmlCodeExample.appendInnerContent("                      </div>");
            xhtmlCodeExample.appendInnerContent("                 </div>");
            xhtmlCodeExample.appendInnerContent("            </f:facet>");
        }

        if (this.isValidation()) {
            xhtmlCodeExample.appendInnerContent("            <f:validator validatorId=\"treeBoxValidator\" />");
        }

        if (StringUtils.isNotEmpty(getTooltip())) {
            xhtmlCodeExample.appendInnerContent("            <b:tooltip>");
            xhtmlCodeExample.appendInnerContent("                " + getTooltip());
            xhtmlCodeExample.appendInnerContent("            </b:tooltip>");
        }

        xhtmlCodeExample.appendInnerContent("        </b:treeBox>\n", false);

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

    public List<SelectItem> getAvailableFacetTypes() {
        final List<SelectItem> items = new ArrayList<>();

        for (final FacetType type : FacetType.values()) {
            items.add(new SelectItem(type, type.label));
        }
        return items;
    }

    public List<SelectItem> getTreeBoxExampleTypes() {
        final List<SelectItem> items = new ArrayList<>();

        for (final TreeBoxExampleType type : TreeBoxExampleType.values()) {
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

    public boolean isAutoFocus() {
        return autoFocus;
    }

    public void setAutoFocus(boolean autoFocus) {
        this.autoFocus = autoFocus;
    }

    public Object getValues() {
        switch (selectedTreeBoxExampleType) {
            case NODES:
                return showcaseTreeNode.getTree().getSubNodes();
            case ROOT_NODE:
            case TEMPLATE:
                return showcaseTreeNode.getTree();
            case OBJECTS:
                return EpisodeConverter.EPISODES;
            default:
                return Arrays.asList("Inbox", "Drafts", "Sent", "Tagged", "Folders", "Trash");
        }
    }

    public FacetType getSelectedFacetType() {
        return selectedFacetType;
    }

    public void setSelectedFacetType(FacetType selectedFacetType) {
        this.selectedFacetType = selectedFacetType;
    }

    public String getNoEntriesText() {
        return noEntriesText;
    }

    public void setNoEntriesText(String noEntriesText) {
        this.noEntriesText = noEntriesText;
    }

    public String getSpinnerText() {
        return spinnerText;
    }

    public void setSpinnerText(String spinnerText) {
        this.spinnerText = spinnerText;
    }

    public TreeBoxExampleType getSelectedTreeBoxExampleType() {
        return selectedTreeBoxExampleType;
    }

    public void setSelectedTreeBoxExampleType(TreeBoxExampleType selectedTreeBoxExampleType) {
        this.selectedTreeBoxExampleType = selectedTreeBoxExampleType;
    }
}
