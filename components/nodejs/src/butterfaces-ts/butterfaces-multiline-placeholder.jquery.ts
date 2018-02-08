///<reference path="../../node_modules/@types/jquery/index.d.ts"/>

(function ($: any) {
    // extend jQuery --------------------------------------------------------------------
    $.fn.multilinePlaceholder = function () {

        return this.each(function () {
            let $originalElement = $(this);
            let $textarea = $originalElement.find("textarea");
            let placeholder = $textarea.attr("placeholder");
            let multilinePlaceholder = placeholder.replace(/\\n/g, "\n");
            $textarea.attr("placeholder", multilinePlaceholder);
        });

    };
}(jQuery));