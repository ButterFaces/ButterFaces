///<reference path="jquery.d.ts"/>

module ButterFaces {

    export class Overlay {
        isHiding:boolean;
        delay:number;
        isTransparentBlockingOverlayActive:boolean;

        constructor(delay = 500, isTransparentBlockingOverlayActive = true) {
            this.isHiding = true;
            this.delay = delay;
            this.isTransparentBlockingOverlayActive = isTransparentBlockingOverlayActive;

            console.log('ButterFaces.Overlay.constructor - creating overlay with delay is ' + this.delay + ' and isTransparentBlockingOverlayActive is ' + this.isTransparentBlockingOverlayActive);
        }

        show() {
            var $elementToDisable = $('body');

            if ($elementToDisable.find(".butter-component-overlay").length === 0) {
                this.isHiding = false;

                console.log("ButterFaces.Overlay.show - appending not displayed overlay to body");
                var $overlay = $('<div class="butter-component-overlay"><div class="butter-component-spinner"><div></div><div></div><div></div><div></div></div></div>');
                $elementToDisable.append($overlay);

                if (this.isTransparentBlockingOverlayActive) {
                    console.log("ButterFaces.Overlay.show - isTransparentBlockingOverlayActive is true, showing transparent overlay direcly");
                    $overlay.show();
                }

                window.setTimeout(function () {
                    if (!this.isTransparentBlockingOverlayActive) {
                        console.log("ButterFaces.Overlay.show - deferred: isTransparentBlockingOverlayActive is false, showing transparent overlay after delay");
                        $overlay.show();
                    }

                    if (!this.isHiding) {
                        console.log("ButterFaces.Overlay.show - deferred: starting animation to make overlay intransparent");
                        $overlay
                            .stop(true)
                            .animate({
                                opacity: 1
                            }, 300, function () {
                                console.log("ButterFaces.Overlay.show - deferred: animation ended to make overlay intransparent");
                            });
                    }
                }, this.delay);
            }
        }

        hide() {
            console.log("ButterFaces.Overlay.hide - starting animation to make overlay transparent");
            var $overlay = $("body > .butter-component-overlay");
            this.isHiding = true;
            $overlay
                .stop(true)
                .animate({
                    opacity: 0
                }, 300, function () {
                    $(this).remove();
                    console.log("ButterFaces.Overlay.hide - animation ended to make overlay transparent, OVERLAY REMOVED");
                });
        }
    }
}