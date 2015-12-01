package de.larmic.butterfaces.resolver;

import org.junit.Before;
import org.junit.Test;

import javax.faces.component.UIComponentBase;
import javax.faces.component.behavior.AjaxBehavior;
import javax.faces.component.behavior.ClientBehavior;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.MockitoAnnotations.initMocks;

public class ClientBehaviorResolverTest {

    private static final String existingEvent = "existingEvent";
    private static final String notExistingEvent = "thisEventIsNotExistingInComponentMock";
    private static final String existingButNotActiveEvent = "existingButNotActiveEvent";

    private final List<ClientBehavior> activeBehaviors = new ArrayList<>();
    private final List<ClientBehavior> notActiveBehaviors = new ArrayList<>();

    private UIComponentBase componentMock;

    private Map<String, List<ClientBehavior>> clientBehaviorsMock;

    @Before
    public void setUp() throws Exception {
        initMocks(this);

        clientBehaviorsMock = new HashMap<>();
        clientBehaviorsMock.put(existingButNotActiveEvent, notActiveBehaviors);
        clientBehaviorsMock.put(existingEvent, activeBehaviors);

        componentMock = new UIComponentBase() {

            @Override
            public Map<String, List<ClientBehavior>> getClientBehaviors() {
                return clientBehaviorsMock;
            }

            @Override
            public String getFamily() {
                return null;
            }
        };

        final AjaxBehavior disabledBehavior = new AjaxBehavior();
        disabledBehavior.setDisabled(true);
        notActiveBehaviors.add(disabledBehavior);

        activeBehaviors.add(new AjaxBehavior());
    }

    @Test
    public void testResolveActiveAjaxBehavior() throws Exception {
        assertThat(ClientBehaviorResolver.resolveActiveAjaxBehavior(componentMock, existingEvent)).isNotNull();
        assertThat(ClientBehaviorResolver.resolveActiveAjaxBehavior(componentMock, notExistingEvent)).isNull();
        assertThat(ClientBehaviorResolver.resolveActiveAjaxBehavior(componentMock, existingButNotActiveEvent)).isNull();
    }
}