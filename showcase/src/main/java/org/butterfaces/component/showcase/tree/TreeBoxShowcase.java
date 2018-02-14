package org.butterfaces.component.showcase.tree;

import org.butterfaces.component.showcase.AbstractInputShowcase;
import org.butterfaces.component.showcase.example.AbstractCodeExample;
import org.butterfaces.component.showcase.example.JavaCodeExample;
import org.butterfaces.component.showcase.example.XhtmlCodeExample;
import org.butterfaces.component.showcase.text.FacetType;
import org.butterfaces.component.showcase.tree.examples.*;
import org.butterfaces.component.showcase.tree.examples.stargate.TreeBoxEpisodesCssExample;
import org.butterfaces.component.showcase.tree.examples.stargate.TreeBoxEpisodesJavaExample;
import org.butterfaces.component.showcase.tree.examples.stargate.TreeBoxImageJavaExample;
import org.butterfaces.component.showcase.tree.examples.stargate.TreeBoxListOfEpisodesJavaExample;
import org.butterfaces.component.showcase.type.StyleClass;
import org.butterfaces.model.tree.EnumTreeBoxWrapper;
import org.butterfaces.model.tree.Node;
import org.butterfaces.util.StringUtils;

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
    private String inputTextProperty;
    private boolean autoFocus;
    private Boolean showClearButton = Boolean.TRUE;
    private String noEntriesText;
    private String spinnerText;

    @Override
    protected Object initValue() {
        return null;
    }

    @Override
    public String getReadableValue() {
        if (this.getValue() instanceof TreeBoxExampleEnum) {
            return getEnumTranslation((TreeBoxExampleEnum) this.getValue());
        } else if (this.getValue() instanceof Node) {
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
            codeExamples.add(new TreeBoxListOfNodesJavaExample(showcaseTreeNode));
        } else if (selectedTreeBoxExampleType == TreeBoxExampleType.ROOT_NODE) {
            codeExamples.add(new TreeBoxRootNodeJavaExample(selectedTreeBoxExampleType, showcaseTreeNode));
        } else if (selectedTreeBoxExampleType == TreeBoxExampleType.TEMPLATE) {
            codeExamples.add(new TreeBoxListOfEpisodesJavaExample("treeBox.demo"));
            codeExamples.add(new TreeBoxEpisodesJavaExample("treeBox.demo", true));
            codeExamples.add(new TreeBoxImageJavaExample("treeBox.demo"));
            codeExamples.add(new TreeBoxEpisodesCssExample());
        } else if (selectedTreeBoxExampleType == TreeBoxExampleType.STRINGS) {
            codeExamples.add(new TreeBoxListOfStringsJavaExample());
        } else if (selectedTreeBoxExampleType == TreeBoxExampleType.OBJECTS) {
            codeExamples.add(new TreeBoxListOfEpisodesJavaExample("treeBox.demo"));
            codeExamples.add(new TreeBoxEpisodesJavaExample("treeBox.demo", true));
            codeExamples.add(new TreeBoxImageJavaExample("treeBox.demo"));
            codeExamples.add(new TreeBoxEpisodesCssExample());
        } else if (selectedTreeBoxExampleType == TreeBoxExampleType.ENUMS) {
            codeExamples.add(new TreeBoxListOfEnumsJavaExample());
            codeExamples.add(new TreeBoxExampleEnumJavaExample());
        }

        if (isValidation()) {
            codeExamples.add(buildValidatorCodeExample());
        }

        generateDemoCSS(codeExamples);
        codeExamples.add(new TreeBoxWebXmlExample());
    }

    private XhtmlCodeExample buildXhtmlCodeExample() {
        final XhtmlCodeExample xhtmlCodeExample = new XhtmlCodeExample(false);

        xhtmlCodeExample.appendInnerContent("        <b:treeBox id=\"input\"");
        xhtmlCodeExample.appendInnerContent("                   label=\"" + this.getLabel() + "\"");
        xhtmlCodeExample.appendInnerContent("                   hideLabel=\"" + isHideLabel() + "\"");
        xhtmlCodeExample.appendInnerContent("                   value=\"#{myBean.selectedValue}\"");
        xhtmlCodeExample.appendInnerContent("                   values=\"#{myBean.values}\"");
        xhtmlCodeExample.appendInnerContent("                   placeholder=\"" + this.getPlaceholder() + "\"");
        if (this.getStyleClass() == StyleClass.BIG_LABEL) {
            xhtmlCodeExample.appendInnerContent("                   styleClass=\"" + this.getSelectedStyleClass() + "\"");
        }
        xhtmlCodeExample.appendInnerContent("                   readonly=\"" + this.isReadonly() + "\"");
        xhtmlCodeExample.appendInnerContent("                   disabled=\"" + this.isDisabled() + "\"");
        xhtmlCodeExample.appendInnerContent("                   inputTextProperty=\"" + this.getInputTextProperty() + "\"");
        xhtmlCodeExample.appendInnerContent("                   required=\"" + this.isRequired() + "\"");
        xhtmlCodeExample.appendInnerContent("                   autoFocus=\"" + this.isAutoFocus() + "\"");
        xhtmlCodeExample.appendInnerContent("                   showClearButton=\"" + this.getShowClearButton() + "\"");
        if (StringUtils.isNotEmpty(spinnerText)) {
            xhtmlCodeExample.appendInnerContent("                   spinnerText=\"" + spinnerText + "\"");
        }
        if (StringUtils.isNotEmpty(noEntriesText)) {
            xhtmlCodeExample.appendInnerContent("                   noEntriesText=\"" + noEntriesText + "\"");
        }
        xhtmlCodeExample.appendInnerContent("                   rendered=\"" + this.isRendered() + "\">");

        this.addAjaxTag(xhtmlCodeExample, "change");

        if (selectedTreeBoxExampleType == TreeBoxExampleType.TEMPLATE) {
            xhtmlCodeExample.appendInnerContent("            <f:facet name=\"selectedEntryTemplate\">");
            xhtmlCodeExample.appendInnerContent("                 <div class=\"stargateEpisodeItem\">");
            xhtmlCodeExample.appendInnerContent("                      <img class=\"stargateEpisodeImg small\"");
            xhtmlCodeExample.appendInnerContent("                           src=\"{{image.url}}\" alt=\"{{title}}\"/>");
            xhtmlCodeExample.appendInnerContent("                      <div class=\"stargateEpisodeDetails\">");
            xhtmlCodeExample.appendInnerContent("                           <h4>{{title}} <small>({{originalAirDate}})</small></h4>");
            xhtmlCodeExample.appendInnerContent("                      </div>");
            xhtmlCodeExample.appendInnerContent("                 </div>");
            xhtmlCodeExample.appendInnerContent("            </f:facet>");
            xhtmlCodeExample.appendInnerContent("            <f:facet name=\"template\">");
            xhtmlCodeExample.appendInnerContent("                 <div class=\"stargateEpisodeItem\">");
            xhtmlCodeExample.appendInnerContent("                      <img class=\"stargateEpisodeImg\"");
            xhtmlCodeExample.appendInnerContent("                           src=\"{{image.url}}\" alt=\"{{title}}\"/>");
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

        xhtmlCodeExample.appendInnerContent("        </b:treeBox>", false);

        this.addOutputExample(xhtmlCodeExample);

        return xhtmlCodeExample;
    }

    private AbstractCodeExample buildValidatorCodeExample() {
        final JavaCodeExample codeExample = new JavaCodeExample("TreeBoxValidator.java", "validator", "treeBox", "TreeBoxValidator", false, "@FacesValidator");

        codeExample.addInterfaces("Validator");

        codeExample.addImport("org.butterfaces.model.tree.Node");

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

    public List<EnumTreeBoxWrapper> getAvailableFacetTypes() {
        final List<EnumTreeBoxWrapper> items = new ArrayList<>();

        for (final FacetType type : FacetType.values()) {
            items.add(new EnumTreeBoxWrapper(type, type.label));
        }
        return items;
    }

    public List<EnumTreeBoxWrapper> getTreeBoxExampleTypes() {
        final List<EnumTreeBoxWrapper> items = new ArrayList<>();

        for (final TreeBoxExampleType type : TreeBoxExampleType.values()) {
            items.add(new EnumTreeBoxWrapper(type, type.label));
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
                return showcaseTreeNode.getTree();
            case ENUMS:
                return this.getEnumValues();
            case TEMPLATE:
            case OBJECTS:
                return Episodes.EPISODES;
            default:
                return Arrays.asList("Inbox", "Drafts", "Sent", "Tagged", "Folders", "Trash");
        }
    }

    private String getEnumTranslation(final TreeBoxExampleEnum treeBoxExampleEnum) {
        for (EnumTreeBoxWrapper enumTreeBoxWrapper : getEnumValues()) {
            if (enumTreeBoxWrapper.getEnumValue().equals(treeBoxExampleEnum)) {
                return enumTreeBoxWrapper.getTranslation();
            }
        }

        return null;
    }

    private List<EnumTreeBoxWrapper> getEnumValues() {
        final List<EnumTreeBoxWrapper> wrappedEnums = new ArrayList<>();

        wrappedEnums.add(new EnumTreeBoxWrapper(TreeBoxExampleEnum.MAIL, "E-Mail"));
        wrappedEnums.add(new EnumTreeBoxWrapper(TreeBoxExampleEnum.PDF, "PDF"));
        wrappedEnums.add(new EnumTreeBoxWrapper(TreeBoxExampleEnum.TXT, "plain text"));

        return wrappedEnums;
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

    public String getInputTextProperty() {
        return inputTextProperty;
    }

    public void setInputTextProperty(String inputTextProperty) {
        this.inputTextProperty = inputTextProperty;
    }

    public Boolean getShowClearButton() {
        return showClearButton;
    }

    public void setShowClearButton(Boolean showClearButton) {
        this.showClearButton = showClearButton;
    }
}
