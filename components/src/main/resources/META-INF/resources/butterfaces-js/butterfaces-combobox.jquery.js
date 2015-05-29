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
            this.optionList = [];
            this.resultSelected = false;

            this._initializeGhostElement();
            this._initializeDropDownButton();
        },

        _initializeGhostElement: function () {
            var self = this;
            self.$ghostInput = self.$select.parent().find(".butter-component-combobox-ghost")
                //.addClass($select.attr("class"))
                .val(self._formatDisplayValue(self.$select.find("option:selected").text()))
                .on("keyup", function (event) {
                    self.optionList = [];
                    var searchText = $(this).val();
                    self.$options.each(function () {
                        var $option = $(this);
                        if ($option.text().toLowerCase().indexOf(searchText.toLowerCase()) >= 0) {
                            self.optionList.push($option);
                        }
                    });
                    self._renderResult(searchText);
                })
                // handle return button
                .on("keydown", function (event) {
                    if (event.keyCode === 13) {
                        event.preventDefault();
                        if (self.optionList.length > 0) {
                            self.$select
                                .val(self.optionList[0].val())
                                .change();
                            self.$ghostInput.val(self._formatDisplayValue(self.optionList[0].text()));
                            self.resultSelected = true;
                            self._removeResultList();
                        }
                    }
                })
                .on("focus", function () {
                    // automatically select whole text on focus
                    this.setSelectionRange(0, $(this).val().length)
                })
                .on("mouseup", function (event) {
                    // in safari the mouseup event unselects the text, so we have to prevent this
                    event.preventDefault();
                })
                .on("blur", function () {
                    self._removeResultList();
                });
        },

        _initializeDropDownButton: function () {
            var self = this;
            self.$ghostInput.next()
                .on("click", function (event) {
                    event.preventDefault();
                    self.optionList = [];
                    self.$options.each(function () {
                        var $option = $(this);
                        self.optionList.push($option);
                    });
                    self._renderResult();
                    self.$ghostInput.focus();
                });
        },

        _formatDisplayValue: function (value) {
            return value;
        },

        _renderResult: function (searchText) {
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

            if (this.optionList.length > 0) {
                for (var i = 0; i < this.optionList.length; i++) {
                    var resultItemHtml;
                    var resultItemLabel;
                    var resultItemText = this.optionList[i].text();
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
                    var test = $("<li>")
                        .html(resultItemHtml)
                        .attr("data-select-value", this.optionList[i].val())
                        .attr("data-select-label", resultItemLabel)
                        .addClass("butter-component-combobox-resultItem")
                        .on("click", function () {
                            self.$select
                                .val($(this).attr("data-select-value"))
                                .change();
                            self.$ghostInput.val(self._formatDisplayValue($(this).attr("data-select-label")));
                            self.resultSelected = true;
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

        _removeResultList: function () {
            var self = this;
            if (self.$resultContainer !== null) {
                window.setTimeout(function () {
                    if (!self.resultSelected) {
                        self._tryToSetValueAfterBlur();
                    }
                    self.$resultContainer.remove();
                    self.$resultContainer = null;
                    self.$resultListContainer = null;
                    self.resultSelected = false;
                }, 200);
            }
        },

        _tryToSetValueAfterBlur: function () {
            var self = this;
            var value = self.$ghostInput.val();
            var valueValid = false;
            if (value !== undefined && value !== "") {
                self.$options.each(function () {
                    if (!valueValid && $(this).text() === value) {
                        valueValid = true;
                        self.$select.val($(this).val());
                    }
                });
            }

            if (!valueValid) {
                // set old value
                self.$ghostInput.val(self._formatDisplayValue(self.$select.find("option:selected").text()));
            }
        }

    });
}(jQuery));