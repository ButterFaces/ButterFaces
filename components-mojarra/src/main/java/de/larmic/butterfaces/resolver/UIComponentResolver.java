package de.larmic.butterfaces.resolver;

import com.sun.faces.component.visit.FullVisitContext;

import javax.faces.component.UIComponent;
import javax.faces.component.UIViewRoot;
import javax.faces.component.visit.VisitCallback;
import javax.faces.component.visit.VisitContext;
import javax.faces.component.visit.VisitResult;
import javax.faces.context.FacesContext;

/**
 * Created by larmic on 25.12.14.
 */
public class UIComponentResolver {

    public <T extends UIComponent> T findComponent(final String id, final Class<T> componentClass) {

        FacesContext context = FacesContext.getCurrentInstance();
        UIViewRoot root = context.getViewRoot();

        final UIComponent[] found = new UIComponent[1];

        root.visitTree(new FullVisitContext(context), new VisitCallback() {
            @Override
            public VisitResult visit(VisitContext context, UIComponent component) {
                if(id.equals(component.getId()) && componentClass.equals(component.getClass())){
                    found[0] = component;
                    return VisitResult.COMPLETE;
                }
                return VisitResult.ACCEPT;
            }
        });

        return (T) found[0];

    }

}
