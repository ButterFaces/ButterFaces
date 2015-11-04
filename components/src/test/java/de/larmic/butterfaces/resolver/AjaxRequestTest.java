package de.larmic.butterfaces.resolver;

import de.larmic.butterfaces.component.partrenderer.StringUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import javax.faces.component.UIComponentBase;
import javax.faces.component.behavior.AjaxBehavior;
import javax.faces.component.behavior.ClientBehavior;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class AjaxRequestTest {

    private static final String existingEvent = "existingEvent";
    private static final String existingEventWithRenderId = "existingEventWithRenderId";
    private static final String existingEventWithOnEvent = "existingEventWithOnEvent";
    private static final String existingEventWithOnEventAndRenderId = "existingEventWithOnEventAndRenderId";
    private static final String notExistingEvent = "thisEventIsNotExistingInComponentMock";
    private static final String existingButNotActiveEvent = "existingButNotActiveEvent";
    private static final String SOME_OTHER_RENDER_ID = "someOtherRenderId";
    private static final String SOME_RENDER_ID = "someRenderId";
    private static final String SOME_EVENT = "someEvent";

    private final List<ClientBehavior> activeBehaviors = new ArrayList<>();
    private final List<ClientBehavior> activeBehaviorsWithRenderId = new ArrayList<>();
    private final List<ClientBehavior> activeBehaviorsWithOnEvent = new ArrayList<>();
    private final List<ClientBehavior> activeBehaviorsWithOnEventAndRenderId = new ArrayList<>();
    private final List<ClientBehavior> notActiveBehaviors = new ArrayList<>();

    @Mock
    private UIComponentBase componentMock;

    @Mock
    private Map<String, List<ClientBehavior>> clientBehaviorsMock;

    @Before
    public void setUp() throws Exception {
        initMocks(this);

        when(componentMock.getClientBehaviors()).thenReturn(clientBehaviorsMock);
        when(clientBehaviorsMock.get(existingButNotActiveEvent)).thenReturn(notActiveBehaviors);
        when(clientBehaviorsMock.get(existingEvent)).thenReturn(activeBehaviors);
        when(clientBehaviorsMock.get(existingEventWithRenderId)).thenReturn(activeBehaviorsWithRenderId);
        when(clientBehaviorsMock.get(existingEventWithOnEvent)).thenReturn(activeBehaviorsWithOnEvent);
        when(clientBehaviorsMock.get(existingEventWithOnEventAndRenderId)).thenReturn(activeBehaviorsWithOnEventAndRenderId);

        final AjaxBehavior disabledBehavior = new AjaxBehavior();
        disabledBehavior.setDisabled(true);
        notActiveBehaviors.add(disabledBehavior);

        activeBehaviors.add(new AjaxBehavior());

        final AjaxBehavior renderBehavior = new AjaxBehavior();
        renderBehavior.setRender(Arrays.asList(SOME_RENDER_ID, SOME_OTHER_RENDER_ID));
        activeBehaviorsWithRenderId.add(renderBehavior);

        final AjaxBehavior onEventBehavior = new AjaxBehavior();
        onEventBehavior.setOnevent(SOME_EVENT);
        activeBehaviorsWithOnEvent.add(onEventBehavior);

        final AjaxBehavior onEventAndRenderBehavior = new AjaxBehavior();
        onEventAndRenderBehavior.setOnevent(SOME_EVENT);
        onEventAndRenderBehavior.setRender(Arrays.asList(SOME_RENDER_ID, SOME_OTHER_RENDER_ID));
        activeBehaviorsWithOnEventAndRenderId.add(onEventAndRenderBehavior);
    }

    @Test
    public void testCreateJavaScriptCall() throws Exception {
        final AjaxRequest ajaxRequest = new AjaxRequest(componentMock, existingEvent);
        final String customEvent = "customEventName";

        Assert.assertEquals(createExpectedJSCall(existingEvent, null, null), ajaxRequest.createJavaScriptCall());
        Assert.assertEquals(createExpectedJSCall(customEvent, null, null), ajaxRequest.createJavaScriptCall(customEvent));
    }

    @Test
    public void testCreateJavaScriptCallWithRenderAttribute() throws Exception {
        final AjaxRequest ajaxRequest = new AjaxRequest(componentMock, existingEventWithRenderId);
        Assert.assertEquals(createExpectedJSCall(existingEventWithRenderId, SOME_RENDER_ID + " " + SOME_OTHER_RENDER_ID, null), ajaxRequest.createJavaScriptCall());
    }

    @Test
    public void testCreateJavaScriptCallWithOnEventAttribute() throws Exception {
        final AjaxRequest ajaxRequest = new AjaxRequest(componentMock, existingEventWithOnEvent, SOME_EVENT);
        Assert.assertEquals(createExpectedJSCall(existingEventWithOnEvent, null, SOME_EVENT), ajaxRequest.createJavaScriptCall());
    }

    private String createExpectedJSCall(final String event, final String render, final String onEvent) {
        if (StringUtils.isEmpty(render) && StringUtils.isEmpty(onEvent)) {
            return "jsf.ajax.request('null','" + event + "',{'javax.faces.behavior.event':'" + event + "'});";
        } else if (StringUtils.isEmpty(render)) {
            return "jsf.ajax.request('null','" + event + "',{onevent: " + onEvent + ", 'javax.faces.behavior.event':'" + event + "'});";
        } else if (StringUtils.isEmpty(onEvent)) {
            return "jsf.ajax.request('null','" + event + "',{render: '" + render + "', 'javax.faces.behavior.event':'" + event + "'});";
        }

        return "jsf.ajax.request('null','" + event + "',{render: '" + render + "', onevent: " + onEvent + ", 'javax.faces.behavior.event':'" + event + "'});";
    }
}