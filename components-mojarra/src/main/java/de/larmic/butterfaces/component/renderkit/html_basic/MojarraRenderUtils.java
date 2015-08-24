/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 1997-2012 Oracle and/or its affiliates. All rights reserved.
 *
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License.  You can
 * obtain a copy of the License at
 * https://glassfish.dev.java.net/public/CDDL+GPL_1_1.html
 * or packager/legal/LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 *
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at packager/legal/LICENSE.txt.
 *
 * GPL Classpath Exception:
 * Oracle designates this particular file as subject to the "Classpath"
 * exception as provided by Oracle in the GPL Version 2 section of the License
 * file that accompanied this code.
 *
 * Modifications:
 * If applicable, add the following below the License Header, with the fields
 * enclosed by brackets [] replaced by your own identifying information:
 * "Portions Copyright [year] [name of copyright owner]"
 *
 * Contributor(s):
 * If you wish your version of this file to be governed by only the CDDL or
 * only the GPL Version 2, indicate your decision by adding "[Contributor]
 * elects to include this software in this distribution under the [CDDL or GPL
 * Version 2] license."  If you don't indicate a single choice of license, a
 * recipient has the option to distribute your version of this file under
 * either the CDDL, the GPL Version 2 or to extend the choice of license to
 * its licensees as provided above.  However, if you add GPL Version 2 code
 * and therefore, elected the GPL Version 2 license, then the option applies
 * only if the new code is made subject to such option by the copyright
 * holder.
 */

/*
 * Copied from com.sun.faces.renderkit.RenderKitUtils to release a myfaces version of butterfaces input components.
 * Mojarra dependencies are removed.
 **/

package de.larmic.butterfaces.component.renderkit.html_basic;

import javax.faces.component.ActionSource;
import javax.faces.component.ActionSource2;
import javax.faces.component.UIComponent;
import javax.faces.component.UIForm;
import javax.faces.component.behavior.ClientBehavior;
import javax.faces.component.behavior.ClientBehaviorContext;
import javax.faces.component.behavior.ClientBehaviorHint;
import javax.faces.component.behavior.ClientBehaviorHolder;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.render.RenderKit;
import java.io.IOException;
import java.util.*;

/**
 * <p>A set of utilities for use in {@link RenderKit}s.</p>
 */
public class MojarraRenderUtils {

    public static final String XHTML_CONTENT_TYPE = "application/xhtml+xml";

    /**
     * <p>The prefix to append to certain attributes when renderking
     * <code>XHTML Transitional</code> content.
     */
    private static final String XHTML_ATTR_PREFIX = "xml:";

    /**
     * <p>An array of attributes that must be prefixed by
     * {@link #XHTML_ATTR_PREFIX} when rendering
     * <code>XHTML Transitional</code> content.
     */
    private static final String[] XHTML_PREFIX_ATTRIBUTES = {
            "lang"
    };

    private MojarraRenderUtils() {
    }

    public static void renderPassThruAttributes(ResponseWriter writer,
                                                UIComponent component,
                                                String attributeName,
                                                String eventName) throws IOException {
        final FacesContext context = FacesContext.getCurrentInstance();

        Map<String, List<ClientBehavior>> behaviors = Collections.emptyMap();

        if (component instanceof ClientBehaviorHolder) {
            behaviors = ((ClientBehaviorHolder) component).getClientBehaviors();
        }

        renderPassThruAttributesUnoptimized(context, writer, component, attributeName, eventName, behaviors);
    }

    public static String prefixAttribute(final String attrName,
                                         boolean isXhtml) {
        if (isXhtml) {
            if (Arrays.binarySearch(XHTML_PREFIX_ATTRIBUTES, attrName) > -1) {
                return XHTML_ATTR_PREFIX + attrName;
            } else {
                return attrName;
            }
        } else {
            return attrName;
        }

    }

    // --------------------------------------------------------- Private Methods

