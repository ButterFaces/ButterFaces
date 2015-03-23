package de.larmic.butterfaces.resolver;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import javax.faces.component.UIComponentBase;
import javax.faces.component.behavior.AjaxBehavior;
import javax.faces.component.behavior.ClientBehavior;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class AjaxRequestTest {

    private static final String notExistingEvent = "thisEventIsNotExistingInComponentMock";
    private static final String existingButNotActiveEvent = "existingButNotActiveEvent";

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

        final AjaxBehavior disabledBehavior = new AjaxBehavior();
        disabledBehavior.setDisabled(true);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFoundNoEventOnComponent() throws Exception {
        new AjaxRequest(componentMock, notExistingEvent);
    }

//    @Test(expected = IllegalAccessException.class)
//    public void testFoundNotActiveEvent() throws Exception {
//        new AjaxRequest(componentMock, existingButNotActiveEvent);
//    }
}