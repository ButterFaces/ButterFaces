/**
 * larmic butterfaces components - An jsf 2 component extension
 * https://bitbucket.org/larmicBB/butterfaces
 *
 * Copyright 2013 by Lars Michaelis & Stephan Zerhusen <br/>
 * Released under the MIT license http://opensource.org/licenses/mit-license.php
 */


function handleSpinner(e, component, min, max) {
    if (!isNaN(component.value)) {
        if (e.keyCode == 38) {
            if (!max || max && component.value < max) {
                component.value = parseInt(component.value) + 1;
                e.preventDefault();
            }
            e.preventDefault();
        } else if (e.keyCode == 40) {
            if (!min || min && component.value > min) {
                component.value = parseInt(component.value) - 1;
                e.preventDefault();
            }
        }
    }
};

function addLabelAttributeToInnerComponent(/*String*/innerInputComponentId, /*String*/ label) {
    var $component = jQuery(document.getElementById(innerInputComponentId));
    var labelIsNull = !label || label.length === 0;

    $component.removeAttr('label');

    if (!labelIsNull) {
        $component.attr('label', label);
    }
}

/**
 * calls pretty print javascript framework and removes first and last empty children.
 */
function handlePrettyPrint() {
    prettyPrint();

    jQuery('.butter-component-prettyprint').each(function () {
        var $firstPreChild = jQuery(this).find("pre span").first();
        var $lastPreChild = jQuery(this).find("pre span").last();


        if (!(typeof $firstPreChild.html() === "undefined")) {
            if (!$firstPreChild.html().trim()) {
                $firstPreChild.remove();
            }
        }
        if (!(typeof $lastPreChild.html() === "undefined")) {
            if (!$lastPreChild.html().trim()) {
                $lastPreChild.remove();
            }
        }
    });
}

function handleTouchPin(/*int*/ min, /*int*/ max) {
    jQuery("input.butter-number-component").TouchSpin({
        verticalbuttons: true,
        min: min,
        max: max
    });
}