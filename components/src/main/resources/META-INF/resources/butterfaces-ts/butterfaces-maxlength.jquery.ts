///<reference path="definitions/external/jquery/jquery.d.ts"/>
///<reference path="butterfaces-util-string.ts"/>
///<reference path="butterfaces-util-object.ts"/>

(function ($: any) {
    $.fn.butterMaxLength = function (maxLength: number, maxLengthText: string) {
        let ERROR_STYLE_CLASS = "has-error";

        return this.each(function () {
            // console.log("initializing max length");
            let $root = $(this);
            let $valueElement = $root.find("textarea");
            let $maxLength = $root.find(".butter-component-maxlength-counter");

            if ($maxLength.length > 0) {
                // console.log("found max length element");
                let hasInitialValidationError = $root.hasClass(ERROR_STYLE_CLASS);

                let _checkValue = function () {
                    let value = $valueElement.val();
                    // console.log("checking value");
                    // console.log(value);
                    if (!ButterFaces.Object.isNullOrUndefined(value)) {
                        let freeLetterCount = maxLength - value.length;
                        let formatted = ButterFaces.String.format(maxLengthText, [freeLetterCount, maxLength]);
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