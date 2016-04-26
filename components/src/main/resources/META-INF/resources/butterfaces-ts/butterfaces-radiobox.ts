module ButterFaces {
    export class RadioBox {

        public static addStyleClassClickEvent(radioBoxId:string) {
            let radioBox:any = document.getElementById(radioBoxId);

            radioBox.addEventListener("change", function(event) {
                console.log('Remove radio selection classes');
                [].forEach.call(
                    document.querySelectorAll('.radio'),
                    function (el) {
                        console.log(el);
                        el.classList.remove("butter-radio-item-selected");
                    }
                );

                console.log('Add radio selection class to ' + event.target);
                event.target.parentNode.classList.add("butter-radio-item-selected");
            });
        }

    }
}