/**
 * larmic jsf2 components - An jsf 2 component extension
 * https://bitbucket.org/larmicBB/larmic-jsf2-components
 *
 * Copyright 2013 by Lars Michaelis & Stephan Zerhusen <br/>
 * Released under the MIT license http://opensource.org/licenses/mit-license.php
 */

/**
 *====================================================================================
 *COMPONENT HANDLER
 *====================================================================================
 */

/**
 * A handler for input components. Will handle the tooltip.
 * @param componentId {String} the id of the outer  dom element
 * @param options {Object} an optional object with option parameters
 * @constructor
 */
ComponentHandler = function (/*String*/componentId, /*Object*/ options) {
    var self = this; // prevent loosing scope "this" in closures

    self._componentNode = document.getElementById(componentId);
    if (typeof self._componentNode === "undefined") {
        throw new Error("couldn't find component node with id '" + componentId + "'");
    }

    self.options = options;

    var isTooltipEnabled = typeof self.options !== "undefined" && self.options.showTooltip;

    self._tooltipOpenedByFocus = false;
    self._inputNode = self.getInputNode();
    if (isTooltipEnabled) {
        self._tooltipNode = self.getTooltipNode();

        self._inputNode.onmouseover = function () {
            if (self._tooltipOpenedByFocus) {
                return;
            }
            self._showTooltip();
        };
        self._inputNode.onmouseout = function () {
            if (self._tooltipOpenedByFocus) {
                return;
            }
            self._hideTooltip();
        };
        self._inputNode.onfocus = function () {
            self._tooltipOpenedByFocus = true;
            self._showTooltip();
        };
        self._inputNode.onblur = function () {
            self._hideTooltip();
            self._tooltipOpenedByFocus = false;
        };
    }
};

/**
 * Returns the input field node. Can be overridden for custom purposes.
 * @returns {Node} the input field node
 */
ComponentHandler.prototype.getInputNode = function () {
    return this.getChildren(this._componentNode, "larmic-component-input")[0];
};

/**
 * Returns the tooltip node. Can be overridden for custom purposes.
 * @returns {Node} the tooltip node
 */
ComponentHandler.prototype.getTooltipNode = function () {
    return this.getChildren(this._componentNode, "larmic-component-tooltip")[0];
};

/**
 * Returns the children of a node. Can optionally select only children by a given class name
 * @param node the node
 * @param className an optional class name for selecting specific children
 * @returns {Array} an array with the selected child nodes
 */
ComponentHandler.prototype.getChildren = function (/*Node*/node, /*String*/className) {
    var children = [];
    if (typeof className !== "undefined" && className !== "") {
        children = [].slice.call(node.children)// convert to array
            // filter out children that don't have the given class
            .filter(function (childNode) {
                var clazz = childNode.className;
                return typeof clazz !== "undefined" && clazz.indexOf(className) > -1;
            });
    } else {
        children = [].slice.call(node.children);
    }
    return children;
};

/**
 * Returns the siblings of a node. Can optionally select only siblings by a given class name
 * @param node the node
 * @param className an optional class name for selecting specific siblings
 * @returns {Array} an array with the selected sibling nodes
 */
ComponentHandler.prototype.getSiblings = function (/*Node*/node, /*String*/className) {
    return this.getChildren(node.parentNode, className);
};

/**
 * Shows the tooltip
 * @private
 */
ComponentHandler.prototype._showTooltip = function () {
    this._tooltipNode.style.display = "inline-block"
};

/**
 * Hides the tooltip
 * @private
 */
ComponentHandler.prototype._hideTooltip = function () {
    this._tooltipNode.style.display = "none"
};

/**
 *====================================================================================
 *TEXTAREA COMPONENT HANDLER
 *====================================================================================
 */

/**
 * Custom component handler for textareas
 * @param componentId {String} the id of the outer dom element
 * @param options {Object} an optional object with option parameters
 * @constructor
 */
TextareaComponentHandler = function (/*String*/componentId, /*Object*/ options) {
    // call super constructor
    ComponentHandler.call(this, componentId, options);

    this._initMaxLengthCounter();
};
TextareaComponentHandler.prototype = Object.create(ComponentHandler.prototype);
TextareaComponentHandler.prototype.constructor = TextareaComponentHandler;

/**
 * Initializes the handling for the max length counter
 * @private
 */
TextareaComponentHandler.prototype._initMaxLengthCounter = function () {
    var self = this; // prevent loosing scope "this" in closures

    var hasMaxLength = typeof self.options !== "undefined" && typeof self.options.maxLength !== "undefined";
    if (hasMaxLength) {
        self._maxLength = self.options.maxLength * 1;
        if (self._maxLength > 0) {
            console.log("_initMaxLengthCounter " + self._maxLength);

            self._maxLengthCounterNode = self.getChildren(self._componentNode, "larmic-component-textarea-maxlength-counter")[0];
            console.log(self._maxLengthCounterNode);
            self._checkValue();

            self._inputNode.onkeyup = function () {
                self._checkValue();
            };
        }
    }
};

/**
 * Checks and displays the number of free letters
 * @private
 */
TextareaComponentHandler.prototype._checkValue = function () {
    var inputValue = this._inputNode.value;
    var freeLetterCount = this._maxLength - inputValue.length;
    this._maxLengthCounterNode.innerHTML = freeLetterCount + "/" + this._maxLength;
    this._maxLengthCounterNode.className = freeLetterCount < 0 ? "larmic-component-error" : "";
};

/*
 ====================================================================================
 EXAMPLE FOR INHERITANCE
 (https://developer.mozilla.org/en/JavaScript/Reference/Global_Objects/Object/create)
 ====================================================================================

 // CustomComponentHandler - subclass
 CustomComponentHandler = function (componentId, options) {
 //call super constructor.
 ComponentHandler.call(this, componentId, options);
 };

 // subclass extends superclass
 CustomComponentHandler.prototype = Object.create(ComponentHandler.prototype);
 // call own constructor (otherwise they would call it from ComponentHandler directly)
 CustomComponentHandler.prototype.constructor = CustomComponentHandler;

 // Override existing function
 CustomComponentHandler.prototype.existingFunction = function() {
 alert("do something");
 // Call the original version of existingFunction that we overrode (like call super).
 ComponentHandler.prototype.existingFunction.call(this);
 return "Something";
 };

 // additional custom function
 CustomComponentHandler.prototype.customFunction=function(){};
 */
