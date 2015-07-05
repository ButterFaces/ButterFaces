/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 1997-2010 Oracle and/or its affiliates. All rights reserved.
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
 * (C) Copyright International Business Machines Corp., 2001,2002
 * The source code for this program is not published or otherwise
 * divested of its trade secrets, irrespective of what has been
 * deposited with the U. S. Copyright Office.
 */

// MenuRenderer.java

package de.larmic.butterfaces.component.renderkit.html_basic.mojarra;

import com.sun.faces.io.FastStringWriter;
import com.sun.faces.renderkit.Attribute;
import com.sun.faces.renderkit.AttributeManager;
import com.sun.faces.renderkit.RenderKitUtils;
import com.sun.faces.renderkit.SelectItemsIterator;
import de.larmic.butterfaces.component.html.HtmlInputComponent;
import de.larmic.butterfaces.component.partrenderer.Constants;
import de.larmic.butterfaces.component.partrenderer.StringUtils;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.model.SelectItem;
import java.io.IOException;
import java.util.logging.Level;

/**
 * Overrides ButterFaces specific things of |@link com.sun.faces.renderkit.html_basic.MenuRenderer}.
 */

public class MenuRenderer extends com.sun.faces.renderkit.html_basic.MenuRenderer {

    private static final Attribute[] ATTRIBUTES =
            AttributeManager.getAttributes(AttributeManager.Key.SELECTMANYMENU);


    /**
     * Add protected method call {@link MenuRenderer#renderHtmlFeatures(UIComponent, ResponseWriter)}.
     */
    protected void renderSelect(FacesContext context,
                                UIComponent component) throws IOException {

        ResponseWriter writer = context.getResponseWriter();
        assert (writer != null);

        if (logger.isLoggable(Level.FINER)) {
            logger.log(Level.FINER, "Rendering 'select'");
        }
        writer.startElement("select", component);
        //writeIdAttributeIfNecessary(context, writer, component);
        writer.writeAttribute("name", component.getClientId(context),
                "clientId");

        final String styleClass = StringUtils.concatWithSpace(Constants.INPUT_COMPONENT_MARKER,
                Constants.BOOTSTRAP_FORM_CONTROL,
                !((HtmlInputComponent) component).isValid() ? Constants.INVALID_STYLE_CLASS : null);

        if (StringUtils.isNotEmpty(styleClass)) {
            writer.writeAttribute("class", styleClass, "styleClass");
        }

        if (!getMultipleText(component).equals("")) {
            writer.writeAttribute("multiple", true, "multiple");
        }

        // Determine how many option(s) we need to render, and update
        // the component's "size" attribute accordingly;  The "size"
        // attribute will be rendered as one of the "pass thru" attributes
        SelectItemsIterator<SelectItem> items = RenderKitUtils.getSelectItems(context, component);

        // render the options to a buffer now so that we can determine
        // the size
        FastStringWriter bufferedWriter = new FastStringWriter(128);
        context.setResponseWriter(writer.cloneWithWriter(bufferedWriter));
        int count = renderOptions(context, component, items);
        context.setResponseWriter(writer);
        // If "size" is *not* set explicitly, we have to default it correctly
        Integer size = (Integer) component.getAttributes().get("size");
        if (size == null || size == Integer.MIN_VALUE) {
            size = count;
        }
        writeDefaultSize(writer, size);

        // *** BEGIN HTML 5 CHANGED **************************
        this.renderHtmlFeatures(component, writer);
        // *** END HTML 5 CHANGED ****************************

        RenderKitUtils.renderPassThruAttributes(context,
                writer,
                component,
                ATTRIBUTES,
                getNonOnChangeBehaviors(component));
        RenderKitUtils.renderXHTMLStyleBooleanAttributes(writer,
                component);

        RenderKitUtils.renderOnchange(context, component, false);

        // Now, write the buffered option content
        writer.write(bufferedWriter.toString());

        writer.endElement("select");

    }

    protected void renderHtmlFeatures(UIComponent component, ResponseWriter writer) throws IOException {

    }


} // end of class MenuRenderer
