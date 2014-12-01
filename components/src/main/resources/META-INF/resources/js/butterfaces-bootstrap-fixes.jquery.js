/**
 * jQuery-Plugin to handle bootstrap fixes.
 * Works with at least jQuery 1.3.2.
 *
 * How to use:
 * jQuery("#someTreeSelector").fixBootstrapDropDown();
 */
(function ($) {
    $.fn.fixBootstrapDropDown = function () {

        return this.each(function () {
            $('.dropdown-menu').on('click', function(e) {
                if($(this).hasClass('dropdown-menu-form')) {
                    e.stopPropagation();
                }
            });
        });

    };
}(jQuery));