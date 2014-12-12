package de.larmic.butterfaces.component.showcase;

import de.larmic.butterfaces.component.showcase.example.AbstractCodeExample;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by larmic on 12.12.14.
 */
public abstract class AbstractShowcaseMultiCodeComponent {

    private boolean rendered = true;
    private boolean disabled = false;

    public abstract void buildCodeExamples(final List<AbstractCodeExample> codeExamples);

    public List<AbstractCodeExample> getCodeExamples() {
        final List<AbstractCodeExample> codeExamples = new ArrayList<>();

        this.buildCodeExamples(codeExamples);

        return codeExamples;
    }

    public boolean isRendered() {
        return rendered;
    }

    public void setRendered(boolean rendered) {
        this.rendered = rendered;
    }

    public boolean isDisabled() {
        return disabled;
    }

    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }
}
