/**
 * jQuery-Plugin "butterHandleAutoComplete" for text autocomplete tag. Initializes auto complete functionality to
 * text component.
 *
 * How to use:
 * jQuery("#selector").butterHandleAutoComplete();
 */
(function ($) {
    // extend jQuery --------------------------------------------------------------------

    $.fn.butterHandleAutoComplete = function () {
        return this.each(function () {
            var $originalElement = $(this);
            var $input = $originalElement.prev();

            $originalElement.find('li').on("click", function () {
                $input.val($(this).attr("data-select-value")).change();
            })

        });
    };

}(jQuery));