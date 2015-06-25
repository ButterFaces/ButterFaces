(function ($) {
    // extend jQuery --------------------------------------------------------------------
    $.fn.butterCombobox = function () {
        return this.each(function () {
            new FilterableCombobox(this);
        });
    };

    // define objects --------------------------------------------------------------------

    var FilterableCombobox = Class.extend({
        init: function (rootElement) {
            this.$select = $(rootElement).find("select");
            this.$ghostInput = null;
            this.$resultContainer = null;
            this.$resultListContainer = null;
            this.$options = this.$select.find("option");
            this.optionResultList = [];
            this.$selectedOption = null;
            this._hasFocus = false;
            this._disabled = this.$select.is('[disabled=disabled]');
            this.isMouseClickBlocked = false;

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

            this._initializeGhostElement();
            this._initializeDropDownButton();
        },

        _initializeGhostElement: function () {
            var self = this;
            self.$ghostInput = self.$select.parent().find(".butter-component-combobox-ghost")
                //.addClass($select.attr("class"))
                .val(self.$select.find("option:selected").text())
                .on("focus", function () {
                    self._hasFocus = true;
                    // automatically select whole text on focus
                    self._selectCompleteTextInGhostInput();
                })
                .on("mouseup", function (event) {
                    // in safari the mouseup event unselects the text, so we have to prevent this
                    self._stopEvent(event);
                })
                .on("blur", function () {
                    self._hasFocus = false;
                    // block next mouse click only if the result list is opened
                    self.isMouseClickBlocked = self.$resultContainer !== null;

                    window.setTimeout(function () {
                        self.isMouseClickBlocked = false;
                        if (!self._hasFocus) {
                            self._hideOptionResultList();
                            self._resetDisplayValue();
                        }
                    }, 200);
                })
                .on("keydown", function (event) {
                    if (event.which === self._keyCodes.enter) {
                        self._handleEnterKeyDown(event);
                    } else if (event.which === self._keyCodes.arrow_up || event.which === self._keyCodes.arrow_down) {
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

                    var searchText = $(this).val();
                    self._createOptionResultList(searchText);
                    self._showOptionResultList(searchText);
                });

            if (self._disabled) {
                self.$ghostInput.attr('disabled', 'disabled');
            }
        },

        _handleEnterKeyDown: function(event) {
            if (this.$selectedOption !== null) {
                this._stopEvent(event);
                this._setSelectedValue();
                this._selectCompleteTextInGhostInput();
            }else if(this.$selectedOption === null && this.$resultContainer !== null){
                // deactivate enter key if result list is opened but nothing is selected
                this._stopEvent(event);
            }else{
                this._resetDisplayValue();
            }
        },

        _handleArrowUpAndDownKeyDown: function(event) {
            this._stopEvent(event);
            if (this.optionResultList.length === 0) {
                this._createOptionResultList();
            }
            if (this.$resultContainer === null) {
                this._showOptionResultList();
            }

            // don't select the no result items message
            if(this.$resultListContainer.find(".butter-dropdownlist-container-noResultItems").length === 0){
                if (this.$selectedOption === null) {
                    this._selectResultOptionElement(this.$resultListContainer.children()[0]);
                } else {
                    this._moveResultOptionElementSelectionCursor(event.which === this._keyCodes.arrow_up ? -1 : 1);
                }
            }
        },

        _handleEscapeKeyDown: function(event) {
            if (this.$resultContainer !== null) {
                this._hideOptionResultList();
                this._resetDisplayValue();
                this._selectCompleteTextInGhostInput();
            } else {
                this.$ghostInput.blur();
            }
        },

        _initializeDropDownButton: function () {
            var self = this;
            if (!self._disabled) {
                self.$ghostInput.next()
                    .on("click", function (event) {
                        self._stopEvent(event);

                        if (self.isMouseClickBlocked) {
                            return;
                        }

                        self._createOptionResultList();
                        self._showOptionResultList();
                        self.$ghostInput.focus();
                    });
            } else {
                self.$ghostInput.next().addClass('disabled');
            }
        },

        _createOptionResultList: function (searchText) {
            var self = this;
            self.optionResultList = [];
            if (!searchText || searchText === "") {
                self.$options.each(function () {
                    var $option = $(this);
                    self.optionResultList.push($option);
                });
            } else {
                self.$options.each(function () {
                    var $option = $(this);
                    if ($option.text().toLowerCase().indexOf(searchText.toLowerCase()) >= 0) {
                        self.optionResultList.push($option);
                    }
                });
            }
        },

        _showOptionResultList: function (searchText) {
            var self = this;

            // create container elements if necessary
            if (this.$resultContainer === null) {
                var $inputGroup = this.$ghostInput.parent();
                var inputGroupOffset = $inputGroup.offset();
                this.$resultContainer = $("<div>")
                    .addClass("butter-dropdownlist-container")
                    .addClass("butter-component-combobox-dropdownlist-container")
                    .css({
                        position: "absolute",
                        left: inputGroupOffset.left,
                        top: inputGroupOffset.top + $inputGroup.outerHeight(),
                        width: $inputGroup.innerWidth()
                    });

                this.$resultListContainer = $("<ul>")
                    .addClass("butter-dropdownlist-resultList")
                    .on("mouseleave", function() {
                        self._clearResultOptionSelection();
                    })
                    .appendTo(this.$resultContainer);
                $("body").append(this.$resultContainer);
            }

            this.$resultListContainer.empty();
            this.$selectedOption = null;

            if (this.optionResultList.length > 0) {
                for (var i = 0; i < this.optionResultList.length; i++) {
                    var resultItemText = this.optionResultList[i].text();

                    var self = this;
                    $("<li>")
                        .text(resultItemText)
                        .attr("data-select-value", this.optionResultList[i].val())
                        .attr("data-select-label", resultItemText)
                        .addClass("butter-dropdownlist-resultItem")
                        .on("mousedown", function () {
                            self._setSelectedValue();
                        })
                        .on("mouseenter", function () {
                            self._selectResultOptionElement(this);
                        })
                        .appendTo(self.$resultListContainer)
                        .highlight(searchText);
                }
            } else {
                $("<li>")
                    .text("Keine Einträge vorhanden!")
                    .addClass("butter-dropdownlist-container-noResultItems")
                    .appendTo(this.$resultListContainer);
            }
        },

        _hideOptionResultList: function () {
            var self = this;
            if (self.$resultContainer !== null) {
                self.$resultContainer.remove();
                self.$resultContainer = null;
                self.$resultListContainer = null;
                this.$selectedOption = null;
            }
        },

        _selectResultOptionElement: function (optionElement) {
            this._clearResultOptionSelection();
            var $selectedOptionElement = $(optionElement);
            $selectedOptionElement.addClass("butter-dropdownlist-resultItem-selected");
            this.$selectedOption = $selectedOptionElement;
        },

        _clearResultOptionSelection: function() {
            this.$selectedOption = null;
            this.$resultListContainer
                    .find(".butter-dropdownlist-resultItem-selected")
                    .removeClass("butter-dropdownlist-resultItem-selected");
        },

        _moveResultOptionElementSelectionCursor: function (direction) {
            if (direction > 0) {
                var $next = this.$selectedOption.next();
                if ($next.length > 0) {
                    this._selectResultOptionElement($next[0]);
                } else {
                    //there is no next
                    this._selectResultOptionElement(this.$resultListContainer.children()[0]);
                }
            } else {
                var $prev = this.$selectedOption.prev();
                if ($prev.length > 0) {
                    this._selectResultOptionElement($prev[0]);
                } else {
                    //there is no previous
                    var resultListOptions = this.$resultListContainer.children();
                    this._selectResultOptionElement(resultListOptions[resultListOptions.length - 1]);
                }
            }
        },

        _setSelectedValue: function () {
            var $selectedOption = this.$selectedOption;
            if ($selectedOption !== null) {
                var valueData = $selectedOption.attr("data-select-value");
                if(valueData !== undefined) {
                    this.$select
                        .val(valueData)
                        .change();
                    this.$ghostInput.val($selectedOption.attr("data-select-label"));
                    this._hideOptionResultList();
                }
            }
        },

        _resetDisplayValue: function () {
            // set old value
            this.$ghostInput.val(this.$select.find("option:selected").text());
        },

        _stopEvent: function (event) {
            event.stopPropagation();
            event.preventDefault();
        },

        _selectCompleteTextInGhostInput: function() {
            this.$ghostInput[0].setSelectionRange(0, this.$ghostInput.val().length);
        }
    });
}(jQuery));