(function ($) {

    $.fn.butterMaxLength = function () {
        var MAX_LENGTH = 20;
        return this.each(function () {
            //console.log("initializing max length");
            var root = $(this);
            var valueElement = root.find("textarea");
            var maxLength = root.find(".butterfaces-maxlength-counter");

            if (maxLength.length > 0) {
                //console.log("found max length element");

                var _checkValue = function () {
                    //console.log("checking value");
                    var value = valueElement.val();
                    var freeLetterCount = MAX_LENGTH - value.length;
                    maxLength.text(freeLetterCount + " von " + MAX_LENGTH + " Zeichen");
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