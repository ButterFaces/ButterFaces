package de.larmic.butterfaces.component.showcase.container;

import de.larmic.butterfaces.component.showcase.AbstractCodeShowcase;
import de.larmic.butterfaces.component.showcase.tree.Episode;
import de.larmic.butterfaces.component.showcase.tree.Episodes;
import de.larmic.butterfaces.component.showcase.container.examples.*;
import de.larmic.butterfaces.component.showcase.example.AbstractCodeExample;
import de.larmic.butterfaces.component.showcase.example.CssCodeExample;
import de.larmic.butterfaces.component.showcase.table.DemoPojo;
import de.larmic.butterfaces.component.showcase.table.example.DemoPojoCodeExample;
import de.larmic.butterfaces.component.showcase.tree.examples.stargate.TreeBoxEpisodesJavaExample;

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
public class RepeatShowcase extends AbstractCodeShowcase implements Serializable {

    private RepeatExampleType selectedExampleType = RepeatExampleType.STARGATE;

    private final List<DemoPojo> values;

    public RepeatShowcase() {
        values = new ArrayList<>();

        for (int i = 0; i < 123; i++) {
            values.add(new DemoPojo(i, "a" + i, "b" + i));
        }
    }

    public void play() {

    }

    @Override
    public void buildCodeExamples(final List<AbstractCodeExample> codeExamples) {
        if (selectedExampleType == RepeatExampleType.SIMPLE) {
            codeExamples.add(new SimpleRepeatListXhtmlCodeExample(this.isRendered()));
            codeExamples.add(new SimpleRepeatBeanCodeExample());
            codeExamples.add(new DemoPojoCodeExample("repeat.demo"));
            final CssCodeExample cssCodeExample = new CssCodeExample();
            cssCodeExample.addCss(".repeat-simple-list", "max-height: 400px", "overflow: auto");
            codeExamples.add(cssCodeExample);
        } else if (selectedExampleType == RepeatExampleType.STARGATE) {
            codeExamples.add(new StargateRepeatListXhtmlCodeExample(this.isRendered()));
            codeExamples.add(new StargateRepeatBeanCodeExample());
            codeExamples.add(new TreeBoxEpisodesJavaExample("repeat.demo"));
            codeExamples.add(new StargateRepeatListCssCodeExample());
        } else {
            codeExamples.add(new NestedRepeatListXhtmlCodeExample());
            codeExamples.add(new NestedRepeatBeanCodeExample());
        }
    }

    public List<Episode> getValues() {
        return Episodes.EPISODES;
    }

    public List<DemoPojo> getDemoPojos() {
        return values;
    }

    public List<String> getNestedOuterValues() {
        return Arrays.asList("A", "B", "C");
    }

    public List<String> getNestedInnerValueForOuterValue(String outerValue) {
        return Arrays.asList(outerValue + "1", outerValue + "2", outerValue + "3");
    }

    public List<SelectItem> getExampleTypes() {
        final List<SelectItem> items = new ArrayList<>();

        for (final RepeatExampleType type : RepeatExampleType.values()) {
            items.add(new SelectItem(type, type.label));
        }
        return items;
    }

    public RepeatExampleType getSelectedExampleType() {
        return selectedExampleType;
    }

    public void setSelectedExampleType(RepeatExampleType selectedExampleType) {
        this.selectedExampleType = selectedExampleType;
    }
}
