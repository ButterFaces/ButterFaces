///<reference path="../../node_modules/@types/jquery/index.d.ts"/>
///<reference path="butterfaces-util-string.ts"/>
///<reference path="butterfaces-util-object.ts"/>

(function ($: JQueryStatic) {
    // you have to extend jQuery with the fn["pluginName"] notation because in Typescript you can't extend
    // the existing typing interface with fn.pluginName!
    $.fn["butterMaxLength"] = function (options: ButterFaces.MaxLengthIndicatorOptions) {
        return this.each((index, element) => {
            new ButterFaces.MaxLengthIndicator($(element), options);
        });
    };
})(jQuery);

namespace ButterFaces {
    const ERROR_STYLE_CLASS = "has-error";
    const INVALID_STYLE_CLASS = "is-invalid";

    export interface MaxLengthIndicatorOptions {
        maxLength: number;
        maxLengthText: string;
    }

    export class MaxLengthIndicator {

        private rootElement: JQuery;
        private options: MaxLengthIndicatorOptions;

        private valueElement: JQuery;
        private maxLengthElement: JQuery;
        private hasInitialValidationError: boolean;

        constructor(rootElement: JQuery, options: MaxLengthIndicatorOptions) {
            this.rootElement = rootElement;
            this.options = options;
            this.valueElement = this.rootElement.find("textarea");
            this.maxLengthElement = $("<div>").addClass("butter-component-maxlength-counter");

            this.maxLengthElement.insertAfter(this.valueElement);

            this.initializeComponent();
        }

        private initializeComponent(): void {
            console.log("ButterFaces.MaxLengthIndicator - initializeComponent");
            this.hasInitialValidationError = this.rootElement.hasClass(ERROR_STYLE_CLASS);

            console.log(this.valueElement);
            let that = this;
            this.valueElement.on("focus blur keyup cut paste", function () {
                that.checkValue();
            });

            // initial check
            this.checkValue();
        }

        private checkValue(): void {
            let value: string = (<string>this.valueElement.val());

            if (!ButterFaces.Object.isNullOrUndefined(value)) {
                console.log("ButterFaces.MaxLengthIndicator: updating maxlength value");
                let freeLetterCount = this.options.maxLength - value.length;
                let formatted = ButterFaces.String.format(this.options.maxLengthText, [freeLetterCount, this.options.maxLength]);
                this.maxLengthElement.text(formatted);

                if (!this.hasInitialValidationError) {
                    if (freeLetterCount < 0) {
                        this.rootElement.addClass(ERROR_STYLE_CLASS);
                        this.valueElement.addClass(INVALID_STYLE_CLASS);
                    } else {
                        this.rootElement.removeClass(ERROR_STYLE_CLASS);
                        this.valueElement.removeClass(INVALID_STYLE_CLASS);
                    }
                }
            }
        }
    }
}