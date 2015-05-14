/* Simple JavaScript Inheritance
 * By John Resig http://ejohn.org/
 * MIT Licensed.
 */
// Inspired by base2 and Prototype
(function () {
    var initializing = false,
        fnTest = /xyz/.test(function () {
            xyz;
        }) ? /\b_super\b/ : /.*/;

    // The base Class implementation (does nothing)
    this.Class = function () {
    };

    // Create a new Class that inherits from this class
    Class.extend = function (prop) {
        var _super = this.prototype;

        // Instantiate a base class (but only create the instance,
        // don't run the init constructor)
        initializing = true;
        var prototype = new this();
        initializing = false;

        // Copy the properties over onto the new prototype
        for (var name in prop) {
            // Check if we're overwriting an existing function
            prototype[name] = typeof prop[name] == "function" &&
            typeof _super[name] == "function" && fnTest.test(prop[name]) ?
                (function (name, fn) {
                    return function () {
                        var tmp = this._super;

                        // Add a new ._super() method that is the same method
                        // but on the super-class
                        this._super = _super[name];

                        // The method only need to be bound temporarily, so we
                        // remove it when we're done executing
                        var ret = fn.apply(this, arguments);
                        this._super = tmp;

                        return ret;
                    };
                })(name, prop[name]) :
                prop[name];
        }

        // The dummy class constructor
        function Class() {
            // All construction is actually done in the init method
            if (!initializing && this.init)
                this.init.apply(this, arguments);
        }

        // Populate our constructed prototype object
        Class.prototype = prototype;

        // Enforce the constructor to be what we expect
        Class.prototype.constructor = Class;

        // And make this class extendable
        Class.extend = arguments.callee;

        return Class;
    };
})();

/**
 * jQuery-Plugin "Number Spinner" for input fields.
 * Works with at least jQuery 1.3.2.
 *
 * How to use:
 * jQuery("someButterComponentSelector").butterNumberSpinner();
 */
(function ($) {
    // extend jQuery --------------------------------------------------------------------
    $.fn.butterNumberSpinner = function (options) {
        return this.each(function () {
            new NumberSpinner(this, options);
        });
    };

    // define classes --------------------------------------------------------------------
    var NumberSpinner = Class.extend({
        init: function (element, options) {
            this.$input = $(element).find("input");
            this._initInput();

            var defaultOptions = {
                step: 1
            };
            this._options = $.extend({}, defaultOptions, options);

            this._counter = 0;
            this._isValueSet = false;

            this._initButtons();
            this._initArrowKeys();
            this._initMouseWheel();
        },

        _initInput: function () {
            var self = this;
            this.$input
                .addClass("butter-component-number")
                .blur(function () {
                    self._setValue();
                })
                .parent().addClass("input-group");
        },

        _initButtons: function () {
            var $addon = $("<span>")
                .addClass("input-group-addon")
                .addClass("butter-component-number-buttons")
                .insertAfter(this.$input);

            var self = this;
            $("<span>")
                .addClass("glyphicon glyphicon-chevron-up")
                .addClass("butter-component-number-button")
                .click(function () {
                    self.increaseCounter();
                })
                .appendTo($addon);

            $("<span>")
                .addClass("glyphicon glyphicon-chevron-down")
                .addClass("butter-component-number-button")
                .click(function () {
                    self.decreaseCounter();
                })
                .appendTo($addon);
        },

        _initArrowKeys: function () {
            var self = this;
            this.$input.keydown(function (event) {
                if (event.which === 38) {
                    event.stopPropagation();
                    event.preventDefault();
                    self.increaseCounter();
                } else if (event.which === 40) {
                    event.stopPropagation();
                    event.preventDefault();
                    self.decreaseCounter();
                }
            });
        },

        _initMouseWheel: function () {
            var self = this;
            this.$input.on("mousewheel DOMMouseScroll", function (event) {
                if (!self.$input.is(':focus')) {
                    return;
                }

                var delta = event.originalEvent.wheelDelta || -event.originalEvent.deltaY || -event.originalEvent.detail;

                event.stopPropagation();
                event.preventDefault();

                if (delta < 0) {
                    self.decreaseCounter();
                } else {
                    self.increaseCounter();
                }
            });
        },

        _setValue: function () {
            var value = this.$input.val();
            if (this._isStringEmpty(value)) {
                this.$input.val("");
                this._isValueSet = false;
            } else {
                var parsedInt = parseInt(value);
                if (isNaN(parsedInt)) {
                    if (this._isValueSet) {
                        this.$input.val(this._counter);
                    } else {
                        this.$input.val("");
                        this._isValueSet = false;
                    }
                } else {
                    this._counter = parsedInt;
                    this.$input.val(this._counter);
                    this._isValueSet = true;
                }
            }
        },

        increaseCounter: function () {
            if (this._isValueSet) {
                this._counter += this._options.step;
            } else {
                this._isValueSet = true;
            }
            this.$input.val(this._counter);
        },

        decreaseCounter: function () {
            if (this._isValueSet) {
                this._counter -= this._options.step;
            } else {
                this._isValueSet = true;
            }
            this.$input.val(this._counter);
        },

        _isStringEmpty: function (value) {
            return (value.length === 0 || !value.trim());
        }
    });
}(jQuery));