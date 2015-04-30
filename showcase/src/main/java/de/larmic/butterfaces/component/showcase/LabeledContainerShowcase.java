package de.larmic.butterfaces.component.showcase;

import de.larmic.butterfaces.component.showcase.example.AbstractCodeExample;

import javax.faces.view.ViewScoped;
import javax.inject.Named;
import java.io.Serializable;
import java.util.List;

@Named
@ViewScoped
@SuppressWarnings("serial")
public class LabeledContainerShowcase extends AbstractCodeShowcase implements Serializable {

    private String label = "label";
    private boolean hideLabel;

    @Override
    public void buildCodeExamples(List<AbstractCodeExample> codeExamples) {

    }

    public boolean isHideLabel() {
        return hideLabel;
    }

    public void setHideLabel(boolean hideLabel) {
        this.hideLabel = hideLabel;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }
}
