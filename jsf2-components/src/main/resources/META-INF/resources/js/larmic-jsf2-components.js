/**
 * A handler for input components. Will handle the tooltip.
 * @param inputId {String} the id of the dom element
 * @constructor
 */
ComponentHandler = function (/*String*/inputId) {
   var self = this; // prevent loosing scope "this" in closures

   self._inputNode = document.getElementById(inputId);
   if (typeof self._inputNode === "undefined") {
      throw new Error("couldn't find input node with id '" + inputId + "'");
   }

   self._tooltipNode = self._getSiblings(self._inputNode, "larmic-component-tooltip")[0];
   self._tooltipOpenedByFocus = false;

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

   self.onInitialized();
};

/**
 * Returns the siblings of a node. Can optionally select only siblings by a given class name
 * @param node the node
 * @param className an optional class name for selecting specific siblings
 * @returns {Array} an array with the selected sibling nodes
 * @private
 */
ComponentHandler.prototype._getSiblings = function (/*Node*/node, /*String*/className) {
   var siblings = [];
   if (typeof className !== "undefined" && className !== "") {
      siblings = [].slice.call(node.parentNode.children) // convert to array
         // filter out siblings that don't have the given class
         .filter(function (siblingNode) {
            var clazz = siblingNode.className;
            return typeof clazz !== "undefined" && clazz.indexOf(className) > -1;
         });
   } else {
      siblings = [].slice.call(node.parentNode.children);
   }
   return siblings;
};

/**
 * Will be called when initialization is done. Can be overridden for additional custom initialization.
 */
ComponentHandler.prototype.onInitialized = function () {
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

/* Example for inheritance
 ==============================
 // here's where the inheritance occurs
 CustomComponentHandler.prototype = new ComponentHandler();

 // if instances of CustomComponentHandler should call their own constructor.
 // otherwise they would call it from ComponentHandler
 CustomComponentHandler.prototype.constructor = CustomComponentHandler;
 function CustomComponentHandler(attribute){
 this.attribute=attribute;
 }

 // additional custom function
 CustomComponentHandler.prototype.customFunction=function(){}
 */
