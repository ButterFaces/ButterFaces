/**
 * larmic jsf2 components - An jsf 2 component extension
 * https://bitbucket.org/larmicBB/larmic-jsf2-components
 *
 * Copyright 2013 by Lars Michaelis & Stephan Zerhusen <br/>
 * Released under the MIT license http://opensource.org/licenses/mit-license.php
 */

/**
 *====================================================================================
 *DOM UTILS
 *====================================================================================
 */

/**
 * An object with dom utility functions
 * @type {Object}
 */
DomUtils = {
    /**
     * Returns the children of a node. Can optionally select only children by a given class name
     * @param node the node
     * @param className an optional class name for selecting specific children
     * @returns {Array} an array with the selected child nodes
     */
    getChildren:function (/*Node*/node, /*String*/className) {
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
    },

    /**
     * Returns the siblings of a node. Can optionally select only siblings by a given class name
     * @param node the node
     * @param className an optional class name for selecting specific siblings
     * @returns {Array} an array with the selected sibling nodes
     */
    getSiblings:function (/*Node*/node, /*String*/className) {
        return DomUtils.getChildren(node.parentNode, className);
    },

    /**
     * Binds a given function to one or more dom events
     * @param node the node on that the events should be bound
     * @param eventNames one or more event names separated by space (e.g. "focus blur")
     * @param handlerFunction the function that will be called on the events (gets the event as parameter)
     */
    bindEvent:function (/*Node*/node, /*String*/eventNames, /*Function*/handlerFunction) {
        var events = eventNames.split(" ");

        if (events.length === 1) {
            // bind the function to the given event
            if (typeof node.addEventListener !== "undefined") {
                node.addEventListener(events[0], handlerFunction, false);
            } else {
                // for older IE versions
                node.attachEvent("on" + events[0], function () {
                    return handlerFunction.call(node, window.event);
                });
            }
        } else {
            // bind function to each given event
            for (var i = 0; i < events.length; i++) {
                DomUtils.bindEvent(node, events[i], handlerFunction);
            }
        }
    }
};

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

        DomUtils.bindEvent(self._inputNode, "mouseover", function () {
            if (self._tooltipOpenedByFocus) {
                return;
            }
            self._showTooltip();
        });
        DomUtils.bindEvent(self._inputNode, "mouseout", function () {
            if (self._tooltipOpenedByFocus) {
                return;
            }
            self._hideTooltip();
        });
        DomUtils.bindEvent(self._inputNode, "focus", function () {
            self._tooltipOpenedByFocus = true;
            self._showTooltip();
        });
        DomUtils.bindEvent(self._inputNode, "blur", function () {
            self._hideTooltip();
            self._tooltipOpenedByFocus = false;
        });
    }
};

/**
 * Returns the input field node. Can be overridden for custom purposes.
 * @returns {Node} the input field node
 */
ComponentHandler.prototype.getInputNode = function () {
    var inputContainer = DomUtils.getChildren(this._componentNode, "larmic-input-container-marker")[0];
    return DomUtils.getChildren(inputContainer, "larmic-input-component-marker")[0];
};

/**
 * Returns the tooltip node. Can be overridden for custom purposes.
 * @returns {Node} the tooltip node
 */
ComponentHandler.prototype.getTooltipNode = function () {
    var inputContainer = DomUtils.getChildren(this._componentNode, "larmic-input-container-marker")[0];
    return DomUtils.getChildren(inputContainer, "larmic-component-tooltip")[0];
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
            var inputContainer = DomUtils.getChildren(this._componentNode, "larmic-input-container-marker")[0];
            self._maxLengthCounterNode = DomUtils.getChildren(inputContainer, "larmic-component-textarea-maxlength-counter")[0];

            // inital call of check function
            self._checkValue();

            if (self._inputNode.oninput !== "undefined") {
                DomUtils.bindEvent(self._inputNode, "input", function () {
                    self._checkValue();
                });
            } else {
                DomUtils.bindEvent(self._inputNode, "focus blur keyup cut paste", function () {
                    self._checkValue();
                });
            }
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
    this._maxLengthCounterNode.innerHTML = freeLetterCount + " of " + this._maxLength + " characters left";
    this._maxLengthCounterNode.className = freeLetterCount < 0 ? "larmic-textarea-counter larmic-component-error" : "larmic-textarea-counter";
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
