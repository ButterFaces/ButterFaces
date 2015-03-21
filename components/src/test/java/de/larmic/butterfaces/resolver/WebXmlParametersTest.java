package de.larmic.butterfaces.resolver;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import javax.faces.context.ExternalContext;

import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class WebXmlParametersTest {

    private static final String OVERRIDDEN_REFRESH = "o_refresh";
    private static final String OVERRIDDEN_OPTIONS = "o_options";

    private static final String OVERRIDDEN_SORT_UNKNOWN = "o_sort_unknown";
    private static final String OVERRIDDEN_SORT_ASC = "o_sort_asc";
    private static final String OVERRIDDEN_SORT_DESC = "o_sort_desc";

    private static final String OVERRIDDEN_COLLAPSING = "o_collape";
    private static final String OVERRIDDEN_EXPANSION = "o_expand";

    @Mock
    private ExternalContext defaultValueExternalContext;

    @Mock
    private ExternalContext overriddenValueExternalContext;

    @Before
    public void setUp() throws Exception {
        initMocks(this);

        when(overriddenValueExternalContext.getInitParameter(WebXmlParameters.CTX_PARAM_REFRESH_GLYPHICON)).thenReturn(OVERRIDDEN_REFRESH);
        when(overriddenValueExternalContext.getInitParameter(WebXmlParameters.CTX_PARAM_OPTIONS_GLYPHICON)).thenReturn(OVERRIDDEN_OPTIONS);

        when(overriddenValueExternalContext.getInitParameter(WebXmlParameters.CTX_PARAM_SORT_GLYPHICON)).thenReturn(OVERRIDDEN_SORT_UNKNOWN);
        when(overriddenValueExternalContext.getInitParameter(WebXmlParameters.CTX_PARAM_SORT_ASC_GLYPHICON)).thenReturn(OVERRIDDEN_SORT_ASC);
        when(overriddenValueExternalContext.getInitParameter(WebXmlParameters.CTX_PARAM_SORT_DESC_GLYPHICON)).thenReturn(OVERRIDDEN_SORT_DESC);

        when(overriddenValueExternalContext.getInitParameter(WebXmlParameters.CTX_PARAM_COLLAPSING_GLYPHICON)).thenReturn(OVERRIDDEN_COLLAPSING);
        when(overriddenValueExternalContext.getInitParameter(WebXmlParameters.CTX_PARAM_EXPANSION_GLYPHICON)).thenReturn(OVERRIDDEN_EXPANSION);

        when(overriddenValueExternalContext.getInitParameter(WebXmlParameters.CTX_PARAM_BOOTSTRAP)).thenReturn("false");
        when(overriddenValueExternalContext.getInitParameter(WebXmlParameters.CTX_PARAM_JQUERY)).thenReturn("false");
        when(overriddenValueExternalContext.getInitParameter(WebXmlParameters.CTX_PARAM_PRETTYPRINT)).thenReturn("false");
        when(overriddenValueExternalContext.getInitParameter(WebXmlParameters.CTX_PARAM_USE_COMPRESSED_RESOURCES)).thenReturn("false");
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
        Assert.assertEquals(true, new WebXmlParameters(defaultValueExternalContext).isProvideBoostrap());
        Assert.assertEquals(false, new WebXmlParameters(overriddenValueExternalContext).isProvideBoostrap());

        Assert.assertEquals(true, new WebXmlParameters(defaultValueExternalContext).isProvideJQuery());
        Assert.assertEquals(false, new WebXmlParameters(overriddenValueExternalContext).isProvideJQuery());

        Assert.assertEquals(true, new WebXmlParameters(defaultValueExternalContext).isProvidePrettyprint());
        Assert.assertEquals(false, new WebXmlParameters(overriddenValueExternalContext).isProvidePrettyprint());
    }

    @Test
    public void testIsUsedCompressedResources() throws Exception {
        Assert.assertEquals(true, new WebXmlParameters(defaultValueExternalContext).isUseCompressedResources());
        Assert.assertEquals(false, new WebXmlParameters(overriddenValueExternalContext).isUseCompressedResources());
    }

    @Test
    public void testSortParameters() throws Exception {
        Assert.assertEquals(WebXmlParameters.DEFAULT_SORT_GLYPHICON, new WebXmlParameters(defaultValueExternalContext).getSortUnknownGlyphicon());
        Assert.assertEquals(OVERRIDDEN_SORT_UNKNOWN, new WebXmlParameters(overriddenValueExternalContext).getSortUnknownGlyphicon());

        Assert.assertEquals(WebXmlParameters.DEFAULT_SORT_ASC_GLYPHICON, new WebXmlParameters(defaultValueExternalContext).getSortAscGlyphicon());
        Assert.assertEquals(OVERRIDDEN_SORT_ASC, new WebXmlParameters(overriddenValueExternalContext).getSortAscGlyphicon());

        Assert.assertEquals(WebXmlParameters.DEFAULT_SORT_DESC_GLYPHICON, new WebXmlParameters(defaultValueExternalContext).getSortDescGlyphicon());
        Assert.assertEquals(OVERRIDDEN_SORT_DESC, new WebXmlParameters(overriddenValueExternalContext).getSortDescGlyphicon());
    }

    @Test
    public void testCollapsingParameters() throws Exception {
        Assert.assertEquals(WebXmlParameters.DEFAULT_COLLAPSING_GLYPHICON, new WebXmlParameters(defaultValueExternalContext).getCollapsingGlyphicon());
        Assert.assertEquals(OVERRIDDEN_COLLAPSING, new WebXmlParameters(overriddenValueExternalContext).getCollapsingGlyphicon());

        Assert.assertEquals(WebXmlParameters.DEFAULT_EXPANSION_GLYPHICON, new WebXmlParameters(defaultValueExternalContext).getExpansionGlyphicon());
        Assert.assertEquals(OVERRIDDEN_EXPANSION, new WebXmlParameters(overriddenValueExternalContext).getExpansionGlyphicon());
    }
}