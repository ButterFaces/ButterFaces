///<reference path="jquery.d.ts"/>

module ButterFaces {

    export class Overlay {
        isHiding:boolean;

        constructor() {
            this.isHiding = true;
        }

        show(delay = 500, isTransparentBlockingOverlayActive = true, elementToDisable = "body") {
            var $elementToDisable = $(elementToDisable);

            if ($elementToDisable.find(".butter-component-overlay").length === 0) {
                this.isHiding = false;

                console.log("butter.overlay.show - appending not displayed overlay to " + elementToDisable);
                var $overlay = $('<div class="butter-component-overlay"><div class="butter-component-spinner"><div></div><div></div><div></div><div></div></div></div>');
                $elementToDisable.append($overlay);

                if (isTransparentBlockingOverlayActive) {
                    console.log("butter.overlay.show - isTransparentBlockingOverlayActive is true, showing transparent overlay direcly");
                    $overlay.show();
                }

                window.setTimeout(function () {
                    if (!isTransparentBlockingOverlayActive) {
                        console.log("butter.overlay.show - deferred: isTransparentBlockingOverlayActive is false, showing transparent overlay after delay");
                        $overlay.show();
                    }

                    if (!this.isHiding) {
                        console.log("butter.overlay.show - deferred: starting animation to make overlay intransparent");
                        $overlay
                            .stop(true)
                            .animate({
                                opacity: 1
                            }, 300, function () {
                                console.log("butter.overlay.show - deferred: animation ended to make overlay intransparent");
                            });
                    }
                }, delay);

            }
        }

        hide() {
            console.log("butter.overlay.hide - starting animation to make overlay transparent");
            var $overlay = $("body > .butter-component-overlay");
            this.isHiding = true;
            $overlay
                .stop(true)
                .animate({
                    opacity: 0
                }, 300, function () {
                    $(this).remove();
                    console.log("butter.overlay.hide - animation ended to make overlay transparent, OVERLAY REMOVED");
                });
        }
    }
}