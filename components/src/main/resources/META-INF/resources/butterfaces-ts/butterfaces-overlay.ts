///<reference path="definitions/external/tsd.d.ts"/>
///<reference path="butterfaces-guid.ts"/>

module ButterFaces {

    export class Overlay {
        isHiding:boolean;
        delay:number;
        selector:string;
        isTransparentBlockingOverlayActive:boolean;

        constructor(delay = 500, isTransparentBlockingOverlayActive = true, selector = 'body') {
            this.isHiding = true;
            this.delay = delay;
            this.isTransparentBlockingOverlayActive = isTransparentBlockingOverlayActive;
            this.selector = selector;

            console.log('ButterFaces.Overlay.constructor - creating overlay with delay is ' + this.delay + ', isTransparentBlockingOverlayActive is ' + this.isTransparentBlockingOverlayActive + ', selector is ' + this.selector);
        }

        public show() {
            let $elementsToDisable = $(this.selector);

            ButterFaces.Overlay.fadeOutDetachtedOverlays();

            $elementsToDisable.each((index, elementToDisable) => {

                let $elementToDisable = $(elementToDisable);

                this.isHiding = false;


                if ($elementToDisable.attr('data-overlay-uuid') !== undefined) {
                    if (ButterFaces.Overlay.findOverlay($elementToDisable.attr('data-overlay-uuid')).length > 0) {
                        console.log("ButterFaces.Overlay.show - overlay already existing. Skip showing overlay");
                        return;
                    } else {
                        $elementToDisable.removeAttr('data-overlay-uuid');
                    }
                }

                console.log("ButterFaces.Overlay.show - appending not displayed overlay to body");
                var uuid = ButterFaces.Guid.newGuid();

                let $overlay = $('<div class="butter-component-overlay" data-overlay-uuid="' + uuid + '"><div class="butter-component-spinner"><div></div><div></div><div></div><div></div></div></div>');

                $elementToDisable.attr('data-overlay-uuid', uuid);

                if (this.selector === 'body') {
                    $overlay.addClass('overlay-body');
                } else {
                    // TODO if blockpage is true set it to max size
                    $overlay.offset($elementToDisable.offset())
                       .width($elementToDisable.outerWidth())
                       .height($elementToDisable.outerHeight())
                       .addClass('overlay-body-child')
                       .css({'position': 'absolute'}); // IE overrides css position so set it here
                }

                $('body').append($overlay);

                if (this.isTransparentBlockingOverlayActive) {
                    console.log("ButterFaces.Overlay.show - isTransparentBlockingOverlayActive is true, showing transparent overlay direcly");
                    $overlay.show();
                }

                window.setTimeout(() => {
                    if (!this.isHiding && !this.isTransparentBlockingOverlayActive) {
                        console.log("ButterFaces.Overlay.show - deferred: isTransparentBlockingOverlayActive is false, showing transparent overlay after delay");
                        $overlay.show();
                    }

                    if (!this.isHiding) {
                        console.log("ButterFaces.Overlay.show - deferred: starting animation to make overlay intransparent");
                        $overlay
                            .stop(true)
                            .animate({
                                opacity: 1
                            }, 300, () => {
                                console.log("ButterFaces.Overlay.show - deferred: animation ended to make overlay intransparent");
                            });
                    }
                }, this.delay);
                //}
            });
        }

        public hide() {
            console.log("ButterFaces.Overlay.hide - starting animation to make overlay transparent");

            let $elementsToDisable = $(this.selector);
            this.isHiding = true;

            ButterFaces.Overlay.fadeOutDetachtedOverlays();

            $elementsToDisable.each((index, elementToDisable) => {
                let $elementToDisable = $(elementToDisable);
                let overlayUuid = $elementToDisable.attr('data-overlay-uuid');

                if (overlayUuid !== undefined && ButterFaces.Overlay.findOverlay(overlayUuid).length > 0) {
                    let $overlay = ButterFaces.Overlay.findOverlay($elementToDisable.attr('data-overlay-uuid'));

                    ButterFaces.Overlay.fadeOutOverlay($overlay);
                }

            });
        }

        public static hideAll() {
            console.log("ButterFaces.Overlay.hideAll - starting animation to make all overlays transparent");

            ButterFaces.Overlay.fadeOutDetachtedOverlays();
            ButterFaces.Overlay.fadeOutAttachtedOverlays();
        }

        private static fadeOutDetachtedOverlays() {
            // remove unbinded elements
            $('.butter-component-overlay').each((index, elementToCheck) => {
                let $overlay = $(elementToCheck);
                let uuidToCheck = $(elementToCheck).attr('data-overlay-uuid');
                if ($('[data-overlay-uuid=' + uuidToCheck + ']').length == 1) {
                    ButterFaces.Overlay.fadeOutOverlay($overlay);
                }
            });
        };

        private static fadeOutAttachtedOverlays() {
            // remove binded elements
            $('.butter-component-overlay').each((index, elementToCheck) => {
                let $overlay = $(elementToCheck);
                let uuidToCheck = $(elementToCheck).attr('data-overlay-uuid');
                let elements = $('[data-overlay-uuid=' + uuidToCheck + ']');
                if (elements.length > 1) {
                    ButterFaces.Overlay.fadeOutOverlay($overlay);

                    elements.each((index, element) => {
                        let $element = $(element);
                        $element.removeAttr('data-overlay-uuid');
                    });
                }
            });
        };

        private static fadeOutOverlay($overlay:any) {
            $overlay
                .stop(true)
                .animate({
                    opacity: 0
                }, 300, () => {
                    $overlay.remove();
                    console.log("ButterFaces.Overlay.hide - animation ended to make overlay transparent, OVERLAY REMOVED");
                });
        };

        private static findOverlay(uuid:String) {
            return $("body .butter-component-overlay[data-overlay-uuid='" + uuid + "']");
        }
    }
}