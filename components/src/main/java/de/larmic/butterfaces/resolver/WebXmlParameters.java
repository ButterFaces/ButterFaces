/*
 * Copyright Lars Michaelis and Stephan Zerhusen 2016.
 * Distributed under the MIT License.
 * (See accompanying file README.md file or copy at http://opensource.org/licenses/MIT)
 */
package de.larmic.butterfaces.resolver;

import javax.faces.context.ExternalContext;

import de.larmic.butterfaces.util.StringUtils;

/**
 * Easy way to get access of all butterfaces web.xml parameters.
 *
 * @author Lars Michaelis
 */
public class WebXmlParameters {

    public static final String CTX_PARAM_JQUERY = "org.butterfaces.provideJQuery";
    public static final String CTX_PARAM_BOOTSTRAP = "org.butterfaces.provideBootstrap";
    public static final String CTX_PARAM_USE_COMPRESSED_RESOURCES = "org.butterfaces.useCompressedResources";

    public static final String CTX_PARAM_REFRESH_GLYPHICON = "org.butterfaces.glyhicon.refresh";
    public static final String CTX_PARAM_OPTIONS_GLYPHICON = "org.butterfaces.glyhicon.options";
    public static final String CTX_PARAM_SORT_GLYPHICON = "org.butterfaces.glyhicon.sort.none";
    public static final String CTX_PARAM_SORT_ASC_GLYPHICON = "org.butterfaces.glyhicon.sort.ascending";
    public static final String CTX_PARAM_SORT_DESC_GLYPHICON = "org.butterfaces.glyhicon.sort.descending";
    public static final String CTX_PARAM_ORDER_LEFT_GLYPHICON = "org.butterfaces.glyhicon.order.left";
    public static final String CTX_PARAM_ORDER_RIGHT_GLYPHICON = "org.butterfaces.glyhicon.order.right";
    public static final String CTX_PARAM_COLLAPSING_GLYPHICON = "org.butterfaces.glyhicon.collapsing";
    public static final String CTX_PARAM_EXPANSION_GLYPHICON = "org.butterfaces.glyhicon.expansion";

    public static final String CTX_PARAM_TREEBOX_SHOW_CLEAR_BUTTON = "org.butterfaces.treebox.showClearButton";

    public static final String CTX_PARAM_AJAX_PROCESSING_TEXT = "org.butterfaces.ajaxProcessingTextOnRequest";
    public static final String CTX_PARAM_AJAX_PROCESSING_GLYPHICON = "org.butterfaces.ajaxProcessingGlyphiconOnRequest";
    public static final String DEFAULT_AJAX_PROCESSING_TEXT = "Processing";

    public static final String DEFAULT_REFRESH_GLYPHICON = "glyphicon glyphicon-refresh";
    public static final String DEFAULT_OPTIONS_GLYPHICON = "glyphicon glyphicon-th";
    public static final String DEFAULT_SORT_GLYPHICON = "glyphicon glyphicon-chevron-right";
    public static final String DEFAULT_SORT_ASC_GLYPHICON = "glyphicon glyphicon-chevron-down";
    public static final String DEFAULT_SORT_DESC_GLYPHICON = "glyphicon glyphicon-chevron-up";
    public static final String DEFAULT_ORDER_LEFT_GLYPHICON = "glyphicon glyphicon-chevron-left";
    public static final String DEFAULT_ORDER_RIGHT_GLYPHICON = "glyphicon glyphicon-chevron-right";

    public static final String CTX_PARAM_MAX_LENGTH_TEXT = "org.butterfaces.maxLengthText";
    public static final String DEFAULT_CTX_PARAM_MAX_LENGTH_TEXT = "{0} of {1} characters";

    public static final String CTX_PARAM_NO_ENTRIES_TEXT = "org.butterfaces.noEntriesText";
    public static final String DEFAULT_CTX_PARAM_NO_ENTRIES_TEXT = "No matching entries...";

