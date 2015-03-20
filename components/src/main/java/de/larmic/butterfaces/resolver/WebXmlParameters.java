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

    public final String CTX_PARAM_REFRESH_GLYPHICON = "de.larmic.butterfaces.glyhicon.refresh";
    public final String CTX_PARAM_OPTIONS_GLYPHICON = "de.larmic.butterfaces.glyhicon.options";

    public final String DEFAULT_REFRESH_GLYPHICON = "glyphicon glyphicon-refresh";
    public final String DEFAULT_OPTIONS_GLYPHICON = "glyphicon glyphicon-th";

    private final boolean provideJQuery;
    private final boolean provideBoostrap;
    private final boolean providePrettyprint;
    private final boolean useCompressedResources;

    private final String refreshGlyphicon;
    private final String optionsGlyphicon;

    public WebXmlParameters(final ExternalContext externalContext) {
        this.provideJQuery = this.readBooleanParameter(CTX_PARAM_JQUERY, externalContext);
        this.provideBoostrap = this.readBooleanParameter(CTX_PARAM_BOOTSTRAP, externalContext);
        this.providePrettyprint = this.readBooleanParameter(CTX_PARAM_PRETTYPRINT, externalContext);
        this.useCompressedResources = this.readBooleanParameter(CTX_PARAM_USE_COMPRESSED_RESOURCES, externalContext);

        this.refreshGlyphicon = this.readParameter(CTX_PARAM_REFRESH_GLYPHICON, DEFAULT_REFRESH_GLYPHICON, externalContext);
        this.optionsGlyphicon = this.readParameter(CTX_PARAM_OPTIONS_GLYPHICON, DEFAULT_OPTIONS_GLYPHICON, externalContext);
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
}
