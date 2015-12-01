package de.larmic.butterfaces.resolver;

import javax.faces.component.UIComponent;
import javax.faces.component.UIComponentBase;
import javax.faces.component.behavior.AjaxBehavior;
import javax.faces.component.behavior.ClientBehavior;
import javax.faces.component.behavior.ClientBehaviorHolder;
import java.util.List;
import java.util.Map;

public class ClientBehaviorResolver {

    public static AjaxBehavior resolveActiveAjaxBehavior(final UIComponent component, final String event) {
        if (component instanceof ClientBehaviorHolder) {
            return findAjaxBehavior(((ClientBehaviorHolder) component).getClientBehaviors(), event);
        }

        return null;
    }

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
