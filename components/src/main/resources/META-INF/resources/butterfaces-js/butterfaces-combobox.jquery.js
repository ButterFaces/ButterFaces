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
                    this.setSelectionRange(0, $(this).val().length)
                })
                .on("mouseup", function (event) {
                    // in safari the mouseup event unselects the text, so we have to prevent this
                    self._stopEvent(event);
                })
                .on("blur", function () {
                    self._hasFocus = false;
                    window.setTimeout(function () {
                        if (!self._hasFocus) {
                            self._hideOptionResultList();
                            self._resetDisplayValue();
                        }
                    }, 200);
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
                })
                .on("keydown", function (event) {
                    if (event.which === self._keyCodes.enter) {
                        self._stopEvent(event);
                        if (self.$selectedOption !== null) {
                            self._setSelectedValue();
                        }
                    } else if (event.which === self._keyCodes.arrow_up || event.which === self._keyCodes.arrow_down) {
                        self._stopEvent(event);
                        if (self.optionResultList.length === 0) {
                            self._createOptionResultList();
                        }
                        if (self.$resultContainer === null) {
                            self._showOptionResultList();
                        }
                        if (self.$selectedOption === null) {
                            self._selectResultOptionElement(self.$resultListContainer.children()[0]);
                        } else {
                            self._moveResultOptionElementSelectionCursor(event.which === self._keyCodes.arrow_up ? -1 : 1);
                        }
                    } else if (event.which === self._keyCodes.escape) {
                        if (self.$resultContainer !== null) {
                            self._hideOptionResultList();
                            self._resetDisplayValue();
                            this.setSelectionRange(0, $(this).val().length);
                        } else {
                            self.$ghostInput.blur();
                        }
                    }
                });

        },

        _initializeDropDownButton: function () {
            var self = this;
            self.$ghostInput.next()
                .on("click", function (event) {
                    self._stopEvent(event);
                    self._createOptionResultList();
                    self._showOptionResultList();
                    self.$ghostInput.focus();
                });
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
            // create container elements if necessary
            if (this.$resultContainer === null) {
                var ghostOffset = this.$ghostInput.offset();
                this.$resultContainer = $("<div>")
                    .addClass("butter-component-combobox-resultContainer")
                    .css({
                        position: "absolute",
                        left: ghostOffset.left,
                        top: ghostOffset.top + this.$ghostInput.outerHeight(),
                        minWidth: ghostOffset.top + this.$ghostInput.innerWidth(),
                        zIndex: 1000
                    });
                this.$resultListContainer = $("<ul>")
                    .addClass("butter-component-combobox-resultListContainer")
                    .appendTo(this.$resultContainer);
                $("body").append(this.$resultContainer);
            }

            this.$resultListContainer.empty();
            this.$selectedOption = null;

            if (this.optionResultList.length > 0) {
                for (var i = 0; i < this.optionResultList.length; i++) {
                    var resultItemHtml;
                    var resultItemLabel;
                    var resultItemText = this.optionResultList[i].text();
                    var separatorIndex = resultItemText.indexOf(" - ");
                    if (separatorIndex > 0) {
                        resultItemLabel = resultItemText.substring(0, separatorIndex);
                        resultItemHtml = "<b>" + resultItemLabel + "</b>";
                        resultItemHtml += "<br/>" + resultItemText.substr(separatorIndex + 3);
                    } else {
                        resultItemLabel = resultItemText;
                        resultItemHtml = "<b>" + resultItemText + "</b>";
                    }

                    var self = this;
                    $("<li>")
                        .html(resultItemHtml)
                        .attr("data-select-value", this.optionResultList[i].val())
                        .attr("data-select-label", resultItemLabel)
                        .addClass("butter-component-combobox-resultItem")
                        .on("click", function () {
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
                    .addClass("butter-component-combobox-noResultItems")
                    .appendTo(this.$resultListContainer);
            }
        },

        _hideOptionResultList: function () {
            var self = this;
            if (self.$resultContainer !== null) {
                self.$resultContainer.remove();
                self.$resultContainer = null;
                self.$resultListContainer = null;
            }
        },

        _selectResultOptionElement: function (optionElement) {
            var selectedOptionElement = $(optionElement);
            selectedOptionElement.addClass("butter-component-combobox-resultItem-selected")
                .siblings().removeClass("butter-component-combobox-resultItem-selected");
            this.$selectedOption = selectedOptionElement;
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
            event.preventDefault();
            //event.preventBubble();
        }
    });
}
(jQuery)
)
;