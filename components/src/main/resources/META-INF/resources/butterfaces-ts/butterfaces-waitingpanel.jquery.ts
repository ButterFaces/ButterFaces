///<reference path="definitions/external/tsd.d.ts"/>
///<reference path="butterfaces-overlay.ts"/>

module ButterFaces {
    export class WaitingPanel {
        overlay:ButterFaces.Overlay;

        constructor(overlay:ButterFaces.Overlay) {
            this.overlay = overlay;
        }

        public processAjaxUpdate() {
            var ajaxRequestsRunning = 0;

            return ({status}) => {
                console.log("ButterFaces.WaitingPanel.onEvent - processEvent: " + status);
                if (status == 'begin') {
                    ajaxRequestsRunning++;
                } else if (status == 'success') {
                    ajaxRequestsRunning--;
                }
                if (ajaxRequestsRunning > 0) {
                    console.log('ButterFaces.WaitingPanel.onEvent  - show ' + ajaxRequestsRunning);
                    this.overlay.show();
                } else {
                    console.log('ButterFaces.WaitingPanel.onEvent  - hide ' + ajaxRequestsRunning);
                    this.overlay.hide();
                }
            }
        }

        public processOnError(data: jsf.ajax.RequestData) {
            if (data) {
                console.error('ButterFaces.WaitingPanel.onError  - An error occured, closing waiting panel. errorType: ' + data.status + ', description: ' + data.description);
                ButterFaces.Overlay.hideAll();
            }
        }
    }
}

(function ($:any) {
    // extend jQuery --------------------------------------------------------------------
    var eventRegistered = false;
    var overlay : ButterFaces.Overlay;

    $.fn.waitingPanel = function ({waitingPanelDelay, blockpage}) {

        return this.each(function () {
            // I found no way to remove event listener from jsf js.
            // I tried to register a callback once and change it on render waiting panel but after this
            // no waiting panel appears anymore.
            // Actually on each rendering of this component a new callback is put on event listener collection.
            if (!eventRegistered) {
                //console.log('waitingPanel - register: ' + _elementId);
                overlay = new ButterFaces.Overlay(waitingPanelDelay, blockpage);
                let waitingPanel = new ButterFaces.WaitingPanel(overlay);
                jsf.ajax.addOnEvent(waitingPanel.processAjaxUpdate());
                jsf.ajax.addOnError(waitingPanel.processOnError);
                eventRegistered = true;
            }

            overlay.delay = waitingPanelDelay;
            overlay.isTransparentBlockingOverlayActive = blockpage;
        });

    };
}(jQuery));