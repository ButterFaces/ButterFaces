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
            this.$autocomplete = $(rootElement);
            this.$input = this.$autocomplete.prev();

            var self = this;
            self.$input.keyup(function (event) {
                if (self.$input.val().length === 0) {
                    event.stopPropagation();
                    event.preventDefault();
                    self._getAutocomplete().hide();
                    self.$input.removeData("data-test");
                    return;
                }

                jsf.ajax.request(this, "autocomplete", {
                    "javax.faces.behavior.event": "autocomplete",
                    render: self.$autocomplete.attr("id"),
                    params: self.$input.val(),
                    onevent: function (data) {
                        self._testOnEvent(data);
                    }
                });
            });

            self.$input.blur(function () {
                window.setTimeout(function () {
                    self._getAutocomplete().hide();
                }, 100);
            });
        },

        _testOnEvent: function (data) {
            console.log("_testOnEvent");
            var self = this;
            var $autocomplete2 = $(document.getElementById(self.$autocomplete.attr("id")));
            if (data.status === "success") {
                if (self.$input.data("data-test") === undefined) {
                    if ($autocomplete2.has("li").size() > 0) {
                        $autocomplete2
                                .show()
                                .css({
                                    width: self.$input.innerWidth()
                                })
                                .highlight(self.$input.val())
                                .find("li").on("click", function () {
                                    self.$input
                                            .val($(this).attr("data-select-value"))
                                            .change()
                                            .data("data-test", "true")
                                            .keyup()
                                            .focus();
                                });
                    } else {
                        $autocomplete2.hide();
                    }
                } else {
                    $autocomplete2.hide();
                    self.$input.removeData("data-test");
                }
            }
        },

        _getAutocomplete: function () {
            return $(document.getElementById(this.$autocomplete.attr("id")));
        }
    });

}(jQuery));