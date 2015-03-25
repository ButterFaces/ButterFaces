/**
 * larmic butterfaces components - An jsf 2 component extension
 * https://bitbucket.org/butterfaces/butterfaces
 *
 * Copyright 2013 by Lars Michaelis & Stephan Zerhusen <br/>
 * Released under the MIT license http://opensource.org/licenses/mit-license.php
 */

function addLabelAttributeToInnerComponent(/*String*/innerInputComponentId, /*String*/ label) {
    var $component = jQuery(document.getElementById(innerInputComponentId));
    var labelIsNull = !label || label.length === 0;

    $component.removeAttr('label');

    if (!labelIsNull) {
        $component.attr('label', label);
    }
}