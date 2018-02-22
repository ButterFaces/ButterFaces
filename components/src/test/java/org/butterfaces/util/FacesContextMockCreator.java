package org.butterfaces.util;

import org.butterfaces.component.renderkit.html_basic.TooltipRenderer;

import javax.faces.application.Application;
import javax.faces.component.UIComponent;
import javax.faces.component.UIViewRoot;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.render.RenderKit;
import java.lang.reflect.Method;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class FacesContextMockCreator {

    // key used to look up current component stack if FacesContext attributes
    private static final String _CURRENT_COMPONENT_STACK_KEY = "javax.faces.component.CURRENT_COMPONENT_STACK";

    public static FacesContext createFacesContextCurrentInstance(UIComponent... elStackComponents) {
        final FacesContext facesContextMock = mock(FacesContext.class);
        final RenderKit renderKitMock = createRenderKitMock(facesContextMock, elStackComponents);

        when(facesContextMock.getExternalContext()).thenReturn(mock(ExternalContext.class));
        when(facesContextMock.getApplication()).thenReturn(mock(Application.class));
        when(facesContextMock.getRenderKit()).thenReturn(renderKitMock);
        when(facesContextMock.getResponseWriter()).thenReturn(mock(ResponseWriter.class));
        when(facesContextMock.getViewRoot()).thenReturn(new UIViewRoot()); // mock is no support, because can not stub createUniqueId()
        when(facesContextMock.getClientIdsWithMessages()).thenReturn(Collections.<String>emptyIterator());

        try {
            final Method m = FacesContext.class.getDeclaredMethod("setCurrentInstance", FacesContext.class);
            m.setAccessible(true); //if security settings allow this
            m.invoke(null, facesContextMock);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return facesContextMock;
    }

    private static RenderKit createRenderKitMock(FacesContext facesContextMock, UIComponent... elStackComponents) {
        final RenderKit renderKitMock = mock(RenderKit.class);

        // needed if component is encoded
        final ArrayDeque<UIComponent> componentELStack = new ArrayDeque<>(Arrays.asList(elStackComponents));

        final HashMap<Object, Object> contextAttributes = new HashMap<>();
        contextAttributes.put(_CURRENT_COMPONENT_STACK_KEY, componentELStack);
        when(facesContextMock.getAttributes()).thenReturn(contextAttributes);

        // handle renderer
        when(renderKitMock.getRenderer("org.butterfaces.component.family", "org.butterfaces.component.renderkit.html_basic.TooltipRenderer"))
                .thenReturn(new TooltipRenderer());
        // add additional renderer if needed (or scan them?)

        return renderKitMock;
    }
}