    public static final String CTX_PARAM_SPINNER_TEXT = "org.butterfaces.spinnerText";
    public static final String DEFAULT_CTX_PARAM_SPINNER_TEXT = "Fetching data...";

    public static final String CTX_PARAM_AJAX_DISABLE_RENDER_REGIONS_ON_REQUEST = "org.butterfaces.ajaxDisableRenderRegionsOnRequest";
    public static final boolean DEFAULT_AJAX_DISABLE_RENDER_REGIONS_ON_REQUEST = true;

    public static final String CTX_PARAM_AUTO_TRIM_INPUT_FIELDS = "org.butterfaces.autoTrimInputFields";

    public static final String CTX_PARAM_INTEGRATION_PRIMEFACES_DISABLEJQUERY = "org.butterfaces.integration.primefaces.disableJQuery";

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
    private final String noEntriesText;
    private final String spinnerText;
    private final String maxLengthText;
    private final boolean ajaxDisableRenderRegionsOnRequest;
    private final boolean autoTrimInputFields;
    private final boolean intergrationPrimeFacesDisableJQuery;

    private final boolean showTreeBoxClearButton;

    public WebXmlParameters(final ExternalContext externalContext) {
        this.provideJQuery = this.readBooleanParameter(CTX_PARAM_JQUERY, externalContext);
        this.provideBoostrap = this.readBooleanParameter(CTX_PARAM_BOOTSTRAP, externalContext);
        this.useCompressedResources = this.readBooleanParameter(CTX_PARAM_USE_COMPRESSED_RESOURCES, externalContext);

        this.showTreeBoxClearButton = this.readBooleanParameter(CTX_PARAM_TREEBOX_SHOW_CLEAR_BUTTON, externalContext);

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
        this.maxLengthText = this.readParameter(CTX_PARAM_MAX_LENGTH_TEXT, DEFAULT_CTX_PARAM_MAX_LENGTH_TEXT, externalContext);

        this.ajaxDisableRenderRegionsOnRequest = this.readParameter(CTX_PARAM_AJAX_DISABLE_RENDER_REGIONS_ON_REQUEST, DEFAULT_AJAX_DISABLE_RENDER_REGIONS_ON_REQUEST, externalContext);

        this.autoTrimInputFields = this.readBooleanParameter(CTX_PARAM_AUTO_TRIM_INPUT_FIELDS, externalContext);

        this.intergrationPrimeFacesDisableJQuery = this.readBooleanParameter(CTX_PARAM_INTEGRATION_PRIMEFACES_DISABLEJQUERY, externalContext);
    }

    private boolean readBooleanParameter(final String parameter, final ExternalContext context) {
        final String param = readParameter(parameter, null, context);
        return Boolean.parseBoolean(param == null ? Boolean.TRUE.toString() : param);
    }

    private String readParameter(final String parameter, final String defaultValue, final ExternalContext context) {
        final String refreshButton = context.getInitParameter(parameter);
        return StringUtils.isEmpty(refreshButton) ? defaultValue : refreshButton;
    }

    private boolean readParameter(final String parameter, final boolean defaultValue, final ExternalContext context) {
        final String refreshButton = context.getInitParameter(parameter);
        return StringUtils.isEmpty(refreshButton) ? defaultValue : Boolean.parseBoolean(refreshButton);
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

    public boolean isAutoTrimInputFields() {
        return autoTrimInputFields;
    }

    public boolean isIntegrationPrimeFacesDisableJQuery() {
        return intergrationPrimeFacesDisableJQuery;
    }

    public boolean isAjaxDisableRenderRegionsOnRequest() {
        return ajaxDisableRenderRegionsOnRequest;
    }

    public String getMaxLengthText() {
        return maxLengthText;
    }

    public boolean isShowTreeBoxClearButton() {
        return showTreeBoxClearButton;
    }
}
