package de.larmic.butterfaces.resolver;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class JsfAjaxRequestBuilderTest {

   @Test
   public void testCreateNewInstanceForSourceId() throws Exception {
      final JsfAjaxRequestBuilder request = new JsfAjaxRequestBuilder("mySourceId", true);
      assertThat(request.toString()).isEqualTo("jsf.ajax.request('mySourceId');");
   }

   @Test(expected = NullPointerException.class)
   public void testCreateNewInstanceThrowsNpeForSourceIsNull() throws Exception {
      new JsfAjaxRequestBuilder(null, false);
   }

   @Test
   public void testCreateNewInstanceForSourceElement() throws Exception {
      final JsfAjaxRequestBuilder request = new JsfAjaxRequestBuilder("mySourceElement", false);
      assertThat(request.toString()).isEqualTo("jsf.ajax.request(mySourceElement);");
   }

   @Test
   public void testSetDifferentParametersOnInstance() throws Exception {
      final JsfAjaxRequestBuilder request = new JsfAjaxRequestBuilder("mySourceElement", false);

      request.setEvent("onchange");
      assertThat(request.toString()).isEqualTo("jsf.ajax.request(mySourceElement, 'onchange');");

      request.setExecute("@this");
      assertThat(request.toString()).isEqualTo("jsf.ajax.request(mySourceElement, 'onchange', {execute: '@this'});");

      request.setRender("someId");
      assertThat(request.toString())
            .isEqualTo("jsf.ajax.request(mySourceElement, 'onchange', {execute: '@this', render: 'someId'});");

      request.addOnEventHandler("myEventHandler(data)");
      assertThat(request.toString())
            .isEqualTo("jsf.ajax.request(mySourceElement, 'onchange', {execute: '@this', render: 'someId'"
                  + ", onevent: function(data){myEventHandler(data);}"
                  + "});");

      request.addOnErrorHandler("myErrorHandler(data)");
      assertThat(request.toString())
            .isEqualTo("jsf.ajax.request(mySourceElement, 'onchange', {execute: '@this', render: 'someId'"
                  + ", onevent: function(data){myEventHandler(data);}"
                  + ", onerror: function(data){myErrorHandler(data);}"
                  + "});");

      request.setParams("myParams");
      assertThat(request.toString())
            .isEqualTo("jsf.ajax.request(mySourceElement, 'onchange', {execute: '@this', render: 'someId'"
                  + ", onevent: function(data){myEventHandler(data);}"
                  + ", onerror: function(data){myErrorHandler(data);}"
                  + ", params: 'myParams'"
                  + "});");
   }

   @Test
   public void testAddOnEventHandlerForDifferentFunctionCalls() throws Exception {
      final JsfAjaxRequestBuilder request = new JsfAjaxRequestBuilder("mySourceElement", false);
      request.addOnEventHandler("myEventHandlerAsCall(data)");
      request.addOnEventHandler("myEventHandlerAsVariable");

      assertThat(request.toString())
            .isEqualTo("jsf.ajax.request(mySourceElement, {"
                  + "onevent: function(data){myEventHandlerAsCall(data);myEventHandlerAsVariable(data);}"
                  + "});");
   }

   @Test
   public void testChaining() throws Exception {
      final String jsString = new JsfAjaxRequestBuilder("mySourceElement", false)
            .setEvent("onchange")
            .setExecute("@this")
            .setRender("@form")
            .toString();

      assertThat(jsString)
            .isEqualTo("jsf.ajax.request(mySourceElement, 'onchange', {execute: '@this', render: '@form'});");
   }
}