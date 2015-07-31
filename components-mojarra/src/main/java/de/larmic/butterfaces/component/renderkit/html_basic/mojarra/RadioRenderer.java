/*
 * Copied from Mojarra. Remove client id, style class and style attributes.
 */

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

// RadioRenderer.java

package de.larmic.butterfaces.component.renderkit.html_basic.mojarra;

import com.sun.faces.renderkit.Attribute;
import com.sun.faces.renderkit.AttributeManager;
import com.sun.faces.renderkit.RenderKitUtils;
import com.sun.faces.renderkit.html_basic.SelectManyCheckboxListRenderer;
import com.sun.faces.util.RequestStateManager;

import javax.faces.component.UIComponent;
import javax.faces.component.UINamingContainer;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.convert.Converter;
import javax.faces.model.SelectItem;
import java.io.IOException;

/**
 * <B>ReadoRenderer</B> is a class that renders the current value of {@link javax.faces.component.UISelectOne} or
 * {@link javax.faces.component.UISelectMany} component as a list of radio buttons
 */

public class RadioRenderer extends SelectManyCheckboxListRenderer {

    private static final Attribute[] ATTRIBUTES =
            AttributeManager.getAttributes(AttributeManager.Key.SELECTONERADIO);

    // ------------------------------------------------------- Protected Methods


    @Override
    protected void renderOption(FacesContext context,
                                UIComponent component,
                                Converter converter,
                                SelectItem curItem,
                                Object currentSelections,
                                Object[] submittedValues,
                                boolean alignVertical,
                                int itemNumber,
                                OptionComponentInfo optionInfo) throws IOException {

        String valueString = getFormattedValue(context, component,
                curItem.getValue(), converter);

        Object valuesArray;
        Object itemValue;
        if (submittedValues != null) {
            valuesArray = submittedValues;
            itemValue = valueString;
        } else {
            valuesArray = currentSelections;
            itemValue = curItem.getValue();
        }

        RequestStateManager.set(context,
                RequestStateManager.TARGET_COMPONENT_ATTRIBUTE_NAME,
                component);

        boolean isSelected = isSelected(context, component, itemValue, valuesArray, converter);
        if (optionInfo.isHideNoSelection()
                && curItem.isNoSelectionOption()
                && currentSelections != null
                && !isSelected) {
            return;
        }

        ResponseWriter writer = context.getResponseWriter();
        assert (writer != null);

        if (alignVertical) {
            writer.writeText("\t", component, null);
            writer.startElement("tr", component);
            writer.writeText("\n", component, null);
        }
        writer.startElement("td", component);
        // ************************************************************* Add style class radio to td
        writer.writeAttribute("class", "radio", "styleClass");
        writer.writeText("\n", component, null);

        writer.startElement("input", component);
        writer.writeAttribute("name", component.getClientId(context),
                "clientId");
        String idString = component.getClientId(context)
                + UINamingContainer.getSeparatorChar(context)
                + Integer.toString(itemNumber);
        writer.writeAttribute("id", idString, "id");

        writer.writeAttribute("value", valueString, "value");
        // ************************************************************* CHANGED checkbox to radio type
        //writer.writeAttribute("type", "checkbox", null);
        writer.writeAttribute("type", "radio", null);

        if (isSelected) {
            writer.writeAttribute(getSelectedTextString(), Boolean.TRUE, null);
        }

        // Don't render the disabled attribute twice if the 'parent'
        // component is already marked disabled.
        if (!optionInfo.isDisabled()) {
            if (curItem.isDisabled()) {
                writer.writeAttribute("disabled", true, "disabled");
            }
        }

        // Apply HTML 4.x attributes specified on UISelectMany component to all
        // items in the list except styleClass and style which are rendered as
        // attributes of outer most table.
        RenderKitUtils.renderPassThruAttributes(context,
                writer,
                component,
                ATTRIBUTES,
                getNonOnClickSelectBehaviors(component));

        RenderKitUtils.renderXHTMLStyleBooleanAttributes(writer, component);

        RenderKitUtils.renderSelectOnclick(context, component, false);

        writer.endElement("input");
        writer.startElement("label", component);
        writer.writeAttribute("for", idString, "for");

        // Set up the label's class, if appropriate
        StringBuilder labelClass = new StringBuilder();
        String style;
        // If disabledClass or enabledClass set, add it to the label's class
        if (optionInfo.isDisabled() || curItem.isDisabled()) {
            style = optionInfo.getDisabledClass();
        } else {  // enabled
            style = optionInfo.getEnabledClass();
        }
        if (style != null) {
            labelClass.append(style);
        }
        // If selectedClass or unselectedClass set, add it to the label's class
        if (isSelected(context, component, itemValue, valuesArray, converter)) {
            style = optionInfo.getSelectedClass();
        } else { // not selected
            style = optionInfo.getUnselectedClass();
        }
        if (style != null) {
            if (labelClass.length() > 0) {
                labelClass.append(' ');
            }
            labelClass.append(style);
        }
        writer.writeAttribute("class", labelClass.toString(), "labelClass");
        String itemLabel = curItem.getLabel();
        if (itemLabel == null) {
            itemLabel = valueString;
        }
        writer.writeText(" ", component, null);
        if (!curItem.isEscape()) {
            // It seems the ResponseWriter API should
            // have a writeText() with a boolean property
            // to determine if it content written should
            // be escaped or not.
            writer.write(itemLabel);
        } else {
            writer.writeText(itemLabel, component, "label");
        }
//        if (isSelected(context, component, itemValue, valuesArray, converter)) {
//
//        } else { // not selected
//
//        }
        writer.endElement("label");
        writer.endElement("td");
        writer.writeText("\n", component, null);
        if (alignVertical) {
            writer.writeText("\t", component, null);
            writer.endElement("tr");
            writer.writeText("\n", component, null);
        }
    }

    String getSelectedTextString() {

        return "checked";

    }

} // end of class RadioRenderer
