(function ($) {
    $.fn.butterDisableElements = function () {

        return this.each(function () {
            var $originalElement = $(this);

            var $overlay = $('<div class="butter-component-link-overlay"><div class="loader" /></div>');
            $overlay.offset($originalElement.offset());
            $overlay.width($originalElement.outerWidth());
            $overlay.height($originalElement.outerHeight());

            if ($overlay.height() > 0 && $overlay.width() > 0) {
                $("body").append($overlay);
                $overlay.show('fast');
            }
        });
    };

    $.fn.butterEnableElements = function () {

        return this.each(function () {
            $('.butter-component-link-overlay').hide('fast', function () {
                $(this).remove();
            });
        });
    };
}(jQuery));