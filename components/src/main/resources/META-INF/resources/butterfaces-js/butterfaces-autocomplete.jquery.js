/**
 * jQuery-Plugin "butterHandleAutoComplete" for text autocomplete tag. Initializes auto complete functionality to
 * text component.
 *
 * How to use:
 * jQuery("#selector").butterHandleAutoComplete();
 */
(function ($) {
    // extend jQuery --------------------------------------------------------------------

    $.fn._butterAutoCompleteOnKeyUp = function() {
        return this.each(function () {
            var $autocomplete = $(this);
            var $input = $autocomplete.prev();

            if ($input.data('data-test') === undefined) {
                if ($autocomplete.has('li').size() > 0) {
                    $autocomplete.css('display', 'inline');
                } else {
                    $autocomplete.css('display', 'none');
                }
            } else {
                $autocomplete.css('display', 'none');
                $input.removeData('data-test');
            }
        });
    }

    $.fn._butterAutoCompleteInit = function () {
        return this.each(function () {
            var $autocomplete = $(this);
            var $input = $autocomplete.prev();

            $input.attr('autocomplete', 'off');
            $input.attr('autocorrect', 'off');
        });
    };

    $.fn._butterAutoCompleteAddClickSupport = function () {
        return this.each(function () {
            var $autocomplete = $(this);
            var $input = $autocomplete.prev();

            $autocomplete.find('li').on("click", function () {
                $input.val($(this).attr("data-select-value")).change();
                $input.data('data-test', 'blub');
                $input.keyup();
            })
        });
    };

}(jQuery));