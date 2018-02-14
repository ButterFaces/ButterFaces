namespace ButterFaces {
    export class Checkbox {

        public static addSwitchClickEvent(checkboxId: string) {
            let checkbox: HTMLElement = document.getElementById(checkboxId);

            checkbox.querySelector(".slider").addEventListener("click", function (event: any) {
                console.log("Checkbox switch clicked to " + event.target);
                checkbox.querySelector("label").click();
            });
        }

    }
}