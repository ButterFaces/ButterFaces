package de.larmic.butterfaces.component.html.ajax;

import de.larmic.butterfaces.component.behavior.JsfAjaxRequest;
import org.junit.Test;

import javax.faces.component.UIComponentBase;
import javax.faces.component.behavior.AjaxBehavior;
import javax.faces.component.behavior.ClientBehavior;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class JsfAjaxRequestTest {

    @Test
    public void testCreateNewInstanceForSourceId() throws Exception {
        final JsfAjaxRequest request = new JsfAjaxRequest("mySourceId", true);
        assertThat(request.toString()).isEqualTo("jsf.ajax.request('mySourceId');");
    }

    @Test(expected = NullPointerException.class)
    public void testCreateNewInstanceThrowsNpeForSourceIsNull() throws Exception {
        new JsfAjaxRequest(null, false);
    }

    @Test
    public void testCreateNewInstanceForSourceElement() throws Exception {
        final JsfAjaxRequest request = new JsfAjaxRequest("mySourceElement", false);
        assertThat(request.toString()).isEqualTo("jsf.ajax.request(mySourceElement);");
    }

    @Test
    public void testSetMultipleRender() throws Exception {
        final JsfAjaxRequest request = new JsfAjaxRequest("mySourceElement", false);
        request.setRender("someId someOtherId");

        assertThat(request.toString())
                .isEqualTo("jsf.ajax.request(mySourceElement, {render: 'someId someOtherId'});");
    }

    @Test
    public void testAddRender() throws Exception {
        final JsfAjaxRequest request = new JsfAjaxRequest("mySourceElement", false);
        request.setRender("someId");

        assertThat(request.toString()).isEqualTo("jsf.ajax.request(mySourceElement, {render: 'someId'});");

        request.addRender("someOtherId");

        assertThat(request.toString()).isEqualTo("jsf.ajax.request(mySourceElement, {render: 'someId someOtherId'});");
    }

    @Test
    public void testSetResetValues() throws Exception {
        final JsfAjaxRequest request = new JsfAjaxRequest("mySourceElement", false);
        request.setResetValues(true);

        assertThat(request.toString()).isEqualTo("jsf.ajax.request(mySourceElement, {resetValues: 'true'});");

        request.setResetValues(false);

        assertThat(request.toString()).isEqualTo("jsf.ajax.request(mySourceElement);");
    }

    @Test
    public void testDelay() throws Exception {
        final JsfAjaxRequest request = new JsfAjaxRequest("mySourceElement", false);
        request.setDelay("200");

        assertThat(request.toString()).isEqualTo("jsf.ajax.request(mySourceElement, {delay: '200'});");

        request.setDelay(null);

        assertThat(request.toString()).isEqualTo("jsf.ajax.request(mySourceElement);");
    }

    @Test
    public void testSetDifferentParametersOnInstance() throws Exception {
        final JsfAjaxRequest request = new JsfAjaxRequest("mySourceElement", false);

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
                        + ", params: myParams"
                        + "});");

        request.setBehaviorEvent("myBehaviorEvent");
        assertThat(request.toString())
                .isEqualTo("jsf.ajax.request(mySourceElement, 'onchange', {execute: '@this', render: 'someId'"
                        + ", onevent: function(data){myEventHandler(data);}"
                        + ", onerror: function(data){myErrorHandler(data);}"
                        + ", params: myParams"
                        + ", 'javax.faces.behavior.event': 'myBehaviorEvent'"
                        + "});");
    }

    @Test
    public void testAddOnEventHandlerForDifferentFunctionCalls() throws Exception {
        final JsfAjaxRequest request = new JsfAjaxRequest("mySourceElement", false);
        request.addOnEventHandler("myEventHandlerAsCall(data)");
        request.addOnEventHandler("myEventHandlerAsVariable");

        assertThat(request.toString())
                .isEqualTo("jsf.ajax.request(mySourceElement, {"
                        + "onevent: function(data){myEventHandlerAsCall(data);myEventHandlerAsVariable(data);}"
                        + "});");
    }

    @Test
    public void testChaining() throws Exception {
        final String jsString = new JsfAjaxRequest("mySourceElement", false)
                .setEvent("onchange")
                .setExecute("@this")
                .setRender("@form")
                .toString();

        assertThat(jsString)
                .isEqualTo("jsf.ajax.request(mySourceElement, 'onchange', {execute: '@this', render: '@form'});");
    }

    @Test
    public void testRenderByComponentEvent() throws Exception {
        final Map<String, List<ClientBehavior>> behaviors = new HashMap<>();
        behaviors.put("click", Arrays.<ClientBehavior>asList(
                createAjaxBehavior(true, "disabled"),
                createAjaxBehavior(false, "@all"),
                createAjaxBehavior(false, "@none"),
                createAjaxBehavior(false, "@this"),
                createAjaxBehavior(false, "@form"),
                createAjaxBehavior(false, "enabled")));

        final UIComponentBase uiComponentMock = mock(UIComponentBase.class);
        when(uiComponentMock.getClientBehaviors()).thenReturn(behaviors);

        final JsfAjaxRequest requestBuilder = new JsfAjaxRequest("mySourceElement", false);

        assertThat(requestBuilder.setRender(uiComponentMock, "toggle").toString())
                .isEqualTo("jsf.ajax.request(mySourceElement);");
        assertThat(requestBuilder.setRender(uiComponentMock, "click").toString())
                .isEqualTo("jsf.ajax.request(mySourceElement, {render: '@all @none @this @form enabled'});");
    }

    private AjaxBehavior createAjaxBehavior(boolean disabled, String... rerenderIds) {
        final AjaxBehavior disabledClickBehaviour = new AjaxBehavior();
        disabledClickBehaviour.setDisabled(disabled);
        disabledClickBehaviour.setRender(Arrays.asList(rerenderIds));
        return disabledClickBehaviour;
    }

}