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
            this._initOptions(options);
            this._counter = null;
            this.setCounter(0);
            this._isValueSet = false;
            this._initButtons();
            this._initArrowKeys();
            this._initMouseWheel();
        },

        _initInput: function () {
            var self = this;
            this.$input
                .addClass("butter-component-number-input")
                .blur(function () {
                    self._setValueOnBlur();
                })
                .parent().addClass("input-group");
        },

        _initOptions: function (options) {
            var defaultOptions = {
                step: 1,
                disabled: false
            };
            this._options = $.extend({}, defaultOptions, options);

            // ensure that this values are numbers
            if (this._options.step !== undefined) {
                this._options.step = this._options.step * 1;
            }
            if (this._options.min !== undefined) {
                this._options.min = this._options.min * 1;
            }
            if (this._options.max !== undefined) {
                this._options.max = this._options.max * 1;
            }
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
                .addClass(function () {
                    return self._options.disabled ? "disabled" : "";
                })
                .click(function () {
                    if (!self._options.disabled) {
                        self.increaseCounter();
                    }
                })
                .appendTo($addon);

            $("<span>")
                .addClass("glyphicon glyphicon-chevron-down")
                .addClass("butter-component-number-button")
                .addClass(function () {
                    return self._options.disabled ? "disabled" : "";
                })
                .click(function () {
                    if (!self._options.disabled) {
                        self.decreaseCounter();
                    }
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

        _setValueOnBlur: function () {
            var value = this.$input.val();
            if (this._isStringEmpty(value)) {
                this.$input.val("");
                this.setCounter(0);
                this._isValueSet = false;
            } else {
                var parsedInt = parseInt(value);
                if (isNaN(parsedInt)) {
                    if (this._isValueSet) {
                        this.$input.val(this._counter);
                    } else {
                        this.$input.val("");
                        this.setCounter(0);
                        this._isValueSet = false;
                    }
                } else {
                    this.setCounter(parsedInt);
                    this.$input.val(this._counter);
                    this._isValueSet = true;
                }
            }
        },

        increaseCounter: function () {
            if (this._isValueSet) {
                this.setCounter(this._counter + this._options.step);
            } else {
                this._isValueSet = true;
            }
            this.$input.val(this._counter);
            this.$input.change();
        },

        decreaseCounter: function () {
            if (this._isValueSet) {
                this.setCounter(this._counter - this._options.step);
            } else {
                this._isValueSet = true;
            }
            this.$input.val(this._counter);
            this.$input.change();
        },

        _isStringEmpty: function (value) {
            return (value.length === 0 || !value.trim());
        },

        setCounter: function (value) {
            if (this._options.min !== undefined && value < this._options.min) {
                this._counter = this._options.min;
            } else if (this._options.max !== undefined && value > this._options.max) {
                this._counter = this._options.max;
            } else {
                this._counter = value;
            }
        }
    });
}(jQuery));