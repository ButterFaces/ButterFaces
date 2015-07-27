package de.larmic.butterfaces.component.showcase.comboBox;

import de.larmic.butterfaces.component.partrenderer.StringUtils;
import de.larmic.butterfaces.component.showcase.AbstractInputShowcase;
import de.larmic.butterfaces.component.showcase.example.AbstractCodeExample;
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
    private final List<Episode> episodes = new ArrayList<>();

    private Episode chosenEpisode;

    public ComboBoxShowcase() {
        this.initFoos();
        this.initStrings();
        this.initEnums();
        this.initEpisodes();
    }

    @Override
    protected Object initValue() {
        return null;
    }

    @Override
    public void buildCodeExamples(final List<AbstractCodeExample> codeExamples) {
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
        }

        xhtmlCodeExample.appendInnerContent("        </b:comboBox>", false);

        this.addOutputExample(xhtmlCodeExample);

        codeExamples.add(xhtmlCodeExample);

        generateDemoCSS(codeExamples);
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

    private void initEpisodes() {
        this.episodes.add(createEpisode(1, "Children of the Gods 1/2", "Mario Azzopardi", "Jonathan Glassner & Brad Wright", "July 27, 1997"));
        this.episodes.add(createEpisode(2, "Children of the Gods 2/2", "Mario Azzopardi", "Jonathan Glassner & Brad Wright", "July 27, 1997"));
        this.episodes.add(createEpisode(3, "The Enemy Within", "Dennis Berry", "Brad Wright", "August 1, 1997"));
        this.episodes.add(createEpisode(4, "Emancipation", "Jeff Woolnough", "Katharyn Michaelian Powers", "August 8, 1997"));
        this.episodes.add(createEpisode(5, "The Broca Divide", "Bill Gereghty", "Jonathan Glassner", "August 15, 1997"));
        this.episodes.add(createEpisode(6, "The First Commandment", "Dennis Berry", "Robert C. Cooper", "August 22, 1997"));
        this.episodes.add(createEpisode(7, "Cold Lazarus", "Kenneth J. Girotti", "Jeff F. King", "August 29, 1997"));
        this.episodes.add(createEpisode(8, "The Nox", "Brad Turner", "Hart Hanson", "September 12, 1997"));
        this.episodes.add(createEpisode(9, "Brief Candle", "Kenneth J. Girotti", "Jeff F. King", "September 19, 1997"));
        this.episodes.add(createEpisode(10, "Thor's Hammer", "Brad Turner", "Katharyn Michaelian Powers", "September 26, 1997"));
        this.episodes.add(createEpisode(11, "The Torment of Tantalus", "Jonathan Glassner", "Robert C. Cooper", "October 3, 1997"));
        this.episodes.add(createEpisode(12, "Bloodlines", "Mario Azzopardi", "Jeff F. King", "October 10, 1997"));
        this.episodes.add(createEpisode(13, "Fire and Water", "Allan Eastman", "Katharyn Michaelian Powers", "October 17, 1997"));
        this.episodes.add(createEpisode(14, "Hathor", "Brad Turner", "Story: David Bennett Carren & J. Larry Carroll", "October 24, 1997"));
        this.episodes.add(createEpisode(15, "Singularity", "Mario Azzopardi", "Robert C. Cooper", "October 31, 1997"));
        this.episodes.add(createEpisode(17, "Enigma", "Bill Gereghty", "Katharyn Michaelian Powers", "January 30, 1998"));
        this.episodes.add(createEpisode(18, "Solitudes", "Martin Wood", "Brad Wright", "February 6, 1998"));
        this.episodes.add(createEpisode(19, "Tin Man", "Jimmy Kaufman", "Jeff F. King", "February 13, 1998"));
        this.episodes.add(createEpisode(20, "There But for the Grace of God", "David Warry-Smith", "Robert C. Cooper", "February 20, 1998"));
        this.episodes.add(createEpisode(21, "Politics", "Martin Wood", "Brad Wright", "February 27, 1998"));
        this.episodes.add(createEpisode(22, "Within the Serpent's Grasp", "David Warry-Smith", "Story: James Crocker", "March 6, 1998"));
    }

    private Episode createEpisode(int numberInSeries, String title, String directedBy, String writtenBy, String originalAirDate) {
        Episode episode = new Episode();
        episode.setNumberInSeries(numberInSeries);
        episode.setTitle(title);
        episode.setDirectedBy(directedBy);
        episode.setWrittenBy(writtenBy);
        episode.setOriginalAirDate(originalAirDate);
        return episode;
    }

    public FacetType getSelectedFacetType() {
        return selectedFacetType;
    }

    public void setSelectedFacetType(FacetType selectedFacetType) {
        this.selectedFacetType = selectedFacetType;
    }

    public List<Episode> getEpisodes() {
        return episodes;
    }

    public Episode getChosenEpisode() {
        return chosenEpisode;
    }

    public void setChosenEpisode(Episode chosenEpisode) {
        this.chosenEpisode = chosenEpisode;
    }
}
