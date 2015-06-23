package de.larmic.butterfaces.event;

import org.junit.Test;

import javax.faces.component.UIComponent;
import javax.faces.component.UIOutput;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

public class ResourceComparatorTest {

    public static final String RENDERER_TYPE_JS = "javax.faces.resource.Script";
    public static final String RENDERER_TYPE_CSS = "javax.faces.resource.Stylesheet";

    @Test
    public void testCompare() {
        List<UIOutput> resources = new ArrayList<>();
        resources.add(createCSSResource("butterfaces-css", "butterfaces-default.css"));
        resources.add(createJSResource("butterfaces-js", "butterfaces-fixed.js"));
        resources.add(createCSSResource("butterfaces-configurable", "bootstrap.min.css"));
        resources.add(createJSResource("butterfaces-js", "butterfaces-expandable.jquery.js"));
        resources.add(createJSResource("butterfaces-configurable", "bootstrap.min.js"));
        resources.add(createJSResource("butterfaces-js", "butterfaces-01-baseClass.js"));

        Collections.sort(resources, new ResourceComparator());

        assertEquals("CSS files have to be rendered first", RENDERER_TYPE_CSS, resources.get(0).getRendererType());
        assertEquals("CSS files have to be rendered first", RENDERER_TYPE_CSS, resources.get(1).getRendererType());
        assertTrue("base class script has to be rendered as first JS", isBaseClassScript(resources.get(2)));
    }

    private UIOutput createJSResource(String library, String name) {
        return createResource(library, name, RENDERER_TYPE_JS);
    }

    private UIOutput createCSSResource(String library, String name) {
        return createResource(library, name, RENDERER_TYPE_CSS);
    }

    private UIOutput createResource(String library, String name, String rendererType) {
        final UIOutput resource = new UIOutput();
        resource.getAttributes().put("library", library);
        resource.getAttributes().put("name", name);
        resource.setRendererType(rendererType);
        return resource;
    }

    private boolean isBaseClassScript(UIComponent o) {
        return ((String) o.getAttributes().get("name")).contains("baseClass");
    }
}
