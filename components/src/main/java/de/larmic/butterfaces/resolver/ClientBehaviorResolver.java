package de.larmic.butterfaces.resolver;

import javax.faces.component.UIComponentBase;
import javax.faces.component.behavior.AjaxBehavior;
import javax.faces.component.behavior.ClientBehavior;
import java.util.List;
import java.util.Map;

public class ClientBehaviorResolver {

    public static AjaxBehavior resolveActiveAjaxBehavior(final UIComponentBase component, final String event) {
        final Map<String, List<ClientBehavior>> behaviors = component.getClientBehaviors();
        if (behaviors.containsKey(event)) {
            final ClientBehavior clientBehavior = behaviors.get(event).get(0);

            if (clientBehavior instanceof AjaxBehavior && !((AjaxBehavior) clientBehavior).isDisabled()) {
                return ((AjaxBehavior) clientBehavior);
            }
        }

        return null;
    }
}
