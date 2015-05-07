package de.larmic.butterfaces.event;

import javax.faces.component.UIComponent;
import java.util.Comparator;

public class ResourceComparator implements Comparator<UIComponent> {

    public static final String RENDERER_TYPE_JS = "javax.faces.resource.Script";
    public static final String RENDERER_TYPE_CSS = "javax.faces.resource.Stylesheet";

    @Override
    public int compare(UIComponent o1, UIComponent o2) {
        if (RENDERER_TYPE_CSS.equals(o1.getRendererType()) && RENDERER_TYPE_JS.equals(o2.getRendererType())) {
            return -1;
        } else if (RENDERER_TYPE_JS.equals(o1.getRendererType()) && RENDERER_TYPE_CSS.equals(o2.getRendererType())) {
            return 1;
        } else {
            return compareScripts(o1, o2);
        }
    }

    private int compareScripts(UIComponent o1, UIComponent o2) {
        if (isBaseClassScript(o1)) {
            return -1;
        } else if (isBaseClassScript(o2)) {
            return 1;
        } else {
            return 0;
        }
    }

    private boolean isBaseClassScript(UIComponent o) {
        return ((String) o.getAttributes().get("name")).contains("baseClass");
    }
}
