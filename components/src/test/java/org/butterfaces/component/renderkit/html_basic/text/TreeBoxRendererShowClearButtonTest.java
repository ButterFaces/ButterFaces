package org.butterfaces.component.renderkit.html_basic.text;

import org.butterfaces.component.html.text.HtmlTreeBox;
import org.butterfaces.resolver.WebXmlParameters;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

public class TreeBoxRendererShowClearButtonTest {

    private TreeBoxRenderer renderer;

    @Mock
    private WebXmlParameters webXmlParametersMock;

    @Mock
    private HtmlTreeBox htmlTreeBoxMock;

    @BeforeEach
    public void init() {
        renderer = new TreeBoxRenderer();
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void evaluateShowClearButtonValueForValueOnComponentIsNullAndContextValueIsFalse() {
        given(htmlTreeBoxMock.getShowClearButton()).willReturn(null);
        given(webXmlParametersMock.isShowTreeBoxClearButton()).willReturn(false);

        assertThat(renderer.evaluateShowClearButtonValue(htmlTreeBoxMock, webXmlParametersMock)).isFalse();
    }

    @Test
    void evaluateShowClearButtonValueForValueOnComponentIsNullAndContextValueIsTrue() {
        given(htmlTreeBoxMock.getShowClearButton()).willReturn(null);
        given(webXmlParametersMock.isShowTreeBoxClearButton()).willReturn(true);

        assertThat(renderer.evaluateShowClearButtonValue(htmlTreeBoxMock, webXmlParametersMock)).isTrue();
    }

    @Test
    void evaluateShowClearButtonValueForValueOnComponentIsFalseAndContextValueIsFalse() {
        given(htmlTreeBoxMock.getShowClearButton()).willReturn(Boolean.FALSE);
        given(webXmlParametersMock.isShowTreeBoxClearButton()).willReturn(false);

        assertThat(renderer.evaluateShowClearButtonValue(htmlTreeBoxMock, webXmlParametersMock)).isFalse();
    }

    @Test
    void evaluateShowClearButtonValueForValueOnComponentIsTrueAndContextValueIsFalse() {
        given(htmlTreeBoxMock.getShowClearButton()).willReturn(Boolean.TRUE);
        given(webXmlParametersMock.isShowTreeBoxClearButton()).willReturn(false);

        assertThat(renderer.evaluateShowClearButtonValue(htmlTreeBoxMock, webXmlParametersMock)).isTrue();
    }

    @Test
    void evaluateShowClearButtonValueForValueOnComponentIsFalseAndContextValueIsTrue() {
        given(htmlTreeBoxMock.getShowClearButton()).willReturn(Boolean.FALSE);
        given(webXmlParametersMock.isShowTreeBoxClearButton()).willReturn(true);

        assertThat(renderer.evaluateShowClearButtonValue(htmlTreeBoxMock, webXmlParametersMock)).isFalse();
    }

    @Test
    void evaluateShowClearButtonValueForValueOnComponentIsTrueAndContextValueIsTrue() {
        given(htmlTreeBoxMock.getShowClearButton()).willReturn(Boolean.TRUE);
        given(webXmlParametersMock.isShowTreeBoxClearButton()).willReturn(true);

        assertThat(renderer.evaluateShowClearButtonValue(htmlTreeBoxMock, webXmlParametersMock)).isTrue();
    }
}