package de.larmic.butterfaces.resolver;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import javax.faces.context.ExternalContext;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class WebXmlParametersTest {

    private static final String OVERRIDDEN_NOENTRIESTEXT = "customNoEntriesText";

    private static final String OVERRIDDEN_REFRESH = "o_refresh";
    private static final String OVERRIDDEN_OPTIONS = "o_options";

    private static final String OVERRIDDEN_SORT_UNKNOWN = "o_sort_unknown";
    private static final String OVERRIDDEN_SORT_ASC = "o_sort_asc";
    private static final String OVERRIDDEN_SORT_DESC = "o_sort_desc";

    private static final String OVERRIDDEN_ORDER_LEFT= "o_order_right";
    private static final String OVERRIDDEN_ORDER_RIGHT= "o_order_left";

    private static final String OVERRIDDEN_COLLAPSING = "o_collape";
    private static final String OVERRIDDEN_EXPANSION = "o_expand";

    private static final String OVERRIDDEN_AJAX_PROCESSING_TEXT = "Loading";
    private static final String OVERRIDDEN_AJAX_PROCESSING_GLYPHICON = "fa fa-refresh fa-spin";

    @Mock
    private ExternalContext defaultValueExternalContext;

    @Mock
    private ExternalContext overriddenValueExternalContext;

    @Before
    public void setUp() throws Exception {
        initMocks(this);

        when(overriddenValueExternalContext.getInitParameter(WebXmlParameters.CTX_PARAM_NO_ENTRIES_TEXT)).thenReturn(OVERRIDDEN_NOENTRIESTEXT);

        when(overriddenValueExternalContext.getInitParameter(WebXmlParameters.CTX_PARAM_REFRESH_GLYPHICON)).thenReturn(OVERRIDDEN_REFRESH);
        when(overriddenValueExternalContext.getInitParameter(WebXmlParameters.CTX_PARAM_OPTIONS_GLYPHICON)).thenReturn(OVERRIDDEN_OPTIONS);

        when(overriddenValueExternalContext.getInitParameter(WebXmlParameters.CTX_PARAM_SORT_GLYPHICON)).thenReturn(OVERRIDDEN_SORT_UNKNOWN);
        when(overriddenValueExternalContext.getInitParameter(WebXmlParameters.CTX_PARAM_SORT_ASC_GLYPHICON)).thenReturn(OVERRIDDEN_SORT_ASC);
        when(overriddenValueExternalContext.getInitParameter(WebXmlParameters.CTX_PARAM_SORT_DESC_GLYPHICON)).thenReturn(OVERRIDDEN_SORT_DESC);

        when(overriddenValueExternalContext.getInitParameter(WebXmlParameters.CTX_PARAM_ORDER_LEFT_GLYPHICON)).thenReturn(OVERRIDDEN_ORDER_LEFT);
        when(overriddenValueExternalContext.getInitParameter(WebXmlParameters.CTX_PARAM_ORDER_RIGHT_GLYPHICON)).thenReturn(OVERRIDDEN_ORDER_RIGHT);

        when(overriddenValueExternalContext.getInitParameter(WebXmlParameters.CTX_PARAM_COLLAPSING_GLYPHICON)).thenReturn(OVERRIDDEN_COLLAPSING);
        when(overriddenValueExternalContext.getInitParameter(WebXmlParameters.CTX_PARAM_EXPANSION_GLYPHICON)).thenReturn(OVERRIDDEN_EXPANSION);

        when(overriddenValueExternalContext.getInitParameter(WebXmlParameters.CTX_PARAM_BOOTSTRAP)).thenReturn("false");
        when(overriddenValueExternalContext.getInitParameter(WebXmlParameters.CTX_PARAM_JQUERY)).thenReturn("false");
        when(overriddenValueExternalContext.getInitParameter(WebXmlParameters.CTX_PARAM_USE_COMPRESSED_RESOURCES)).thenReturn("false");

        when(overriddenValueExternalContext.getInitParameter(WebXmlParameters.CTX_PARAM_AJAX_PROCESSING_TEXT)).thenReturn(OVERRIDDEN_AJAX_PROCESSING_TEXT);
        when(overriddenValueExternalContext.getInitParameter(WebXmlParameters.CTX_PARAM_AJAX_PROCESSING_GLYPHICON)).thenReturn(OVERRIDDEN_AJAX_PROCESSING_GLYPHICON);
    }

    @Test
    public void testGetNoEntriesText() throws Exception {
        Assert.assertEquals(WebXmlParameters.DEFAULT_CTX_PARAM_NO_ENTRIES_TEXT, new WebXmlParameters(defaultValueExternalContext).getNoEntriesText());
        Assert.assertEquals(OVERRIDDEN_NOENTRIESTEXT, new WebXmlParameters(overriddenValueExternalContext).getNoEntriesText());
    }

    @Test
    public void testGetRefreshGlyphicon() throws Exception {
        Assert.assertEquals(WebXmlParameters.DEFAULT_REFRESH_GLYPHICON, new WebXmlParameters(defaultValueExternalContext).getRefreshGlyphicon());
        Assert.assertEquals(OVERRIDDEN_REFRESH, new WebXmlParameters(overriddenValueExternalContext).getRefreshGlyphicon());
    }

    @Test
    public void testGetOptionsGlyphicon() throws Exception {
        Assert.assertEquals(WebXmlParameters.DEFAULT_OPTIONS_GLYPHICON, new WebXmlParameters(defaultValueExternalContext).getOptionsGlyphicon());
        Assert.assertEquals(OVERRIDDEN_OPTIONS, new WebXmlParameters(overriddenValueExternalContext).getOptionsGlyphicon());
    }

    @Test
    public void testResourceParameters() throws Exception {
        assertThat(new WebXmlParameters(defaultValueExternalContext).isProvideBoostrap()).isTrue();
        assertThat(new WebXmlParameters(overriddenValueExternalContext).isProvideBoostrap()).isFalse();

        assertThat(new WebXmlParameters(defaultValueExternalContext).isProvideJQuery()).isTrue();
        assertThat(new WebXmlParameters(overriddenValueExternalContext).isProvideJQuery()).isFalse();
    }

    @Test
    public void testIsUsedCompressedResources() throws Exception {
        assertThat(new WebXmlParameters(defaultValueExternalContext).isUseCompressedResources()).isTrue();
        assertThat(new WebXmlParameters(overriddenValueExternalContext).isUseCompressedResources()).isFalse();
    }

    @Test
    public void testSortParameters() throws Exception {
        assertThat(new WebXmlParameters(defaultValueExternalContext).getSortUnknownGlyphicon()).isEqualTo(WebXmlParameters.DEFAULT_SORT_GLYPHICON);
        assertThat(new WebXmlParameters(overriddenValueExternalContext).getSortUnknownGlyphicon()).isEqualTo(OVERRIDDEN_SORT_UNKNOWN);
        assertThat(new WebXmlParameters(defaultValueExternalContext).getSortAscGlyphicon()).isEqualTo(WebXmlParameters.DEFAULT_SORT_ASC_GLYPHICON);
        assertThat(new WebXmlParameters(overriddenValueExternalContext).getSortAscGlyphicon()).isEqualTo(OVERRIDDEN_SORT_ASC);
        assertThat(new WebXmlParameters(defaultValueExternalContext).getSortDescGlyphicon()).isEqualTo(WebXmlParameters.DEFAULT_SORT_DESC_GLYPHICON);
        assertThat(new WebXmlParameters(overriddenValueExternalContext).getSortDescGlyphicon()).isEqualTo(OVERRIDDEN_SORT_DESC);
    }

    @Test
    public void testOrderParameters() throws Exception {
        Assert.assertEquals(WebXmlParameters.DEFAULT_ORDER_LEFT_GLYPHICON, new WebXmlParameters(defaultValueExternalContext).getOrderLeftGlyphicon());
        Assert.assertEquals(OVERRIDDEN_ORDER_LEFT, new WebXmlParameters(overriddenValueExternalContext).getOrderLeftGlyphicon());

        Assert.assertEquals(WebXmlParameters.DEFAULT_ORDER_RIGHT_GLYPHICON, new WebXmlParameters(defaultValueExternalContext).getOrderRightGlyphicon());
        Assert.assertEquals(OVERRIDDEN_ORDER_RIGHT, new WebXmlParameters(overriddenValueExternalContext).getOrderRightGlyphicon());
    }

    @Test
    public void testAjaxProcessingTextOnRequest() {
        Assert.assertEquals(WebXmlParameters.DEFAULT_AJAX_PROCESSING_TEXT, new WebXmlParameters(defaultValueExternalContext).getAjaxProcessingTextOnRequest());
        Assert.assertEquals(OVERRIDDEN_AJAX_PROCESSING_TEXT, new WebXmlParameters(overriddenValueExternalContext).getAjaxProcessingTextOnRequest());
    }

    @Test
    public void testAjaxProcessingGlyphiconOnRequest() {
        Assert.assertEquals("", new WebXmlParameters(defaultValueExternalContext).getAjaxProcessingGlyphiconOnRequest());
        Assert.assertEquals(OVERRIDDEN_AJAX_PROCESSING_GLYPHICON, new WebXmlParameters(overriddenValueExternalContext).getAjaxProcessingGlyphiconOnRequest());
    }
}