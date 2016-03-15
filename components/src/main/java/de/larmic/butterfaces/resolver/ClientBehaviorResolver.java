package de.larmic.butterfaces.resolver;

import javax.faces.component.UIComponentBase;
import javax.faces.component.behavior.AjaxBehavior;
import javax.faces.component.behavior.ClientBehavior;
import java.util.List;
import java.util.Map;

public class ClientBehaviorResolver {

    public static AjaxBehavior findFirstActiveAjaxBehavior(final UIComponentBase component, final String eventName) {
        if (component != null) {
            final List<ClientBehavior> behaviors = component.getClientBehaviors().get(eventName);
            if (behaviors != null) {
                for (ClientBehavior behavior : behaviors) {
                    if (behavior instanceof AjaxBehavior && !((AjaxBehavior) behavior).isDisabled()) {
                        return (AjaxBehavior) behavior;
                    }
                }
            }
        }

        return null;
    }

    /**
     * @deprecated use {@link ClientBehaviorResolver#findFirstActiveAjaxBehavior(UIComponentBase, String)} instead
     */
    @Deprecated
    public static AjaxBehavior resolveActiveAjaxBehavior(final UIComponentBase component, final String event) {
        return findAjaxBehavior(component.getClientBehaviors(), event);
    }

    private static AjaxBehavior findAjaxBehavior(Map<String, List<ClientBehavior>> behaviors, String event) {
        if (behaviors.containsKey(event)) {
            final ClientBehavior clientBehavior = behaviors.get(event).get(0);

            if (clientBehavior instanceof AjaxBehavior && !((AjaxBehavior) clientBehavior).isDisabled()) {
                return ((AjaxBehavior) clientBehavior);
            }
        }

        return null;
    }
}
