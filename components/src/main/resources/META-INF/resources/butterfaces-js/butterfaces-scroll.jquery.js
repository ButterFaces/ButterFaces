/**
 * How to use:
 * jQuery("#someSelector").scrollToFirst() or jQuery("#someSelector").scrollToLast();
 */
(function ($) {
    // extend jQuery --------------------------------------------------------------------
    $.fn.butterScrollToFirst = function (/* int */ offset) {
        console.log(offset);
        var _offset = offset === undefined ? $(this).first().offset().top : $(this).first().offset().top + offset;
        $('html, body').animate({scrollTop: _offset + 'px'}, 'fast');
    };

    $.fn.butterScrollToLast = function (/* int */ offset) {
        console.log(offset);
        var _offset = offset === undefined ? $(this).last().offset().top : $(this).last().offset().top + offset;
        $('html, body').animate({scrollTop: _offset + 'px'}, 'fast');
    };
}(jQuery));