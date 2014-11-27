(function ($) {

    $.fn.butterMaxLength = function (options) {

        if (typeof options === "undefined" || typeof options.maxLength === "undefined") {
            throw new Error("no maxLength parameter is given!");
        }

        var ERROR_STYLE_CLASS = "has-error";

        return this.each(function () {
            //console.log("initializing max length");
            var root = $(this);
            var valueElement = root.find("textarea");
            var maxLength = root.find(".butter-component-maxlength-counter");

            if (maxLength.length > 0) {
                //console.log("found max length element");
                var hasInitialValidationError = root.hasClass(ERROR_STYLE_CLASS);

                var _checkValue = function () {
                    var value = valueElement.val();
                    // console.log("checking value");
                    // console.log(value);
                    if (typeof value !== 'undefined') {
                        var freeLetterCount = options.maxLength - value.length;
                        maxLength.text(freeLetterCount + " von " + options.maxLength + " Zeichen");

                        if (!hasInitialValidationError) {
                            if (freeLetterCount < 0) {
                                root.addClass(ERROR_STYLE_CLASS);
                            } else {
                                root.removeClass(ERROR_STYLE_CLASS);
                            }
                        }
                    }
                };

                valueElement.on("focus blur keyup cut paste", _checkValue);

                // initial check
                _checkValue();
            }
        });
    };
}(jQuery));