    /**
     * <p>Loops over all known attributes and attempts to render each one.
     *
     * @param context   the {@link FacesContext} of the current request
     * @param writer    the current writer
     * @param component the component whose attributes we're rendering
     * @param behaviors the non-null behaviors map for this request.
     * @throws IOException if an error occurs during the write
     */
    private static void renderPassThruAttributesUnoptimized(FacesContext context,
                                                            ResponseWriter writer,
                                                            UIComponent component,
                                                            String attributeName,
                                                            String eventName,
                                                            Map<String, List<ClientBehavior>> behaviors) throws IOException {
        boolean isXhtml = XHTML_CONTENT_TYPE.equals(writer.getContentType());

        Map<String, Object> attrMap = component.getAttributes();

        boolean hasBehavior = ((eventName != null) && (behaviors.containsKey(eventName)));

        Object value = attrMap.get(attributeName);

        if (value != null && shouldRenderAttribute(value) && !hasBehavior) {
            writer.writeAttribute(prefixAttribute(attributeName, isXhtml), value, attributeName);
        } else if (hasBehavior) {

            // If we've got a behavior for this attribute,
            // we may need to chain scripts together, so use
            // renderHandler().
            renderHandler(context, component, attributeName, value, eventName, null, false);
        }
    }

    /**
     * <p>Determines if an attribute should be rendered based on the
     * specified #attributeVal.</p>
     *
     * @param attributeVal the attribute value
     * @return <code>true</code> if and only if #attributeVal is
     * an instance of a wrapper for a primitive type and its value is
     * equal to the default value for that type as given in the specification.
     */
    private static boolean shouldRenderAttribute(Object attributeVal) {

        if (attributeVal instanceof String) {
            return true;
        } else if (attributeVal instanceof Boolean &&
                Boolean.FALSE.equals(attributeVal)) {
            return false;
        } else if (attributeVal instanceof Integer &&
                (Integer) attributeVal == Integer.MIN_VALUE) {
            return false;
        } else if (attributeVal instanceof Double &&
                (Double) attributeVal == Double.MIN_VALUE) {
            return false;
        } else if (attributeVal instanceof Character &&
                (Character) attributeVal
                        == Character
                        .MIN_VALUE) {
            return false;
        } else if (attributeVal instanceof Float &&
                (Float) attributeVal == Float.MIN_VALUE) {
            return false;
        } else if (attributeVal instanceof Short &&
                (Short) attributeVal == Short.MIN_VALUE) {
            return false;
        } else if (attributeVal instanceof Byte &&
                (Byte) attributeVal == Byte.MIN_VALUE) {
            return false;
        } else if (attributeVal instanceof Long &&
                (Long) attributeVal == Long.MIN_VALUE) {
            return false;
        }
        return true;

    }

    /**
     * <p>Utility method to return the client ID of the parent form.</p>
     *
     * @param component typically a command component
     * @param context   the <code>FacesContext</code> for the current request
     * @return the client ID of the parent form, if any
     */
    public static String getFormClientId(UIComponent component,
                                         FacesContext context) {
        UIForm form = getForm(component);
        if (form != null) {
            return form.getClientId(context);
        }

        return null;
    }


    /**
     * <p>Utility method to return the client ID of the parent form.</p>
     *
     * @param component typically a command component
     * @return the parent form, if any
     */
    public static UIForm getForm(UIComponent component) {

        UIComponent parent = component.getParent();
        while (parent != null) {
            if (parent instanceof UIForm) {
                break;
            }
            parent = parent.getParent();
        }

        UIForm form = (UIForm) parent;
        if (form != null) {
            return form;
        }

        return null;
    }

    // --------------------------------------------------------- Private Methods


    // Appends a script to a jsf.util.chain() call
    private static void appendScriptToChain(StringBuilder builder,
                                            String script) {

        if ((script == null) || (script.length() == 0)) {
            return;
        }

        if (builder.length() == 0) {
            builder.append("jsf.util.chain(this,event,");
        }

        if (builder.charAt(builder.length() - 1) != ',')
            builder.append(',');

        appendQuotedValue(builder, script);
    }

