package org.butterfaces.resolver;

import org.butterfaces.util.StringUtils;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import java.util.Map;

public class AjaxRequestParameter {

    public static String findRequestParameter(final FacesContext context) {
        final ExternalContext externalContext = context.getExternalContext();
        final Map<String, String> requestParameterMap = externalContext.getRequestParameterMap();
        final String params = requestParameterMap.get("params");

        return StringUtils.isNotEmpty(params) ? params : requestParameterMap.get("butterfaces.params");
    }

}
