///<reference path="definitions/external/jquery/jquery.d.ts"/>
///<reference path="butterfaces-util-string.ts"/>

(function ($:any) {
    $.fn.butterMaxLength = function (maxLength:number, maxLengthText:string) {
        var ERROR_STYLE_CLASS = "has-error";

        return this.each(function () {
            //console.log("initializing max length");
            var $root = $(this);
            var $valueElement = $root.find("textarea");
            var $maxLength = $root.find(".butter-component-maxlength-counter");

            if ($maxLength.length > 0) {
                //console.log("found max length element");
                var hasInitialValidationError = $root.hasClass(ERROR_STYLE_CLASS);

                var _checkValue = function () {
                    var value = $valueElement.val();
                    // console.log("checking value");
                    // console.log(value);
                    if (typeof value !== 'undefined') {
                        let freeLetterCount = maxLength - value.length;
                        let formatted = ButterFaces.String.format(maxLengthText, freeLetterCount, maxLength);
                        $maxLength.text(formatted);

                        if (!hasInitialValidationError) {
                            if (freeLetterCount < 0) {
                                $root.addClass(ERROR_STYLE_CLASS);
                            } else {
                                $root.removeClass(ERROR_STYLE_CLASS);
                            }
                        }
                    }
                };

                $valueElement.on("focus blur keyup cut paste", _checkValue);

                // initial check
                _checkValue();
            }
        });
    };
}(jQuery));