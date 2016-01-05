/**
 * jQuery-Plugin "Expanded TextAreas" for expandable text areas. It is used for the JSF-Component "b:textarea".
 * Works with at least jQuery 1.7.
 *
 * How to use:
 * jQuery("#someTextAreaSelector").butterExpandable();
 */
(function ($) {
    // extend jQuery --------------------------------------------------------------------
    $.fn.butterExpandable = function () {
        return this.each(function () {
            if ($(this).find("textarea").length > 0) {
                new TextareaExpandable(this);
            } else {
                new DivExpandable(this);
            }
        });
    };

    // define objects --------------------------------------------------------------------

    var _AbstractExpandable = Class.extend({
        init: function (rootElement) {
            this.EXPAND_HEIGHT = 250; //in px
            this.EXPAND_WIDTH = 500; //in px
            this.ANIMATION_DURATION = 200; //in ms
            this.REPOSITION_INTERVAL = 500; //in ms
            this.EASING = "swing";
            this.KEYCODE_ESCAPE = 27;

            this.$rootElement = $(rootElement);

            this.$ghostElement = null;
            this.$originalElement;
            this.initialHeight;
            this.initialWidth;
            this.initialOffset;
            this.positionTriggerInterval;

            this.initialize();
        },

        initialize: function () {
            // should intentionally be overridden
        },

        isExpansionEventIgnored: function (event) {
            // should intentionally be overridden
            return false;
        },

        onGhostElementCreated: function () {
            // should intentionally be overridden
        },

        onGhostElementCollapsed: function (isCancelled) {
            // should intentionally be overridden
        },

        transferValueToGhostElement: function () {
            // should intentionally be overridden
        },

        expandElement: function (event) {
            if (this.isExpansionEventIgnored(event)) {
                return;
            }

            // console.log("expanding element");

            this.initialHeight = this.$originalElement.outerHeight();
            this.initialWidth = this.$originalElement.outerWidth();
            this.initialOffset = this.$originalElement.offset();

            //create a ghost element that be animated on gets the focus
            var self = this;
            this.$ghostElement = this.createGhostElement();
            this.transferValueToGhostElement();
            this.$ghostElement.css("width", this.initialWidth)
                    .css("height", this.initialHeight)
                    .css("position", "absolute")
                    .css("top", this.initialOffset.top)
                    .css("left", this.initialOffset.left)
                    .css("z-index", 2000)
                    .css("box-shadow", "5px 5px 5px 0 #999")
                    .addClass("butter-component-expandable-ghost")
                    .appendTo($("body"))
                    .animate({
                        height: self.EXPAND_HEIGHT,
                        width: self.initialWidth > self.EXPAND_WIDTH ? self.initialWidth : self.EXPAND_WIDTH
                    }, self.ANIMATION_DURATION, self.EASING, function () {
                        $(document)
                                .on("click.expandable", function (event) {
                                    self._handleMouseClick(event);
                                })
                                .on("keydown.expandable", function (event) {
                                    self._handleEscapeKey(event);
                                });

                        $(window).on("resize.expandable", function (event) {
                            self._repositionGhostElement(event);
                        });

                        //keep track of the orginal element's position
                        self.positionTriggerInterval =
                                window.setInterval(self._repositionGhostElement, self.REPOSITION_INTERVAL);
                    });

            //make original invisible
            this.$originalElement
                    .css("visibility", "hidden")
                    .siblings()
                    .css("visibility", "hidden");

            this.onGhostElementCreated();
        },

        /**
         * @returns {*|HTMLElement}
         */
        createGhostElement: function () {
            // should intentionally be overridden
            return null;
        },

        /**
         * Collapses the ghost element and sets the value if not isCancelled
         * @param isCancelled
         */
        collapseElement: function (cancelled) {
            // console.log("collapsing element");

            // 'cancelled' can be an event object
            var isCancelled = typeof cancelled === "boolean" && cancelled;

            $(document)
                    .off("click.expandable")
                    .off("keydown.expandable");

            //make original visible again
            this.$originalElement
                    .css("visibility", "visible")
                    .siblings()
                    .css("visibility", "visible");

            var self = this;
            this.$ghostElement.animate({
                height: self.initialHeight,
                width: self.initialWidth
            }, self.ANIMATION_DURATION, self.EASING, function () {
                //on animation complete

                self.onGhostElementCollapsed(isCancelled);

                //delete the ghost element
                self.$ghostElement.remove();
                self.$ghostElement = null;

                //delete position trigger timeout and resize listener
                window.clearInterval(self.positionTriggerInterval);
                $(window).off("resize.expandable");
            });
        },

        _handleMouseClick: function (event) {
            // collapse ghost element if user clicks beside it
            if (!$(event.target).is(".butter-component-expandable-ghost")) {
                this.collapseElement(false);
            }
        },

        _handleEscapeKey: function (event) {
            if (event.which === this.KEYCODE_ESCAPE) {
                this.collapseElement(true);
            }
        },

        _repositionGhostElement: function () {
            //keep track of window resizing and reposition the ghost element
            if (this.$ghostElement !== undefined && this.$ghostElement != null) {
                this.initialOffset = this.$originalElement.offset();
                this.$ghostElement
                        .css("top", this.initialOffset.top)
                        .css("left", this.initialOffset.left);
            }
        }
    });

    var DivExpandable = _AbstractExpandable.extend({
        initialize: function () {
            this.$originalElement = this.$rootElement.find(".butter-component-value-readonly");
            this._rearrangeOriginalElementStructure();
        },

        _rearrangeOriginalElementStructure: function () {
            var self = this;
            var _label = this.$rootElement.find(".butter-component-label");

            this.$originalElement
                    .addClass("butter-component-expandable-original")
                    .click(function (event) {
                        self.expandElement(event);
                    })
                    .detach();

            var _container = $("<div>")
                    .addClass("butter-component-expandable-readonly-container")
                    .insertAfter(_label);

            var _icon = $("<span>").addClass("glyphicon glyphicon-resize-full");

            this.$originalElement.appendTo(_container);
            $("<div>")
                    .addClass("butter-component-expandable-readonly-icon")
                    .append(_icon)
                    .appendTo(_container);
        },

        createGhostElement: function () {
            return $("<div>");
        },

        transferValueToGhostElement: function () {
            $("<div>")
                    .html(this.$originalElement.html())
                    .addClass("butter-component-expandable-ghost-readonlyContent")
                    .appendTo(this.$ghostElement);
        }
    });

    var TextareaExpandable = _AbstractExpandable.extend({
        initialize: function () {
            this.blockFocusEventOnOriginal = false;
            this.blockBlurEventOnOriginal = false;

            this.$originalElement = this.$rootElement.find("textarea");
            this.$originalElement.addClass("butter-component-expandable-original");

            var self = this;
            this.$originalElement.focus(function (event) {
                self.expandElement(event);
            });
            this.$originalElement.blur(function (event) {
                self._handleBlurEvent(event);
            });

            this._addInputGroupAddon();
        },

        _addInputGroupAddon: function () {
            this.$originalElement
                    .addClass("form-control")
                    .parent()
                    .addClass("input-group");
            $("<span class='input-group-addon'><span class='glyphicon glyphicon-resize-full'></span></span>")
                    .insertAfter(this.$originalElement);
        },

        _handleBlurEvent: function (event) {
            if (this.blockBlurEventOnOriginal) {
                // prevent blur event bubbling, so it will not be triggered in jsf
                event.preventDefault();
            }
        },

        createGhostElement: function () {
            return $("<textarea>");
        },

        transferValueToGhostElement: function () {
            this.$ghostElement.val(this.$originalElement.val());
        },

        isExpansionEventIgnored: function (event) {
            this.blockBlurEventOnOriginal = true;
            if (this.blockFocusEventOnOriginal) {
                event.preventDefault();
                return true;
            } else {
                return false;
            }
        },

        onGhostElementCreated: function () {
            var self = this;
            this.$ghostElement
                    .blur(function (event) {
                        self.collapseElement(event);
                    })
                    .focus();
            this._moveCaretToEnd(this.$ghostElement);
        },

        onGhostElementCollapsed: function (isCancelled) {
            if (!isCancelled) {
                //transfer value back from ghost to original
                this.$originalElement.val(this.$ghostElement.val())

                // trigger blur and keyup event on original textarea and don't block
                // it for jsf
                this.blockBlurEventOnOriginal = false;
                this.blockFocusEventOnOriginal = true;
                // defer the events a little bit, look at
                // (http://stackoverflow.com/questions/8380759/why-isnt-this-textarea-focusing-with-focus#8380785)
                var self = this;
                window.setTimeout(function () {
                    self.$originalElement.trigger('keyup');
                    self.$originalElement.trigger('change');
                    self.$originalElement.trigger('blur');
                    self.blockFocusEventOnOriginal = false;
                }, 50);
            } else {
                this.blockBlurEventOnOriginal = true;
                this.blockFocusEventOnOriginal = false;
            }
        },

        _moveCaretToEnd: function (element) {
            if (typeof element.selectionStart == "number") {
                element.selectionStart = element.selectionEnd = element.value.length;
            } else if (typeof element.createTextRange !== "undefined") {
                var range = element.createTextRange();
                range.collapse(false);
                range.select();
            } else {
                var strLength= this.$ghostElement.val().length * 2;
                this.$ghostElement[0].setSelectionRange(strLength, strLength);
            }

        }
    });
}(jQuery));