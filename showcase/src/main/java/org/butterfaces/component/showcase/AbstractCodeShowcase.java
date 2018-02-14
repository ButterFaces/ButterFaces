package org.butterfaces.component.showcase;

import org.butterfaces.component.showcase.example.AbstractCodeExample;
import org.butterfaces.component.showcase.example.AbstractCodeExample;

import java.util.ArrayList;
import java.util.List;

/**
 * Abstract showcase class allows to build code examples (java, css, xhtml, ...) and show it generic by using
 * codeExamples.xhtml component.
 */
public abstract class AbstractCodeShowcase {

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
