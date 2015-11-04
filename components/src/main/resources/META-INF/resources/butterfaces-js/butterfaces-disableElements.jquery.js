(function ($) {
    $.fn.butterDisableElements = function () {

        return this.each(function () {
            var $originalElement = $(this);

            var $overlay = $('<div class="butter-component-link-overlay"><div class="butter-component-spinner"><div></div><div></div><div></div><div></div></div></div>');
            $overlay.offset($originalElement.offset());
            $overlay.width($originalElement.outerWidth());
            $overlay.height($originalElement.outerHeight());
            // IE overrides css position so set it here
            $overlay.css({'position':'absolute'});

            if ($overlay.height() > 0 && $overlay.width() > 0) {
                $("body").append($overlay);
                $overlay.show();
            }
        });
    };

    $.fn.butterEnableElements = function () {

        return this.each(function () {
            $(".butter-component-link-overlay").fadeOut(300, function() {
                $(this).remove();
            });
        });
    };
}(jQuery));