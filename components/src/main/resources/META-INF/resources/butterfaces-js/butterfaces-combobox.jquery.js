(function ($) {
    // extend jQuery --------------------------------------------------------------------
    $.fn.butterCombobox = function () {
        return this.each(function () {
            new FilterableCombobox(this);
        });
    };

    // define objects --------------------------------------------------------------------

    /**
     * @class
     */
    var FilterableComboboxMouseEventListener = Class.extend({
        /**
         * Is called when a mouse down event is triggered on a result list option. Intended to be overridden.
         * @param {String} value the value of the result list option
         */
        onResultItemMouseDown: function (value) {
        },

        /**
         * Is called when a mouse enter event is triggered on a result list option. Intended to be overridden.
         * @param {String} value the value of the result list option
         */
        onResultItemMouseEnter: function (value) {
        }
    });

    /**
     * @class
     */
    var FilterableComboboxOption = Class.extend({
        /**
         * @constructs
         * @param {HTMLElement} optionElement
         * @param {FilterableComboboxMouseEventListener} mouseEventListener
         * @param {jQuery} $template
         */
        init: function (optionElement, mouseEventListener, $template) {
            this._$optionElement = $(optionElement);
            this._mouseEventListener = mouseEventListener;
            this._$resultElement;

            this._$noResultOption = $("<li>")
                .text("Keine Einträge vorhanden!")
                .addClass("butter-dropdownlist-container-noResultItems");

            this._initResultItem($template);
        },

        _initResultItem: function ($template) {
            var self = this;
            var resultItemHtml = self._$optionElement.text();
            if ($template !== undefined) {
                var dataFields = self._$optionElement.data();
                resultItemHtml = $template;
                $template.replace(/\{\{(.*?)\}\}/g, function (group0, group1) {
                    resultItemHtml = resultItemHtml.replace("{{" + group1 + "}}", dataFields[group1.toLowerCase()])
                    return;
                })
            }

            this._$resultElement = $("<li>")
                .html(resultItemHtml)
                .addClass("butter-dropdownlist-resultItem")
                .on("mousedown", function () {
                    self._mouseEventListener.onResultItemMouseDown(self.getValue());
                })
                .on("mouseenter", function () {
                    self._mouseEventListener.onResultItemMouseEnter(self.getValue());
                });
        },

        /**
         * @returns {String} the option's value
         */
        getValue: function () {
            return this._$optionElement.val();
        },

        /**
         * @returns {jQuery} a jQuery extended HTML element
         */
        getResultElement: function () {
            return this._$resultElement;
        }
    });

    /**
     * @class
     */
    var FilterableComboboxOptionList = Class.extend({
        /**
         * @constructs
         */
        init: function () {
            this._options = {};
        },

        /**
         * @param {FilterableComboboxOption} resultOption
         */
        add: function (resultOption) {
            this._options[resultOption.getValue()] = resultOption;
        },

        /**
         *
         * @param filter {String} the filter text
         * @returns {Array} the array with the filtered option elements
         */
        getFilteredResultElements: function (filter) {
            var filteredElements = [];
            for (var key in this._options) {
                var $resultElement = this._options[key];
                $resultElement.getResultElement().highlight(filter);
                if (filter === undefined || filter === "") {
                    filteredElements.push($resultElement.getResultElement());
                } else if ($resultElement.getResultElement().find(".search-highlighted").length > 0) {
                    filteredElements.push($resultElement.getResultElement());
                }
            }
            return filteredElements;
        },

        /**
         *
         * @param {String} value the value of the options that should be selected
         */
        select: function (value) {
            this.unselect();
            // TODO implementieren
        },

        selectPrevious: function (value) {
            // TODO implementieren
        },

        selectNext: function (value) {
            // TODO implementieren
        },

        /**
         * Unselect possible selected option
         */
        unselect: function () {
            // TODO implementieren
        },

        /**
         * * @returns {FilterableComboboxOption} the option that is selected
         */
        getSelected: function () {
            // TODO implementieren
            return;
        }
    });

    /**
     * @class
     */
    var FilterableCombobox = FilterableComboboxMouseEventListener.extend({
        /**
         * @constructs
         * @param {HTMLElement} rootElement
         */
        init: function (rootElement) {
            this.$select = $(rootElement).find("select");
            this.$template = this.$select.parent().siblings(".butter-component-combobox-resultListItemTemplate").html();
            this.$ghostInput = null;
            this.$resultContainer = null;
            this.$resultListContainer = null;
            this._optionList;
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
            this._initializeOptionList();
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
                    self._showOptionResultList(searchText);
                });

            if (self._disabled) {
                self.$ghostInput.attr('disabled', 'disabled');
            }
        },

        _initializeOptionList: function () {
            var self = this;
            self._optionList = new FilterableComboboxOptionList();
            self.$select.find("option").each(function (index, element) {
                self._optionList.add(new FilterableComboboxOption(element, self, self.$template));
            });
        },

        _handleEnterKeyDown: function (event) {
            if (this.$selectedOption !== null) {
                this._stopEvent(event);
                this._setSelectedValue();
                this._selectCompleteTextInGhostInput();
            } else if (this.$selectedOption === null && this.$resultContainer !== null) {
                // deactivate enter key if result list is opened but nothing is selected
                this._stopEvent(event);
            } else {
                this._resetDisplayValue();
            }
        },

        _handleArrowUpAndDownKeyDown: function (event) {
            this._stopEvent(event);
            // TODO
            /* if (this.optionResultList.length === 0) {
             this._createOptionResultList();
             }
             if (this.$resultContainer === null) {
             this._showOptionResultList();
             }*/

            // don't select the no result items message
            if (this.$resultListContainer.find(".butter-dropdownlist-container-noResultItems").length === 0) {
                if (this.$selectedOption === null) {
                    this._selectResultOptionElement(this.$resultListContainer.children()[0]);
                } else {
                    this._moveResultOptionElementSelectionCursor(event.which === this._keyCodes.arrow_up ? -1 : 1);
                }
            }
        },

        _handleEscapeKeyDown: function (event) {
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

                        self._showOptionResultList();
                        self.$ghostInput.focus();
                    });
            } else {
                self.$ghostInput.next().addClass('disabled');
            }
        },

        _showOptionResultList: function (searchText) {
            var self = this;

            // create container elements if necessary
            if (self.$resultContainer === null) {
                var $inputGroup = self.$ghostInput.parent();
                var inputGroupOffset = $inputGroup.offset();
                self.$resultContainer = $("<div>")
                    .addClass("butter-dropdownlist-container")
                    .addClass("butter-component-combobox-dropdownlist-container")
                    .css({
                        position: "absolute",
                        left: inputGroupOffset.left,
                        top: inputGroupOffset.top + $inputGroup.outerHeight(),
                        width: $inputGroup.innerWidth()
                    });

                self.$resultListContainer = $("<ul>")
                    .addClass("butter-dropdownlist-resultList")
                    .on("mouseleave", function () {
                        self._clearResultOptionSelection();
                    })
                    .appendTo(self.$resultContainer);
                $("body").append(self.$resultContainer);
            }

            self._detachResultListElements();
            self.$selectedOption = null;

            $.each(self._optionList.getFilteredResultElements(searchText), function (index, element) {
                self.$resultListContainer.append(element);
            });
        },

        _hideOptionResultList: function () {
            var self = this;
            if (self.$resultContainer !== null) {
                self._detachResultListElements();
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

        _clearResultOptionSelection: function () {
            this.$selectedOption = null;
            this.$resultListContainer
                .find(".butter-dropdownlist-resultItem-selected")
                .removeClass("butter-dropdownlist-resultItem-selected");
        },

        _detachResultListElements: function () {
            this.$resultListContainer.children().detach();
        },

        /**
         * @inheritdoc
         */
        onResultItemMouseDown: function (value) {
            console.log("FilterableCombobox.onResultItemMouseDown: %s", value);
        },

        /**
         * @inheritdoc
         */
        onResultItemMouseEnter: function (value) {
            console.log("FilterableCombobox.onResultItemMouseEnter: %s", value);
        },

        _moveResultOptionElementSelectionCursor: function (direction) {
            if (direction > 0) {
                this._optionList.selectNext();
            } else {
                this._optionList.selectPrevious();
            }
        },

        _setSelectedValue: function () {
            var $selectedOption = this.$selectedOption;
            if ($selectedOption !== null) {
                var valueData = $selectedOption.attr("data-select-value");
                if (valueData !== undefined) {
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

        _selectCompleteTextInGhostInput: function () {
            this.$ghostInput[0].setSelectionRange(0, this.$ghostInput.val().length);
        }
    });
}(jQuery));