///<reference path="../../node_modules/@types/jquery/index.d.ts"/>
///<reference path="butterfaces-util-object.ts"/>

namespace ButterFaces {
    export class CommandLink {

        static disableOnClick(data: { type: string, status: string, source: { id: string } },
                              showDots: boolean,
                              linkText: string,
                              linkProcessingText: string,
                              linkGlyphicon: string,
                              linkProcessingGlyphicon: string,
                              hideGlyphicon: boolean,
                              disableRenderRegionsIds: string) {
            const status = data.type === "error" ? "error" : data.status;

            // console.log(data.source.id);

            const $commandLink = $(document.getElementById(data.source.id));

            switch (status) {
                case "begin": // Before the ajax request is sent.
                    // console.log('ajax request begin');
                    $commandLink.addClass("disabled");

                    const $glyphicon = $commandLink.find(".butter-component-glyphicon");

                    if (hideGlyphicon) {
                        $glyphicon.hide();
                    }

                    if (linkProcessingGlyphicon.length > 0) {
                        $glyphicon.removeAttr("class");
                        $glyphicon.addClass("butter-component-glyphicon butter-component-processing-spinner");
                        $glyphicon.addClass(linkProcessingGlyphicon);
                        if (linkProcessingText.length > 0 && linkGlyphicon.length === 0) {
                            // glyphicon only appears on ajax request
                            $glyphicon.addClass("butter-component-glyphicon-width-margin");
                        }
                    } else {
                        $glyphicon.removeAttr("class");
                        $glyphicon.addClass("butter-component-spinner tiny butter-component-processing-spinner");
                    }

                    if (showDots) {
                        $commandLink.find(".butter-component-glyphicon-processing").startDots();
                        $commandLink.find(".butter-component-glyphicon-processing").css("display", "inline-block");
                        $commandLink.find(".butter-component-glyphicon-text").html(linkProcessingText);
                    }
                    if (disableRenderRegionsIds !== "undefined") {
                        // console.log('Disable field');
                        new ButterFaces.Overlay(0, false, disableRenderRegionsIds.replace(/[:]/g, "\:")).show();
                    }
                    break;

                case "complete": // After the ajax response is arrived.
                    // console.log('ajax request complete');
                    break;

                case "success": // After update of HTML DOM based on ajax response..
                case "error": // After update of HTML DOM based on ajax response..
                    // console.log('ajax request success');
                    $commandLink.removeClass("disabled");
                    if (showDots) {
                        $commandLink.find(".butter-component-glyphicon-processing").stopDots();
                        $commandLink.find(".butter-component-glyphicon-processing").css("display", "none");
                        $commandLink.find(".butter-component-glyphicon-text").html(linkText ? linkText : "");
                    }

                    const $glyphiconError = $commandLink.find(".butter-component-processing-spinner");
                    $glyphiconError.removeAttr("class");
                    $glyphiconError.addClass("butter-component-glyphicon");

                    if (hideGlyphicon) {
                        $glyphiconError.show();
                    }
                    if (linkGlyphicon.length > 0) {
                        $glyphiconError.addClass(linkGlyphicon);
                    }

                    if (disableRenderRegionsIds !== "undefined") {
                        // console.log('Enable field');
                        new ButterFaces.Overlay(0, false, disableRenderRegionsIds.replace(/[:]/g, "\:")).hide();
                    }
                    break;
            }
        }

        /**
         * Submit given form.
         * @param formId the target form id to submit
         * @param params a list of params to be added to form as hidden input fields
         * @param target the target of the form submission
         */
        static submitForm(formId: string, params: any, target: string) {
            let form: any = document.getElementById(formId);

            ButterFaces.CommandLink.addParametersAsHiddenFieldsToForm(form, params);
            let oldFormTarget = ButterFaces.CommandLink.setFormTarget(form, target);

            if (form.onsubmit) {
                let result = form.onsubmit();
                if (ButterFaces.Object.isNullOrUndefined(result) || result) {
                    form.submit();
                }
            } else {
                form.submit();
            }

            form.target = oldFormTarget;
            ButterFaces.CommandLink.removeHiddenFieldsFromForm(form);
        }

        private static addParametersAsHiddenFieldsToForm(form: any, params: any) {
            let bfHiddenInputFields = [];
            form.bfHiddenInputFields = bfHiddenInputFields;

            let i: number = 0;

            for (let k in params) {
                if (params.hasOwnProperty(k)) {
                    let p = document.createElement("input");
                    p.type = "hidden";
                    p.name = k;
                    p.value = params[k];
                    form.appendChild(p);
                    bfHiddenInputFields[i++] = p;
                }
            }
        }

        private static removeHiddenFieldsFromForm(form: any) {
            let bfHiddenInputFields = form.bfHiddenInputFields;
            if (bfHiddenInputFields !== null) {
                for (let i = 0; i < bfHiddenInputFields.length; i++) {
                    form.removeChild(bfHiddenInputFields[i]);
                }
            }
        }

        /**
         * Updates form target (if exists) and returns previous form target
         * @param form the torm to add the new target to
         * @param target the target to add (if not null)
         * @return the previous form target
         */
        private static setFormTarget(form: any, target: string): string {
            let previousTarget: string = form.target;

            if (target) {
                form.target = target;
            }

            return previousTarget;
        }
    }
}