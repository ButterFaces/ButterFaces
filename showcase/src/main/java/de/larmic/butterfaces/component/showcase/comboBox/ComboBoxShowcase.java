package de.larmic.butterfaces.component.showcase.comboBox;

import de.larmic.butterfaces.component.partrenderer.StringUtils;
import de.larmic.butterfaces.component.showcase.AbstractInputShowcase;
import de.larmic.butterfaces.component.showcase.example.AbstractCodeExample;
import de.larmic.butterfaces.component.showcase.example.CssCodeExample;
import de.larmic.butterfaces.component.showcase.example.JavaCodeExample;
import de.larmic.butterfaces.component.showcase.example.XhtmlCodeExample;
import de.larmic.butterfaces.component.showcase.type.ComboBoxValueType;

import javax.faces.model.SelectItem;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Named
@ViewScoped
@SuppressWarnings("serial")
public class ComboBoxShowcase extends AbstractInputShowcase implements Serializable {

    private ComboBoxValueType comboBoxValueType = ComboBoxValueType.TEMPLATE;
    private FacetType selectedFacetType = FacetType.NONE;

    private boolean autoFocus;

    private final List<SelectItem> foos = new ArrayList<>();
    private final List<SelectItem> enums = new ArrayList<>();
    private final List<SelectItem> strings = new ArrayList<>();

    private Episode chosenEpisode;

    public ComboBoxShowcase() {
        this.initFoos();
        this.initStrings();
        this.initEnums();
    }

    @Override
    protected Object initValue() {
        return null;
    }

    @Override
    public void buildCodeExamples(final List<AbstractCodeExample> codeExamples) {
        final XhtmlCodeExample xhtmlCodeExample = createXhtmlExample();

        codeExamples.add(xhtmlCodeExample);
        if (this.comboBoxValueType == ComboBoxValueType.TEMPLATE) {
            codeExamples.add(createEpisodeJavaCodeExample());
            codeExamples.add(createEpisodeCssCodeExample());
        }
        generateDemoCSS(codeExamples);
    }

    private JavaCodeExample createEpisodeJavaCodeExample() {
        final JavaCodeExample javaCodeExample = new JavaCodeExample("Episode.java", "episode", "combobox.demo", "Episode", false);

        javaCodeExample.appendInnerContent("    private int numberInSeries;");
        javaCodeExample.appendInnerContent("    private String title;");
        javaCodeExample.appendInnerContent("    private String writtenBy;");
        javaCodeExample.appendInnerContent("    private String originalAirDate;");
        javaCodeExample.appendInnerContent("    private String image;");
        javaCodeExample.appendInnerContent("    // [...] getter + setter");

        return javaCodeExample;
    }

    private CssCodeExample createEpisodeCssCodeExample() {
        final CssCodeExample cssCodeExample = new CssCodeExample();

        cssCodeExample.addCss(".stargateEpisodeItem", "display: flex", "align-items: stretch");
        cssCodeExample.addCss(".stargateEpisodeItem img", "height: 75px");
        cssCodeExample.addCss(".stargateEpisodeItem h4", "font-size: 16px", "margin-top: 5px");
        cssCodeExample.addCss(".stargateEpisodeItem .stargateEpisodeDetails", "font-size: 12px", "margin-left: 5px");
        cssCodeExample.addCss(".stargateEpisodeItem .stargateEpisodeDetails > div", "display: flex", "align-items: baseline");
        cssCodeExample.addCss(".stargateEpisodeItem .stargateEpisodeDetails label", "width: 80px", "font-weight: bold");
        cssCodeExample.addCss(".stargateEpisodeItem .stargateEpisodeDetails span", "flex: 1");

        return cssCodeExample;
    }

