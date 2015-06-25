/**
 * jQuery-Plugin "butterHandleAutoComplete" for text autocomplete tag. Initializes auto complete functionality to
 * text component.
 *
 * How to use:
 * jQuery("#selector")._butterAutoCompleteInit();
 */
(function ($) {
    // extend jQuery --------------------------------------------------------------------

    $.fn._butterAutoCompleteInit = function () {
        return this.each(function () {
            new AutocompleteList(this);
        });
    };

    // define objects --------------------------------------------------------------------

    var AutocompleteList = Class.extend({
        init: function (rootElement) {
            console.log("init AutocompleteList");
            var $autocompleteTmp = $(rootElement);
            this.$input = $autocompleteTmp.prev();
            this.autocompleteId = $autocompleteTmp.attr("id");
            this.$selectedOption = null;

            this._keyCodes = {
                //backspace: 8,
                tab: 9,
                enter: 13,
                shift: 16,
                ctrl: 17,
                alt: 18,
                pause: 19,
                caps_lock: 20,
                escape: 27,
                page_up: 33,
                page_down: 34,
                end: 35,
                home: 36,
                arrow_left: 37,
                arrow_up: 38,
                arrow_right: 39,
                arrow_down: 40,
                insert: 45,
                // 'delete' is a reserved key word
                delete_key: 46,
                left_window_key: 91,
                right_window_key: 92,
                select_key: 93,
                num_lock: 144,
                scroll_lock: 145
            };

            var self = this;
            self.$input
                    .on("keyup", function (event) {
                        if (self.$input.val().length === 0) {
                            self._stopEvent(event);
                            self._hideAutocomplete();
                            self.$input.removeData("data-test");
                            return;
                        }

                        // don't handle other keys than character keys
                        for (keyName in self._keyCodes) {
                            if (self._keyCodes[keyName] === event.which) {
                                self._stopEvent(event);
                                return;
                            }
                        }

                        self._sendJsfAjaxRequest();
                    })
                    .on("blur", function (event) {
                        window.setTimeout(function () {
                            self._hideAutocomplete();
                        }, 100);
                    });
        },

        _sendJsfAjaxRequest: function () {
            var self = this;
            jsf.ajax.request(self.$input[0], "autocomplete", {
                "javax.faces.behavior.event": "autocomplete",
                render: self.autocompleteId,
                params: self.$input.val(),
                onevent: function (data) {
                    self._handleJsfAjaxEvent(data);
                }
            });
        },

        _handleJsfAjaxEvent: function (data) {
            console.log("_handleJsfAjaxEvent");
            if (data.status === "success") {
                this._handleAutocompleteVisibility();
            }
        },

        _handleAutocompleteVisibility: function () {
            var self = this;
            var $autocomplete2 = self._getAutocompleteElement();

            if (self.$input.data("data-test") === undefined) {
                if ($autocomplete2.has("li").size() > 0) {
                    self._showAutocomplete();
                } else {
                    self._hideAutocomplete();
                }
            } else {
                self._hideAutocomplete();
                self.$input.removeData("data-test");
            }
        },

        _showAutocomplete: function () {
            var self = this;
            var $autocomplete = self._getAutocompleteElement();
            $autocomplete
                    .show()
                    .css({
                        width: self.$input.innerWidth()
                    })
                    .highlight(self.$input.val());

            $autocomplete.find("ul")
                    .on("mouseleave", function () {
                        self._clearResultOptionSelection();
                    });

            $autocomplete.find("li")
                    .on("mousedown", function () {
                        self._hideAutocomplete();
                        /*self.$input
                         .val($(this).attr("data-select-value"))
                         .change()
                         .keyup()
                         .focus();*/
                    })
                    .on("mouseenter", function () {
                        self._selectResultOptionElement(this);
                    });
        },

        _selectResultOptionElement: function (optionElement) {
            this._clearResultOptionSelection();
            var $selectedOptionElement = $(optionElement);
            $selectedOptionElement.addClass("butter-dropdownlist-resultItem-selected");
            this.$selectedOption = $selectedOptionElement;
        },

        _clearResultOptionSelection: function () {
            this.$selectedOption = null;
            this._getAutocompleteElement()
                    .find(".butter-dropdownlist-resultItem-selected")
                    .removeClass("butter-dropdownlist-resultItem-selected");
        },

        _hideAutocomplete: function () {
            this.$selectedOption = null;
            this._getAutocompleteElement().hide();
        },

        _getAutocompleteElement: function () {
            return $(document.getElementById(this.autocompleteId));
        },

        _stopEvent: function (event) {
            event.stopPropagation();
            event.preventDefault();
        }
    });

}(jQuery));