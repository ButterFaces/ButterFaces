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
            this._initResultItem($template);
        },

        _initResultItem: function ($template) {
            var self = this;
            var resultItemHtml = self._$optionElement.text();
            if ($template !== undefined) {
                var dataFields = self._$optionElement.data();
                // for the no selection option there are no data fields. don't take the template in this case!
                if(!$.isEmptyObject(dataFields)) {
                    resultItemHtml = $template;
                    $template.replace(/\{\{(.*?)\}\}/g, function (group0, group1) {
                        resultItemHtml = resultItemHtml.replace("{{" + group1 + "}}", dataFields[group1.toLowerCase()] || '')
                        return;
                    })
                }
            }

            self._$resultElement = $("<li>")
                .html(resultItemHtml)
                .addClass("butter-dropdownlist-resultItem");

            if (self._mouseEventListener !== undefined) {
                self._$resultElement.
                    on("mousedown", function () {
                        self._mouseEventListener.onResultItemMouseDown(self.getValue());
                    })
                    .on("mouseenter", function () {
                        self._mouseEventListener.onResultItemMouseEnter(self.getValue());
                    });
            }
        },

        /**
         * @returns {String} the option's value
         */
        getValue: function () {
            return this._$optionElement.val();
        },

        /**
         * @returns {String} the option's label text. This is the text between the option tags.
         */
        getLabel: function () {
            return this._$optionElement.text();
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
    var FilterableComboboxEmtpyOption = FilterableComboboxOption.extend({
        /**
         * @constructs
         */
        init: function () {
            this._$optionElement = $("<option>")
                .text(butter.message.get("combobox.noResultsFound"));
            this._$resultElement;
            this._initResultItem();
            this.getResultElement()
                .removeClass("butter-dropdownlist-resultItem")
                .addClass("butter-dropdownlist-noResultItem");
        },

        /**
         * @inheritDoc
         */
        getValue: function () {
            return "butter-noResultItem";
        },
    });

    /**
     * @class
     */
    var FilterableComboboxOptionList = Class.extend({
        /**
         * @constructs
         */
        init: function () {
            this._options = [];
            this._filteredOptions = [];
            this._selectedIndex = -1;
        },

        /**
         * @param {FilterableComboboxOption} resultOption
         */
        add: function (resultOption) {
            this._options.push(resultOption);
        },

        /**
         *
         * @param filter {String} the filter text
         * @returns {Array} the array with the filtered option elements
         */
        getFilteredOptions: function (filter) {
            //console.log("FilterableComboboxOptionList - getting filtered options for '%s'", filter);
            var self = this;
            self.unselect();
            self._filteredOptions = [];

            $.each(self._options, function (index, element) {
                element.getResultElement().highlight(filter);
                if (filter === undefined || filter === "") {
                    self._filteredOptions.push(element);
                } else if (element.getResultElement().find(".search-highlighted").length > 0) {
                    self._filteredOptions.push(element);
                }
            });

            if (this.hasResultOptions()) {
                //console.log("FilterableComboboxOptionList - returning %s results", self._filteredOptions.length);
                return self._filteredOptions;
            } else {
                //console.log("FilterableComboboxOptionList - found no result returning empty result option");
                return [new FilterableComboboxEmtpyOption()];
            }
        },

        /**
         *
         * @param {String} value the value of the options that should be selected
         */
        select: function (value) {
            if (!this.hasResultOptions()) {
                return;
            }

            this.unselect();
            for (var i = 0; i < this._filteredOptions.length; i++) {
                if (this._filteredOptions[i].getValue() === value) {
                    this._doSelectAndMarkElement(i);
                    break;
                }
            }
        },

        /**
         * Select the previous element in the filtered list. If there is no previous element the last will be selected.
         */
        selectPrevious: function () {
            if (!this.hasResultOptions()) {
                return;
            }

            this._unmarkSelectedElement();
            if (this.hasSelected()) {
                if (this._selectedIndex - 1 >= 0) {
                    this._doSelectAndMarkElement(this._selectedIndex - 1);
                } else {
                    this._doSelectAndMarkElement(this._filteredOptions.length - 1);
                }
            } else {
                this._doSelectAndMarkElement(0);
            }
        },

        /**
         * Select the nex element in the filtered list. If there is no next element the first will be selected.
         */
        selectNext: function () {
            if (!this.hasResultOptions()) {
                return;
            }

            this._unmarkSelectedElement();
            if (this.hasSelected()) {
                if (this._selectedIndex + 1 < this._filteredOptions.length) {
                    this._doSelectAndMarkElement(this._selectedIndex + 1);
                } else {
                    this._doSelectAndMarkElement(0);
                }
            } else {
                this._doSelectAndMarkElement(0);
            }
        },

        /**
         * Unselect possible selected option
         */
        unselect: function () {
            if (this.hasResultOptions()) {
                this._unmarkSelectedElement();
            }
            this._selectedIndex = -1;
        },

        _unmarkSelectedElement: function () {
            if (this.hasSelected()) {
                //console.log("FilterableComboboxOptionList - unmark selected element on position %s", this._selectedIndex);
                this.getSelected().getResultElement().removeClass("butter-dropdownlist-resultItem-selected");
            }
        },

        /**
         * * @returns {FilterableComboboxOption} the option that is selected
         */
        getSelected: function () {
            return this.hasSelected() ? this._filteredOptions[this._selectedIndex] : null;
        },

        /**
         * @returns {boolean} true if an element is selected
         */
        hasSelected: function () {
            return this._selectedIndex >= 0;
        },

        /**
         * @returns {boolean} true if there elements in the filtered list
         */
        hasResultOptions: function () {
            return this._filteredOptions.length > 0;
        },

        _doSelectAndMarkElement: function (index) {
            this._filteredOptions[index].getResultElement().addClass("butter-dropdownlist-resultItem-selected");
            this._selectedIndex = index;
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
            this.$template = this.$select.parent().siblings(".butter-component-combobox-template").html();
            this.$ghostInput = null;
            this.$resultContainer = null;
            this.$resultListContainer = null;
            this._optionList;
            this._hasFocus = false;
            this._disabled = this.$select.is('[disabled=disabled]');
            this._isMouseClickBlocked = false;
            this._isMouseEnterBlocked = false;

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
                    self._isMouseClickBlocked = self.$resultContainer !== null;

                    window.setTimeout(function () {
                        self._isMouseClickBlocked = false;
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
            if (this._optionList.hasSelected() && this._isResultContainerShown()) {
                this._stopEvent(event);
                this._setSelectedValue();
                this._selectCompleteTextInGhostInput();
            } else if (!this._optionList.hasSelected() && this._isResultContainerShown()) {
                // deactivate enter key if result list is opened but nothing is selected
                this._stopEvent(event);
            } else {
                this._resetDisplayValue();
            }
        },

        _handleArrowUpAndDownKeyDown: function (event) {
            this._stopEvent(event);
            if (!this._isResultContainerShown()) {
                this._showOptionResultList();
            }

            this._moveResultOptionElementSelectionCursor(event.which === this._keyCodes.arrow_up ? -1 : 1);
        },

        _handleEscapeKeyDown: function (event) {
            if (this._isResultContainerShown()) {
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

                        if (self._isMouseClickBlocked) {
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
            if (!self._isResultContainerShown()) {
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
                        self._optionList.unselect();
                    })
                    .appendTo(self.$resultContainer);
                $("body").append(self.$resultContainer);
            }

            self._detachResultListElements();
            self.$selectedOption = null;

            $.each(self._optionList.getFilteredOptions(searchText), function (index, element) {
                self.$resultListContainer.append(element.getResultElement());
            });
        },

        _hideOptionResultList: function () {
            var self = this;
            if (self._isResultContainerShown()) {
                self._detachResultListElements();
                self.$resultContainer.remove();
                self.$resultContainer = null;
                self.$resultListContainer = null;
                this._optionList.unselect();
            }
        },

        _detachResultListElements: function () {
            this._optionList.unselect();
            this.$resultListContainer.children().detach();
        },

        /**
         * @inheritdoc
         */
        onResultItemMouseDown: function (value) {
            this._setSelectedValue();
        },

        /**
         * @inheritdoc
         */
        onResultItemMouseEnter: function (value) {
            if (!this._isMouseEnterBlocked) {
                this._optionList.select(value);
            }
        },

        _moveResultOptionElementSelectionCursor: function (direction) {
            var self = this;

            if (direction > 0) {
                self._optionList.selectNext();
            } else {
                self._optionList.selectPrevious();
            }

            self._isMouseEnterBlocked = true;
            self._scrollToSelectedOptionElementIfNecessary();

            // this is necessary because if the mouse cursor is over a result item after the list has been scrolled a
            // mouse enter event will be triggered and the result item under the mouse cursor will be selected unintentionally.
            window.setTimeout(function () {
                self._isMouseEnterBlocked = false;
            }, 100);
        },

        _setSelectedValue: function () {
            if (this._optionList.hasSelected()) {
                var selectOption = this._optionList.getSelected();
                this.$select
                    .val(selectOption.getValue())
                    .change();
                this.$ghostInput.val(selectOption.getLabel());
                this._hideOptionResultList();
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
        },

        _isResultContainerShown: function () {
            return this.$resultContainer !== null;
        },

        _scrollToSelectedOptionElementIfNecessary: function () {
            if (this._optionList.hasSelected()) {
                var $item = this._optionList.getSelected().getResultElement();
                var containerHeight = this.$resultContainer.innerHeight();
                var containerScrollTop = this.$resultContainer.scrollTop();
                //console.log("FilterableCombobox - containerHeight: %s", containerHeight);
                //console.log("FilterableCombobox - containerScrollTop: %s", containerScrollTop);
                var itemHeight = $item.outerHeight();
                //console.log("FilterableCombobox - itemHeight: %s", itemHeight);
                var itemTop = $item.position().top;
                //console.log("FilterableCombobox - itemTop: %s", itemTop);

                if (itemTop < 0) {
                    // resultItem is scrolled out to top
                    this.$resultContainer.scrollTop(containerScrollTop + itemTop);
                } else if (itemTop + itemHeight > containerHeight) {
                    // resultItem is scrolled out to bottom
                    this.$resultContainer.scrollTop(containerScrollTop + ((itemTop + itemHeight) - containerHeight));
                }
            }
        }
    });
}(jQuery));