    // Appends an name/value property pair to a JSON object.  Assumes
    // object has already been opened by the caller.  The value will
    // be quoted (ie. wrapped in single quotes and escaped appropriately).
    public static void appendProperty(StringBuilder builder,
                                      String name,
                                      Object value) {
        appendProperty(builder, name, value, true);
    }

    // Appends an name/value property pair to a JSON object.  Assumes
    // object has already been opened by the caller.
    public static void appendProperty(StringBuilder builder,
                                      String name,
                                      Object value,
                                      boolean quoteValue) {

        if ((null == name) || (name.length() == 0))
            throw new IllegalArgumentException();


        char lastChar = builder.charAt(builder.length() - 1);
        if ((lastChar != ',') && (lastChar != '{'))
            builder.append(',');

        MojarraRenderUtils.appendQuotedValue(builder, name);
        builder.append(":");

        if (value == null) {
            builder.append("''");
        } else if (quoteValue) {
            MojarraRenderUtils.appendQuotedValue(builder, value.toString());
        } else {
            builder.append(value.toString());
        }
    }

    // Append a script to the chain, escaping any single quotes, since
    // our script content is itself nested within single quotes.
    private static void appendQuotedValue(StringBuilder builder,
                                          String script) {

        builder.append("'");

        int length = script.length();

        for (int i = 0; i < length; i++) {
            char c = script.charAt(i);

            if (c == '\'' || c == '\\') {
                builder.append('\\');
            }

            builder.append(c);
        }

        builder.append("'");
    }

    // Appends one or more behavior scripts a jsf.util.chain() call
    private static boolean appendBehaviorsToChain(StringBuilder builder,
                                                  FacesContext context,
                                                  UIComponent component,
                                                  List<ClientBehavior> behaviors,
                                                  String behaviorEventName,
                                                  Collection<ClientBehaviorContext.Parameter> params) {

        if ((behaviors == null) || (behaviors.isEmpty())) {
            return false;
        }

        ClientBehaviorContext bContext = createClientBehaviorContext(context,
                component,
                behaviorEventName,
                params);

        boolean submitting = false;

        for (ClientBehavior behavior : behaviors) {
            String script = behavior.getScript(bContext);
            if ((script != null) && (script.length() > 0)) {
                appendScriptToChain(builder, script);

                if (isSubmitting(behavior)) {
                    submitting = true;
                }
            }
        }

        return submitting;
    }

    // Ensures that the user-specified DOM event handler script
    // is non-empty, and trimmed if necessary.
    private static String getNonEmptyUserHandler(Object handlerObject) {

        String handler = null;

        if (null != handlerObject) {
            handler = handlerObject.toString();
            handler = handler.trim();

            if (handler.length() == 0)
                handler = null;
        }

        return handler;
    }

    // Returns the Behaviors for the specified component/event name,
    // or null if no Behaviors are available
    private static List<ClientBehavior> getClientBehaviors(UIComponent component,
                                                           String behaviorEventName) {

        if (component instanceof ClientBehaviorHolder) {
            ClientBehaviorHolder bHolder = (ClientBehaviorHolder) component;
            Map<String, List<ClientBehavior>> behaviors = bHolder.getClientBehaviors();
            if (null != behaviors) {
                return behaviors.get(behaviorEventName);
            }
        }

        return null;
    }

    // Returns a submit handler - ie. a script that calls
    // mojara.jsfcljs()
    private static String getSubmitHandler(FacesContext context,
                                           UIComponent component,
                                           Collection<ClientBehaviorContext.Parameter> params,
                                           String submitTarget,
                                           boolean preventDefault) {

        StringBuilder builder = new StringBuilder(256);

        String formClientId = getFormClientId(component, context);
        String componentClientId = component.getClientId(context);

        builder.append("mojarra.jsfcljs(document.getElementById('");
        builder.append(formClientId);
        builder.append("'),{");

        appendProperty(builder, componentClientId, componentClientId);

        if ((null != params) && (!params.isEmpty())) {
            for (ClientBehaviorContext.Parameter param : params) {
                appendProperty(builder, param.getName(), param.getValue());
            }
        }

        builder.append("},'");

        if (submitTarget != null) {
            builder.append(submitTarget);
        }

        builder.append("')");

        if (preventDefault) {
            builder.append(";return false");
        }

        return builder.toString();
    }

