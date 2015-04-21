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
            new Expandable(this);
        });
    };

    // define object --------------------------------------------------------------------

    /**
     * Constructor function
     * @param rootElement the root rootElement (textarea or div)
     * @constructor
     */
    var Expandable = function (rootElement) {
        this.EXPAND_HEIGHT = 250; //in px
        this.EXPAND_WIDTH = 500; //in px
        this.ANIMATION_DURATION = 200; //in ms
        this.REPOSITION_INTERVAL = 500; //in ms
        this.EASING = "swing";
        this.KEYCODE_ESCAPE = 27;

        this.$rootElement = $(rootElement);

        this.$ghostElement = null;
        this.$originalElement;
        this.blockBlurEventOnOriginal = true;
        this.blockFocusEventOnOriginal = false;
        this.initialHeight;
        this.initialWidth;
        this.initialOffset;
        this.positionTriggerInterval;

        this._initialize();
    };

    Expandable.prototype = {
        constructor: Expandable,

        _initialize: function () {
            this.$originalElement = this.$rootElement.find("textarea");
            this.$originalElement.addClass("butter-component-expandable-original");

            var self = this;
            this.$originalElement.focus(function (event) {
                self.expandElement(event);
            });
            this.$originalElement.blur(function (event) {
                self._handleBlurEvent(event);
            });
        },

        expandElement: function (event) {
            if (this.blockFocusEventOnOriginal) {
                event.preventDefault();
                return;
            }

            // console.log("expanding element");

            this.blockBlurEventOnOriginal = true;

            this.initialHeight = this.$originalElement.outerHeight();
            this.initialWidth = this.$originalElement.outerWidth();
            this.initialOffset = this.$originalElement.offset();

            //create a ghost element that be animated on gets the focus
            var self = this;
            this.$ghostElement = $("<textarea>")
                    .val(this.$originalElement.val()) //transfer value from original to ghost
                    .css("width", this.initialWidth)
                    .css("height", this.initialHeight)
                    .css("position", "absolute")
                    .css("top", this.initialOffset.top)
                    .css("left", this.initialOffset.left)
                    .css("z-index", 2000)
                    .css("box-shadow", "5px 5px 5px 0 #999")
                    .addClass("butter-component-expandable-ghost")
                    .blur(function (event) {
                        self.collapseElement(event);
                    })
                    .appendTo($("body"))
                    .focus() //set cursor into the ghost element
                    .animate({
                        height: self.EXPAND_HEIGHT,
                        width: self.EXPAND_WIDTH
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

                        self._moveCaretToEnd(this);

                        //keep track of the orginal element's position
                        self.positionTriggerInterval =
                                window.setInterval(self._repositionGhostElement, self.REPOSITION_INTERVAL);
                    });

            //make original invisible
            this.$originalElement.css("opacity", 0);
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
            this.$originalElement.css("opacity", 1);

            var self = this;
            this.$ghostElement.animate({
                height: self.initialHeight,
                width: self.initialWidth
            }, self.ANIMATION_DURATION, self.EASING, function () {
                //on animation complete

                if (!isCancelled) {
                    //transfer value back from ghost to original
                    self.$originalElement.val(self.$ghostElement.val())
                }

                //delete the ghost element
                self.$ghostElement.remove();
                self.$ghostElement = null;

                //delete position trigger timeout and resize listener
                window.clearInterval(self.positionTriggerInterval);
                $(window).off("resize.expandable");

                if (!isCancelled) {
                    // trigger blur and keyup event on original textarea and don't block
                    // it for jsf
                    self.blockBlurEventOnOriginal = false;
                    self.blockFocusEventOnOriginal = true;
                    // defer the events a little bit, look at
                    // (http://stackoverflow.com/questions/8380759/why-isnt-this-textarea-focusing-with-focus#8380785)
                    window.setTimeout(function () {
                        self.$originalElement.focus();
                        self.blockFocusEventOnOriginal = false;
                        self.$originalElement.keyup();
                        self.$originalElement.blur();
                    }, 50);
                } else {
                    self.blockBlurEventOnOriginal = true;
                    self.blockFocusEventOnOriginal = false;
                }
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

        _handleBlurEvent: function (event) {
            if (this.blockBlurEventOnOriginal) {
                // prevent blur event bubbling, so it will not be triggered in jsf
                event.preventDefault();
            }
        },

        _moveCaretToEnd: function (element) {
            if (typeof element.selectionStart == "number") {
                element.selectionStart = element.selectionEnd = element.value.length;
            } else if (typeof element.createTextRange !== "undefined") {
                var range = element.createTextRange();
                range.collapse(false);
                range.select();
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
    };
}(jQuery));