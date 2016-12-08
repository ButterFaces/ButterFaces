package de.larmic.butterfaces.component.renderkit.html_basic.text;

import de.larmic.butterfaces.component.html.text.HtmlTreeBox;
import de.larmic.butterfaces.resolver.WebXmlParameters;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.assertEquals;
import static org.mockito.BDDMockito.given;

public class TreeBoxRendererShowClearButtonTest {

   private TreeBoxRenderer renderer;

   @Mock
   private WebXmlParameters webXmlParametersMock;

   @Mock
   private HtmlTreeBox htmlTreeBoxMock;

   @Before
   public void init() {
      renderer = new TreeBoxRenderer();
      MockitoAnnotations.initMocks(this);
   }

   @Test
   public void evaluateShowClearButtonValueForValueOnComponentIsNullAndContextValueIsFalse() throws Exception {
      given(htmlTreeBoxMock.getShowClearButton()).willReturn(null);
      given(webXmlParametersMock.isShowTreeBoxClearButton()).willReturn(false);

      assertEquals(false, renderer.evaluateShowClearButtonValue(htmlTreeBoxMock, webXmlParametersMock));
   }

   @Test
   public void evaluateShowClearButtonValueForValueOnComponentIsNullAndContextValueIsTrue() throws Exception {
      given(htmlTreeBoxMock.getShowClearButton()).willReturn(null);
      given(webXmlParametersMock.isShowTreeBoxClearButton()).willReturn(true);

      assertEquals(true, renderer.evaluateShowClearButtonValue(htmlTreeBoxMock, webXmlParametersMock));
   }

   @Test
   public void evaluateShowClearButtonValueForValueOnComponentIsFalseAndContextValueIsFalse() throws Exception {
      given(htmlTreeBoxMock.getShowClearButton()).willReturn(Boolean.FALSE);
      given(webXmlParametersMock.isShowTreeBoxClearButton()).willReturn(false);

      assertEquals(false, renderer.evaluateShowClearButtonValue(htmlTreeBoxMock, webXmlParametersMock));
   }

   @Test
   public void evaluateShowClearButtonValueForValueOnComponentIsTrueAndContextValueIsFalse() throws Exception {
      given(htmlTreeBoxMock.getShowClearButton()).willReturn(Boolean.TRUE);
      given(webXmlParametersMock.isShowTreeBoxClearButton()).willReturn(false);

      assertEquals(true, renderer.evaluateShowClearButtonValue(htmlTreeBoxMock, webXmlParametersMock));
   }

   @Test
   public void evaluateShowClearButtonValueForValueOnComponentIsFalseAndContextValueIsTrue() throws Exception {
      given(htmlTreeBoxMock.getShowClearButton()).willReturn(Boolean.FALSE);
      given(webXmlParametersMock.isShowTreeBoxClearButton()).willReturn(true);

      assertEquals(false, renderer.evaluateShowClearButtonValue(htmlTreeBoxMock, webXmlParametersMock));
   }

   @Test
   public void evaluateShowClearButtonValueForValueOnComponentIsTrueAndContextValueIsTrue() throws Exception {
      given(htmlTreeBoxMock.getShowClearButton()).willReturn(Boolean.TRUE);
      given(webXmlParametersMock.isShowTreeBoxClearButton()).willReturn(true);

      assertEquals(true, renderer.evaluateShowClearButtonValue(htmlTreeBoxMock, webXmlParametersMock));
   }
}