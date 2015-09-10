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
    var waitingPanelOpeningDelay = null;
    var blockpage = null;

    $.fn.waitingPanel = function (data) {

        function processAjaxUpdate() {
            var ajaxRequestsRunning = 0;

            function processEvent(data) {
                console.log("processEvent: " + data.status);
                if (data.status == 'begin') {
                    ajaxRequestsRunning++;
                } else if (data.status == 'complete') {
                    ajaxRequestsRunning--;
                }
                if (ajaxRequestsRunning > 0) {
                    console.log(" -> " + ajaxRequestsRunning + " active ajax requests. Showing waitingPanel.");
                    butter.overlay.show({delay: waitingPanelOpeningDelay, blockpage: blockpage})
                } else {
                    console.log(" -> No more active requests. Hiding waitingPanel.");
                    butter.overlay.hide();
                }
            }

            return processEvent;
        }

        return this.each(function () {
            var $originalElement = $(this);
            var _elementId = $originalElement.attr('id');
            var _msg = document.getElementById(_elementId);

            $waitingPanelDialog = $(_msg);
            waitingPanelOpeningDelay = data.waitingPanelDelay;
            blockpage = data.blockpage;

            // I found no way to remove event listener from jsf js.
            // I tried to register a callback once and change it on render waiting panel but after this
            // no waiting panel appears anymore.
            // Actually on each rendering of this component a new callback is put on event listener collection.
            if (!eventRegistered) {
                //console.log('waitingPanel - register: ' + _elementId);

                jsf.ajax.addOnEvent(processAjaxUpdate());
                eventRegistered = true;
            }
        });

    };
}(jQuery));