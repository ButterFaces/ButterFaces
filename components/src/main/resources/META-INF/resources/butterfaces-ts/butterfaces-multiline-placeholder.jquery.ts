///<reference path="definitions/external/tsd.d.ts"/>

(function ($:any) {
    // extend jQuery --------------------------------------------------------------------
    $.fn.multilinePlaceholder = function () {

        return this.each(function () {
            var $originalElement = $(this);
            var $textarea = $originalElement.find('textarea');
            var placeholder = $textarea.attr('placeholder');
            var multilinePlaceholder = placeholder.replace(/\\n/g, '\n');
            $textarea.attr('placeholder', multilinePlaceholder);
        });

    };
}(jQuery));