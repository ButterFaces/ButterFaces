(function ($) {

    $.fn.butterMaxLength = function (options) {

        if (typeof options === "undefined" || typeof options.maxLength === "undefined") {
            throw new Error("no maxLength parameter is given!");
        }

        return this.each(function () {
            //console.log("initializing max length");
            var root = $(this);
            var valueElement = root.find("textarea");
            var maxLength = root.find(".butter-component-maxlength-counter");

            if (maxLength.length > 0) {
                //console.log("found max length element");

                var _checkValue = function () {
                    //console.log("checking value");
                    var value = valueElement.val();
                    var freeLetterCount = options.maxLength - value.length;
                    maxLength.text(freeLetterCount + " von " + options.maxLength + " Zeichen");
                    if (freeLetterCount < 0) {
                        root.addClass("has-error");
                    } else {
                        root.removeClass("has-error");
                    }
                };

                valueElement.on("focus blur keyup cut paste", _checkValue);

                // initial check
                _checkValue();
            }
        });
    };
}(jQuery));