    // Chains together a number of Behavior scripts with a user handler
    // script.
    private static String getChainedHandler(FacesContext context,
                                            UIComponent component,
                                            List<ClientBehavior> behaviors,
                                            Collection<ClientBehaviorContext.Parameter> params,
                                            String behaviorEventName,
                                            String userHandler,
                                            String submitTarget,
                                            boolean needsSubmit) {


        // Hard to pre-compute builder initial capacity
        StringBuilder builder = new StringBuilder(100);

        appendScriptToChain(builder, userHandler);

        boolean submitting = appendBehaviorsToChain(builder,
                context,
                component,
                behaviors,
                behaviorEventName,
                params);


        boolean hasParams = ((null != params) && !params.isEmpty());

        // If we've got parameters but we didn't render a "submitting"
        // behavior script, we need to explicitly render a submit script.
        if (!submitting && (hasParams || needsSubmit)) {
            String submitHandler = getSubmitHandler(context,
                    component,
                    params,
                    submitTarget,
                    false);

            appendScriptToChain(builder, submitHandler);

            // We are now submitting since we've rendered a submit script.
            submitting = true;
        }

        if (builder.length() == 0) {
            return null;
        }

        builder.append(")");

        // If we're submitting (either via a behavior, or by rendering
        // a submit script), we need to return false to prevent the
        // default button/link action.
        if (submitting &&
                ("action".equals(behaviorEventName) ||
                        "click".equals(behaviorEventName))) {
            builder.append(";return false");
        }

        return builder.toString();
    }

    // Returns the script for a single Behavior
    private static String getSingleBehaviorHandler(FacesContext context,
                                                   UIComponent component,
                                                   ClientBehavior behavior,
                                                   Collection<ClientBehaviorContext.Parameter> params,
                                                   String behaviorEventName,
                                                   String submitTarget,
                                                   boolean needsSubmit) {
        final ClientBehaviorContext bContext = createClientBehaviorContext(context, component, behaviorEventName, params);
        String script = behavior.getScript(bContext);

        boolean preventDefault = ((needsSubmit || isSubmitting(behavior)) &&
                (component instanceof ActionSource || component instanceof ActionSource2));

        if (script == null) {
            if (needsSubmit) {
                script = getSubmitHandler(context,
                        component,
                        params,
                        submitTarget,
                        preventDefault);
            }
        } else if (preventDefault) {
            script = script + ";return false";
        }

        return script;
    }

    // Creates a ClientBehaviorContext with the specified properties.
    private static ClientBehaviorContext createClientBehaviorContext(FacesContext context,
                                                                     UIComponent component,
                                                                     String behaviorEventName,
                                                                     Collection<ClientBehaviorContext.Parameter> params) {

        return ClientBehaviorContext.createClientBehaviorContext(context,
                component,
                behaviorEventName,
                null,
                params);
    }

    // Tests whether the specified behavior is submitting
    private static boolean isSubmitting(ClientBehavior behavior) {
        return behavior.getHints().contains(ClientBehaviorHint.SUBMITTING);
    }

