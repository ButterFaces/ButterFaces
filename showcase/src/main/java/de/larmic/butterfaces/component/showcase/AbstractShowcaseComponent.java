package de.larmic.butterfaces.component.showcase;

/**
 * Created by larmic on 31.07.14.
 */
public abstract class AbstractShowcaseComponent {

    private boolean rendered = true;

    public abstract String getXHtml();

    /**
     * Is called by getCss() and can be used to add custom css output.
     */
    protected void addCss(final StringBuilder sb) {

    }

    /**
     * Is called by getJS() and can be used to add custom js output.
     */
    protected void addJs(final StringBuilder sb) {

    }

    public String getCss() {
        final StringBuilder sb = new StringBuilder();

        this.addCss(sb);

        return sb.toString();
    }

    public String getJs() {
        final StringBuilder sb = new StringBuilder();

        this.addJs(sb);

        return sb.toString();
    }

    public boolean isRendered() {
        return this.rendered;
    }

    public void setRendered(final boolean rendered) {
        this.rendered = rendered;
    }
}
