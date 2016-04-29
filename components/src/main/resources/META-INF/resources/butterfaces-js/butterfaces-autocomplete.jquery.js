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
            this.SEARCH_REQUEST_DELAY = 300;// in ms

            var $autocompleteTmp = $(rootElement);
            this.$input = $autocompleteTmp.prev();
            this.$input.parent().css({position: "relative"});
            this.autocompleteId = $autocompleteTmp.attr("id");
            this.$selectedOption = null;
            this.ignoreKeyupEvent = false;
            this.requestDelayTimerId = null;
            this.isRequestRunning = false;
            this.areChangesMadeWhileRequestWasRunning = false;

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
                    .on("keydown", function (event) {
                        if (event.which === self._keyCodes.enter) {
                            self._handleEnterKeyDown(event);
                        } else if (event.which === self._keyCodes.arrow_up
                                || event.which === self._keyCodes.arrow_down) {
                            self._handleArrowUpAndDownKeyDown(event);
                        } else if (event.which === self._keyCodes.escape) {
                            self._handleEscapeKeyDown(event);
                        }
                    })
                    .on("keyup", function (event) {
                        // don't handle other keys than character keys
                        for (keyName in self._keyCodes) {
                            if (self._keyCodes[keyName] === event.which) {
                                self._stopEvent(event);
                                return;
                            }
                        }

                        if (self.ignoreKeyupEvent) {
                            self._stopEvent(event);
                            self.ignoreKeyupEvent = false;
                            return;
                        }

                        if (self.$input.val().length === 0) {
                            self._stopEvent(event);
                            self._hideAutocompleteResultList();
                            return;
                        }

                        self._trySendJsfAjaxRequest();
                    })
                    .on("blur", function (event) {
                        window.setTimeout(function () {
                            self._hideAutocompleteResultList();
                        }, 100);
                    });
        },

        _handleEnterKeyDown: function (event) {
            if (this.$selectedOption !== null) {
                this._stopEvent(event);
                this._setSelectedValue();
            }
        },

        _handleArrowUpAndDownKeyDown: function (event) {
            this._stopEvent(event);
            var $autocomplete = this._getAutocompleteElement();
            if (!$autocomplete.is(":visible") && this.$input.val().length > 0) {
                this._trySendJsfAjaxRequest();
            } else if ($autocomplete.is(":visible") && $autocomplete.find("li").length > 0) {
                if (this.$selectedOption === null) {
                    this._selectResultOptionElement($autocomplete.find("li")[0]);
                } else {
                    this._moveResultOptionElementSelectionCursor(
                            $autocomplete, event.which === this._keyCodes.arrow_up ? -1 : 1);
                }
            }
        },

        _handleEscapeKeyDown: function (event) {
            this._stopEvent(event);
            this._hideAutocompleteResultList();
        },

        _trySendJsfAjaxRequest: function () {
            var self = this;

            if (self.isRequestRunning) {
                // console.log("request is active, so remember that changes has been made while request was running");
                self.areChangesMadeWhileRequestWasRunning = true;
            }

            if (self.requestDelayTimerId !== null) {
                window.clearTimeout(self.requestDelayTimerId)
            }

            self.requestDelayTimerId = window.setTimeout(function () {
                self.requestDelayTimerId = null;
                self._sendJsfAjaxRequest();
            }, self.SEARCH_REQUEST_DELAY);
        },

        _sendJsfAjaxRequest: function () {
            var self = this;

            if (self.isRequestRunning) {
                // console.log("request is running, abort");
                return;
            }
            self.isRequestRunning = true;

            self.areChangesMadeWhileRequestWasRunning = false;
            self._showLoadingSpinner();

            // console.log("starting request");

            var id = self.$input.parent().parent().attr('id');

            jsf.ajax.request(id, "autocomplete", {
                "javax.faces.behavior.event": "autocomplete",
                render: self.autocompleteId,
                params: self.$input.val(),
                onevent: function (data) {
                    if (data.status === "success") {
                        // console.log("request finished");

                        // only show result if input field still has focus
                        if (self.$input.is(":focus")) {
                            self._handleAutocompleteResultListVisibility();
                        }
                        self._hideLoadingSpinner();
                        self.isRequestRunning = false;

                        if (self.areChangesMadeWhileRequestWasRunning) {
                            // console.log("changes made while request was running, start new request automatically");
                            self._sendJsfAjaxRequest();
                        }
                    }
                }
            });
        },

        _handleAutocompleteResultListVisibility: function () {
            var self = this;
            var $autocomplete = self._getAutocompleteElement();

            if ($autocomplete.find("li").length > 0) {
                self._initAndShowAutocompleteResultList();
            } else {
                self._hideAutocompleteResultList();
            }
        },

        _showLoadingSpinner: function () {
            $('<div class="butter-dropdownlist-spinner-container"><div class="butter-dropdownlist-spinner"></div></div>')
                    .appendTo(this.$input.parent());
        },

        _hideLoadingSpinner: function () {
            this.$input.parent().find(".butter-dropdownlist-spinner").remove();
        },

        _initAndShowAutocompleteResultList: function () {
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
                        self._setSelectedValue();
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

        _moveResultOptionElementSelectionCursor: function ($autocomplete, direction) {
            if (direction > 0) {
                var $next = this.$selectedOption.next();
                if ($next.length > 0) {
                    this._selectResultOptionElement($next[0]);
                } else {
                    //there is no next
                    this._selectResultOptionElement($autocomplete.find("li")[0]);
                }
            } else {
                var $prev = this.$selectedOption.prev();
                if ($prev.length > 0) {
                    this._selectResultOptionElement($prev[0]);
                } else {
                    //there is no previous
                    var resultListOptions = $autocomplete.find("li");
                    this._selectResultOptionElement(resultListOptions[resultListOptions.length - 1]);
                }
            }
        },

        _setSelectedValue: function () {
            if (this.$selectedOption !== null) {
                this.ignoreKeyupEvent = true;
                this.$input
                        .val(this.$selectedOption.attr("data-select-value"))
                        .change()
                        .focus()
                        .keyup();
                this._hideAutocompleteResultList();
            }
        },

        _hideAutocompleteResultList: function () {
            if (this.requestDelayTimerId !== null) {
                window.clearTimeout(this.requestDelayTimerId)
            }
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