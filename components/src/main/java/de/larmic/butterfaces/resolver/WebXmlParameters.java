package de.larmic.butterfaces.resolver;

import de.larmic.butterfaces.component.partrenderer.StringUtils;

import javax.faces.context.ExternalContext;

/**
 * Created by larmic on 20.03.15.
 */
public class WebXmlParameters {

    public static final String CTX_PARAM_JQUERY = "de.larmic.butterfaces.provideJQuery";
    public static final String CTX_PARAM_BOOTSTRAP = "de.larmic.butterfaces.provideBootstrap";
    public static final String CTX_PARAM_PRETTYPRINT = "de.larmic.butterfaces.providePrettify";
    public static final String CTX_PARAM_USE_COMPRESSED_RESOURCES = "de.larmic.butterfaces.useCompressedResources";

    public static final String CTX_PARAM_REFRESH_GLYPHICON = "de.larmic.butterfaces.glyhicon.refresh";
    public static final String CTX_PARAM_OPTIONS_GLYPHICON = "de.larmic.butterfaces.glyhicon.options";
    public static final String CTX_PARAM_SORT_GLYPHICON = "de.larmic.butterfaces.glyhicon.sort.none";
    public static final String CTX_PARAM_SORT_ASC_GLYPHICON = "de.larmic.butterfaces.glyhicon.sort.ascending";
    public static final String CTX_PARAM_SORT_DESC_GLYPHICON = "de.larmic.butterfaces.glyhicon.sort.descending";
    public static final String CTX_PARAM_COLLAPSING_GLYPHICON = "de.larmic.butterfaces.glyhicon.collapsing";
    public static final String CTX_PARAM_EXPANSION_GLYPHICON = "de.larmic.butterfaces.glyhicon.expansion";

    public static final String DEFAULT_REFRESH_GLYPHICON = "glyphicon glyphicon-refresh";
    public static final String DEFAULT_OPTIONS_GLYPHICON = "glyphicon glyphicon-th";
    public static final String DEFAULT_SORT_GLYPHICON = "glyphicon glyphicon-chevron-right";
    public static final String DEFAULT_SORT_ASC_GLYPHICON = "glyphicon glyphicon-chevron-down";
    public static final String DEFAULT_SORT_DESC_GLYPHICON = "glyphicon glyphicon-chevron-up";
    public static final String DEFAULT_COLLAPSING_GLYPHICON = "glyphicon glyphicon-minus-sign";
    public static final String DEFAULT_EXPANSION_GLYPHICON = "glyphicon glyphicon-plus-sign";

    private final boolean provideJQuery;
    private final boolean provideBoostrap;
    private final boolean providePrettyprint;
    private final boolean useCompressedResources;

    private final String refreshGlyphicon;
    private final String optionsGlyphicon;
    private final String sortUnknownGlyphicon;
    private final String sortAscGlyphicon;
    private final String sortDescGlyphicon;
    private final String collapsingGlyphicon;
    private final String expansionGlyphicon;

    public WebXmlParameters(final ExternalContext externalContext) {
        this.provideJQuery = this.readBooleanParameter(CTX_PARAM_JQUERY, externalContext);
        this.provideBoostrap = this.readBooleanParameter(CTX_PARAM_BOOTSTRAP, externalContext);
        this.providePrettyprint = this.readBooleanParameter(CTX_PARAM_PRETTYPRINT, externalContext);
        this.useCompressedResources = this.readBooleanParameter(CTX_PARAM_USE_COMPRESSED_RESOURCES, externalContext);

        this.refreshGlyphicon = this.readParameter(CTX_PARAM_REFRESH_GLYPHICON, DEFAULT_REFRESH_GLYPHICON, externalContext);
        this.optionsGlyphicon = this.readParameter(CTX_PARAM_OPTIONS_GLYPHICON, DEFAULT_OPTIONS_GLYPHICON, externalContext);
        this.sortUnknownGlyphicon = this.readParameter(CTX_PARAM_SORT_GLYPHICON, DEFAULT_SORT_GLYPHICON, externalContext);
        this.sortAscGlyphicon = this.readParameter(CTX_PARAM_SORT_ASC_GLYPHICON, DEFAULT_SORT_ASC_GLYPHICON, externalContext);
        this.sortDescGlyphicon = this.readParameter(CTX_PARAM_SORT_DESC_GLYPHICON, DEFAULT_SORT_DESC_GLYPHICON, externalContext);

        this.collapsingGlyphicon = this.readParameter(CTX_PARAM_COLLAPSING_GLYPHICON, DEFAULT_COLLAPSING_GLYPHICON, externalContext);
        this.expansionGlyphicon = this.readParameter(CTX_PARAM_EXPANSION_GLYPHICON, DEFAULT_EXPANSION_GLYPHICON, externalContext);
    }

    private boolean readBooleanParameter(final String parameter, final ExternalContext context) {
        final String param = readParameter(parameter, null, context);
        return Boolean.parseBoolean(param == null ? Boolean.TRUE.toString() : param);
    }

    private String readParameter(final String parameter, final String defaultValue, final ExternalContext context) {
        final String refreshButton = context.getInitParameter(parameter);
        return StringUtils.isEmpty(refreshButton) ? defaultValue : refreshButton;
    }

    public String getRefreshGlyphicon() {
        return refreshGlyphicon;
    }

    public String getOptionsGlyphicon() {
        return optionsGlyphicon;
    }

    public boolean isProvideJQuery() {
        return provideJQuery;
    }

    public boolean isProvideBoostrap() {
        return provideBoostrap;
    }

    public boolean isProvidePrettyprint() {
        return providePrettyprint;
    }

    public boolean isUseCompressedResources() {
        return useCompressedResources;
    }

    public String getSortUnknownGlyphicon() {
        return sortUnknownGlyphicon;
    }

    public String getSortAscGlyphicon() {
        return sortAscGlyphicon;
    }

    public String getSortDescGlyphicon() {
        return sortDescGlyphicon;
    }

    public String getCollapsingGlyphicon() {
        return collapsingGlyphicon;
    }

    public String getExpansionGlyphicon() {
        return expansionGlyphicon;
    }
}