    private XhtmlCodeExample createXhtmlExample() {
        final XhtmlCodeExample xhtmlCodeExample = new XhtmlCodeExample(false);

        xhtmlCodeExample.appendInnerContent("        <b:comboBox id=\"input\"");
        xhtmlCodeExample.appendInnerContent("                    label=\"" + this.getLabel() + "\"");
        xhtmlCodeExample.appendInnerContent("                    value=\"" + this.getValue() + "\"");
        xhtmlCodeExample.appendInnerContent("                    tooltip=\"" + this.getTooltip() + "\"");
        xhtmlCodeExample.appendInnerContent("                    styleClass=\"" + this.getStyleClass() + "\"");
        xhtmlCodeExample.appendInnerContent("                    readonly=\"" + this.isReadonly() + "\"");
        xhtmlCodeExample.appendInnerContent("                    required=\"" + this.isRequired() + "\"");
        xhtmlCodeExample.appendInnerContent("                    disabled=\"" + this.isDisabled() + "\"");
        xhtmlCodeExample.appendInnerContent("                    autoFocus=\"" + this.isAutoFocus() + "\"");
        xhtmlCodeExample.appendInnerContent("                    rendered=\"" + this.isRendered() + "\">");

        this.addAjaxTag(xhtmlCodeExample, "change");

        if (StringUtils.isNotEmpty(getTooltip())) {
            xhtmlCodeExample.appendInnerContent("            <b:tooltip>");
            xhtmlCodeExample.appendInnerContent("                " + getTooltip());
            xhtmlCodeExample.appendInnerContent("            </b:tooltip>");
        }

        if (selectedFacetType == FacetType.INPUT_GROUP_ADDON) {
            xhtmlCodeExample.appendInnerContent("            <f:facet name=\"input-group-addon-left\">");
            xhtmlCodeExample.appendInnerContent("                Left input-group-addon");
            xhtmlCodeExample.appendInnerContent("            </f:facet>");
        } else if (selectedFacetType == FacetType.INPUT_GROUP_BTN) {
            xhtmlCodeExample.appendInnerContent("            <f:facet name=\"input-group-btn-left\">");
            xhtmlCodeExample.appendInnerContent("                <button type=\"button\" class=\"btn btn-default\">");
            xhtmlCodeExample.appendInnerContent("                     Go!");
            xhtmlCodeExample.appendInnerContent("                 </button>");
            xhtmlCodeExample.appendInnerContent("            </f:facet>");
        }

        if (this.comboBoxValueType == ComboBoxValueType.STRING) {
            xhtmlCodeExample.appendInnerContent("            <f:selectItem itemValue=\"#{null}\"");
            xhtmlCodeExample.appendInnerContent("                          itemLabel=\"Choose one...\"/>");
            xhtmlCodeExample.appendInnerContent("            <f:selectItem itemValue=\"2000\"");
            xhtmlCodeExample.appendInnerContent("                          itemLabel=\"Year 2000\"/>");
            xhtmlCodeExample.appendInnerContent("            <f:selectItem itemValue=\"2010\"");
            xhtmlCodeExample.appendInnerContent("                          itemLabel=\"Year 2010\"/>");
            xhtmlCodeExample.appendInnerContent("            <f:selectItem itemValue=\"2020\"");
            xhtmlCodeExample.appendInnerContent("                          itemLabel=\"Year 2020\"/>");
        } else if (this.comboBoxValueType == ComboBoxValueType.ENUM) {
            xhtmlCodeExample.appendInnerContent("            <f:selectItems value=\"#{bean.fooEnums}\"/>");
        } else if (this.comboBoxValueType == ComboBoxValueType.OBJECT) {
            xhtmlCodeExample.appendInnerContent("            <f:selectItems value=\"#{bean.fooObjects}\"/>");
            xhtmlCodeExample.appendInnerContent("            <f:converter converterId=\"fooConverter\"/>    ");
        } else if (this.comboBoxValueType == ComboBoxValueType.TEMPLATE) {
            xhtmlCodeExample.appendInnerContent("            <f:selectItems value=\"#{bean.episodes}\"");
            xhtmlCodeExample.appendInnerContent("                           var=\"episode\"");
            xhtmlCodeExample.appendInnerContent("                           itemLabel=\"#{episode.title}\"");
            xhtmlCodeExample.appendInnerContent("                           itemValue=\"#{episode}\"");
            xhtmlCodeExample.appendInnerContent("                           noSelectionValue=\"please choose\"/>");
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

        xhtmlCodeExample.appendInnerContent("        </b:comboBox>", false);

        this.addOutputExample(xhtmlCodeExample);
        return xhtmlCodeExample;
    }

    @Override
    public Object getValue() {
        return super.getValue() != null ? super.getValue() : "(item is null)";
    }

    @Override
    public String getReadableValue() {
        if (super.getValue() != null) {
            if (super.getValue() instanceof Foo) {
                return ((Foo) super.getValue()).getValue();
            } else if (super.getValue() instanceof FooType) {
                return ((FooType) super.getValue()).label;
            } else if (super.getValue() instanceof Episode) {
                return ((Episode) super.getValue()).getTitle();
            }

            return (String) super.getValue();
        }

        return "(item is null)";
    }

    public List<SelectItem> getAvailableFacetTypes() {
        final List<SelectItem> items = new ArrayList<>();

        for (final FacetType type : FacetType.values()) {
            items.add(new SelectItem(type, type.label));
        }
        return items;
    }

    public List<SelectItem> getValues() {
        switch (this.comboBoxValueType) {
            case OBJECT:
                return this.foos;
            case ENUM:
                return this.enums;
            default:
                return this.strings;
        }
    }

    public boolean isConverterActive() {
        return this.comboBoxValueType == ComboBoxValueType.OBJECT;
    }

    public List<SelectItem> getComboBoxTypes() {
        final List<SelectItem> items = new ArrayList<>();

        for (final ComboBoxValueType type : ComboBoxValueType.values()) {
            items.add(new SelectItem(type, type.label));
        }
        return items;
    }

    public ComboBoxValueType getComboBoxValueType() {
        return this.comboBoxValueType;
    }

    public void setComboBoxValueType(final ComboBoxValueType comboBoxValueType) {
        this.comboBoxValueType = comboBoxValueType;
    }

    public boolean isAutoFocus() {
        return autoFocus;
    }

    public void setAutoFocus(boolean autoFocus) {
        this.autoFocus = autoFocus;
    }

    private void initFoos() {
        this.foos.add(new SelectItem(null, "Choose one..."));

        for (final String key : FooConverter.fooMap.keySet()) {
            final Foo foo = FooConverter.fooMap.get(key);
            this.foos.add(new SelectItem(foo, foo.getKey()));
        }
    }

    private void initEnums() {
        this.enums.add(new SelectItem(null, "Choose one..."));

        for (final FooType fooType : FooType.values()) {
            this.enums.add(new SelectItem(fooType.label));
        }
    }

    private void initStrings() {
        this.strings.add(new SelectItem(null, "Choose one..."));
        this.strings.add(new SelectItem("2000", "Year 2000"));
        this.strings.add(new SelectItem("2010", "Year 2010"));
        this.strings.add(new SelectItem("2020", "Year 2020"));
    }

    public FacetType getSelectedFacetType() {
        return selectedFacetType;
    }

    public void setSelectedFacetType(FacetType selectedFacetType) {
        this.selectedFacetType = selectedFacetType;
    }

    public List<Episode> getEpisodes() {
        return EpisodeConverter.EPISODES;
    }

    public Episode getChosenEpisode() {
        return chosenEpisode;
    }

    public void setChosenEpisode(Episode chosenEpisode) {
        this.chosenEpisode = chosenEpisode;
    }
}
