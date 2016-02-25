package de.larmic.butterfaces.resolver;

import de.larmic.butterfaces.util.StringUtils;

import javax.faces.context.ExternalContext;

/**
 * Easy way to get access of all butterfaces web.xml parameters.
 */
public class WebXmlParameters {

    public static final String CTX_PARAM_JQUERY = "de.larmic.butterfaces.provideJQuery";
    public static final String CTX_PARAM_BOOTSTRAP = "de.larmic.butterfaces.provideBootstrap";
    public static final String CTX_PARAM_USE_COMPRESSED_RESOURCES = "de.larmic.butterfaces.useCompressedResources";

    public static final String CTX_PARAM_REFRESH_GLYPHICON = "de.larmic.butterfaces.glyhicon.refresh";
    public static final String CTX_PARAM_OPTIONS_GLYPHICON = "de.larmic.butterfaces.glyhicon.options";
    public static final String CTX_PARAM_SORT_GLYPHICON = "de.larmic.butterfaces.glyhicon.sort.none";
    public static final String CTX_PARAM_SORT_ASC_GLYPHICON = "de.larmic.butterfaces.glyhicon.sort.ascending";
    public static final String CTX_PARAM_SORT_DESC_GLYPHICON = "de.larmic.butterfaces.glyhicon.sort.descending";
    public static final String CTX_PARAM_ORDER_LEFT_GLYPHICON = "de.larmic.butterfaces.glyhicon.order.left";
    public static final String CTX_PARAM_ORDER_RIGHT_GLYPHICON = "de.larmic.butterfaces.glyhicon.order.right";
    public static final String CTX_PARAM_COLLAPSING_GLYPHICON = "de.larmic.butterfaces.glyhicon.collapsing";
    public static final String CTX_PARAM_EXPANSION_GLYPHICON = "de.larmic.butterfaces.glyhicon.expansion";

    public static final String CTX_PARAM_AJAX_PROCESSING_TEXT = "de.larmic.butterfaces.ajaxProcessingTextOnRequest";
    public static final String CTX_PARAM_AJAX_PROCESSING_GLYPHICON = "de.larmic.butterfaces.ajaxProcessingGlyphiconOnRequest";
    public static final String DEFAULT_AJAX_PROCESSING_TEXT = "Processing";

    public static final String DEFAULT_REFRESH_GLYPHICON = "glyphicon glyphicon-refresh";
    public static final String DEFAULT_OPTIONS_GLYPHICON = "glyphicon glyphicon-th";
    public static final String DEFAULT_SORT_GLYPHICON = "glyphicon glyphicon-chevron-right";
    public static final String DEFAULT_SORT_ASC_GLYPHICON = "glyphicon glyphicon-chevron-down";
    public static final String DEFAULT_SORT_DESC_GLYPHICON = "glyphicon glyphicon-chevron-up";
    public static final String DEFAULT_ORDER_LEFT_GLYPHICON = "glyphicon glyphicon-chevron-left";
    public static final String DEFAULT_ORDER_RIGHT_GLYPHICON = "glyphicon glyphicon-chevron-right";

    public static final String CTX_PARAM_NO_ENTRIES_TEXT = "de.larmic.butterfaces.noEntriesText";
    public static final String DEFAULT_CTX_PARAM_NO_ENTRIES_TEXT = "No matching entries...";

    public static final String CTX_PARAM_SPINNER_TEXT = "de.larmic.butterfaces.spinnerText";
    public static final String DEFAULT_CTX_PARAM_SPINNER_TEXT = "Fetching data...";

    private final boolean provideJQuery;
    private final boolean provideBoostrap;
    private final boolean useCompressedResources;

    private final String refreshGlyphicon;
    private final String optionsGlyphicon;
    private final String sortUnknownGlyphicon;
    private final String sortAscGlyphicon;
    private final String sortDescGlyphicon;
    private final String orderLeftGlyphicon;
    private final String orderRightGlyphicon;

    private final String ajaxProcessingTextOnRequest;
    private final String ajaxProcessingGlyphiconOnRequest;
    private String noEntriesText;
    private String spinnerText;

    public WebXmlParameters(final ExternalContext externalContext) {
        this.provideJQuery = this.readBooleanParameter(CTX_PARAM_JQUERY, externalContext);
        this.provideBoostrap = this.readBooleanParameter(CTX_PARAM_BOOTSTRAP, externalContext);
        this.useCompressedResources = this.readBooleanParameter(CTX_PARAM_USE_COMPRESSED_RESOURCES, externalContext);

        this.refreshGlyphicon = this.readParameter(CTX_PARAM_REFRESH_GLYPHICON, DEFAULT_REFRESH_GLYPHICON, externalContext);
        this.optionsGlyphicon = this.readParameter(CTX_PARAM_OPTIONS_GLYPHICON, DEFAULT_OPTIONS_GLYPHICON, externalContext);
        this.sortUnknownGlyphicon = this.readParameter(CTX_PARAM_SORT_GLYPHICON, DEFAULT_SORT_GLYPHICON, externalContext);
        this.sortAscGlyphicon = this.readParameter(CTX_PARAM_SORT_ASC_GLYPHICON, DEFAULT_SORT_ASC_GLYPHICON, externalContext);
        this.sortDescGlyphicon = this.readParameter(CTX_PARAM_SORT_DESC_GLYPHICON, DEFAULT_SORT_DESC_GLYPHICON, externalContext);

        this.orderLeftGlyphicon = this.readParameter(CTX_PARAM_ORDER_LEFT_GLYPHICON, DEFAULT_ORDER_LEFT_GLYPHICON, externalContext);
        this.orderRightGlyphicon = this.readParameter(CTX_PARAM_ORDER_RIGHT_GLYPHICON, DEFAULT_ORDER_RIGHT_GLYPHICON, externalContext);

        this.ajaxProcessingTextOnRequest = this.readParameter(CTX_PARAM_AJAX_PROCESSING_TEXT, DEFAULT_AJAX_PROCESSING_TEXT, externalContext);
        this.ajaxProcessingGlyphiconOnRequest = this.readParameter(CTX_PARAM_AJAX_PROCESSING_GLYPHICON, "", externalContext);

        this.noEntriesText = this.readParameter(CTX_PARAM_NO_ENTRIES_TEXT, DEFAULT_CTX_PARAM_NO_ENTRIES_TEXT, externalContext);
        this.spinnerText = this.readParameter(CTX_PARAM_SPINNER_TEXT, DEFAULT_CTX_PARAM_SPINNER_TEXT, externalContext);
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

    public String getOrderLeftGlyphicon() {
        return orderLeftGlyphicon;
    }

    public String getOrderRightGlyphicon() {
        return orderRightGlyphicon;
    }

    public String getAjaxProcessingTextOnRequest() {
        return ajaxProcessingTextOnRequest;
    }

    public String getAjaxProcessingGlyphiconOnRequest() {
        return ajaxProcessingGlyphiconOnRequest;
    }

    public String getNoEntriesText() {
        return noEntriesText;
    }

    public String getSpinnerText() {
        return spinnerText;
    }
}
