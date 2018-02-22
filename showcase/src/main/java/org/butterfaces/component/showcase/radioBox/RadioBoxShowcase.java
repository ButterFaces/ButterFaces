package org.butterfaces.component.showcase.radioBox;

import org.butterfaces.component.showcase.AbstractInputShowcase;
import org.butterfaces.component.showcase.example.AbstractCodeExample;
import org.butterfaces.component.showcase.example.XhtmlCodeExample;
import org.butterfaces.component.showcase.radioBox.examples.*;
import org.butterfaces.component.showcase.tree.Episode;
import org.butterfaces.component.showcase.tree.Episodes;
import org.butterfaces.component.showcase.tree.examples.stargate.TreeBoxEpisodesJavaExample;
import org.butterfaces.component.showcase.tree.examples.stargate.TreeBoxListOfEpisodesJavaExample;
import org.butterfaces.component.showcase.type.RadioBoxExampleType;
import org.butterfaces.component.showcase.type.RadioBoxLayoutType;
import org.butterfaces.component.showcase.type.StyleClass;
import org.butterfaces.model.tree.EnumTreeBoxWrapper;
import org.butterfaces.util.StringUtils;
import org.butterfaces.component.showcase.radioBox.examples.*;
import org.butterfaces.component.showcase.type.StyleClass;
import org.butterfaces.util.StringUtils;

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
public class RadioBoxShowcase extends AbstractInputShowcase implements Serializable {

    private RadioBoxExampleType exampleType = RadioBoxExampleType.STRING;
    private RadioBoxLayoutType radioBoxLayoutType = RadioBoxLayoutType.LINE_DIRECTION;

    private final List<Foo> foos = new ArrayList<>();
    private final List<String> strings = new ArrayList<>();

    public RadioBoxShowcase() {
        this.initFoos();
        this.initStrings();
    }

    @Override
    protected Object initValue() {
        return null;
    }

    @Override
    public Object getValue() {
        if (super.getValue() != null) {
            return super.getValue();
        }

        return "(item is null)";
    }

    @Override
    public void buildCodeExamples(final List<AbstractCodeExample> codeExamples) {
        final XhtmlCodeExample xhtmlCodeExample = new XhtmlCodeExample(false);

        xhtmlCodeExample.appendInnerContent("        <b:radioBox id=\"input\"");
        xhtmlCodeExample.appendInnerContent("                    label=\"" + this.getLabel() + "\"");
        xhtmlCodeExample.appendInnerContent("                    hideLabel=\"" + isHideLabel() + "\"");
        xhtmlCodeExample.appendInnerContent("                    value=\"#{myBean.selectedValue}\"");
        xhtmlCodeExample.appendInnerContent("                    values=\"#{myBean.values}\"");
        if (this.getStyleClass() == StyleClass.BIG_LABEL) {
            xhtmlCodeExample.appendInnerContent("                    styleClass=\"" + this.getSelectedStyleClass() + "\"");
        }
        xhtmlCodeExample.appendInnerContent("                    readonly=\"" + this.isReadonly() + "\"");
        xhtmlCodeExample.appendInnerContent("                    required=\"" + this.isRequired() + "\"");
        xhtmlCodeExample.appendInnerContent("                    disabled=\"" + this.isDisabled() + "\"");
        //xhtmlCodeExample.appendInnerContent("                    layout=\"" + radioBoxLayoutType.label + "\"");
        xhtmlCodeExample.appendInnerContent("                    rendered=\"" + this.isRendered() + "\">");

        this.addAjaxTag(xhtmlCodeExample, "change");

        if (RadioBoxExampleType.TEMPLATE.equals(this.exampleType)) {
            xhtmlCodeExample.appendInnerContent("            <f:facet name=\"template\">");
            xhtmlCodeExample.appendInnerContent("               <div class=\"radio-box-stargate-template\">");
            xhtmlCodeExample.appendInnerContent("                  <h4>{{title}}");
            xhtmlCodeExample.appendInnerContent("                     <small>({{originalAirDate}})</small>");
            xhtmlCodeExample.appendInnerContent("                  </h4>");
            xhtmlCodeExample.appendInnerContent("                  <div>");
            xhtmlCodeExample.appendInnerContent("                     <label>Episode:</label>");
            xhtmlCodeExample.appendInnerContent("                     <span>No. {{numberInSeries}} of Stargate - Kommando SG-1, Season 1</span>");
            xhtmlCodeExample.appendInnerContent("                  </div>");
            xhtmlCodeExample.appendInnerContent("                  <div>");
            xhtmlCodeExample.appendInnerContent("                     <label>written by:</label>");
            xhtmlCodeExample.appendInnerContent("                     <span>{{writtenBy}}</span>");
            xhtmlCodeExample.appendInnerContent("                  </div>");
            xhtmlCodeExample.appendInnerContent("               </div>");
            xhtmlCodeExample.appendInnerContent("            </f:facet>");
        }

        if (StringUtils.isNotEmpty(getTooltip())) {
            xhtmlCodeExample.appendInnerContent("            <b:tooltip>");
            xhtmlCodeExample.appendInnerContent("                " + getTooltip());
            xhtmlCodeExample.appendInnerContent("            </b:tooltip>");
        }

        xhtmlCodeExample.appendInnerContent("        </b:radioBox>", false);

        this.addOutputExample(xhtmlCodeExample);

        codeExamples.add(xhtmlCodeExample);

        if (RadioBoxExampleType.STRING.equals(this.exampleType)) {
            codeExamples.add(new RadioBoxListOfStringsMyBeanExample());
        } else if (RadioBoxExampleType.ENUM.equals(this.exampleType)) {
            codeExamples.add(new RadioBoxListOfEnumsMyBeanExample());
            codeExamples.add(new RadioBoxFooTypeExample());
        } else if (RadioBoxExampleType.OBJECT.equals(this.exampleType)) {
            codeExamples.add(new RadioBoxListOfObjectsMyBeanExample());
            codeExamples.add(new RadioBoxFooExample());
        } else if (RadioBoxExampleType.TEMPLATE.equals(this.exampleType)) {
            codeExamples.add(new TreeBoxListOfEpisodesJavaExample("radiobox.demo"));
            codeExamples.add(new TreeBoxEpisodesJavaExample("radiobox.demo"));
            codeExamples.add(new RadioBoxListOfTemplatesCssExample());
        }

        generateDemoCSS(codeExamples);
    }


