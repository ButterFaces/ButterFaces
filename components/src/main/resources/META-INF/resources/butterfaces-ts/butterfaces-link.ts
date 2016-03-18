module ButterFaces {
    export class CommandLink {

        /**
         * Submit given form.
         * @param formId the target form id to submit
         * @param params a list of params to be added to form as hidden input fields
         * @param target the target of the form submission
         */
        static submitForm(formId:string, params:any, target:string) {
            let form:any = document.getElementById(formId);

            ButterFaces.CommandLink.addParametersAsHiddenFieldsToForm(form, params);
            let oldFormTarget = ButterFaces.CommandLink.setFormTarget(form, target);

            if (form.onsubmit) {
                var result = form.onsubmit();
                if ((typeof result == 'undefined') || result) {
                    form.submit();
                }
            } else {
                form.submit();
            }

            form.target = oldFormTarget;
            ButterFaces.CommandLink.removeHiddenFieldsFromForm(form);
        }

        private static addParametersAsHiddenFieldsToForm(form:any, params:any) {
            let bfHiddenInputFields = new Array();
            form.bfHiddenInputFields = bfHiddenInputFields;

            var i:number = 0;

            for (var k in params) {
                if (params.hasOwnProperty(k)) {
                    var p = document.createElement("input");
                    p.type = "hidden";
                    p.name = k;
                    p.value = params[k];
                    form.appendChild(p);
                    bfHiddenInputFields[i++] = p;
                }
            }
        }

        private static removeHiddenFieldsFromForm(form:any) {
            var bfHiddenInputFields = form.bfHiddenInputFields;
            if (bfHiddenInputFields !== null) {
                for (var i = 0; i < bfHiddenInputFields.length; i++) {
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
        private static setFormTarget(form:any, target:string):string {
            let previousTarget:string = form.target;

            if (target) {
                form.target = target;
            }

            return previousTarget;
        }
    }
}