    /**
     * Renders a handler script, which may require chaining together
     * the user-specified event handler, any scripts required by attached
     * Behaviors, and also possibly the mojarra.jsfcljs() "submit" script.
     *
     * @param context           the FacesContext for this request.
     * @param component         the UIComponent that we are rendering
     * @param handlerName       the name of the handler attribute to render (eg.
     *                          "onclick" or "ommouseover")
     * @param handlerValue      the user-specified value for the handler attribute
     * @param behaviorEventName the name of the behavior event that corresponds
     *                          to this handler (eg. "action" or "mouseover").
     * @param needsSubmit       indicates whether the mojarra.jsfcljs()
     *                          "submit" script is required by the component.  Most components
     *                          do not need this, either because they submit themselves
     *                          (eg. commandButton), or because they do not perform submits
     *                          (eg. non-command components).  This flag is mainly here for
     *                          the commandLink case, where we need to render the submit
     *                          script to make the link submit.
     */
    private static void renderHandler(FacesContext context,
                                      UIComponent component,
                                      String handlerName,
                                      Object handlerValue,
                                      String behaviorEventName,
                                      String submitTarget,
                                      boolean needsSubmit) throws IOException {

        ResponseWriter writer = context.getResponseWriter();
        String userHandler = getNonEmptyUserHandler(handlerValue);
        List<ClientBehavior> behaviors = getClientBehaviors(component, behaviorEventName);

        // Don't render behavior scripts if component is disabled
        if ((null != behaviors) &&
                (behaviors.size() > 0) &&
                componentIsDisabled(component)) {
            behaviors = null;
        }

        Collection<ClientBehaviorContext.Parameter> params = Collections.emptyList();
        String handler = null;
        switch (getHandlerType(behaviors, params, userHandler, needsSubmit)) {

            case USER_HANDLER_ONLY:
                handler = userHandler;
                break;

            case SINGLE_BEHAVIOR_ONLY:
                handler = getSingleBehaviorHandler(context,
                        component,
                        behaviors.get(0),
                        params,
                        behaviorEventName,
                        submitTarget,
                        needsSubmit);
                break;

            case SUBMIT_ONLY:
                handler = getSubmitHandler(context,
                        component,
                        params,
                        submitTarget,
                        true);
                break;

            case CHAIN:
                handler = getChainedHandler(context,
                        component,
                        behaviors,
                        params,
                        behaviorEventName,
                        userHandler,
                        submitTarget,
                        needsSubmit);
                break;
            default:
                assert (false);
        }


        writer.writeAttribute(handlerName, handler, null);
    }

    public static boolean componentIsDisabled(UIComponent component) {

        return (Boolean.valueOf(String.valueOf(component.getAttributes().get("disabled"))));

    }

    // Determines the type of handler to render based on what sorts of
    // scripts we need to render/chain.
    private static HandlerType getHandlerType(List<ClientBehavior> behaviors,
                                              Collection<ClientBehaviorContext.Parameter> params,
                                              String userHandler,
                                              boolean needsSubmit) {

        if ((behaviors == null) || (behaviors.isEmpty())) {

            // No behaviors and no params means user handler only,
            // if we have a param only because of includeExec while having
            // no behaviors, also, user handler only
            if ((params.isEmpty() && !needsSubmit))
                return HandlerType.USER_HANDLER_ONLY;

            // We've got params.  If we've also got a user handler, we need
            // to chain.  Otherwise, we only render the submit script.
            return (userHandler == null) ? HandlerType.SUBMIT_ONLY :
                    HandlerType.CHAIN;
        }


        // We've got behaviors.  See if we can optimize for the single
        // behavior case.  We can only do this if we don't have a user
        // handler.
        if ((behaviors.size() == 1) && (userHandler == null)) {
            ClientBehavior behavior = behaviors.get(0);

            // If we've got a submitting behavior, then it will handle
            // submitting the params.  If not, then we need to use
            // a submit script to handle the params.
            if (isSubmitting(behavior) || ((params.isEmpty()) && !needsSubmit))
                return HandlerType.SINGLE_BEHAVIOR_ONLY;
        }

        return HandlerType.CHAIN;
    }

    // Little utility enum that we use to identify the type of
    // handler that we are going to render.
    private enum HandlerType {

        // Indicates that we only have a user handler - nothing else
        USER_HANDLER_ONLY,

        // Indicates that we only have a single behavior - no chaining
        SINGLE_BEHAVIOR_ONLY,

        // Indicates that we only render the mojarra.jsfcljs() script
        SUBMIT_ONLY,

        // Indicates that we've got a chain
        CHAIN
    }

}

