package de.larmic.butterfaces.resolver;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import javax.faces.context.ExternalContext;

import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class WebXmlParametersTest {

    public static final String OVERRIDDEN_REFRESH = "o_refresh";
    public static final String OVERRIDDEN_OPTIONS = "o_options";

    @Mock
    private ExternalContext defaultValueExternalContext;

    @Mock
    private ExternalContext overriddenValueExternalContext;

    @Before
    public void setUp() throws Exception {
        initMocks(this);

        when(overriddenValueExternalContext.getInitParameter(WebXmlParameters.CTX_PARAM_REFRESH_GLYPHICON)).thenReturn(OVERRIDDEN_REFRESH);
        when(overriddenValueExternalContext.getInitParameter(WebXmlParameters.CTX_PARAM_OPTIONS_GLYPHICON)).thenReturn(OVERRIDDEN_OPTIONS);
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
}