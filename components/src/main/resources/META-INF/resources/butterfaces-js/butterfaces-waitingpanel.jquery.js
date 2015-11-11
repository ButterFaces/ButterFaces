/**
 * jQuery-Plugin "Waiting panel popup" for ajax popup animation. If registered an waiting panel popup will be rendered
 * as long as ajax request is running.
 * Works with at least jQuery 1.3.2.
 *
 * How to use:
 * jQuery("#someSelector").waitingPanel();
 */
(function ($) {
    // extend jQuery --------------------------------------------------------------------
    var $waitingPanelDialog = null;
    var eventRegistered = false;
    var overlay = null;

    $.fn.waitingPanel = function (data) {

        function processOnEvent(data) {
            //console.log("processEvent: " + data.status);
            if (data.status == 'begin') {
                console.log('butter.waitingpanel - show');
                overlay.show();
            } else if (data.status == 'success') {
                console.log('butter.waitingpanel - hide');
                overlay.hide();
            }
        }


        function processOnError(data) {
            if (data) {
                console.error('An error occured, closing waiting panel. errorType: ' + data.status + ', description: ' + data.description);
                butter.overlay.hide();
            }
        }

        return this.each(function () {
            var $originalElement = $(this);
            var _elementId = $originalElement.attr('id');
            var _msg = document.getElementById(_elementId);

            $waitingPanelDialog = $(_msg);
            overlay = new ButterFaces.Overlay(data.waitingPanelDelay, data.blockpage);


            // I found no way to remove event listener from jsf js.
            // I tried to register a callback once and change it on render waiting panel but after this
            // no waiting panel appears anymore.
            // Actually on each rendering of this component a new callback is put on event listener collection.
            if (!eventRegistered) {
                //console.log('waitingPanel - register: ' + _elementId);
                jsf.ajax.addOnEvent(processOnEvent);
                jsf.ajax.addOnError(processOnError);
                eventRegistered = true;
            }
        });

    };
}(jQuery));