    @Override
    public String getReadableValue() {
        if (super.getValue() != null) {
            if (super.getValue() instanceof Foo) {
                return ((Foo) super.getValue()).getValue();
            } else if (super.getValue() instanceof FooType) {
                return ((FooType) super.getValue()).getLabel();
            } else if (super.getValue() instanceof SelectItem) {
                return ((SelectItem) super.getValue()).getLabel();
            } else if (super.getValue() instanceof Episode) {
                return ((Episode) super.getValue()).getTitle();
            }

            return (String) super.getValue();
        }

        return "(item is null)";
    }

    public List getValues() {
        switch (this.exampleType) {
            case OBJECT:
                return this.foos;
            case ENUM:
                return Arrays.asList(FooType.values());
            case TEMPLATE:
                return Episodes.EPISODES.subList(0, 3);
            default:
                return this.strings;
        }
    }

    public List<EnumTreeBoxWrapper> getExampleTypes() {
        final List<EnumTreeBoxWrapper> items = new ArrayList<>();

        for (final RadioBoxExampleType type : RadioBoxExampleType.values()) {
            items.add(new EnumTreeBoxWrapper(type, type.label));
        }
        return items;
    }

    public List<EnumTreeBoxWrapper> getRadioLayoutTypes() {
        final List<EnumTreeBoxWrapper> items = new ArrayList<>();

        for (final RadioBoxLayoutType type : RadioBoxLayoutType.values()) {
            items.add(new EnumTreeBoxWrapper(type, type.label));
        }
        return items;
    }

    public RadioBoxExampleType getExampleType() {
        return this.exampleType;
    }

    public void setExampleType(final RadioBoxExampleType exampleType) {
        this.exampleType = exampleType;
    }

    public RadioBoxLayoutType getRadioBoxLayoutType() {
        return radioBoxLayoutType;
    }

    public void setRadioBoxLayoutType(RadioBoxLayoutType radioBoxLayoutType) {
        this.radioBoxLayoutType = radioBoxLayoutType;
    }

    private void initFoos() {
        this.foos.add(new Foo("fooKey1", "fooValue1"));
        this.foos.add(new Foo("fooKey2", "fooValue2"));
        this.foos.add(new Foo("fooKey3", "fooValue3"));
    }

    private void initStrings() {
        this.strings.add("Year 2000");
        this.strings.add("Year 2010");
        this.strings.add("Year 2020");
    }
}
