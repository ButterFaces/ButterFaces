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
    private static final String notExistingEvent = "thisEventIsNotExistingInComponentMock";
    private static final String existingButNotActiveEvent = "existingButNotActiveEvent";
    private static final String SOME_OTHER_RENDER_ID = "someOtherRenderId";
    private static final String SOME_RENDER_ID = "someRenderId";

    private final List<ClientBehavior> activeBehaviors = new ArrayList<>();
    private final List<ClientBehavior> activeBehaviorsWithRenderId = new ArrayList<>();
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

        final AjaxBehavior disabledBehavior = new AjaxBehavior();
        disabledBehavior.setDisabled(true);
        notActiveBehaviors.add(disabledBehavior);

        activeBehaviors.add(new AjaxBehavior());

        final AjaxBehavior renderBehavior = new AjaxBehavior();
        renderBehavior.setRender(Arrays.asList(SOME_RENDER_ID, SOME_OTHER_RENDER_ID));
        activeBehaviorsWithRenderId.add(renderBehavior);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFoundNoEventOnComponent() throws Exception {
        new AjaxRequest(componentMock, notExistingEvent);
    }

    @Test(expected = IllegalStateException.class)
    public void testFoundNotActiveEvent() throws Exception {
        new AjaxRequest(componentMock, existingButNotActiveEvent);
    }

    @Test
    public void testCreateJavaScriptCall() throws Exception {
        final AjaxRequest ajaxRequest = new AjaxRequest(componentMock, existingEvent);
        Assert.assertNotNull(ajaxRequest);

        final String customEvent = "customEventName";

        Assert.assertEquals(createExpectedJSCall(existingEvent), ajaxRequest.createJavaScriptCall());
        Assert.assertEquals(createExpectedJSCall(customEvent), ajaxRequest.createJavaScriptCall(customEvent));
    }


    @Test
    public void testCreateJavaScriptCallWithRenderAttribute() throws Exception {
        final AjaxRequest ajaxRequest = new AjaxRequest(componentMock, existingEventWithRenderId);
        Assert.assertNotNull(ajaxRequest);

        Assert.assertEquals(createExpectedJSCall(existingEventWithRenderId, SOME_RENDER_ID + " " + SOME_OTHER_RENDER_ID), ajaxRequest.createJavaScriptCall());
    }

    private String createExpectedJSCall(final String event) {
        return this.createExpectedJSCall(event, null);
    }

    private String createExpectedJSCall(final String event, final String render) {
        if (StringUtils.isEmpty(render)) {
            return "jsf.ajax.request('null','" + event + "',{'javax.faces.behavior.event':'" + event + "'});";
        }

        return "jsf.ajax.request('null','" + event + "',{render: '" + render + "', 'javax.faces.behavior.event':'" + event + "'});";
    }
}