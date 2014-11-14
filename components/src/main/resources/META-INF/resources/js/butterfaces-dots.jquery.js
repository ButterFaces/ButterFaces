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

    var intervalTrigger = null;

    $.fn.startDots = function () {
        return this.each(function () {
            var $originalElement = $(this);

            $originalElement.html('');

            intervalTrigger = setInterval(function () {
                $originalElement.append('.');

                if ($originalElement.html().length > 5) {
                    $originalElement.html('');
                }
            }, 200);
        });
    };

    $.fn.stopDots = function () {
        return this.each(function () {
            var $originalElement = $(this);

            $originalElement.html('');
            window.clearInterval(intervalTrigger);
        });
    };

}(jQuery));