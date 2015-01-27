/**
 * jQuery-Plugin "Animate dots" for tree animation. Animates a waiting dot line (4 dots) in an interval of 200 millis
 * as html string in given component. Note: existing html code will be cleared.
 * Works with at least jQuery 1.3.2.
 *
 * How to use:
 * jQuery("#selector").startDots();
 * jQuery("#selector").stopDots();
 */
(function ($) {
    // extend jQuery --------------------------------------------------------------------

    $.fn.markTagsInputAsReadonly = function () {
        return this.each(function () {
            var $originalElement = $(this);

            $originalElement.addClass('butter-component-tags-readonly');
            $originalElement.find('.bootstrap-tagsinput span[data-role=remove]').css({'display':'none'});
            $originalElement.find('.bootstrap-tagsinput input[type=text]').css({'display':'none'});
        });
    };

}(jQuery));