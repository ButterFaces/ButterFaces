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
            this.$autocomplete = $(rootElement);
            this.$input = this.$autocomplete.prev();

            var self = this;
            self.$input.keyup(function (event) {
                var testOnEvent = function (data) {
                    var $autocomplete2 = $(document.getElementById(self.$autocomplete.attr('id')));
                    if (data.status === 'success') {
                        if (self.$input.data('data-test') === undefined) {
                            if ($autocomplete2.has('li').size() > 0) {
                                $autocomplete2
                                        .css('display', 'inline')
                                        .highlight(self.$input.val())
                                        .find('li').on("click", function () {
                                            self.$input
                                                    .val($(this).attr("data-select-value"))
                                                    .change()
                                                    .data('data-test', 'true')
                                                    .keyup()
                                                    .focus();
                                        });
                            } else {
                                $autocomplete2.css('display', 'none');
                            }
                        } else {
                            $autocomplete2.css('display', 'none');
                            self.$input.removeData('data-test');
                        }
                    }
                };

                if (self.$input.val().length === 0) {
                    event.stopPropagation();
                    event.preventDefault();
                    $(document.getElementById(self.$autocomplete.attr('id'))).css('display', 'none');
                    self.$input.removeData('data-test');
                    return;
                }

                jsf.ajax.request(this, 'autocomplete', {
                    'javax.faces.behavior.event': 'autocomplete',
                    render: self.$autocomplete.attr('id'),
                    params: self.$input.val(),
                    onevent: testOnEvent
                });
            });

            self.$input.blur(function () {
                window.setTimeout(function () {
                    $(document.getElementById(self.$autocomplete.attr('id')))
                            .css('display', 'none');
                }, 100);
            });
        }
    });

}(jQuery));